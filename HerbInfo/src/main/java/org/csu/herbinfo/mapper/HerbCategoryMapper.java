package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.HerbCategory;
import org.springframework.stereotype.Repository;

@Repository
@DS("mysql")
public interface HerbCategoryMapper extends BaseMapper<HerbCategory> {
}
