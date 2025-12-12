package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.herbinfo.entity.GrowthAudit;
import org.springframework.stereotype.Repository;

@Repository
@DS("mysql")
public interface GrowthAuditMapper extends BaseMapper<GrowthAudit> {
}
