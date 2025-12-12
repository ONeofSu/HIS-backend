/*
 Navicat Premium Dump SQL

 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : his_userinfo

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 08/07/2025 18:45:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invitation_code
-- ----------------------------
DROP TABLE IF EXISTS `invitation_code`;
CREATE TABLE `invitation_code`  (
  `code_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
  `category_id` bigint NOT NULL COMMENT '邀请类型主键',
  `user_id` bigint NOT NULL COMMENT '邀请人用户主键',
  `invite_school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请对象学校',
  `invite_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请对象姓名',
  `code_is_used` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已被使用',
  `code_expire_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`code_id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_category_id` FOREIGN KEY (`category_id`) REFERENCES `user_category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '邀请码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invitation_code
-- ----------------------------
INSERT INTO `invitation_code` VALUES (1, '8338A079', 2, 2, '中南大学', '张三', 1, '2025-07-07 15:21:56');
INSERT INTO `invitation_code` VALUES (2, 'FC35310E', 2, 2, '中南大学', '张三', 0, '2025-07-05 15:22:57');
INSERT INTO `invitation_code` VALUES (4, 'B63F867A', 2, 2, '中南大学', '张三', 1, '2025-07-05 15:41:48');
INSERT INTO `invitation_code` VALUES (5, '7C6403E5', 1, 17, '中南大学', '李四', 1, '2025-07-07 16:45:16');
INSERT INTO `invitation_code` VALUES (7, '4364CFD6', 1, 17, '中南大学', '李四', 1, '2025-07-07 16:47:51');
INSERT INTO `invitation_code` VALUES (8, '2BB5AD90', 1, 17, '中南大学', '李四', 0, '2025-07-07 16:47:52');
INSERT INTO `invitation_code` VALUES (9, 'CC2711E4', 1, 2, '中南大学', '李四', 0, '2025-07-07 16:58:15');
INSERT INTO `invitation_code` VALUES (10, '50658AE2', 1, 2, '中山大学', '李四', 0, '2025-07-07 16:58:23');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'root', 'root', '1111111', 'admin@gmail.com', NULL);
INSERT INTO `user` VALUES (4, 'student', '123', 'xxxxx2', 'xxxxx', 'xxxxx');
INSERT INTO `user` VALUES (6, 'admin', '123', '31243', '43552314@his.com', NULL);
INSERT INTO `user` VALUES (7, 'student2', '123', '31243', '43552344@his.com', NULL);
INSERT INTO `user` VALUES (8, 'studen123t', '123', '31243', '43552354@his.com', NULL);
INSERT INTO `user` VALUES (9, 'student123', '123', '31243', '435523234@his.com', NULL);
INSERT INTO `user` VALUES (10, 'student1', '123', '31243', '4355212334@his.com', NULL);
INSERT INTO `user` VALUES (11, 'student3', '123', '31243', '4355223534@his.com', NULL);
INSERT INTO `user` VALUES (12, 'student23', '123', '31243', '4355243234@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (13, 'normal', '123', '31243', '4355225134@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (15, 'admin1', '123', '31243', '4335255234@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (16, 'teacher1', '123', '31243', '4654355234@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (17, 'inviteTeacher1', '123', '31243', '4355216234@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (19, 'inviteStudent', '123', '31243', '4351575234@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (20, 'inviteStudent1', '123', '31243', '4355234963@his.com', 'xxxxxxx');
INSERT INTO `user` VALUES (24, 'test', '123', '432423', '13342087188@163.com', 'xxx');

-- ----------------------------
-- Table structure for user_category
-- ----------------------------
DROP TABLE IF EXISTS `user_category`;
CREATE TABLE `user_category`  (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_category
-- ----------------------------
INSERT INTO `user_category` VALUES (0, '普通用户');
INSERT INTO `user_category` VALUES (1, '学生');
INSERT INTO `user_category` VALUES (2, '教师');
INSERT INTO `user_category` VALUES (3, '管理员');
INSERT INTO `user_category` VALUES (4, '超级管理员');

-- ----------------------------
-- Table structure for user_link_category
-- ----------------------------
DROP TABLE IF EXISTS `user_link_category`;
CREATE TABLE `user_link_category`  (
  `ulc_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`ulc_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `user_link_category_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_link_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `user_category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_link_category
-- ----------------------------
INSERT INTO `user_link_category` VALUES (1, 2, 4);
INSERT INTO `user_link_category` VALUES (3, 4, 1);
INSERT INTO `user_link_category` VALUES (5, 6, 3);
INSERT INTO `user_link_category` VALUES (6, 7, 1);
INSERT INTO `user_link_category` VALUES (7, 8, 1);
INSERT INTO `user_link_category` VALUES (8, 9, 1);
INSERT INTO `user_link_category` VALUES (9, 10, 1);
INSERT INTO `user_link_category` VALUES (10, 11, 1);
INSERT INTO `user_link_category` VALUES (11, 12, 1);
INSERT INTO `user_link_category` VALUES (12, 13, 0);
INSERT INTO `user_link_category` VALUES (14, 15, 3);
INSERT INTO `user_link_category` VALUES (15, 16, 2);
INSERT INTO `user_link_category` VALUES (17, 17, 2);
INSERT INTO `user_link_category` VALUES (19, 19, 1);
INSERT INTO `user_link_category` VALUES (20, 20, 1);

-- ----------------------------
-- Table structure for user_link_invitation
-- ----------------------------
DROP TABLE IF EXISTS `user_link_invitation`;
CREATE TABLE `user_link_invitation`  (
  `uli_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户主键',
  `code_id` bigint NOT NULL COMMENT '邀请码主键',
  PRIMARY KEY (`uli_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_code_id`(`code_id` ASC) USING BTREE,
  CONSTRAINT `fk_uli_code` FOREIGN KEY (`code_id`) REFERENCES `invitation_code` (`code_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_uli_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户邀请码关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_link_invitation
-- ----------------------------
INSERT INTO `user_link_invitation` VALUES (1, 16, 1);
INSERT INTO `user_link_invitation` VALUES (2, 17, 4);
INSERT INTO `user_link_invitation` VALUES (4, 19, 5);
INSERT INTO `user_link_invitation` VALUES (5, 20, 7);

SET FOREIGN_KEY_CHECKS = 1;
