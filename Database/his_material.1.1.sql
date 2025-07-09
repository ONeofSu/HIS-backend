/*
 Navicat Premium Dump SQL

 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : his_material

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 08/07/2025 18:47:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for content
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content`  (
  `content_id` bigint NOT NULL AUTO_INCREMENT,
  `material_id` bigint NOT NULL,
  `content_type` int NOT NULL,
  `content_order` int NOT NULL,
  `content_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content_isvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`content_id`) USING BTREE,
  INDEX `material_id`(`material_id` ASC) USING BTREE,
  CONSTRAINT `content_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `material` (`material_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES (1, 10, 0, 1, 'xxx', 'null', 1);
INSERT INTO `content` VALUES (2, 11, 0, 1, 'xxx', 'null', 1);
INSERT INTO `content` VALUES (3, 11, 2, 2, 'xxx', 'xxxx', 1);
INSERT INTO `content` VALUES (4, 12, 0, 1, 'xxx', 'null', 1);
INSERT INTO `content` VALUES (5, 12, 2, 2, 'xxx', 'xxxx', 1);
INSERT INTO `content` VALUES (6, 13, 0, 1, 'xxx', 'null', 1);
INSERT INTO `content` VALUES (7, 13, 2, 2, 'xxx', 'xxxx', 1);
INSERT INTO `content` VALUES (8, 14, 0, 1, 'xxx', 'null', 0);
INSERT INTO `content` VALUES (9, 14, 2, 2, 'xxx', 'xxxx', 0);
INSERT INTO `content` VALUES (10, 16, 0, 1, 'xxx', 'null', 0);
INSERT INTO `content` VALUES (11, 16, 2, 2, 'xxx', 'xxxx', 0);
INSERT INTO `content` VALUES (18, 16, 0, 1, 'xxx111', 'null', 0);
INSERT INTO `content` VALUES (19, 16, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (20, 16, 0, 1, 'xxx111', 'null', 0);
INSERT INTO `content` VALUES (21, 16, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (22, 15, 0, 1, 'xxx111', NULL, 0);
INSERT INTO `content` VALUES (23, 15, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (24, 15, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (25, 15, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (26, 15, 0, 1, 'xxx', NULL, 0);
INSERT INTO `content` VALUES (27, 15, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (28, 15, 0, 1, 'xxx', NULL, 0);
INSERT INTO `content` VALUES (29, 15, 2, 2, 'xxx111', 'xxxx', 0);
INSERT INTO `content` VALUES (30, 15, 0, 1, 'xxx', NULL, 1);
INSERT INTO `content` VALUES (31, 15, 2, 2, 'xxx111', 'xxxx', 1);

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `feedback_id` bigint NOT NULL AUTO_INCREMENT,
  `material_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `feedback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `submit_time` datetime NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`feedback_id`) USING BTREE,
  INDEX `material_id`(`material_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `material` (`material_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (1, 15, 9, 'xxxx', '2025-07-01 20:10:50', 98);

-- ----------------------------
-- Table structure for live_record
-- ----------------------------
DROP TABLE IF EXISTS `live_record`;
CREATE TABLE `live_record`  (
  `live_record_id` bigint NOT NULL AUTO_INCREMENT,
  `live_room_id` bigint NOT NULL COMMENT '直播间主键',
  `live_record_file_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名',
  `live_record_file_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件路径',
  `live_record_file_size` bigint NULL DEFAULT NULL COMMENT '文件大小(字节)',
  `live_record_duration` int NULL DEFAULT NULL COMMENT '时长(秒)',
  `live_record_status` int NOT NULL DEFAULT 0 COMMENT '状态: 0:录制中 1:录制完成 2:处理中 3:可用 4:删除',
  `live_record_start_time` datetime NOT NULL COMMENT '开始时间',
  `live_record_end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `live_record_creat_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立时间',
  `live_record_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`live_record_id`) USING BTREE,
  INDEX `idx_room_id`(`live_room_id` ASC) USING BTREE,
  INDEX `idx_status`(`live_record_status` ASC) USING BTREE,
  INDEX `idx_start_time`(`live_record_start_time` ASC) USING BTREE,
  CONSTRAINT `fk_live_record_room` FOREIGN KEY (`live_room_id`) REFERENCES `live_room` (`live_room_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '直播录像表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of live_record
-- ----------------------------
INSERT INTO `live_record` VALUES (1, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751510616533.mp4', NULL, NULL, NULL, 4, '2025-07-03 10:43:37', NULL, '2025-07-03 10:43:37', '2025-07-03 10:49:39');
INSERT INTO `live_record` VALUES (2, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751510645785.mp4', NULL, NULL, NULL, 4, '2025-07-03 10:44:06', NULL, '2025-07-03 10:44:06', '2025-07-03 10:49:41');
INSERT INTO `live_record` VALUES (3, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751511001987.mp4', NULL, NULL, NULL, 4, '2025-07-03 10:50:02', '2025-07-03 10:50:11', '2025-07-03 10:50:02', '2025-07-03 11:06:23');
INSERT INTO `live_record` VALUES (4, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751511703516.mp4', NULL, NULL, NULL, 4, '2025-07-03 11:01:44', '2025-07-03 11:01:58', '2025-07-03 11:01:44', '2025-07-03 11:06:26');
INSERT INTO `live_record` VALUES (5, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751511996507.mp4', NULL, NULL, NULL, 4, '2025-07-03 11:06:37', NULL, '2025-07-03 11:06:37', '2025-07-03 11:09:29');
INSERT INTO `live_record` VALUES (6, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751512171570.mp4', NULL, NULL, NULL, 4, '2025-07-03 11:09:32', '2025-07-03 11:15:10', '2025-07-03 11:09:32', '2025-07-03 11:17:23');
INSERT INTO `live_record` VALUES (7, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751512648150.mp4', NULL, NULL, NULL, 1, '2025-07-03 11:17:28', '2025-07-03 11:18:10', '2025-07-03 11:17:28', '2025-07-03 11:18:10');
INSERT INTO `live_record` VALUES (8, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751512805425.mp4', NULL, NULL, NULL, 4, '2025-07-03 11:20:05', '2025-07-03 11:20:17', '2025-07-03 11:20:05', '2025-07-03 11:20:20');
INSERT INTO `live_record` VALUES (9, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751519507702.mp4', NULL, NULL, NULL, 4, '2025-07-03 13:11:48', '2025-07-03 13:12:47', '2025-07-03 13:11:48', '2025-07-03 13:12:50');
INSERT INTO `live_record` VALUES (10, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751519838742.mp4', NULL, NULL, NULL, 4, '2025-07-03 13:17:19', '2025-07-03 13:17:27', '2025-07-03 13:17:19', '2025-07-03 13:17:27');
INSERT INTO `live_record` VALUES (11, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751520043106.mp4', NULL, NULL, NULL, 4, '2025-07-03 13:20:43', '2025-07-03 13:20:51', '2025-07-03 13:20:43', '2025-07-03 13:20:51');
INSERT INTO `live_record` VALUES (12, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751520456033.mp4', NULL, NULL, NULL, 2, '2025-07-03 13:27:36', NULL, '2025-07-03 13:27:36', '2025-07-03 13:28:09');
INSERT INTO `live_record` VALUES (13, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751520528154.mp4', NULL, NULL, NULL, 1, '2025-07-03 13:28:48', '2025-07-03 13:30:09', '2025-07-03 13:28:48', '2025-07-03 13:30:09');
INSERT INTO `live_record` VALUES (14, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751520795462.mp4', NULL, NULL, NULL, 1, '2025-07-03 13:33:15', '2025-07-03 13:33:21', '2025-07-03 13:33:15', '2025-07-03 13:33:21');
INSERT INTO `live_record` VALUES (15, 10, '55bcf47c8adbb81b3aad9a9847863e9a_1751521015935.mp4', NULL, NULL, NULL, 1, '2025-07-03 13:36:56', '2025-07-03 13:37:01', '2025-07-03 13:36:56', '2025-07-03 13:37:01');

-- ----------------------------
-- Table structure for live_room
-- ----------------------------
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE `live_room`  (
  `live_room_id` bigint NOT NULL AUTO_INCREMENT,
  `live_room_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '直播间标题',
  `live_room_cover_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面地址',
  `user_id` bigint NOT NULL COMMENT '用户主键',
  `stream_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推流密钥',
  `stream_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推流地址',
  `hls_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'HLS播放地址',
  `flv_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'FLV播放地址',
  `live_room_status` int NOT NULL DEFAULT 0 COMMENT '状态:0未开播 1直播中 2已结束',
  `live_room_view_count` bigint NOT NULL DEFAULT 0 COMMENT '观看人数',
  `live_room_like_count` bigint NOT NULL DEFAULT 0 COMMENT '点赞数',
  `live_room_start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `live_room_end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `live_room_creat_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立时间',
  `live_room_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`live_room_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`live_room_status` ASC) USING BTREE,
  INDEX `idx_start_time`(`live_room_start_time` ASC) USING BTREE,
  CONSTRAINT `fk_live_room_user` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '直播间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of live_room
-- ----------------------------
INSERT INTO `live_room` VALUES (3, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, 'd66b7207a8ccb3f659d796505e00890e', 'rtmp://192.168.195.100:1935/live/d66b7207a8ccb3f659d796505e00890e?auth_key=26a2495affd7c1097db56086d7dd885e&expire=1751534735', 'http://192.168.195.100:8095/live/hls/d66b7207a8ccb3f659d796505e00890e.m3u8', 'http://192.168.195.100:8095/live/d66b7207a8ccb3f659d796505e00890e.flv', 1, 0, 0, '2025-07-02 17:33:48', NULL, '2025-07-02 17:25:35', '2025-07-02 17:25:35');
INSERT INTO `live_room` VALUES (4, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, 'bdbf8449bfa556b5642ebefb598a0043', 'rtmp://192.168.195.100:1935/live/bdbf8449bfa556b5642ebefb598a0043', 'http://192.168.195.100:8095/live/hls/bdbf8449bfa556b5642ebefb598a0043.m3u8', 'http://192.168.195.100:8095/live/bdbf8449bfa556b5642ebefb598a0043.flv', 0, 0, 0, NULL, NULL, '2025-07-02 17:40:15', '2025-07-02 17:40:15');
INSERT INTO `live_room` VALUES (5, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp://192.168.195.100:1935/live/00fa2daa1cb68cb3e50ea07259abab54', 'http://192.168.195.100:8095/live/hls/00fa2daa1cb68cb3e50ea07259abab54.m3u8', 'http://192.168.195.100:8095/live/00fa2daa1cb68cb3e50ea07259abab54.flv', 1, 0, 0, '2025-07-02 18:48:57', NULL, '2025-07-02 17:40:16', '2025-07-02 17:40:16');
INSERT INTO `live_room` VALUES (6, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, '93d65bc49c40ea17a28f4e1db7afeb71', 'rtmp://192.168.195.200:1935/live/93d65bc49c40ea17a28f4e1db7afeb71', 'http://192.168.195.100:8095/live/hls/93d65bc49c40ea17a28f4e1db7afeb71.m3u8', 'http://192.168.195.100:8095/live/93d65bc49c40ea17a28f4e1db7afeb71.flv', 0, 0, 0, NULL, NULL, '2025-07-02 18:49:39', '2025-07-02 18:49:39');
INSERT INTO `live_room` VALUES (7, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, 'dd0a4dba8f65998238a768ce63d56a7c', 'rtmp://192.168.195.200:1935/live/dd0a4dba8f65998238a768ce63d56a7c', 'http://192.168.195.200:8095/live/hls/dd0a4dba8f65998238a768ce63d56a7c.m3u8', 'http://192.168.195.200:8095/live/dd0a4dba8f65998238a768ce63d56a7c.flv', 1, 0, 0, '2025-07-02 19:01:57', NULL, '2025-07-02 18:56:40', '2025-07-02 18:56:40');
INSERT INTO `live_room` VALUES (8, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, 'a80c03aa7596a37faaebf522886ef875', 'rtmp://192.168.195.100:1935/live/a80c03aa7596a37faaebf522886ef875', 'http://192.168.195.100:8095/live/hls/a80c03aa7596a37faaebf522886ef875.m3u8', 'http://192.168.195.100:8095/live/a80c03aa7596a37faaebf522886ef875.flv', 0, 0, 0, NULL, NULL, '2025-07-02 19:14:49', '2025-07-02 19:14:49');
INSERT INTO `live_room` VALUES (9, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, '0676d660cfe347523b183470ab9fe9c7', 'rtmp://192.168.195.100:1935/live/0676d660cfe347523b183470ab9fe9c7', 'http://192.168.195.100:8095/live/hls/0676d660cfe347523b183470ab9fe9c7.m3u8', 'http://192.168.195.100:8095/live/0676d660cfe347523b183470ab9fe9c7.flv', 0, 0, 0, NULL, NULL, '2025-07-02 19:17:52', '2025-07-02 19:17:52');
INSERT INTO `live_room` VALUES (10, '此是千秋第一秋', 'xxxxxxxxxxxx', 4, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp://192.168.195.100:1935/live/55bcf47c8adbb81b3aad9a9847863e9a', 'http://192.168.195.100:8095/live/hls/55bcf47c8adbb81b3aad9a9847863e9a.m3u8', 'http://192.168.195.100:8095/live/55bcf47c8adbb81b3aad9a9847863e9a.flv', 2, 32, 0, '2025-07-03 13:10:17', '2025-07-03 14:21:41', '2025-07-02 19:27:27', '2025-07-02 19:27:27');

-- ----------------------------
-- Table structure for live_stream
-- ----------------------------
DROP TABLE IF EXISTS `live_stream`;
CREATE TABLE `live_stream`  (
  `live_stream_id` bigint NOT NULL AUTO_INCREMENT,
  `live_room_id` bigint NOT NULL COMMENT '直播间主键',
  `stream_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流id',
  `live_stream_protocol` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '协议类型(rtmp/flv/hls/webrtc等)',
  `live_stream_bitrate` int NULL DEFAULT NULL COMMENT '码率(kbps)',
  `live_stream_resolution` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分辨率(如1920x1080)',
  `live_stream_status` int NOT NULL DEFAULT 0 COMMENT '状态: 未开始0 活跃1 结束2',
  `live_stream_creat_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立时间',
  `live_stream_update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`live_stream_id`) USING BTREE,
  INDEX `idx_room_id`(`live_room_id` ASC) USING BTREE,
  INDEX `idx_status`(`live_stream_status` ASC) USING BTREE,
  CONSTRAINT `fk_live_stream_room` FOREIGN KEY (`live_room_id`) REFERENCES `live_room` (`live_room_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '直播流表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of live_stream
-- ----------------------------
INSERT INTO `live_stream` VALUES (1, 3, 'd66b7207a8ccb3f659d796505e00890e', 'rtmp', NULL, NULL, 2, '2025-07-02 17:33:48', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (2, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 17:41:00', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (3, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:44:51', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (4, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:44:54', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (5, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:46:34', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (6, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:48:55', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (7, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:48:56', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (8, 5, '00fa2daa1cb68cb3e50ea07259abab54', 'rtmp', NULL, NULL, 2, '2025-07-02 18:48:57', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (9, 7, 'dd0a4dba8f65998238a768ce63d56a7c', 'rtmp', NULL, NULL, 2, '2025-07-02 19:01:57', '2025-07-03 09:08:39');
INSERT INTO `live_stream` VALUES (10, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 19:50:19', '2025-07-02 20:47:39');
INSERT INTO `live_stream` VALUES (11, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 20:49:34', '2025-07-02 21:03:50');
INSERT INTO `live_stream` VALUES (12, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:05:20', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (13, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:20:46', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (14, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:26:37', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (15, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:28:21', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (16, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:35:31', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (17, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-02 21:37:27', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (18, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 08:29:24', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (19, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 08:49:34', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (20, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 09:03:07', '2025-07-03 09:08:31');
INSERT INTO `live_stream` VALUES (21, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 09:06:06', '2025-07-03 09:08:47');
INSERT INTO `live_stream` VALUES (22, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 09:14:29', '2025-07-03 09:14:54');
INSERT INTO `live_stream` VALUES (23, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 09:17:55', '2025-07-03 12:21:26');
INSERT INTO `live_stream` VALUES (24, 10, '55bcf47c8adbb81b3aad9a9847863e9a', 'rtmp', NULL, NULL, 2, '2025-07-03 13:10:17', '2025-07-03 14:21:41');

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material`  (
  `material_id` bigint NOT NULL AUTO_INCREMENT,
  `material_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `material_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `material_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `herb_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `material_time` datetime NOT NULL,
  `use_count` int NOT NULL DEFAULT 0,
  `material_isvalid` tinyint NOT NULL,
  PRIMARY KEY (`material_id`) USING BTREE,
  INDEX `herb_id`(`herb_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `material_ibfk_1` FOREIGN KEY (`herb_id`) REFERENCES `his_herbinfo`.`herb` (`herb_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `material_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO `material` VALUES (7, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:20:22', 0, 1);
INSERT INTO `material` VALUES (8, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:22:09', 0, 1);
INSERT INTO `material` VALUES (9, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:23:52', 0, 1);
INSERT INTO `material` VALUES (10, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:27:16', 0, 1);
INSERT INTO `material` VALUES (11, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:28:09', 0, 1);
INSERT INTO `material` VALUES (12, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:29:57', 0, 1);
INSERT INTO `material` VALUES (13, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:30:40', 0, 1);
INSERT INTO `material` VALUES (14, 'xxx', 'xxx', 'xxx', 3, 4, '2025-07-01 17:35:08', 0, 0);
INSERT INTO `material` VALUES (15, 'es', 'xxx', 'xxx', 3, 4, '2025-07-01 19:24:09', 0, 1);
INSERT INTO `material` VALUES (16, 'xxx3', 'xxx', 'xxx', 3, 4, '2025-07-01 19:16:13', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
