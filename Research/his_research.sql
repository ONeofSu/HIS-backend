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