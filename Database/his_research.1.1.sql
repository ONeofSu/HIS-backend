/*
 Navicat Premium Dump SQL

 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : his_research

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 08/07/2025 18:47:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth
-- ----------------------------
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth`  (
  `auth_id` bigint NOT NULL AUTO_INCREMENT,
  `auth_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth
-- ----------------------------
INSERT INTO `auth` VALUES (1, '私有');
INSERT INTO `auth` VALUES (2, '团队成员可见');
INSERT INTO `auth` VALUES (3, '公开');

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`  (
  `content_id` bigint NOT NULL AUTO_INCREMENT,
  `topic_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content_type_id` bigint NOT NULL,
  `auth_id` bigint NOT NULL,
  `content_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `content_time` datetime NOT NULL,
  `content_isvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`content_id`) USING BTREE,
  INDEX `topic_id`(`topic_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `content_type_id`(`content_type_id` ASC) USING BTREE,
  INDEX `auth_id`(`auth_id` ASC) USING BTREE,
  CONSTRAINT `content_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`topic_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `content_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `content_ibfk_3` FOREIGN KEY (`content_type_id`) REFERENCES `content_type` (`content_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `content_ibfk_4` FOREIGN KEY (`auth_id`) REFERENCES `auth` (`auth_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (1, 1, 4, 1, 1, '水出来的实验', '水一水', '2025-07-05 09:13:12', 1);
INSERT INTO `content` VALUES (2, 7, 4, 1, 1, '水出来的论文,只有我自己能看', '水一水', '2025-07-05 09:22:23', 1);
INSERT INTO `content` VALUES (3, 7, 4, 2, 2, '保密', '水一水', '2025-07-05 09:24:47', 1);
INSERT INTO `content` VALUES (4, 7, 4, 3, 3, '成果3', '现在补水了', '2025-07-05 09:25:28', 1);
INSERT INTO `content` VALUES (5, 7, 4, 3, 3, '成果2', '水一水', '2025-07-05 09:26:04', 0);

-- ----------------------------
-- Table structure for content_block
-- ----------------------------
DROP TABLE IF EXISTS `content_block`;
CREATE TABLE `content_block`  (
  `content_block_id` bigint NOT NULL AUTO_INCREMENT,
  `content_id` bigint NOT NULL,
  `content_block_type` int NOT NULL COMMENT '0:文本 1:图片',
  `content_block_order` int NOT NULL,
  `content_block_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_block_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `content_block_isvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`content_block_id`) USING BTREE,
  INDEX `content_id`(`content_id` ASC) USING BTREE,
  CONSTRAINT `content_block_ibfk_1` FOREIGN KEY (`content_id`) REFERENCES `content` (`content_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_block
-- ----------------------------
INSERT INTO `content_block` VALUES (1, 4, 0, 1, '目前国内外研究现状是', NULL, 0);
INSERT INTO `content_block` VALUES (2, 4, 1, 2, '非常不错的,由此可见', 'xxxxx', 0);
INSERT INTO `content_block` VALUES (3, 4, 0, 3, '目前国内外研究现状是', NULL, 0);
INSERT INTO `content_block` VALUES (4, 4, 1, 4, '非常不错的,由此可见', 'xxxxx', 0);
INSERT INTO `content_block` VALUES (5, 4, 0, 1, '目前国内外研究现状是', NULL, 0);
INSERT INTO `content_block` VALUES (6, 4, 1, 2, '不是不错的,由此可见', 'xxxxx', 0);
INSERT INTO `content_block` VALUES (7, 2, 0, 1, '目前国内外研究现状是', NULL, 1);
INSERT INTO `content_block` VALUES (8, 2, 1, 2, '非常不错的,由此可见', 'xxxxx', 1);
INSERT INTO `content_block` VALUES (9, 4, 0, 1, '目前国内外研究现状是', NULL, 1);
INSERT INTO `content_block` VALUES (10, 4, 1, 2, '不是不错的,由此可见', 'xxxxx', 1);

-- ----------------------------
-- Table structure for content_type
-- ----------------------------
DROP TABLE IF EXISTS `content_type`;
CREATE TABLE `content_type`  (
  `content_type_id` bigint NOT NULL AUTO_INCREMENT,
  `content_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`content_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_type
-- ----------------------------
INSERT INTO `content_type` VALUES (1, '论文');
INSERT INTO `content_type` VALUES (2, '实验数据');
INSERT INTO `content_type` VALUES (3, '分析报告');
INSERT INTO `content_type` VALUES (4, '参考资料');

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document`  (
  `document_id` bigint NOT NULL AUTO_INCREMENT,
  `topic_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `auth_id` bigint NOT NULL,
  `document_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `document_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `document_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `document_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `document_time` datetime NOT NULL,
  `document_isvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`document_id`) USING BTREE,
  INDEX `topic_id`(`topic_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `auth_id`(`auth_id` ASC) USING BTREE,
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`topic_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `document_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `document_ibfk_3` FOREIGN KEY (`auth_id`) REFERENCES `auth` (`auth_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of document
-- ----------------------------
INSERT INTO `document` VALUES (1, 7, 4, 2, 'document', 'this is a document', '.doc', 'xxxxxxxxxx', '2025-07-05 18:31:27', 1);
INSERT INTO `document` VALUES (2, 7, 4, 3, 'document', 'this is a document', '.doc', 'xxxxxxxxxx', '2025-07-05 18:18:50', 1);
INSERT INTO `document` VALUES (3, 7, 4, 1, 'document', 'this is a document', '.doc', 'xxxxxxxxxx', '2025-07-05 18:18:55', 1);
INSERT INTO `document` VALUES (4, 7, 4, 3, 'document', 'this is a document', '.doc', 'xxxxxxxxxx', '2025-07-05 18:18:56', 0);
INSERT INTO `document` VALUES (5, 7, 7, 3, 'document', 'this is a document', '.doc', 'xxxxxxxxxx', '2025-07-05 18:53:59', 1);

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `team_id` bigint NOT NULL AUTO_INCREMENT,
  `team_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `team_time` datetime NOT NULL,
  `team_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `team_isvalid` tinyint NOT NULL,
  PRIMARY KEY (`team_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of team
-- ----------------------------
INSERT INTO `team` VALUES (1, 'xx23x', '2024-10-25 14:30:00', 'xxxxxx', 1);
INSERT INTO `team` VALUES (2, 'xxx', '2024-10-25 14:30:00', 'xxxxxx', 1);
INSERT INTO `team` VALUES (4, 'xxx432', '2024-10-25 14:30:00', 'xxxxxx', 0);
INSERT INTO `team` VALUES (5, '5队', '2024-10-25 14:30:00', '5队最牛', 0);
INSERT INTO `team` VALUES (6, '爱玩权限没办法的', '2024-10-25 14:30:00', 'xxxxxx', 1);

-- ----------------------------
-- Table structure for team_member
-- ----------------------------
DROP TABLE IF EXISTS `team_member`;
CREATE TABLE `team_member`  (
  `team_member_id` bigint NOT NULL AUTO_INCREMENT,
  `team_id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `team_member_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `team_member_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `team_member_iscaptain` tinyint(1) NOT NULL,
  PRIMARY KEY (`team_member_id`) USING BTREE,
  INDEX `team_id`(`team_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `team_member_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`team_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `team_member_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of team_member
-- ----------------------------
INSERT INTO `team_member` VALUES (1, 1, NULL, '不是很好', '很厉害', 0);
INSERT INTO `team_member` VALUES (2, 1, NULL, '你好你好你好2', '很厉害', 0);
INSERT INTO `team_member` VALUES (4, 1, 2, '你好你好你好3', '很厉害', 0);
INSERT INTO `team_member` VALUES (5, 1, 4, '你好你好你好3', '很厉害', 0);
INSERT INTO `team_member` VALUES (7, 5, 4, '这是我', '很厉害', 0);
INSERT INTO `team_member` VALUES (8, 5, 2, '这不是我', '很厉害', 0);
INSERT INTO `team_member` VALUES (9, 5, 10, '这也不是我', '很厉害', 1);
INSERT INTO `team_member` VALUES (11, 6, 10, '这也不是我', '很厉害', 1);
INSERT INTO `team_member` VALUES (12, 6, 7, '这也不是我', '很厉害', 0);
INSERT INTO `team_member` VALUES (13, 6, NULL, '这也不是我', '很厉害', 0);

-- ----------------------------
-- Table structure for topic
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic`  (
  `topic_id` bigint NOT NULL AUTO_INCREMENT,
  `topic_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `team_id` bigint NOT NULL,
  `topic_start_time` datetime NOT NULL,
  `topic_end_time` datetime NOT NULL,
  `topic_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `topic_status` int NOT NULL COMMENT '0:立项中 1:进行中 2:已结题',
  `topic_isvalid` tinyint NOT NULL,
  PRIMARY KEY (`topic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of topic
-- ----------------------------
INSERT INTO `topic` VALUES (1, '把大象放进冰箱的研究1', 1, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 1, 1);
INSERT INTO `topic` VALUES (2, '把大象放进冰箱的研究2', 1, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 0, 1);
INSERT INTO `topic` VALUES (4, '把大象放进冰箱的研究13', 1, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 0, 1);
INSERT INTO `topic` VALUES (5, '把大象放进冰箱的研究4', 1, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 0, 0);
INSERT INTO `topic` VALUES (6, '2队', 2, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 0, 1);
INSERT INTO `topic` VALUES (7, '5队', 5, '2024-10-25 14:30:00', '2025-09-02 12:00:00', 'xxxxxxxxxxxxx', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
