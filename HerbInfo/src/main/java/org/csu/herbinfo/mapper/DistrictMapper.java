package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.District;
import org.springframework.stereotype.Repository;

@Repository
@DS("mysql")
public interface DistrictMapper extends BaseMapper<District> {
}
