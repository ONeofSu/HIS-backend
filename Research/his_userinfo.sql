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
