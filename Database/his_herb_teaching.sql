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

 Date: 10/07/2025 18:43:31
*/
DROP DATABASE IF EXISTS his_herb_teaching;
CREATE DATABASE IF NOT EXISTS his_herb_teaching DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE his_herb_teaching;
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
) ENGINE = InnoDB AUTO_INCREMENT = 1472855183 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (32004, '传统文化与中医养生', 'https://img-ph-mirror.nosdn.127.net/YDyjxhPw-dVyafYX4xB3DQ==/6608705898213038072.png', 0, 3, 2, '2025-02-13 10:00:00', '2025-06-12 23:30:00', '传统文化与中医养生', 4.78, 541);
INSERT INTO `course` VALUES (387003, '中药安全用药导论', 'https://edu-image.nosdn.127.net/FC1DA19E563AE5BFD428E5E70E464656.jpg?imageView&thumbnail=426y240&quality=100', 1, 3, 2, '2025-02-24 10:00:00', '2025-06-01 23:30:00', '中药安全用药导论', 4.81, 304);
INSERT INTO `course` VALUES (388001, '中医诊断学（上）', 'https://edu-image.nosdn.127.net/73502B533AA0EECFF118E3F7F025F056.jpg?imageView&thumbnail=426y240&quality=100', 0, 3, 2, '2025-02-24 10:00:00', '2025-06-30 23:30:00', '中医诊断学（上）', 4.79, 1390);
INSERT INTO `course` VALUES (1001573003, '中药与美容', 'https://edu-image.nosdn.127.net/947065FC1EB208D313C549C111BE5152.jpg?imageView&thumbnail=426y240&quality=100', 0, 1, 2, '2025-09-08 10:00:00', '2025-12-31 23:30:00', '中药与美容', 4.72, 1691);
INSERT INTO `course` VALUES (1001682002, '中医与诊断—学做自己的医生', 'https://edu-image.nosdn.127.net/4175253CFD086F8853AAD8A9C0EB29DF.jpg?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-24 10:00:00', '2025-06-29 23:30:00', '中医与诊断—学做自己的医生', 4.90, 1567);
INSERT INTO `course` VALUES (1001753352, '中医儿科学', 'http://edu-image.nosdn.127.net/0D930305DC5B71ACBA925A94CC10D70E.jpg?imageView&thumbnail=426y240&quality=100', 0, 0, 2, '2022-08-20 10:00:00', '2023-01-10 23:00:00', '中医儿科学', 4.55, 64);
INSERT INTO `course` VALUES (1001755352, '中医护理学', 'https://edu-image.nosdn.127.net/31EF3FB00382F93B6F3A5B35D88DDA0A.png?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-02-17 09:00:00', '2025-06-27 23:30:00', '中医护理学', 4.81, 330);
INSERT INTO `course` VALUES (1001755354, '中医内科学', 'https://mooc-image.nosdn.127.net/60cb3ce2da1e474293a80bfce6413d68.png', 1, 1, 2, '2025-03-10 10:00:00', '2025-06-16 23:30:00', '中医内科学', 4.71, 263);
INSERT INTO `course` VALUES (1001795012, '中药学', 'https://edu-image.nosdn.127.net/31E890861AC1968644D894338D887083.png?imageView&thumbnail=510y288&quality=100', 1, 1, 2, '2025-02-13 10:00:00', '2025-06-27 23:30:00', '中药学', 4.79, 1156);
INSERT INTO `course` VALUES (1002126013, '中医基础理论', 'https://edu-image.nosdn.127.net/DEADEE7733328F6EB6BD7CA5EAD37C27.jpg?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-02-03 00:00:00', '2025-06-20 00:00:00', '中医基础理论', 4.80, 1330);
INSERT INTO `course` VALUES (1002338005, '中医养生与亚健康防治', 'https://edu-image.nosdn.127.net/9B14B43A13ED00CC3C3700E4192C9935.jpg?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2024-10-28 10:00:00', '2025-01-13 23:30:00', '中医养生与亚健康防治', 4.75, 748);
INSERT INTO `course` VALUES (1002533010, '中药学', 'https://edu-image.nosdn.127.net/2A44AF3CF14768A3370EC38D297322F1.jpg?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-02-17 10:00:00', '2025-06-30 23:30:00', '中药学', 4.77, 1659);
INSERT INTO `course` VALUES (1002857002, '中医膏方学', 'https://edu-image.nosdn.127.net/EBD05129A4C133635368BFD50A5AEA2B.jpg?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-03-01 10:00:00', '2025-07-01 23:30:00', '中医膏方学', 4.55, 97);
INSERT INTO `course` VALUES (1003248017, '饮食文化与中医学', 'https://edu-image.nosdn.127.net/F4028453DFEC37A1784A13DAC0B2964A.png?imageView&thumbnail=510y288&quality=100', 0, 1, 2, '2025-02-10 10:00:00', '2025-06-08 23:30:00', '饮食文化与中医学', 4.71, 1646);
INSERT INTO `course` VALUES (1003533003, '走进神奇的中药', 'http://edu-image.nosdn.127.net/6C3DDFD3583289FD5645C2481813EF55.jpg?imageView&thumbnail=510y288&quality=100', 0, 3, 2, '2021-11-03 10:00:00', '2021-12-31 23:30:00', '走进神奇的中药', 4.70, 287);
INSERT INTO `course` VALUES (1003790006, '中医药创新创业', 'https://edu-image.nosdn.127.net/CB6D92D496D524E927F4E674E7B795B1.png?imageView&thumbnail=510y288&quality=100', 0, 1, 2, '2025-02-10 10:00:00', '2025-06-08 23:30:00', '中医药创新创业', 4.78, 2820);
INSERT INTO `course` VALUES (1205591803, '杏林探秘—走近中医', 'http://edu-image.nosdn.127.net/C1C0ECABE0CF5B06429DF99F40D29EB2.png?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2023-12-06 10:00:00', '2024-01-12 23:30:00', '杏林探秘—走近中医', 4.80, 91);
INSERT INTO `course` VALUES (1205682803, '中药炮制学', 'https://edu-image.nosdn.127.net/916BAA3553255025801C9259DD357FA0.jpg?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-02-17 00:00:00', '2025-06-15 00:00:00', '中药炮制学', 4.77, 393);
INSERT INTO `course` VALUES (1205691801, '中医诊断学', 'https://edu-image.nosdn.127.net/6C64A244E55FB65CE5F0A845BB045A2C.png?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-08-30 10:00:00', '2026-01-04 23:30:00', '中医诊断学', 4.78, 729);
INSERT INTO `course` VALUES (1205912818, '中医健康理念', 'https://edu-image.nosdn.127.net/E227BD7C4F0CDA1B47ECAC1A92E73AA8.jpg?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-17 10:00:00', '2025-06-22 23:30:00', '中医健康理念', 4.80, 815);
INSERT INTO `course` VALUES (1206246805, '浙派中医', 'http://edu-image.nosdn.127.net/200989006C38B0E5032281C542391B4F.png?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2021-05-06 10:00:00', '2021-08-20 23:00:00', '浙派中医', 4.33, 9);
INSERT INTO `course` VALUES (1206405809, '中医入门', 'http://edu-image.nosdn.127.net/C306DF508D6EA585BDE7813C8B2F8625.png?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2024-09-23 10:00:00', '2024-12-31 23:30:00', '中医入门', 4.75, 464);
INSERT INTO `course` VALUES (1206406805, '中医气功学导论', 'https://edu-image.nosdn.127.net/4AFAEC33CA47DC002646BE9F28AF874C.png?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-02-21 10:00:00', '2025-06-28 23:30:00', '中医气功学导论', 4.84, 251);
INSERT INTO `course` VALUES (1206410812, '中医基础理论', 'https://edu-image.nosdn.127.net/74B46A0B9B0692AA0686CF859D53AEF3.jpg?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-02-28 10:00:00', '2025-06-30 23:30:00', '中医基础理论', 4.77, 845);
INSERT INTO `course` VALUES (1206412807, '中医护理学', 'http://edu-image.nosdn.127.net/C0203475C7C098E008DAF9DE624BA8D2.jpg?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2024-03-18 10:00:00', '2024-07-30 23:30:00', '中医护理学', 4.78, 67);
INSERT INTO `course` VALUES (1206413805, '中医儿科学', 'https://edu-image.nosdn.127.net/BE47F992BB12FD77F306A1F0F52D6BDD.jpg?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-03-03 10:00:00', '2025-06-30 23:30:00', '中医儿科学', 4.70, 64);
INSERT INTO `course` VALUES (1206414807, '中医内科学选讲', 'http://edu-image.nosdn.127.net/140A90D070DAC8CD8BF6FE3EAD60B5A7.png?imageView&thumbnail=510y288&quality=100', 0, 3, 2, '2019-12-31 11:00:00', '2020-06-30 23:30:00', '中医内科学选讲', 4.57, 72);
INSERT INTO `course` VALUES (1206420806, '中医诊断学（下）', 'https://edu-image.nosdn.127.net/C41CA524DB070CC8A5493AA583ABE4F8.png?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-24 10:00:00', '2025-06-30 23:30:00', '中医诊断学（下）', 4.81, 286);
INSERT INTO `course` VALUES (1206450825, '中医基础理论', 'https://edu-image.nosdn.127.net/4C62601A9B75893AE70B58D84C7B5F7F.jpg?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-10 10:00:00', '2025-06-27 23:30:00', '中医基础理论', 4.79, 336);
INSERT INTO `course` VALUES (1206575808, '现代中药科学前沿及研究技术', 'https://edu-image.nosdn.127.net/963A6F2B94791F07D65940C01093F796.jpg?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-06-01 10:00:00', '2025-08-30 23:30:00', '现代中药科学前沿及研究技术', 4.89, 47);
INSERT INTO `course` VALUES (1206600810, '中药变形记', 'http://edu-image.nosdn.127.net/EE31EFB229720D4074F0F2386D2003EA.jpg?imageView&thumbnail=510y288&quality=100', 0, 1, 2, '2023-10-01 10:00:00', '2023-12-31 23:00:00', '中药变形记', 4.27, 15);
INSERT INTO `course` VALUES (1206620837, '中医儿科学', 'https://edu-image.nosdn.127.net/3CBB23E6B541673014B02A2CD84670BF.jpg?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-08-25 10:00:00', '2025-12-31 23:30:00', '中医儿科学', 4.85, 320);
INSERT INTO `course` VALUES (1206686835, '生活里的中医中药', 'http://edu-image.nosdn.127.net/828983E3A42896588DC67E9A7056E7CF.PNG?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2019-10-21 09:00:00', '2019-12-20 23:30:00', '生活里的中医中药', 4.84, 58);
INSERT INTO `course` VALUES (1206696807, '中医诊断学', 'https://edu-image.nosdn.127.net/DD818E156004D2E43684A06BAE85D6CA.png?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-02-17 10:00:00', '2025-06-29 23:30:00', '中医诊断学', 4.77, 518);
INSERT INTO `course` VALUES (1207058808, '舌尖上的中药-五脏健康百病消', 'https://edu-image.nosdn.127.net/ABE07298BD14ECB66CDB25BA7ADA041A.jpg?imageView&thumbnail=510y288&quality=100', 0, 1, 2, '2025-03-01 10:00:00', '2025-07-31 23:30:00', '舌尖上的中药-五脏健康百病消', 4.75, 824);
INSERT INTO `course` VALUES (1207127805, '中药药理学-学做自己的调理师', 'https://mooc-image.nosdn.127.net/bfa1d83e644548b1828766aaa1bf47e6.jpg', 1, 1, 2, '2025-02-21 10:00:00', '2025-06-08 23:30:00', '中药药理学-学做自己的调理师', 4.85, 325);
INSERT INTO `course` VALUES (1207127806, '实用中医儿科学', 'https://edu-image.nosdn.127.net/56F133B4EAFDEF5C65DEB3A687C8A262.jpg?imageView&thumbnail=510y288&quality=100', 0, 3, 2, '2025-02-24 10:00:00', '2025-05-23 23:30:00', '实用中医儿科学', 4.90, 68);
INSERT INTO `course` VALUES (1207425813, '中医看妇科——女性一生的康与病', 'https://edu-image.nosdn.127.net/E255A04F89314C1CD70F98FFE0F0CB44.jpg?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-09-08 10:00:00', '2025-11-28 23:30:00', '中医看妇科——女性一生的康与病', 4.78, 828);
INSERT INTO `course` VALUES (1207430818, '中医耳鼻喉科学', 'https://edu-image.nosdn.127.net/DEF1D5D7A85F2B6744DCF970073C4425.jpg?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-02-17 10:00:00', '2025-07-16 23:00:00', '中医耳鼻喉科学', 4.92, 120);
INSERT INTO `course` VALUES (1207439804, '中医养生与健康', 'https://edu-image.nosdn.127.net/6F25F9D2EC24029377C0F577EE718E2A.JPG?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-26 10:00:00', '2025-06-15 23:30:00', '中医养生与健康', 4.75, 465);
INSERT INTO `course` VALUES (1207446808, '颈肩腰腿痛中医防治', 'https://edu-image.nosdn.127.net/840541C9AC5D2EADA26C2D4A2C0D0B3B.jpg?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-08-01 10:00:00', '2025-11-25 23:30:00', '颈肩腰腿痛中医防治', 4.92, 706);
INSERT INTO `course` VALUES (1449634164, '中医护理学', 'https://edu-image.nosdn.127.net/DE63741275D38A111DC595265A6E1F78.png?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2025-02-24 10:00:00', '2025-06-22 23:30:00', '中医护理学', 4.81, 142);
INSERT INTO `course` VALUES (1450181199, '中药学', 'https://edu-image.nosdn.127.net/C870BAA74251FD2F50D77B41875D7BC1.jpg?imageView&thumbnail=510y288&quality=100', 0, 0, 2, '2025-02-24 10:00:00', '2025-07-13 23:00:00', '中药学', 4.75, 175);
INSERT INTO `course` VALUES (1450194213, '中医基础理论', 'https://edu-image.nosdn.127.net/42E02BC22EE4B217514D836611155040.png?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-03-01 10:00:00', '2025-06-30 23:30:00', '中医基础理论', 4.73, 179);
INSERT INTO `course` VALUES (1450261181, '中医儿科学', 'http://edu-image.nosdn.127.net/08A435ED2DFD556E415A34E63D11E711.jpg?imageView&thumbnail=510y288&quality=100', 0, 1, 2, '2020-05-05 10:00:00', '2020-11-16 23:30:00', '中医儿科学', 4.61, 79);
INSERT INTO `course` VALUES (1450272188, '中医内科学', 'http://edu-image.nosdn.127.net/60C331A1F3C15A3ADF650C5BFECD9A9C.png?imageView&thumbnail=510y288&quality=100', 1, 1, 2, '2021-04-02 10:00:00', '2021-07-26 23:30:00', '中医内科学', 4.79, 234);
INSERT INTO `course` VALUES (1450278183, '中医骨伤科学', 'http://edu-image.nosdn.127.net/ABB0B4856F75479B948F1C81136FBEB0.jpg?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2021-04-05 10:00:00', '2022-01-05 23:30:00', '中医骨伤科学', 4.25, 59);
INSERT INTO `course` VALUES (1450284161, '中医妇科学', 'http://edu-image.nosdn.127.net/0B01546C6FB1FF852572563E914C1EB2.jpg?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2020-05-09 10:00:00', '2020-11-30 23:30:00', '中医妇科学', 4.00, 2);
INSERT INTO `course` VALUES (1450780222, '女性中医保健', 'https://edu-image.nosdn.127.net/D955BE4A3723C3BDF7F1ED8616C89684.JPG?imageView&thumbnail=510y288&quality=100', 1, 0, 2, '2025-03-01 10:00:00', '2025-07-01 23:30:00', '女性中医保健', 4.94, 72);
INSERT INTO `course` VALUES (1457870192, '中医养生', 'https://edu-image.nosdn.127.net/239444DA24501A200357E9E41620B463.png?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2025-02-26 10:00:00', '2025-05-26 23:30:00', '中医养生', 4.76, 215);
INSERT INTO `course` VALUES (1460876164, '中医与辨证', 'https://edu-image.nosdn.127.net/184F22DC87C29A0C04E1326175189D39.png?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-09-01 10:00:00', '2025-12-06 23:30:00', '中医与辨证', 4.90, 417);
INSERT INTO `course` VALUES (1460879163, '中药化学', 'http://edu-image.nosdn.127.net/DCE54525108B175DC747758F6A2FF5FD.png?imageView&thumbnail=510y288&quality=100', 0, 2, 2, '2020-12-30 10:00:00', '2023-01-15 23:30:00', '中药化学', 4.76, 17);
INSERT INTO `course` VALUES (1461533168, '《中医外科学》诠说', 'https://edu-image.nosdn.127.net/D84398574E37AD11582F47985785273E.jpg?imageView&thumbnail=510y288&quality=100', 1, 3, 2, '2025-03-03 10:00:00', '2025-07-13 23:30:00', '《中医外科学》诠说', 4.78, 350);
INSERT INTO `course` VALUES (1462115161, '中药鉴定学', 'https://mooc-image.nosdn.127.net/f36cc4e4c0a04fcab104d4499c16f884.png', 1, 0, 2, '2025-02-21 10:00:00', '2025-06-08 23:30:00', '中药鉴定学', 5.00, 10);
INSERT INTO `course` VALUES (1463129161, '中医肿瘤学', 'https://nos.netease.com/edu-image/8b005da6b259470b82c4437236ae49e4.png', 0, 0, 2, '2025-02-27 10:00:00', '2025-07-09 23:30:00', '中医肿瘤学', 4.97, 40);
INSERT INTO `course` VALUES (1463150167, '中医饮食营养学', 'https://edu-image.nosdn.127.net/13F8361FB4E44F1EDBDD63AC414F313A.jpg?imageView&thumbnail=510y288&quality=100', 1, 2, 2, '2025-02-24 10:00:00', '2025-06-23 23:30:00', '中医饮食营养学', 4.76, 51);
INSERT INTO `course` VALUES (1463216170, '岭南中药炮制', 'https://nos.netease.com/edu-image/6f9e2893fc0f4053b92a0d6ee1c34f15.jpg', 0, 1, 2, '2025-02-24 10:00:00', '2025-07-14 23:00:00', '岭南中药炮制', 4.85, 396);
INSERT INTO `course` VALUES (1463234175, '岭南中草药精粹——从基础到临床', 'http://edu-image.nosdn.127.net/67AF828371F10FEDE9014B17D8CE0EC3.jpg?imageView&thumbnail=510y288&quality=100', 0, 3, 2, '2021-05-06 10:00:00', '2021-07-20 23:30:00', '岭南中草药精粹——从基础到临床', 5.00, 15);
INSERT INTO `course` VALUES (1464102180, '中医藏象理论与临床', 'https://nos.netease.com/edu-image/2e5457ab752e426386ab372c4b3d4093.jpg', 0, 2, 2, '2025-03-04 10:00:00', '2025-07-01 23:30:00', '中医藏象理论与临床', 4.76, 268);
INSERT INTO `course` VALUES (1466044161, '中医饮食调护', 'http://mooc-image.nosdn.127.net/33cd532ddf7a4d598b6ec87e046d63db.png', 1, 2, 2, '2022-10-08 10:00:00', '2023-01-15 23:30:00', '中医饮食调护', 4.88, 16);
INSERT INTO `course` VALUES (1472333163, '中医妇科学', 'https://mooc-image.nosdn.127.net/04ae8fd67b124d5aacc2a8996ea0c5af.png', 0, 0, 2, '2025-03-15 14:00:00', '2025-06-30 23:30:00', '中医妇科学', 4.50, 2);
INSERT INTO `course` VALUES (1472334163, '中医皮肤病学', 'https://mooc-image.nosdn.127.net/05408ff11fcb43ab980cb9ba31b03d1f.png', 0, 1, 2, '2025-03-01 10:00:00', '2025-06-30 23:30:00', '中医皮肤病学', 4.25, 4);

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
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程药材关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_herb_link
-- ----------------------------
INSERT INTO `course_herb_link` VALUES (24, 2, 2);
INSERT INTO `course_herb_link` VALUES (25, 2, 3);
INSERT INTO `course_herb_link` VALUES (14, 6, 1);
INSERT INTO `course_herb_link` VALUES (26, 11, 2);

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程评分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_rating
-- ----------------------------
INSERT INTO `course_rating` VALUES (3, 2, 1, 2, '2025-07-02 19:49:35');
INSERT INTO `course_rating` VALUES (4, 2, 2, 5, '2025-06-30 23:38:38');
INSERT INTO `course_rating` VALUES (5, 2, 12345, 5, '2025-07-01 11:13:23');
INSERT INTO `course_rating` VALUES (8, 4, 1, 5, '2025-07-01 11:15:02');
INSERT INTO `course_rating` VALUES (9, 4, 2, 2, '2025-07-01 16:15:23');
INSERT INTO `course_rating` VALUES (10, 4, 123, 2, '2025-07-01 16:13:03');
INSERT INTO `course_rating` VALUES (11, 2, 10, 2, '2025-07-05 08:17:51');
INSERT INTO `course_rating` VALUES (12, 9, 10, 2, '2025-07-05 08:19:24');
INSERT INTO `course_rating` VALUES (13, 8, 10, 2, '2025-07-05 08:22:50');
INSERT INTO `course_rating` VALUES (14, 14, 10, 2, '2025-07-06 11:44:53');
INSERT INTO `course_rating` VALUES (15, 14, 2, 2, '2025-07-06 11:45:57');

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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_resource
-- ----------------------------
INSERT INTO `course_resource` VALUES (7, 2, 0, 2, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:43:49', 1);
INSERT INTO `course_resource` VALUES (8, 2, 0, 3, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:46:35', 1);
INSERT INTO `course_resource` VALUES (9, 2, 0, 4, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:47:46', 1);
INSERT INTO `course_resource` VALUES (10, 2, 0, 5, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:49:13', 1);
INSERT INTO `course_resource` VALUES (12, 4, 0, 1, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:52:00', 1);
INSERT INTO `course_resource` VALUES (13, 4, 0, 2, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-01 16:52:59', 1);
INSERT INTO `course_resource` VALUES (14, 2, 0, 7, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-02 18:54:23', 1);
INSERT INTO `course_resource` VALUES (15, 2, 0, 8, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-02 18:54:48', 1);
INSERT INTO `course_resource` VALUES (16, 2, 0, 9, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-02 19:38:01', 1);
INSERT INTO `course_resource` VALUES (17, 14, 0, 1, '第一讲：中药学概论.mp4', 'https://your-oss-bucket.com/videos/chapter1.mp4', '{\"duration\": \"45min\"}', '2025-07-06 11:47:48', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lab
-- ----------------------------
INSERT INTO `lab` VALUES (7, 4, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);
INSERT INTO `lab` VALUES (12, 2, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 3);
INSERT INTO `lab` VALUES (13, 2, '当归的性状鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 4);
INSERT INTO `lab` VALUES (14, 14, '当归的性状与显微鉴别', '1. 取当归样品，观察其外观、颜色、质地。\n2. 切片观察。', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户课程收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_course_collection
-- ----------------------------
INSERT INTO `user_course_collection` VALUES (8, 2, 2, '2025-07-01 16:16:45');
INSERT INTO `user_course_collection` VALUES (9, 2, 3, '2025-07-01 16:17:54');
INSERT INTO `user_course_collection` VALUES (11, 8, 3, '2025-07-01 16:20:50');
INSERT INTO `user_course_collection` VALUES (12, 10, 3, '2025-07-01 16:22:32');
INSERT INTO `user_course_collection` VALUES (14, 10, 1, '2025-07-02 19:37:14');
INSERT INTO `user_course_collection` VALUES (15, 10, 10, '2025-07-05 08:26:20');
INSERT INTO `user_course_collection` VALUES (16, 14, 10, '2025-07-06 11:45:05');
INSERT INTO `user_course_collection` VALUES (17, 14, 2, '2025-07-06 11:46:38');

SET FOREIGN_KEY_CHECKS = 1;
