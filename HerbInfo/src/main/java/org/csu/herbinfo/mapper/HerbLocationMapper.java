package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.HerbLocation;
import org.springframework.stereotype.Repository;

@Deprecated
@Repository
@DS("mysql")
public interface HerbLocationMapper extends BaseMapper<HerbLocation> {
}
