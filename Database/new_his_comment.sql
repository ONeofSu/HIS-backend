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

 Date: 07/07/2025 13:59:47
*/

CREATE DATABASE IF NOT EXISTS his_comment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
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
  `original_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '原始内容(未过滤)',
  `sensitive_words` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '检测到的敏感词,逗号分隔',
  `sensitive_types` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '敏感词类型,逗号分隔',
  `is_filtered` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被过滤过(0-否,1-是)',
  `filter_level` tinyint(1) NOT NULL DEFAULT 0 COMMENT '过滤级别(0-无,1-轻度,2-中度,3-重度)',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_comment_target_user`(`target_type` ASC, `target_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_comment_time_deleted`(`create_time` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 'course', 7, 2, '这门课讲得很细致，受益匪浅！', 0, 1, 0, '2025-07-02 13:23:08', '2025-07-02 15:32:44', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (2, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 2, 0, '2025-07-02 13:33:49', '2025-07-02 13:33:49', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (3, 'course', 6, 2, '这门课讲得很细致，受益匪浅！', 0, 3, 1, '2025-07-02 13:36:23', '2025-07-02 15:32:42', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (4, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 4, 0, '2025-07-02 13:41:53', '2025-07-02 13:41:53', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (5, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 5, 0, '2025-07-02 13:43:25', '2025-07-02 13:43:25', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (6, 'course', 4, 1, '这门课讲得很细致，受益匪浅！', 0, 6, 0, '2025-07-02 13:46:39', '2025-07-02 15:32:11', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (7, 'course', 5, 2, '这门课讲得很细致，受益匪浅！', 0, 7, 0, '2025-07-02 13:49:34', '2025-07-02 15:32:40', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (8, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 8, 0, '2025-07-02 13:51:42', '2025-07-02 13:51:42', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (9, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 0, 9, 0, '2025-07-02 13:52:24', '2025-07-02 13:53:22', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (10, 'course', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 10, 0, '2025-07-02 13:55:50', '2025-07-02 15:32:37', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (11, 'course', 4, 3, '这门课讲得很细致，受益匪浅！', 0, 11, 0, '2025-07-02 13:56:49', '2025-07-02 13:56:49', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (12, 'course', 2, 1, '这门课讲得很细致，受益匪浅！', 0, 12, 0, '2025-07-02 13:57:02', '2025-07-02 14:57:37', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (13, 'course', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 13, 0, '2025-07-02 14:56:43', '2025-07-02 14:56:43', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (14, 'course', 5, 1, '这门课讲得很细致，受益匪浅！', 0, 14, 0, '2025-07-02 15:11:15', '2025-07-02 15:11:15', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (15, 'herb', 1, 1, '这门课讲得很细致，受益匪浅！', 0, 15, 0, '2025-07-02 15:23:49', '2025-07-02 15:23:49', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (16, 'herb', 2, 1, '这门课讲得很细致，受益匪浅！', 0, 16, 0, '2025-07-02 15:25:31', '2025-07-02 15:25:31', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (17, 'herb', 2, 3, '这门课讲得很细致，受益匪浅！', 0, 17, 0, '2025-07-02 15:34:33', '2025-07-02 15:34:33', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (18, 'herb', 2, 2, '这门课讲得很细致，受益匪浅！', 0, 18, 0, '2025-07-02 15:34:37', '2025-07-02 15:34:37', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (19, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 15, 15, 0, '2025-07-02 16:18:07', '2025-07-02 16:18:07', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (20, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 19, 15, 0, '2025-07-02 16:19:46', '2025-07-02 16:19:46', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (22, 'herb', 1, 2, '这门课讲得很细致，受益匪浅！', 20, 15, 0, '2025-07-02 16:34:16', '2025-07-02 16:34:16', 1, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (23, 'course', 4, 2, '这门课讲得很细致，受益匪浅！', 8, 8, 0, '2025-07-02 16:37:01', '2025-07-02 16:37:01', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (24, 'course', 4, 1, '这门课讲得很细致，受益匪浅！', 8, 8, 0, '2025-07-02 16:57:05', '2025-07-02 16:57:05', 0, NULL, NULL, NULL, 0, 0);
INSERT INTO `comment` VALUES (25, 'course', 2, 10, '这门课程内容很***和***，老师讲解得很清楚！', 0, 25, 0, '2025-07-07 13:37:43', '2025-07-07 13:37:43', 0, '这门课程内容很废物和智障，老师讲解得很清楚！', '废物,智障', '辱骂,辱骂', 1, 1);
INSERT INTO `comment` VALUES (26, 'course', 2, 10, '这门课程内容很***和***和***，老师讲解得很清楚！', 0, 26, 0, '2025-07-07 13:38:39', '2025-07-07 13:38:39', 0, '这门课程内容很废物和智障和垃圾，老师讲解得很清楚！', '废物,智障,垃圾', '辱骂,辱骂,辱骂', 1, 1);
INSERT INTO `comment` VALUES (27, 'herb', 1, 10, '人参的功效确实很***，但要注意用量。', 0, 27, 0, '2025-07-07 13:39:43', '2025-07-07 13:42:04', 1, '人参的功效确实很垃圾，但要注意用量。', '垃圾', '辱骂', 1, 1);
INSERT INTO `comment` VALUES (28, 'course', 2, 10, '确实不错，学到了很多实用的知识。', 25, 25, 0, '2025-07-07 13:40:14', '2025-07-07 13:40:14', 0, NULL, '', '', 0, 0);
INSERT INTO `comment` VALUES (29, 'course', 2, 10, '确实***，学到了很多实用的知识。', 25, 25, 0, '2025-07-07 13:40:18', '2025-07-07 13:40:18', 0, '确实垃圾，学到了很多实用的知识。', '垃圾', '辱骂', 1, 1);
INSERT INTO `comment` VALUES (30, 'course', 2, 10, '这门课程内容很***，老师讲解得很清楚！', 0, 30, 0, '2025-07-07 13:46:43', '2025-07-07 13:46:43', 0, '这门课程内容很测试敏感词，老师讲解得很清楚！', '测试敏感词', '违法内容', 1, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论点赞表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment_like
-- ----------------------------
INSERT INTO `comment_like` VALUES (5, 3, 10, '2025-07-02 15:44:16');

-- ----------------------------
-- Table structure for sensitive_words
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_words`;
CREATE TABLE `sensitive_words`  (
  `sensitive_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '敏感词主键',
  `sensitive_word` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '敏感词',
  `sensitive_word_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '敏感词类型',
  `sensitive_level` tinyint(4) NOT NULL DEFAULT 1 COMMENT '敏感级别(1-低,2-中,3-高)',
  `sensitive_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`sensitive_id`) USING BTREE,
  INDEX `idx_word_type`(`sensitive_word_type` ASC) USING BTREE,
  INDEX `idx_level`(`sensitive_level` ASC) USING BTREE,
  INDEX `idx_status`(`sensitive_status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_sensitive_type_status`(`sensitive_word_type` ASC, `sensitive_status` ASC) USING BTREE,
  INDEX `idx_sensitive_level_status`(`sensitive_level` ASC, `sensitive_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '敏感词表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sensitive_words
-- ----------------------------
INSERT INTO `sensitive_words` VALUES (1, '反对郭庆', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (2, '颠覆政权', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (3, '分裂国家', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (4, '台独', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (5, '藏独', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (6, '疆独', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (7, '港独', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (8, '法轮功', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (9, '六四', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (10, '天安门事件', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (11, '政治敏感', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (12, '反动', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (13, '反革命', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (14, '颠覆', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (15, '暴乱', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (16, '动乱', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (17, '政变', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (18, '反政府', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (19, '反党', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (20, '反社会主义', '政治敏感', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (21, '恐怖主义', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (22, '恐怖分子', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (23, '炸弹', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (24, '爆炸', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (25, '自杀式袭击', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (26, '极端组织', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (27, 'ISIS', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (28, '基地组织', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (29, '恐怖袭击', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (30, '暴力', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (31, '血腥', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (32, '恐怖', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (33, '极端分子', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (34, '恐怖组织', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (35, '爆炸物', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (36, '炸弹制作', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (37, '恐怖活动', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (38, '袭击', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (39, '杀人', '暴力恐怖', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (40, '自杀', '暴力恐怖', 2, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (41, '自残', '暴力恐怖', 2, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (42, '极端主义', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (43, '种族主义', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (44, '纳粹', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (45, '法西斯', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (46, '仇恨言论', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (47, '歧视', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (48, '排外', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (49, '民族仇恨', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (50, '宗教极端', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (51, '邪教', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (52, '极端思想', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (53, '种族歧视', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (54, '民族主义', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (55, '排外主义', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (56, '仇恨', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (57, '极端', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (58, '激进', '极端思想', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (59, '傻逼', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (60, '狗屎', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (61, '混蛋', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (62, '王八蛋', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (63, '贱人', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (64, '婊子', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (65, '狗娘养的', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (66, '去死', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (67, '该死', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (68, '垃圾', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (69, '废物', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (70, '白痴', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (71, '智障', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (72, '脑残', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (73, '傻叉', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (74, '狗东西', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (75, '贱货', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (76, '臭婊子', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (77, '死全家', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (78, '滚蛋', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (79, '去你妈的', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (80, '操你妈', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (81, '傻逼玩意', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (82, '垃圾玩意', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (83, '笨蛋', '辱骂攻击', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (84, '毒品', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (85, '大麻', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (86, '海洛因', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (87, '冰毒', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (88, '摇头丸', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (89, '色情', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (90, '黄色', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (91, '赌博', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (92, '博彩', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (93, '私服', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (94, '外挂', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (95, '盗版', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (96, '破解', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (97, '违禁品', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (98, '管制刀具', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (99, '枪支弹药', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (100, '非法交易', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (101, '洗钱', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (102, '诈骗', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (103, '传销', '其他', 3, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (104, '代刷', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (105, '刷单', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (106, '刷钻', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (107, '刷信誉', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (108, '刷流量', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (109, '刷粉丝', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (110, '代考', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (111, '代写', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (112, '代做', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (113, '代发', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (114, '群发', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (115, '推广', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (116, '垃圾广告', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (117, '恶意推广', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (118, '刷屏', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (119, '灌水', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (120, '垃圾信息', '垃圾信息', 1, 1, '2024-01-15 10:00:00', 1);
INSERT INTO `sensitive_words` VALUES (121, '更新更新的敏感词', '其他', 2, 1, '2025-07-07 13:46:19', 10);

-- ----------------------------
-- Triggers structure for table comment_like
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_comment_like_insert`;
delimiter ;;
CREATE TRIGGER `tr_comment_like_insert` AFTER INSERT ON `comment_like` FOR EACH ROW BEGIN
    UPDATE comment SET like_count = like_count + 1 WHERE comment_id = NEW.comment_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table comment_like
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_comment_like_delete`;
delimiter ;;
CREATE TRIGGER `tr_comment_like_delete` AFTER DELETE ON `comment_like` FOR EACH ROW BEGIN
    UPDATE comment SET like_count = like_count - 1 WHERE comment_id = OLD.comment_id;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
