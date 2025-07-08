package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.DTO.GrowthAuditDTO;
import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.GrowthAudit;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.mapper.GrowthAuditMapper;
import org.csu.herbinfo.mapper.HerbGrowthMapper;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class HerbGrowthServiceImpl implements HerbGrowthService {
    @Autowired
    HerbGrowthMapper herbGrowthMapper;
    @Autowired
    HerbService herbService;
    @Autowired
    GrowthAuditMapper growthAuditMapper;
    private final RedisTemplate<String,Object> redisTemplate;

    private final static String GET_AUDIT_GROWTH_BY_BATCH_CODE = "auditGrowth:batchCode";
    private final static String GET_ALL_AUDIT_GROWTH = "auditGrowth:all";
    private final static String GET_ALL_NEED_AUDIT_GROWTH = "auditGrowth:need";
    private final static String GET_ALL_GROWTH = "growth:all";
    private final static String GET_ALL_AUDIT = "audit:all";

    HerbGrowthServiceImpl(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
        hg.setGrowthAuditStatus(0);
        herbGrowthMapper.insert(hg);

        //清理缓存
        redisTemplate.delete(GET_ALL_NEED_AUDIT_GROWTH);
        redisTemplate.delete(GET_ALL_GROWTH);
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
    public boolean updateHerbGrowth(HerbGrowth hg) {
        if(!isInputHerbGrowthValid(hg)) {
            return false;
        }
        HerbGrowth ori = herbGrowthMapper.selectById(hg.getId());

        hg.setGrowthAuditStatus(0);
        herbGrowthMapper.updateById(hg);

        //清理缓存
        if(ori.getGrowthAuditStatus()==1){
            redisTemplate.delete(GET_AUDIT_GROWTH_BY_BATCH_CODE+ori.getBatchCode());
            redisTemplate.delete(GET_ALL_AUDIT_GROWTH);
        }
        redisTemplate.delete(GET_ALL_NEED_AUDIT_GROWTH);
        redisTemplate.delete(GET_ALL_GROWTH);

        return true;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowthsPassAudit() {
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("growth_audit_status",1);
        List<HerbGrowth> list = herbGrowthMapper.selectList(queryWrapper);
        if(list==null || list.isEmpty()){
            int timeout =30+ new Random().nextInt(10);
            redisTemplate.opsForValue().set(GET_ALL_AUDIT_GROWTH,Collections.emptyList(),timeout,TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(GET_ALL_AUDIT_GROWTH,list,10,TimeUnit.MINUTES);
        }
        return list;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowthsNotPassAudit() {
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("growth_audit_status",2);
        return herbGrowthMapper.selectList(queryWrapper);
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowthsNeedToAudit() {
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("growth_audit_status",0);
        List<HerbGrowth> herbGrowths = herbGrowthMapper.selectList(queryWrapper);
        if(herbGrowths==null || herbGrowths.isEmpty()){
            int timeout =30+ new Random().nextInt(10);
            redisTemplate.opsForValue().set(GET_ALL_NEED_AUDIT_GROWTH,Collections.emptyList(),timeout,TimeUnit.SECONDS);
        }else {
            redisTemplate.opsForValue().set(GET_ALL_NEED_AUDIT_GROWTH,herbGrowths,10,TimeUnit.MINUTES);
        }
        return herbGrowths;
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
        HerbGrowth herbGrowth = getHerbGrowthById(id);
        //清理缓存
        if(herbGrowth.getGrowthAuditStatus()==1){
            redisTemplate.delete(GET_ALL_AUDIT_GROWTH);
            redisTemplate.delete(GET_AUDIT_GROWTH_BY_BATCH_CODE+herbGrowth.getBatchCode());
        } else if (herbGrowth.getGrowthAuditStatus()==0) {
            redisTemplate.delete(GET_ALL_NEED_AUDIT_GROWTH);
        }
        redisTemplate.delete(GET_ALL_GROWTH);

        herbGrowthMapper.deleteById(id);
        return true;
    }

    @Override
    public List<HerbGrowth> getHerbGrowthsByBatchCodeThatPassAudit(String batchCode) {
        String key = GET_AUDIT_GROWTH_BY_BATCH_CODE + batchCode;
        ArrayList<HerbGrowth> list = (ArrayList<HerbGrowth>) redisTemplate.opsForValue().get(key);
        if(list!=null && !list.isEmpty()){
            return list;
        }

        list = new ArrayList<>();
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<HerbGrowth>();
        queryWrapper.eq("batch_code", batchCode).eq("growth_audit_status",1);
        list.addAll(herbGrowthMapper.selectList(queryWrapper));
        if(list == null || list.isEmpty()){
            int timeout = 30 + new Random().nextInt(10);
            redisTemplate.opsForValue().set(key, Collections.emptyList(),timeout, TimeUnit.SECONDS);
        }else {
            redisTemplate.opsForValue().set(key, list,10,TimeUnit.MINUTES);
        }
        return list;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowths() {
        ArrayList<HerbGrowth> list = new ArrayList<>();
        list.addAll(herbGrowthMapper.selectList(null));
        if(list == null || list.isEmpty()){
            int timeout = 30 + new Random().nextInt(10);
            redisTemplate.opsForValue().set(GET_ALL_GROWTH,Collections.emptyList(),timeout,TimeUnit.SECONDS);
        }else {
            redisTemplate.opsForValue().set(GET_ALL_GROWTH,list,10,TimeUnit.MINUTES);
        }
        return list;
    }

    @Override
    public List<HerbGrowth> getAllHerbGrowthByUserIdThatPassAudit(int userId) {
        List<HerbGrowth> list = new ArrayList<>();
        QueryWrapper<HerbGrowth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("growth_audit_status",1);
        list.addAll(herbGrowthMapper.selectList(queryWrapper));
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
        herbGrowth.setGrowthAuditStatus(0);

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

        switch (hg.getGrowthAuditStatus()){
            case 0:{
                hgVO.setAuditStatus("审核中");
                break;
            }
            case 1:{
                hgVO.setAuditStatus("已通过");
                break;
            }
            case 2:{
                hgVO.setAuditStatus("未通过");
                break;
            }
            default:{
                hgVO.setAuditStatus("ERROR");
                break;
            }
        }

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

    @Override
    public GrowthAudit handleAudit(GrowthAudit growthAudit) {
        HerbGrowth herbGrowth = getHerbGrowthById(growthAudit.getGrowthId());
        if(herbGrowth==null || herbGrowth.getGrowthAuditStatus()!=0){
            return null;
        }

        redisTemplate.delete(GET_ALL_NEED_AUDIT_GROWTH);
        redisTemplate.delete(GET_ALL_GROWTH);

        if(growthAudit.getAuditResult() == 1){
            herbGrowth.setGrowthAuditStatus(1);
            herbGrowthMapper.updateById(herbGrowth);
            //清理缓存
            redisTemplate.delete(GET_AUDIT_GROWTH_BY_BATCH_CODE+herbGrowth.getBatchCode());
            redisTemplate.delete(GET_ALL_AUDIT_GROWTH);

        }else if(growthAudit.getAuditResult() == 2){
            herbGrowth.setGrowthAuditStatus(2);
            herbGrowthMapper.updateById(herbGrowth);
        }else{
            return null;
        }

        growthAudit.setAuditTime(LocalDateTime.now());
        growthAuditMapper.insert(growthAudit);
        return growthAudit;
    }

    @Override
    public GrowthAudit updateAudit(GrowthAudit growthAudit) {
        HerbGrowth herbGrowth = getHerbGrowthById(growthAudit.getGrowthId());
        if(herbGrowth==null){
            return null;
        }

        //清理缓存
        redisTemplate.delete(GET_AUDIT_GROWTH_BY_BATCH_CODE+herbGrowth.getBatchCode());
        redisTemplate.delete(GET_ALL_AUDIT_GROWTH);
        redisTemplate.delete(GET_ALL_NEED_AUDIT_GROWTH);
        redisTemplate.delete(GET_ALL_GROWTH);

        if(growthAudit.getAuditResult() == 1){
            herbGrowth.setGrowthAuditStatus(1);
            herbGrowthMapper.updateById(herbGrowth);
        }else if(growthAudit.getAuditResult() == 2){
            herbGrowth.setGrowthAuditStatus(2);
            herbGrowthMapper.updateById(herbGrowth);
        }else{
            return null;
        }

        growthAuditMapper.updateById(growthAudit);
        return growthAudit;
    }

    @Override
    public GrowthAudit getAuditById(Long id) {
        return growthAuditMapper.selectById(id);
    }

    @Override
    public boolean isAuditExist(Long id) {
        return growthAuditMapper.selectById(id) != null;
    }

    @Override
    public List<GrowthAudit> getAllAudit() {
        List<GrowthAudit> growthAudits = growthAuditMapper.selectList(null);
        
        return growthAudits;
    }

    @Override
    public GrowthAudit transferDTOToAudit(GrowthAuditDTO dto,int userId) {
        GrowthAudit growthAudit = new GrowthAudit();
        growthAudit.setGrowthId(dto.getGrowthId());
        growthAudit.setAuditDes(dto.getAuditDes());
        switch (dto.getAuditResult()){
            case "通过":{
                growthAudit.setAuditResult(1);
                break;
            }
            case "拒绝":{
                growthAudit.setAuditResult(2);
                break;
            }
            default:{
                growthAudit.setAuditResult(0);
            }
        }

        growthAudit.setAuditorUserId(userId);
        growthAudit.setAuditTime(LocalDateTime.now());
        return growthAudit;
    }
}
