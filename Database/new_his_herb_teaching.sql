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

 Date: 30/06/2025 15:36:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `course_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_start_time` datetime NOT NULL,
  `course_end_time` datetime NOT NULL,
  `course_des` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `course_type` int(11) NOT NULL DEFAULT 1 COMMENT '0:选修 1:必修',
  `course_object` int(11) NOT NULL DEFAULT 0 COMMENT '0:本科生 1:研究生 2:博士生',
  PRIMARY KEY (`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (5, '6月30日新加入的当归课程', '2024-03-01 08:00:00', '2024-07-01 08:00:00', '这是一个测试课程', 1, 0);
INSERT INTO `course` VALUES (6, '人参切片课程', '2024-03-01 08:00:00', '2024-07-01 08:00:00', '这是一个测试课程', 0, 1);
INSERT INTO `course` VALUES (7, '丁香标本制作课程', '2024-03-01 08:00:00', '2024-07-01 08:00:00', '这是一个测试课程', 0, 1);

-- ----------------------------
-- Table structure for lab
-- ----------------------------
DROP TABLE IF EXISTS `lab`;
CREATE TABLE `lab`  (
  `lab_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `course_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `lab_submit_time` datetime NOT NULL,
  `lab_end_time` datetime NOT NULL,
  `lab_des` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`lab_id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `lab_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab
-- ----------------------------
INSERT INTO `lab` VALUES (16, 5, 1, '2025-06-30 14:05:30', '2025-01-01 02:00:00', '这是课程5的某个实验');
INSERT INTO `lab` VALUES (17, 6, 1, '2025-06-30 14:05:39', '2025-01-01 02:00:00', '这是课程6的某个实验');

-- ----------------------------
-- Table structure for lab_info
-- ----------------------------
DROP TABLE IF EXISTS `lab_info`;
CREATE TABLE `lab_info`  (
  `lab_info_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lab_id` bigint(20) NOT NULL,
  `herb_id` bigint(20) NULL DEFAULT NULL COMMENT '关联的草药ID，可以为空',
  `lab_info_goal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验目的',
  `lab_info_summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '实验总结',
  `lab_info_isvalid` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`lab_info_id`) USING BTREE,
  INDEX `lab_id`(`lab_id` ASC) USING BTREE,
  INDEX `herb_id`(`herb_id` ASC) USING BTREE,
  CONSTRAINT `lab_info_ibfk_1` FOREIGN KEY (`lab_id`) REFERENCES `lab` (`lab_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_info
-- ----------------------------
INSERT INTO `lab_info` VALUES (11, 16, 1, '这是更新了实验16的详情', '更新后的实验总结', 1);
INSERT INTO `lab_info` VALUES (12, 17, 1, '学习实验目标', '实验总结', 1);

-- ----------------------------
-- Table structure for lab_resource
-- ----------------------------
DROP TABLE IF EXISTS `lab_resource`;
CREATE TABLE `lab_resource`  (
  `lab_resource_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lab_id` bigint(20) NOT NULL,
  `lab_resource_type` int(11) NOT NULL COMMENT '0:文本 1:图片 2:视频 3:文档',
  `lab_resource_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序，0为封面',
  `lab_resource_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源标题',
  `lab_resource_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文本内容或文件路径',
  `lab_resource_metadata` json NULL COMMENT '额外信息，如视频长度、图片尺寸等',
  `lab_resource_time` datetime NOT NULL,
  `lab_resource_isvalid` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`lab_resource_id`) USING BTREE,
  INDEX `idx_lab_resource_type`(`lab_id` ASC, `lab_resource_type` ASC) USING BTREE,
  INDEX `idx_resource_order`(`lab_id` ASC, `lab_resource_order` ASC) USING BTREE,
  CONSTRAINT `lab_resource_ibfk_1` FOREIGN KEY (`lab_id`) REFERENCES `lab` (`lab_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lab_resource
-- ----------------------------
INSERT INTO `lab_resource` VALUES (13, 16, 3, 2, '实验报告', 'https://qingkaka.oss-cn-hongkong.aliyuncs.com/documents/1742536740688-kvsb4d-NgDB4UZrloy679a94cc57b36ce7efdf371080d7784cb.pptx', '{\"size\": \"2MB\", \"pages\": 12}', '2025-06-30 14:36:10', 1);

SET FOREIGN_KEY_CHECKS = 1;
