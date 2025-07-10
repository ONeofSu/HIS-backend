/*
 Navicat Premium Data Transfer

 Source Server         : ch_medcine
 Source Server Type    : MySQL
 Source Server Version : 80011 (8.0.11)
 Source Host           : localhost:3306
 Source Schema         : his_performance

 Target Server Type    : MySQL
 Target Server Version : 80011 (8.0.11)
 File Encoding         : 65001

 Date: 08/07/2025 18:21:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for perform
-- ----------------------------
DROP TABLE IF EXISTS `perform`;
CREATE TABLE `perform`  (
  `perform_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业绩主键',
  `perform_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业绩(工作)名称',
  `perform_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '业绩内容',
  `perform_type_id` bigint(20) NOT NULL COMMENT '业绩类型',
  `perform_status` int(11) NOT NULL DEFAULT 0 COMMENT '业绩状态:0:草稿1:已提交2:已通过3:已驳回',
  `perform_time` datetime NOT NULL COMMENT '提交日期',
  `perform_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核评语',
  `submit_user_id` bigint(20) NOT NULL COMMENT '提交人ID',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_by` bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
  PRIMARY KEY (`perform_id`) USING BTREE,
  INDEX `idx_perform_type`(`perform_type_id` ASC) USING BTREE,
  INDEX `idx_perform_status`(`perform_status` ASC) USING BTREE,
  INDEX `idx_submit_user`(`submit_user_id` ASC) USING BTREE,
  INDEX `idx_audit_by`(`audit_by` ASC) USING BTREE,
  INDEX `idx_perform_time`(`perform_time` ASC) USING BTREE,
  CONSTRAINT `fk_perform_type` FOREIGN KEY (`perform_type_id`) REFERENCES `perform_type` (`perform_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '业绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of perform
-- ----------------------------
INSERT INTO `perform` VALUES (1, '中草药鉴定学教学成果过aaa', '本人在2024年春季学期承担中药鉴定学课程教学任务，完成了理论教学48学时，实验教学32学时，学生满意度达到95%以上。', 1, 0, '2024-12-15 10:00:00', NULL, 2, '2025-07-07 11:20:03', '2025-07-07 11:20:03', NULL, NULL);

-- ----------------------------
-- Table structure for perform_file
-- ----------------------------
DROP TABLE IF EXISTS `perform_file`;
CREATE TABLE `perform_file`  (
  `perform_file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文档主键',
  `perform_id` bigint(20) NOT NULL COMMENT '所属业绩',
  `perform_file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文档名称',
  `perform_file_des` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文档描述',
  `perform_file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文档类型',
  `perform_file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件URL',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `perform_file_isvalid` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `upload_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `upload_by` bigint(20) NOT NULL COMMENT '上传人ID',
  PRIMARY KEY (`perform_file_id`) USING BTREE,
  INDEX `idx_perform_id`(`perform_id` ASC) USING BTREE,
  INDEX `idx_upload_by`(`upload_by` ASC) USING BTREE,
  INDEX `idx_upload_time`(`upload_time` ASC) USING BTREE,
  CONSTRAINT `perform_file_ibfk_1` FOREIGN KEY (`perform_id`) REFERENCES `perform` (`perform_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '业绩附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of perform_file
-- ----------------------------

-- ----------------------------
-- Table structure for perform_type
-- ----------------------------
DROP TABLE IF EXISTS `perform_type`;
CREATE TABLE `perform_type`  (
  `perform_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业绩种类主键',
  `perform_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业绩种类名称',
  `perform_type_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业绩种类描述',
  `is_active` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`perform_type_id`) USING BTREE,
  INDEX `idx_is_active`(`is_active` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '业绩种类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of perform_type
-- ----------------------------
INSERT INTO `perform_type` VALUES (1, '教学成果', '教学相关的业绩成果', 1, 1, '2025-07-07 10:59:31', '2025-07-07 10:59:31');
INSERT INTO `perform_type` VALUES (2, '科研成果', '科研相关的业绩成果', 1, 2, '2025-07-07 10:59:31', '2025-07-07 10:59:31');
INSERT INTO `perform_type` VALUES (3, '社会服务', '社会服务相关的业绩成果', 1, 3, '2025-07-07 10:59:31', '2025-07-07 10:59:31');
INSERT INTO `perform_type` VALUES (4, '学术交流', '学术交流相关的业绩成果', 1, 4, '2025-07-07 10:59:31', '2025-07-07 10:59:31');
INSERT INTO `perform_type` VALUES (5, '其他业绩', '其他类型的业绩成果', 1, 5, '2025-07-07 10:59:31', '2025-07-07 10:59:31');

-- ----------------------------
-- View structure for v_perform_detail
-- ----------------------------
DROP VIEW IF EXISTS `v_perform_detail`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_perform_detail` AS select `p`.`perform_id` AS `perform_id`,`p`.`perform_name` AS `perform_name`,`p`.`perform_content` AS `perform_content`,`p`.`perform_type_id` AS `perform_type_id`,`pt`.`perform_type_name` AS `perform_type_name`,`p`.`perform_status` AS `perform_status`,`p`.`perform_time` AS `perform_time`,`p`.`perform_comment` AS `perform_comment`,`p`.`submit_user_id` AS `submit_user_id`,`p`.`created_time` AS `created_time`,`p`.`updated_time` AS `updated_time`,`p`.`audit_time` AS `audit_time`,`p`.`audit_by` AS `audit_by`,(case `p`.`perform_status` when 0 then '草稿' when 1 then '已提交' when 2 then '已通过' when 3 then '已驳回' else '未知状态' end) AS `perform_status_name` from (`perform` `p` left join `perform_type` `pt` on((`p`.`perform_type_id` = `pt`.`perform_type_id`))) where (`pt`.`is_active` = TRUE);

-- ----------------------------
-- View structure for v_perform_file_detail
-- ----------------------------
DROP VIEW IF EXISTS `v_perform_file_detail`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_perform_file_detail` AS select `pf`.`perform_file_id` AS `perform_file_id`,`pf`.`perform_id` AS `perform_id`,`pf`.`perform_file_name` AS `perform_file_name`,`pf`.`perform_file_des` AS `perform_file_des`,`pf`.`perform_file_type` AS `perform_file_type`,`pf`.`perform_file_url` AS `perform_file_url`,`pf`.`file_size` AS `file_size`,`pf`.`perform_file_isvalid` AS `perform_file_isvalid`,`pf`.`upload_time` AS `upload_time`,`pf`.`upload_by` AS `upload_by`,`p`.`perform_name` AS `perform_name`,`p`.`perform_status` AS `perform_status` from (`perform_file` `pf` left join `perform` `p` on((`pf`.`perform_id` = `p`.`perform_id`))) where (`pf`.`perform_file_isvalid` = TRUE);

SET FOREIGN_KEY_CHECKS = 1;
