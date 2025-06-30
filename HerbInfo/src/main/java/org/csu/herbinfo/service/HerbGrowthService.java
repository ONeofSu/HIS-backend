package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbGrowth;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

public interface HerbGrowthService {
    public boolean addHerbGrowth(HerbGrowth hg);
    public boolean isHerbGrowthExist(int id);
    public HerbGrowth getHerbGrowthById(int id);
    public HerbGrowth getHerbGrowthByHerbGrowthExceptId(HerbGrowth hg);
    public boolean deleteHerbGrowthById(int id);
    //public boolean addHerbGrowth(HerbGrowthDTO hg);
    public List<HerbGrowth> getHerbGrowthsByBatchCode(String batchCode);
    public List<HerbGrowth> getAllHerbGrowths();
    public List<HerbGrowth> getAllHerbGrowthByUserId(int userId);

    public boolean isBatchCodeExist(String batchCode);

    public HerbGrowth transferHerbGrowthDTOToHerbGrowth(HerbGrowthDTO hgDTO,int userId);
    public HerbGrowthVO transferHerbGrowthToVO(HerbGrowth hg);
    public List<HerbGrowthVO> transferGrowthListToVOList(List<HerbGrowth> hgList);
}
