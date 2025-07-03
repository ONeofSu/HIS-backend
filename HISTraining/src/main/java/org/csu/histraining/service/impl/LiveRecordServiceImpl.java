package org.csu.histraining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.csu.histraining.entity.LiveRecord;
import org.csu.histraining.entity.LiveRoom;
import org.csu.histraining.mapper.LiveRecordMapper;
import org.csu.histraining.mapper.LiveRoomMapper;
import org.csu.histraining.service.LiveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LiveRecordServiceImpl implements LiveRecordService {
    @Autowired
    LiveRoomMapper liveRoomMapper;
    @Autowired
    LiveRecordMapper liveRecordMapper;
    @Autowired
    MinioClient minioClient;
    @Value("${minio.bucket}")
    private String minioBucket;
    @Value("${live.record.save-path}")
    private String recordSavePath;

    private final ConcurrentHashMap<Long, Process> recordingProcesses = new ConcurrentHashMap<>();

    private void startRecordingProcess(LiveRoom liveRoom,LiveRecord liveRecord) {
        CompletableFuture.runAsync(() -> {
           try {
//               ProcessBuilder pbT = new ProcessBuilder("ffmpeg", "-version");
//               pbT.redirectErrorStream(true);
//               Process processT = pbT.start();
//               BufferedReader reader = new BufferedReader(new InputStreamReader(processT.getInputStream()));
//               String line;
//               while ((line = reader.readLine()) != null) {
//                   System.out.println(line);
//               }

               File saveDir = new File(recordSavePath);
               if(!saveDir.exists()){
                   saveDir.mkdirs();
               }

               String outputPath = recordSavePath + "/" + liveRecord.getFileName();
               String inputUrl = liveRoom.getFlvUrl();

               //使用ffmpeg录制
               ProcessBuilder pb = new ProcessBuilder(
                       "ffmpeg",
                       "-i",inputUrl,
                       "-c:v","copy",
                       "-c:a","aac",
                       "-strict","-2",
                       outputPath
               );

               Process process = pb.start();
               recordingProcesses.put(liveRecord.getId(), process);

               //保存进程id
               String processKey = "live:recording:process:" + liveRecord.getId();
               Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                   process.destroy();
               }));

               log.info("开始录制直播, roomId={}, recordingId={}", liveRoom.getId(), liveRecord.getId());

               int exitCode = process.waitFor();
               log.info("录制进程结束, roomId={}, recordingId={}, exitCode={}",
                       liveRoom.getId(), liveRecord.getId(), exitCode);

               recordingProcesses.remove(liveRecord.getId());

               if(exitCode == 0){
                    uploadRecording(liveRecord,new File(outputPath));
               }
           }catch (Exception e){
               log.error("录制直播异常", e);

               // 更新录制状态为失败
               LiveRecord failedRecord = new LiveRecord();
               failedRecord.setId(liveRecord.getRoomId());
               failedRecord.setStatus(4);  // 失败状态
               failedRecord.setUpdatedAt(LocalDateTime.now());

               liveRecordMapper.updateById(failedRecord);
           }
        });
    }

    private void stopRecordingProcess(LiveRecord liveRecord) {
        try {
            Process process = recordingProcesses.get(liveRecord.getId());
            if (process != null && process.isAlive()) {
                // 尝试通过标准输入发送 'q' 命令（优雅退出）
                try (OutputStream os = process.getOutputStream()) {
                    os.write("q\n".getBytes());
                    os.flush();
                }

                // 等待最多 10 秒
                if (!process.waitFor(10, TimeUnit.SECONDS)) {
                    log.warn("ffmpeg 未正常退出，强制终止");
                    process.destroyForcibly(); // 最终手段
                }

                log.info("成功停止录制, recordingId={}, exitCode={}",
                        liveRecord.getId(), process.exitValue());
            } else {
                log.warn("未找到活跃的录制进程, recordingId={}", liveRecord.getId());
            }
        } catch (Exception e) {
            log.error("停止录制进程异常", e);
            throw new RuntimeException("停止录制失败", e);
        }
        log.info("手动停止录制, recordingId={}", liveRecord.getId());
    }

    private void uploadRecording(LiveRecord liveRecord,File file) {
        try{
            LiveRecord processingRecord  = new LiveRecord();
            processingRecord.setId(liveRecord.getId());
            processingRecord.setStatus(LiveRecord.RECORD_HANDLING);
            processingRecord.setUpdatedAt(LocalDateTime.now());
            liveRecordMapper.updateById(processingRecord);

            long fileSize = file.length();

            // 使用FFmpeg获取视频时长
            String[] cmd = {
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    file.getAbsolutePath()
            };

            Process process = Runtime.getRuntime().exec(cmd);

            String durationStr;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                durationStr = reader.readLine();
            }

            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    log.error("ffprobe 错误输出: {}", errorLine);
                }
            }

            File saveDir = new File(recordSavePath);
            if (!saveDir.exists() && !saveDir.mkdirs()) {
                throw new IOException("无法创建目录: " + recordSavePath);
            }

            if (!saveDir.canWrite()) {
                throw new IOException("目录不可写: " + recordSavePath);
            }

