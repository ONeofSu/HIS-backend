package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.mapper.HerbGrowthMapper;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HerbGrowthServiceImpl implements HerbGrowthService {
    @Autowired
    HerbGrowthMapper herbGrowthMapper;
    @Autowired
    HerbService herbService;

    //是否符合not NULL 要求
    private boolean isInputHerbGrowthValid(HerbGrowth herbGrowth) {
        if(herbGrowth==null){
            System.out.println("herbGrowth is null");
            return false;
        }
        if(herbGrowth.getBatchCode()==null || herbGrowth.getRecordTime()==null || herbGrowth.getImgUrl()==null){
            System.out.println("batchCode or recordTime is null");
            return false;
        }

        /*
        is userid valid(预留接口)
        */

        if(!herbService.isHerbIdExist(herbGrowth.getHerbId())){
            System.out.println("herbId is not exist");
            return false;
        }

        return true;
    }

    @Override
    public boolean addHerbGrowth(HerbGrowth hg) {
        if(!isInputHerbGrowthValid(hg)) {
            return false;
        }
        //System.out.println(hg.getRecordTime());
        herbGrowthMapper.insert(hg);
        return true;
    }

//    @Override
//    public boolean addHerbGrowth(HerbGrowthDTO hgDTO) {
//        hgDTO.setRecordTime(new Timestamp(new Date().getTime()));   //设置时间戳
//        HerbGrowth hg = hgDTO.transferIntoHerbGrowthExceptImg();
//
//        //handle img
//        String imgUrl = null;
//        try{
//            Path uploadDir_path = Paths.get("src/../../resources/his_info/growth_images/").toAbsolutePath();
//            String uploadDir = uploadDir_path.toString();
//            System.out.println(uploadDir);
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs(); // 创建目录
//            }
//
//            //时间处理
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
//            Timestamp hgTime = hgDTO.getRecordTime();
//            LocalDateTime localDateTime = hgTime.toLocalDateTime();
//            String timeForFileName = localDateTime.format(formatter);
//
//            //文件名
//            String fileName = hgDTO.getBatchCode() + timeForFileName + ".jpg";
//            File destFile = new File(uploadDir, fileName);
//            hgDTO.getImg().transferTo(destFile);
//
//            //存储图片路径
//            imgUrl = "/../resources/his_info/growth_images/" + fileName;
//        }catch (IOException e){
//            e.printStackTrace();
//            return false;
//        }
//
//        hg.setImgUrl(imgUrl);
//        return addHerbGrowth(hg);
//    }


    @Override
    public boolean isHerbGrowthExist(int id) {
        return herbGrowthMapper.selectById(id) != null;
    }

    @Override
    public HerbGrowth getHerbGrowthById(int id) {
        return herbGrowthMapper.selectById(id);
    }

    @Override
    public HerbGrowth getHerbGrowthByHerbGrowthExceptId(HerbGrowth hg) {
        long roundedTime = (hg.getRecordTime().getTime() + 500) / 1000 * 1000;
        Timestamp truncatedTime = new Timestamp(roundedTime);

        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_code", hg.getBatchCode()).eq("growth_time", truncatedTime)
                .eq("user_id", hg.getUserId())
                .eq("growth_longitude",hg.getLongitude()).eq("growth_latitude",hg.getLatitude());;
        //System.out.println(hg.getLongitude()+" "+hg.getLatitude());
        //System.out.println(truncatedTime);
//
        return herbGrowthMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean deleteHerbGrowthById(int id) {
        if(!isHerbGrowthExist(id)){
            return false;
        }
        herbGrowthMapper.deleteById(id);
        return true;
    }

    @Override
    public List<HerbGrowth> getHerbGrowthsByBatchCode(String batchCode) {
        ArrayList<HerbGrowth> list = new ArrayList<>();
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<HerbGrowth>();
        queryWrapper.eq("batch_code", batchCode);
        list.addAll(herbGrowthMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowths() {
        ArrayList<HerbGrowth> list = new ArrayList<>();
        list.addAll(herbGrowthMapper.selectList(null));
        return list;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowthByUserId(int userId) {
        List<HerbGrowth> list = new ArrayList<>();
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        list.addAll(herbGrowthMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public boolean isBatchCodeExist(String batchCode) {
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_code", batchCode);
        return herbGrowthMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public HerbGrowth transferHerbGrowthDTOToHerbGrowth(HerbGrowthDTO hgDTO,int userId) {
        HerbGrowth herbGrowth = new HerbGrowth();
        herbGrowth.setHerbId(herbService.getHerbIdByName(hgDTO.getHerbName()));
        herbGrowth.setBatchCode(hgDTO.getBatchCode());
        herbGrowth.setWet(hgDTO.getWet());
        herbGrowth.setTemperature(hgDTO.getTemperature());
        herbGrowth.setLatitude(hgDTO.getLatitude());
        herbGrowth.setLongitude(hgDTO.getLongitude());
        herbGrowth.setUserId(userId);
        herbGrowth.setRecordTime(Timestamp.valueOf(LocalDateTime.now()));
        herbGrowth.setImgUrl(hgDTO.getImgUrl());

        if(hgDTO.getDes()!=null){
            herbGrowth.setDes(hgDTO.getDes());
        }
        return herbGrowth;
    }

    @Override
    public HerbGrowthVO transferHerbGrowthToVO(HerbGrowth hg) {
        HerbGrowthVO hgVO = new HerbGrowthVO();
        hgVO.setId(hg.getId());
        hgVO.setHerbId(hg.getHerbId());
        hgVO.setBatchCode(hg.getBatchCode());
        hgVO.setWet(hg.getWet());
        hgVO.setTemperature(hg.getTemperature());
        hgVO.setLatitude(hg.getLatitude());
        hgVO.setLongitude(hg.getLongitude());
        hgVO.setRecordTime(hg.getRecordTime());
        hgVO.setUserId(hg.getUserId());
        hgVO.setImgUrl(hg.getImgUrl());
        if(hg.getDes()!=null){
            hgVO.setDes(hg.getDes());
        }

        hgVO.setHerbName(herbService.getHerbById(hg.getHerbId()).getName());

        return hgVO;
    }

    @Override
    public List<HerbGrowthVO> transferGrowthListToVOList(List<HerbGrowth> hgList) {
        ArrayList<HerbGrowthVO> list = new ArrayList<>();
        for(HerbGrowth hg : hgList){
            HerbGrowthVO hgVO = transferHerbGrowthToVO(hg);
            list.add(hgVO);
        }
        return list;
    }
}
