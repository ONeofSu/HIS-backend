/*
 Navicat Premium Data Transfer

 Source Server         : ch_medcine
 Source Server Type    : MySQL
 Source Server Version : 80011 (8.0.11)
 Source Host           : localhost:3306
 Source Schema         : his_comment

 Target Server Type    : MySQL
 Target Server Version : 80011 (8.0.11)
 File Encoding         : 65001

 Date: 02/07/2025 17:03:36
*/
CREATE DATABASE IF NOT EXISTS his_comment;
USE his_comment;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论主键',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` bigint(20) NOT NULL COMMENT '目标ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父评论ID',
  `root_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '根评论ID',
  `like_count` int(11) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 'course', 7, 2, '这门课讲得很细致，受益匪浅！', 0, 1, 0, '2025-07-02 13:23:08', '2025-07-02 15:32:44', 1);
INSERT INTO `comment` VALUES (2, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 2, 0, '2025-07-02 13:33:49', '2025-07-02 13:33:49', 0);
INSERT INTO `comment` VALUES (3, 'course', 6, 2, '这门课讲得很细致，受益匪浅！', 0, 3, 1, '2025-07-02 13:36:23', '2025-07-02 15:32:42', 0);
INSERT INTO `comment` VALUES (4, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 4, 0, '2025-07-02 13:41:53', '2025-07-02 13:41:53', 0);
INSERT INTO `comment` VALUES (5, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 5, 0, '2025-07-02 13:43:25', '2025-07-02 13:43:25', 0);
INSERT INTO `comment` VALUES (6, 'course', 4, 1, '这门课讲得很细致，受益匪浅！', 0, 6, 0, '2025-07-02 13:46:39', '2025-07-02 15:32:11', 0);
INSERT INTO `comment` VALUES (7, 'course', 5, 2, '这门课讲得很细致，受益匪浅！', 0, 7, 0, '2025-07-02 13:49:34', '2025-07-02 15:32:40', 1);
INSERT INTO `comment` VALUES (8, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 8, 0, '2025-07-02 13:51:42', '2025-07-02 13:51:42', 0);
INSERT INTO `comment` VALUES (9, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 9, 0, '2025-07-02 13:52:24', '2025-07-02 13:53:22', 0);
INSERT INTO `comment` VALUES (10, 'course', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 10, 0, '2025-07-02 13:55:50', '2025-07-02 15:32:37', 0);
INSERT INTO `comment` VALUES (11, 'course', 4, 3, '这门课讲得很细致，受益匪浅！', 0, 11, 0, '2025-07-02 13:56:49', '2025-07-02 13:56:49', 1);
INSERT INTO `comment` VALUES (12, 'course', 2, 1, '这门课讲得很细致，受益匪浅！', 0, 12, 0, '2025-07-02 13:57:02', '2025-07-02 14:57:37', 0);
INSERT INTO `comment` VALUES (13, 'course', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 13, 0, '2025-07-02 14:56:43', '2025-07-02 14:56:43', 0);
INSERT INTO `comment` VALUES (14, 'course', 5, 1, '这门课讲得很细致，受益匪浅！', 0, 14, 0, '2025-07-02 15:11:15', '2025-07-02 15:11:15', 0);
INSERT INTO `comment` VALUES (15, 'herb', 1, 1, '这门课讲得很细致，受益匪浅！', 0, 15, 0, '2025-07-02 15:23:49', '2025-07-02 15:23:49', 1);
INSERT INTO `comment` VALUES (16, 'herb', 2, 1, '这门课讲得很细致，受益匪浅！', 0, 16, 0, '2025-07-02 15:25:31', '2025-07-02 15:25:31', 1);
INSERT INTO `comment` VALUES (17, 'herb', 2, 3, '这门课讲得很细致，受益匪浅！', 0, 17, 0, '2025-07-02 15:34:33', '2025-07-02 15:34:33', 1);
INSERT INTO `comment` VALUES (18, 'herb', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 18, 0, '2025-07-02 15:34:37', '2025-07-02 15:34:37', 1);
INSERT INTO `comment` VALUES (19, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 15, 15, 0, '2025-07-02 16:18:07', '2025-07-02 16:18:07', 1);
INSERT INTO `comment` VALUES (20, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 19, 15, 0, '2025-07-02 16:19:46', '2025-07-02 16:19:46', 1);
INSERT INTO `comment` VALUES (22, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 20, 15, 0, '2025-07-02 16:34:16', '2025-07-02 16:34:16', 1);
INSERT INTO `comment` VALUES (23, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 8, 8, 0, '2025-07-02 16:37:01', '2025-07-02 16:37:01', 0);
INSERT INTO `comment` VALUES (24, 'course', 4, 1, '这门课讲得很细致，受益匪浅！', 8, 8, 0, '2025-07-02 16:57:05', '2025-07-02 16:57:05', 0);

-- ----------------------------
-- Table structure for comment_like
-- ----------------------------
DROP TABLE IF EXISTS `comment_like`;
CREATE TABLE `comment_like`  (
  `like_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '点赞主键',
  `comment_id` bigint(20) NOT NULL COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`like_id`) USING BTREE,
  INDEX `fk_comment_id`(`comment_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_id` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_like
-- ----------------------------
INSERT INTO `comment_like` VALUES (5, 3, 3, '2025-07-02 15:44:16');

SET FOREIGN_KEY_CHECKS = 1;
