package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.Street;
import org.springframework.stereotype.Repository;

@Repository
@DS("mysql")
public interface StreetMapper extends BaseMapper<Street> {
}
