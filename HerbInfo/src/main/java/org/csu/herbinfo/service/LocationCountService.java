package org.csu.herbinfo.service;

import org.csu.herbinfo.VO.DistrictCountVO;
import org.csu.herbinfo.VO.TopHerbVO;
import org.csu.herbinfo.model.TopHerbModel;

import java.util.List;

public interface LocationCountService {
    public List<DistrictCountVO> getDistrictCount();
    public List<TopHerbModel> getHerbNameAndCountNumsOnHerbLocation();   //获得记录的中药名称及其数量
    public List<TopHerbVO> transferTopHerbModelToVO(List<TopHerbModel> topHerbModels);
    public List<TopHerbVO> getTopHerbs();
}
