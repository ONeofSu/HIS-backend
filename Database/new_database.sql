-- 创建用户信息库
CREATE DATABASE IF NOT EXISTS his_userinfo;
USE his_userinfo;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户类型表
CREATE TABLE IF NOT EXISTS user_category (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户类型关系表
CREATE TABLE IF NOT EXISTS user_link_category (
    ulc_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (ulc_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (category_id) REFERENCES user_category(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建中药信息库
CREATE DATABASE IF NOT EXISTS his_herbinfo;
USE his_herbinfo;

-- 中药表
CREATE TABLE IF NOT EXISTS herb (
    herb_id BIGINT NOT NULL AUTO_INCREMENT,
    herb_name VARCHAR(50) NOT NULL,
    herb_origin VARCHAR(50),
    herb_img VARCHAR(255),
    herb_des1 VARCHAR(255),
    herb_des2 VARCHAR(255),
    herb_isvalid BOOLEAN NOT NULL,
    PRIMARY KEY (herb_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 中药种类表
CREATE TABLE IF NOT EXISTS herb_category (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 中药类型关系表
CREATE TABLE IF NOT EXISTS herb_link_category (
    hlc_id BIGINT NOT NULL AUTO_INCREMENT,
    herb_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (hlc_id),
    FOREIGN KEY (herb_id) REFERENCES herb(herb_id),
    FOREIGN KEY (category_id) REFERENCES herb_category(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 中药地理信息表
CREATE TABLE IF NOT EXISTS herb_location (
    location_id BIGINT NOT NULL AUTO_INCREMENT,
    herb_id BIGINT NOT NULL,
    location_count INT NOT NULL,
    district VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    location_longitude DECIMAL(9,6) NOT NULL,
    location_latitude DECIMAL(8,6) NOT NULL,
    PRIMARY KEY (location_id),
    FOREIGN KEY (herb_id) REFERENCES herb(herb_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 中药成长记录表
CREATE TABLE IF NOT EXISTS herb_growth (
    growth_id BIGINT NOT NULL AUTO_INCREMENT,
    herb_id BIGINT NOT NULL,
    batch_code VARCHAR(50) NOT NULL,
    wet DECIMAL(4,1) NOT NULL,
    temperature DECIMAL(4,1) NOT NULL,
    growth_des VARCHAR(255),
    growth_longitude DECIMAL(9,6) NOT NULL,
    growth_latitude DECIMAL(8,6) NOT NULL,
    user_id BIGINT NOT NULL,
    growth_time DATETIME NOT NULL,
    growth_img VARCHAR(255) NOT NULL,
    PRIMARY KEY (growth_id),
    FOREIGN KEY (herb_id) REFERENCES herb(herb_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建中药教学数据库
CREATE DATABASE IF NOT EXISTS his_herb_teaching;
USE his_herb_teaching;

-- 试验课程表
CREATE TABLE IF NOT EXISTS course (
    course_id BIGINT NOT NULL AUTO_INCREMENT,
    course_name VARCHAR(50) NOT NULL,
    course_start_time DATETIME NOT NULL,
    course_end_time DATETIME NOT NULL,
    course_des VARCHAR(255),
    PRIMARY KEY (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 实验表
CREATE TABLE IF NOT EXISTS lab (
    lab_id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    lab_type INT NOT NULL,
    lab_object INT NOT NULL,
    lab_submit_time DATETIME NOT NULL,
    lab_end_time DATETIME NOT NULL,
    lab_des VARCHAR(255),
    PRIMARY KEY (lab_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 实验图片表
CREATE TABLE IF NOT EXISTS lab_img (
    lab_img_id BIGINT NOT NULL AUTO_INCREMENT,
    lab_id BIGINT NOT NULL,
    lab_img_order INT NOT NULL,
    lab_img_time DATETIME NOT NULL,
    lab_img VARCHAR(255) NOT NULL,
    lab_img_position INT,
    lab_img_isvalid BOOLEAN NOT NULL,
    PRIMARY KEY (lab_img_id),
    FOREIGN KEY (lab_id) REFERENCES lab(lab_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 实验视频表
CREATE TABLE IF NOT EXISTS lab_video (
    lab_video_id BIGINT NOT NULL AUTO_INCREMENT,
    lab_id BIGINT NOT NULL,
    lab_video_order INT NOT NULL,
    lab_video_time DATETIME NOT NULL,
    lab_video_length BIGINT NOT NULL,
    lab_video VARCHAR(255) NOT NULL,
    lab_video_isvalid BOOLEAN NOT NULL,
    PRIMARY KEY (lab_video_id),
    FOREIGN KEY (lab_id) REFERENCES lab(lab_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 实验详情表
CREATE TABLE IF NOT EXISTS lab_info (
    lab_info_id BIGINT NOT NULL AUTO_INCREMENT,
    lab_id BIGINT NOT NULL,
    herb_id BIGINT NOT NULL,
    lab_info_goal VARCHAR(255),
    lab_info_des1 VARCHAR(255),
    lab_info_des2 VARCHAR(255),
    lab_info_isvalid BOOLEAN,
    PRIMARY KEY (lab_info_id),
    FOREIGN KEY (lab_id) REFERENCES lab(lab_id),
    FOREIGN KEY (herb_id) REFERENCES his_herbinfo.herb(herb_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建研究课题库(his_research)
CREATE DATABASE IF NOT EXISTS his_research;
USE his_research;

-- 4.1 课题表(topic)
CREATE TABLE IF NOT EXISTS topic (
    topic_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_name VARCHAR(50) NOT NULL,
    team_id BIGINT NOT NULL,
    topic_start_time DATETIME NOT NULL,
    topic_end_time DATETIME NOT NULL,
    topic_des VARCHAR(255),
    topic_status INT NOT NULL COMMENT '0:立项中 1:进行中 2:已结题'
);

-- 4.2 研究团队表(team)
CREATE TABLE IF NOT EXISTS team (
    team_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(50) NOT NULL,
    team_time DATETIME NOT NULL,
    team_des VARCHAR(255)
);

-- 4.3 团队成员表(team_member)
CREATE TABLE IF NOT EXISTS team_member (
    team_member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT,
    team_member_name VARCHAR(50) NOT NULL,
    team_member_des TEXT,
    team_member_iscaptain BOOLEAN NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team(team_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 4.4 研究资料表(content)
CREATE TABLE IF NOT EXISTS content (
    content_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content_type_id BIGINT NOT NULL,
    auth_id BIGINT NOT NULL,
    content_name VARCHAR(50) NOT NULL,
    content_des VARCHAR(255),
    content_time DATETIME NOT NULL,
    content_isvalid BOOLEAN NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES topic(topic_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 4.5 研究资料内容块表(content_block)
CREATE TABLE IF NOT EXISTS content_block (
    content_block_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT NOT NULL,
    content_block_type INT NOT NULL COMMENT '0:文本 1:图片',
    content_block_order INT NOT NULL,
    content_block_des VARCHAR(255) NOT NULL,
    content_block_url VARCHAR(255),
    content_block_isvalid BOOLEAN NOT NULL,
    FOREIGN KEY (content_id) REFERENCES content(content_id)
);

-- 4.6 研究内容类型(content_type)
CREATE TABLE IF NOT EXISTS content_type (
    content_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_type_name VARCHAR(50) NOT NULL
);

-- 4.7 文档表(document)
CREATE TABLE IF NOT EXISTS document (
    document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    auth_id BIGINT NOT NULL,
    document_name VARCHAR(50) NOT NULL,
    document_des VARCHAR(255),
    document_type VARCHAR(50) NOT NULL,
    document_url VARCHAR(255) NOT NULL,
    document_time DATETIME NOT NULL,
    document_isvalid BOOLEAN NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES topic(topic_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 4.8 权限表(auth)
CREATE TABLE IF NOT EXISTS auth (
    auth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_name VARCHAR(50) NOT NULL
);

-- 创建培训资料库(his_material)
CREATE DATABASE IF NOT EXISTS his_material;
USE his_material;

-- 5.1 培训资料表(material)
CREATE TABLE IF NOT EXISTS material (
    material_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_title VARCHAR(50) NOT NULL,
    material_type VARCHAR(50) NOT NULL,
    materail_des VARCHAR(255),
    herb_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    material_time DATETIME NOT NULL,
    use_count INT NOT NULL DEFAULT 0,
    FOREIGN KEY (herb_id) REFERENCES his_herbinfo.herb(herb_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 5.2 培训资料内容表(content)
CREATE TABLE IF NOT EXISTS content (
    content_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL,
    content_type INT NOT NULL,
    content_order INT NOT NULL,
    content_des VARCHAR(255) NOT NULL,
    content_url VARCHAR(255),
    content_isvalid BOOLEAN NOT NULL,
    FOREIGN KEY (material_id) REFERENCES material(material_id)
);

-- 5.3 培训反馈表(feedback)
CREATE TABLE IF NOT EXISTS feedback (
    feedback_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    feedback_content VARCHAR(255) NOT NULL,
    submit_time DATETIME NOT NULL,
    rating INT NOT NULL,
    FOREIGN KEY (material_id) REFERENCES material(material_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 创建中药材评价库(herb_rating)
CREATE DATABASE IF NOT EXISTS herb_rating;
USE herb_rating;

-- 6.1 评价指标表(evaluation_indicator)
CREATE TABLE IF NOT EXISTS evaluation_indicator (
    indicator_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    indicator_name VARCHAR(50) NOT NULL,
    weight DECIMAL(5,2) NOT NULL
);

-- 6.2 中药材评价记录表(herb_evaluation)
CREATE TABLE IF NOT EXISTS herb_evaluation (
    evaluation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    herb_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    evaluation_state VARCHAR(50) NOT NULL,
    auditor_user_id BIGINT NOT NULL,
    total_score DECIMAL(5,2) NOT NULL,
    evaluate_time DATETIME NOT NULL,
    FOREIGN KEY (herb_id) REFERENCES his_herbinfo.herb(herb_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id),
    FOREIGN KEY (auditor_user_id) REFERENCES his_userinfo.user(user_id)
);

-- 6.3 评价明细表(evaluation_detail)
CREATE TABLE IF NOT EXISTS evaluation_detail (
    evaluation_detail_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluation_id BIGINT NOT NULL,
    indicator_id BIGINT NOT NULL,
    indicator_score DECIMAL(5,2) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    material VARCHAR(255) NOT NULL,
    FOREIGN KEY (evaluation_id) REFERENCES herb_evaluation(evaluation_id),
    FOREIGN KEY (indicator_id) REFERENCES evaluation_indicator(indicator_id)
);

-- 6.4 审核记录表(evaluation_application)
CREATE TABLE IF NOT EXISTS evaluation_application (
    application_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluation_id BIGINT NOT NULL,
    application_type BIGINT NOT NULL,
    application_state VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (evaluation_id) REFERENCES herb_evaluation(evaluation_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 6.5 单个中药材评分(herb_rating)
CREATE TABLE IF NOT EXISTS herb_rating (
    herb_evaluation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    herb_id BIGINT NOT NULL,
    total_score DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (herb_id) REFERENCES his_herbinfo.herb(herb_id)
);

-- 创建业绩管理库(his_performance)
CREATE DATABASE IF NOT EXISTS his_performance;
USE his_performance;

-- 7.1 业绩表(perform)
CREATE TABLE IF NOT EXISTS perform (
    perform_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perform_name VARCHAR(50) NOT NULL,
    perform_type_id BIGINT NOT NULL,
    perform_status INT NOT NULL COMMENT '0:申请中 1:已通过 2:已拒绝',
    perform_time DATETIME NOT NULL
);

-- 7.2 业绩参与人表(perform_member)
CREATE TABLE IF NOT EXISTS perform_member (
    perform_member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perform_id BIGINT NOT NULL,
    user_id BIGINT,
    perform_member_name VARCHAR(50) NOT NULL,
    perform_member_isenter BOOLEAN NOT NULL,
    FOREIGN KEY (perform_id) REFERENCES perform(perform_id),
    FOREIGN KEY (user_id) REFERENCES his_userinfo.user(user_id)
);

-- 7.3 业绩附件表(perform_file)
CREATE TABLE IF NOT EXISTS perform_file (
    perform_file_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perform_id BIGINT NOT NULL,
    perform_file_name VARCHAR(50) NOT NULL,
    perform_file_des VARCHAR(255),
    perform_file_type VARCHAR(50) NOT NULL,
    perform_file_url VARCHAR(255) NOT NULL,
    document_isvalid BOOLEAN NOT NULL,
    FOREIGN KEY (perform_id) REFERENCES perform(perform_id)
);

-- 7.4 业绩种类表(perform_type)
CREATE TABLE IF NOT EXISTS perform_type (
    content_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_type_name VARCHAR(50) NOT NULL
);

-- 添加外键约束（之前创建表时可能引用了未创建的表）
ALTER TABLE his_research.content ADD FOREIGN KEY (content_type_id) REFERENCES his_research.content_type(content_type_id);
ALTER TABLE his_research.content ADD FOREIGN KEY (auth_id) REFERENCES his_research.auth(auth_id);
ALTER TABLE his_research.document ADD FOREIGN KEY (auth_id) REFERENCES his_research.auth(auth_id);
ALTER TABLE his_performance.perform ADD FOREIGN KEY (perform_type_id) REFERENCES his_performance.perform_type(content_type_id);
