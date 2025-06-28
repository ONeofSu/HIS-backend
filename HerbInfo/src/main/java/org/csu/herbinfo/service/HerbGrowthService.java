package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbGrowth;
import org.springframework.stereotype.Service;
import java.util.List;

public interface HerbGrowthService {
    public boolean addHerbGrowth(HerbGrowth hg);
    public boolean addHerbGrowth(HerbGrowthDTO hg);
    public List<HerbGrowth> getHerbGrowthsByBatchCode(String batchCode);
    public List<HerbGrowth> getAllHerbGrowths();
}
