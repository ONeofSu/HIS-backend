drop database if exists his_herb_teaching;
create database his_herb_teaching;
use his_herb_teaching;
/*
 Navicat Premium Data Transfer

 Source Server         : ch_medcine
 Source Server Type    : MySQL
 Source Server Version : 80011 (8.0.11)
 Source Host           : localhost:3306
 Source Schema         : his_herb_teaching

 Target Server Type    : MySQL
 Target Server Version : 80011 (8.0.11)
 File Encoding         : 65001

 Date: 01/07/2025 12:58:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '课程主键',
  `course_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `cover_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `course_type` int(11) NOT NULL DEFAULT 1 COMMENT '0:选修, 1:必修',
  `course_object` int(11) NOT NULL DEFAULT 0 COMMENT '0:本科生, 1:研究生, 2:博士生',
  `teacher_id` bigint(20) NOT NULL COMMENT '授课教师ID (关联用户服务)',
  `course_start_time` datetime NOT NULL COMMENT '开始时间',
  `course_end_time` datetime NOT NULL COMMENT '结束时间',
  `course_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程描述',
  `course_average_rating` decimal(3, 2) NOT NULL DEFAULT 0.00 COMMENT '平均评分',
  `course_rating_count` int(11) NOT NULL DEFAULT 0 COMMENT '评分人数',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (2, '中药鉴定学（下）[2025修订版]', 'https://example.com/images/cover2.jpg', 1, 0, 2, '2025-02-20 08:00:00', '2025-06-20 18:00:00', '补充了最新的鉴定技术章节。', 5.00, 3);
INSERT INTO `course` VALUES (3, '研究生专题：中药炮制研究', 'https://example.com/images/cover3.jpg', 1, 1, 2, '2025-03-01 08:00:00', '2025-07-01 08:00:00', '深入探讨中药炮制的现代科学方法与研究进展。', 2.50, 2);
INSERT INTO `course` VALUES (4, '中药鉴定学（中）', 'https://example.com/images/cover_zydx.jpg', 1, 0, 2, '2025-09-01 09:00:00', '2026-01-15 18:00:00', '系统讲授中药鉴定学的基础理论与实践操作。', 3.50, 2);
INSERT INTO `course` VALUES (5, '中药鉴定学（综合）', 'https://example.com/images/cover_zydx.jpg', 1, 0, 2, '2025-09-01 09:00:00', '2026-01-15 18:00:00', '系统讲授中药鉴定学的基础理论与实践操作。', 0.00, 0);
INSERT INTO `course` VALUES (6, '中药鉴定学（下）', 'https://example.com/images/cover_zydx.jpg', 1, 0, 2, '2025-09-01 09:00:00', '2026-01-15 18:00:00', '系统讲授中药鉴定学的基础理论与实践操作。', 0.00, 0);
INSERT INTO `course` VALUES (7, '中药学（上）', 'https://example.com/images/cover_zydx.jpg', 1, 0, 2, '2025-09-01 09:00:00', '2026-01-15 18:00:00', '系统讲授中药鉴定学的基础理论与实践操作。', 0.00, 0);

-- ----------------------------
-- Table structure for course_herb_link
-- ----------------------------
DROP TABLE IF EXISTS `course_herb_link`;
CREATE TABLE `course_herb_link`  (
  `link_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `course_id` bigint(20) NOT NULL COMMENT '所属课程ID',
  `herb_id` bigint(20) NOT NULL COMMENT '关联药材ID',
  PRIMARY KEY (`link_id`) USING BTREE,
  UNIQUE INDEX `uk_course_herb`(`course_id` ASC, `herb_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程药材关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_herb_link
-- ----------------------------
INSERT INTO `course_herb_link` VALUES (6, 2, 1);
INSERT INTO `course_herb_link` VALUES (7, 2, 2);
INSERT INTO `course_herb_link` VALUES (5, 3, 2);

-- ----------------------------
-- Table structure for course_rating
-- ----------------------------
DROP TABLE IF EXISTS `course_rating`;
CREATE TABLE `course_rating`  (
  `rating_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `course_id` bigint(20) NOT NULL COMMENT '所属课程ID',
  `user_id` bigint(20) NOT NULL COMMENT '评分用户ID',
  `rating_value` int(11) NOT NULL COMMENT '评分星级 (0-5)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评分时间',
  PRIMARY KEY (`rating_id`) USING BTREE,
  UNIQUE INDEX `uk_course_user_rating`(`course_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程评分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_rating
-- ----------------------------
INSERT INTO `course_rating` VALUES (3, 2, 1, 5, '2025-04-01 09:00:00');
INSERT INTO `course_rating` VALUES (4, 2, 2, 5, '2025-06-30 23:38:38');
INSERT INTO `course_rating` VALUES (5, 2, 12345, 5, '2025-07-01 11:13:23');
INSERT INTO `course_rating` VALUES (6, 3, 1, 2, '2025-07-01 11:13:45');
INSERT INTO `course_rating` VALUES (7, 3, 2, 3, '2025-07-01 11:14:23');
INSERT INTO `course_rating` VALUES (8, 4, 1, 5, '2025-07-01 11:15:02');
INSERT INTO `course_rating` VALUES (9, 4, 2, 2, '2025-07-01 12:37:54');

-- ----------------------------
-- Table structure for course_resource
-- ----------------------------
DROP TABLE IF EXISTS `course_resource`;
CREATE TABLE `course_resource`  (
  `course_resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源主键',
  `course_id` bigint(20) NOT NULL COMMENT '所属课程主键',
  `course_resource_type` int(11) NOT NULL COMMENT '资源类型 (e.g., 0:视频, 1:文档)',
  `course_resource_order` int(11) NOT NULL DEFAULT 0 COMMENT '资源排序',
  `course_resource_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源标题',
  `course_resource_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文本内容或文件路径/URL',
  `course_resource_metadata` json NULL COMMENT '额外信息',
  `course_resource_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `course_resource_isvalid` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  PRIMARY KEY (`course_resource_id`) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_resource
-- ----------------------------
INSERT INTO `course_resource` VALUES (4, 1, 0, 0, '第一讲：中药学概论 (高清版).mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-06-30 23:44:05', 1);

-- ----------------------------
-- Table structure for lab
-- ----------------------------
DROP TABLE IF EXISTS `lab`;
CREATE TABLE `lab`  (
  `lab_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '实验主键',
  `course_id` bigint(20) NOT NULL COMMENT '所属课程主键',
  `lab_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验名称',
  `lab_steps` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '实验步骤 (建议使用Markdown)',
  `lab_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`lab_id`) USING BTREE,
  INDEX `idx_course_id`(`course_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lab
-- ----------------------------
INSERT INTO `lab` VALUES (3, 2, '校园常见药用植物识别', '1. 前往校园药草园。\n2. 根据图谱识别并采集至少5种药用植物。\n3. 制作植物标本。', 1);
INSERT INTO `lab` VALUES (6, 5, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);
INSERT INTO `lab` VALUES (7, 4, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);
INSERT INTO `lab` VALUES (8, 5, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);
INSERT INTO `lab` VALUES (9, 3, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);
INSERT INTO `lab` VALUES (10, 3, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);

-- ----------------------------
-- Table structure for user_course_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_course_collection`;
CREATE TABLE `user_course_collection`  (
  `collection_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `course_id` bigint(20) NOT NULL COMMENT '所属课程ID',
  `user_id` bigint(20) NOT NULL COMMENT '收藏用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`collection_id`) USING BTREE,
  UNIQUE INDEX `uk_course_user_collection`(`course_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户课程收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_course_collection
-- ----------------------------
INSERT INTO `user_course_collection` VALUES (6, 2, 12345, '2025-07-01 11:45:26');

SET FOREIGN_KEY_CHECKS = 1;