//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            durationStr = reader.readLine();
            int duration = Integer.parseInt(durationStr);

            //Minio
            String objectName = "recordings/" + liveRecord.getFileName();
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(minioBucket)
                            .object(objectName)
                            .filename(file.getAbsolutePath())
                            .contentType("video/mp4")
                            .build()
            );

            //构建访问URL
            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioBucket)
                            .object(objectName)
                            .method(Method.GET)
                            .build()
            );

            //更新
            LiveRecord updateRecord = new LiveRecord();
            updateRecord.setId(liveRecord.getId());
            updateRecord.setFileUrl(fileUrl);
            updateRecord.setFileSize(fileSize);
            updateRecord.setDuration(duration);
            updateRecord.setStatus(LiveRecord.RECORD_USABLE);
            updateRecord.setUpdatedAt(LocalDateTime.now());
            liveRecordMapper.updateById(updateRecord);

            log.info("录制文件上传完成, recordingId={}, fileSize={}, duration={}s",
                    liveRecord.getId(), fileSize, duration);

            // 删除本地文件
            file.delete();
        } catch (Exception e) {
            log.error("上传录制文件异常",e);

            LiveRecord failedRecord = new LiveRecord();
            failedRecord.setId(liveRecord.getId());
            failedRecord.setStatus(LiveRecord.RECORD_DELETED);
            failedRecord.setUpdatedAt(LocalDateTime.now());
            liveRecordMapper.updateById(failedRecord);
        }
    }

    @Transactional
    @Override
    public LiveRecord startRecord(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null || liveRoom.getStatus()!=LiveRoom.LIVE) {
            throw new IllegalArgumentException("直播间不存在或未开播");
        }

        LiveRecord liveRecord = new LiveRecord();
        liveRecord.setRoomId(roomId);
        liveRecord.setFileName(liveRoom.getStreamKey() + "_" + System.currentTimeMillis() + ".mp4");
        liveRecord.setStatus(LiveRecord.RECORDING);
        liveRecord.setStartTime(LocalDateTime.now());
        liveRecord.setCreatedAt(LocalDateTime.now());
        liveRecord.setUpdatedAt(LocalDateTime.now());
        liveRecordMapper.insert(liveRecord);

        startRecordingProcess(liveRoom,liveRecord);//启动录制进程
        return liveRecord;
    }

    @Transactional
    @Override
    public LiveRecord stopRecord(Long recordId) {
        LiveRecord liveRecord = liveRecordMapper.selectById(recordId);
        if(liveRecord == null || liveRecord.getStatus()!=LiveRecord.RECORDING) {
            throw new IllegalArgumentException("录制任务不存在或已结束");
        }

        //更新
        liveRecord.setStatus(LiveRecord.RECORD_FINISH);
        liveRecord.setUpdatedAt(LocalDateTime.now());
        liveRecord.setEndTime(LocalDateTime.now());
        liveRecordMapper.updateById(liveRecord);


        //停止录制 上传文件
        stopRecordingProcess(liveRecord);

        return liveRecord;
    }

    @Override
    public List<LiveRecord> getRecordsByRoomId(Long roomId, int page, int size) {
        Page<LiveRecord> pageParam = new Page<>(page, size);
        QueryWrapper<LiveRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", roomId).eq("live_record_status", LiveRecord.RECORD_USABLE)
                .orderByDesc("live_record_start_time");

        return liveRecordMapper.selectPage(pageParam, queryWrapper).getRecords();
    }

    @Override
    public boolean isLiveRecording(Long recordId) {
        LiveRecord liveRecord = liveRecordMapper.selectById(recordId);
        if(liveRecord == null || liveRecord.getStatus()!=LiveRecord.RECORDING) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isLiveRecordingByRoomId(Long roomId) {
        QueryWrapper<LiveRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", roomId).eq("live_record_status",LiveRecord.RECORDING);
        return liveRecordMapper.selectCount(queryWrapper) > 0;
    }
}
