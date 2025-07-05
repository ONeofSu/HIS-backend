-- 业绩管理数据库 (his_performance)
-- 创建数据库
CREATE DATABASE IF NOT EXISTS his_performance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE his_performance;

-- 7.1 业绩表(perform)
CREATE TABLE IF NOT EXISTS perform (
    perform_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '业绩主键',
    perform_name VARCHAR(50) NOT NULL COMMENT '业绩(工作)名称',
    perform_content TEXT COMMENT '业绩内容',
    perform_type_id BIGINT NOT NULL COMMENT '业绩类型',
    perform_status INT NOT NULL DEFAULT 0 COMMENT '业绩状态:0:草稿1:已提交2:已通过3:已驳回',
    perform_time DATETIME NOT NULL COMMENT '提交日期',
    perform_comment TEXT COMMENT '审核评语',
    submit_user_id BIGINT NOT NULL COMMENT '提交人ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    audit_time DATETIME COMMENT '审核时间',
    audit_by BIGINT COMMENT '审核人ID',
    INDEX idx_perform_type (perform_type_id),
    INDEX idx_perform_status (perform_status),
    INDEX idx_submit_user (submit_user_id),
    INDEX idx_audit_by (audit_by),
    INDEX idx_perform_time (perform_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业绩表';

-- 7.2 业绩附件表(perform_file)
CREATE TABLE IF NOT EXISTS perform_file (
    perform_file_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文档主键',
    perform_id BIGINT NOT NULL COMMENT '所属业绩',
    perform_file_name VARCHAR(100) NOT NULL COMMENT '文档名称',
    perform_file_des VARCHAR(255) COMMENT '文档描述',
    perform_file_type VARCHAR(50) NOT NULL COMMENT '文档类型',
    perform_file_url VARCHAR(255) NOT NULL COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小',
    perform_file_isvalid BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否有效',
    upload_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    upload_by BIGINT NOT NULL COMMENT '上传人ID',
    INDEX idx_perform_id (perform_id),
    INDEX idx_upload_by (upload_by),
    INDEX idx_upload_time (upload_time),
    FOREIGN KEY (perform_id) REFERENCES perform(perform_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业绩附件表';

-- 7.3 业绩种类表(perform_type)
CREATE TABLE IF NOT EXISTS perform_type (
    perform_type_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '业绩种类主键',
    perform_type_name VARCHAR(50) NOT NULL COMMENT '业绩种类名称',
    perform_type_desc VARCHAR(50) COMMENT '业绩种类描述',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否启用',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_is_active (is_active),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业绩种类表';

-- 添加外键约束
ALTER TABLE perform ADD CONSTRAINT fk_perform_type FOREIGN KEY (perform_type_id) REFERENCES perform_type(perform_type_id);

-- 插入默认业绩种类数据
INSERT INTO perform_type (perform_type_name, perform_type_desc, sort_order) VALUES
('教学成果', '教学相关的业绩成果', 1),
('科研成果', '科研相关的业绩成果', 2),
('社会服务', '社会服务相关的业绩成果', 3),
('学术交流', '学术交流相关的业绩成果', 4),
('其他业绩', '其他类型的业绩成果', 5);

-- 创建视图：业绩详情视图（包含提交人信息）
CREATE VIEW v_perform_detail AS
SELECT 
    p.perform_id,
    p.perform_name,
    p.perform_content,
    p.perform_type_id,
    pt.perform_type_name,
    p.perform_status,
    p.perform_time,
    p.perform_comment,
    p.submit_user_id,
    p.created_time,
    p.updated_time,
    p.audit_time,
    p.audit_by,
    CASE p.perform_status 
        WHEN 0 THEN '草稿'
        WHEN 1 THEN '已提交'
        WHEN 2 THEN '已通过'
        WHEN 3 THEN '已驳回'
        ELSE '未知状态'
    END AS perform_status_name
FROM perform p
LEFT JOIN perform_type pt ON p.perform_type_id = pt.perform_type_id
WHERE pt.is_active = TRUE;

-- 创建视图：业绩附件详情视图
CREATE VIEW v_perform_file_detail AS
SELECT 
    pf.perform_file_id,
    pf.perform_id,
    pf.perform_file_name,
    pf.perform_file_des,
    pf.perform_file_type,
    pf.perform_file_url,
    pf.file_size,
    pf.perform_file_isvalid,
    pf.upload_time,
    pf.upload_by,
    p.perform_name,
    p.perform_status
FROM perform_file pf
LEFT JOIN perform p ON pf.perform_id = p.perform_id
WHERE pf.perform_file_isvalid = TRUE; 