package org.csu.herbinfo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.csu.herbinfo.config.RabbitMQConfig;
import org.csu.herbinfo.entity.GrowthAudit;
import org.csu.herbinfo.mapper.GrowthAuditMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditLogConsumer {
    @Autowired
    private GrowthAuditMapper growthAuditMapper;

    @RabbitListener(queues = RabbitMQConfig.AUDIT_LOG_QUEUE)
    public void processAuditLog(GrowthAudit growthAudit) {
        try {
            // 异步插入审核日志
            growthAuditMapper.insert(growthAudit);
        } catch (Exception e) {
            // 失败重试或记录错误日志
            log.error("审核日志入库失败: {}", growthAudit, e);
        }
    }
}
