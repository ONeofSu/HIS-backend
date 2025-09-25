package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.GisHerbLocation;
import org.springframework.stereotype.Repository;

@Repository
@DS("postgresql")
public interface GisHerbLocationPGSqlMapper extends BaseMapper<GisHerbLocation> {
}
