package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.GrowthAuditDTO;
import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.GrowthAudit;
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
    boolean updateHerbGrowth(HerbGrowth hg);
    public boolean deleteHerbGrowthById(int id);


    //public boolean addHerbGrowth(HerbGrowthDTO hg);
    public List<HerbGrowth> getHerbGrowthsByBatchCodeThatPassAudit(String batchCode);
    List<HerbGrowth> getAllHerbGrowthsPassAudit();
    List<HerbGrowth> getAllHerbGrowthsNotPassAudit();
    List<HerbGrowth> getAllHerbGrowthsNeedToAudit();
    public List<HerbGrowth> getAllHerbGrowths();
    public List<HerbGrowth> getAllHerbGrowthByUserIdThatPassAudit(int userId);
    public List<HerbGrowth> getAllHerbGrowthByUserId(int userId);

    public boolean isBatchCodeExist(String batchCode);

    public HerbGrowth transferHerbGrowthDTOToHerbGrowth(HerbGrowthDTO hgDTO,int userId);
    public HerbGrowthVO transferHerbGrowthToVO(HerbGrowth hg);
    public List<HerbGrowthVO> transferGrowthListToVOList(List<HerbGrowth> hgList);

    GrowthAudit handleAudit(GrowthAudit growthAudit);
    GrowthAudit updateAudit(GrowthAudit growthAudit);
    GrowthAudit getAuditById(Long id);
    boolean isAuditExist(Long id);
    List<GrowthAudit> getAllAudit();
    GrowthAudit transferDTOToAudit(GrowthAuditDTO dto,int userId);
}
