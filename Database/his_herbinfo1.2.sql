/*
 Navicat Premium Data Transfer

 Source Server         : ch_medcine
 Source Server Type    : MySQL
 Source Server Version : 80011 (8.0.11)
 Source Host           : localhost:3306
 Source Schema         : his_herbinfo

 Target Server Type    : MySQL
 Target Server Version : 80011 (8.0.11)
 File Encoding         : 65001

 Date: 11/07/2025 08:00:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for district
-- ----------------------------
DROP TABLE IF EXISTS `district`;
CREATE TABLE `district`  (
  `district_id` bigint(20) NOT NULL,
  `district_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`district_id`) USING BTREE,
  INDEX `idx_district_name`(`district_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of district
-- ----------------------------
INSERT INTO `district` VALUES (500101, '万州区');
INSERT INTO `district` VALUES (500230, '丰都县');
INSERT INTO `district` VALUES (500107, '九龙坡区');
INSERT INTO `district` VALUES (500235, '云阳县');
INSERT INTO `district` VALUES (500109, '北碚区');
INSERT INTO `district` VALUES (500108, '南岸区');
INSERT INTO `district` VALUES (500119, '南川区');
INSERT INTO `district` VALUES (500117, '合川区');
INSERT INTO `district` VALUES (500231, '垫江县');
INSERT INTO `district` VALUES (500229, '城口县');
INSERT INTO `district` VALUES (500104, '大渡口区');
INSERT INTO `district` VALUES (500111, '大足区');
INSERT INTO `district` VALUES (500236, '奉节县');
INSERT INTO `district` VALUES (500237, '巫山县');
INSERT INTO `district` VALUES (500238, '巫溪县');
INSERT INTO `district` VALUES (500113, '巴南区');
INSERT INTO `district` VALUES (500154, '开州区');
INSERT INTO `district` VALUES (500243, '彭水苗族土家族自治县');
INSERT INTO `district` VALUES (500233, '忠县');
INSERT INTO `district` VALUES (500155, '梁平区');
INSERT INTO `district` VALUES (500156, '武隆区');
INSERT INTO `district` VALUES (500118, '永川区');
INSERT INTO `district` VALUES (500105, '江北区');
INSERT INTO `district` VALUES (500116, '江津区');
INSERT INTO `district` VALUES (500106, '沙坪坝区');
INSERT INTO `district` VALUES (500102, '涪陵区');
INSERT INTO `district` VALUES (500103, '渝中区');
INSERT INTO `district` VALUES (500112, '渝北区');
INSERT INTO `district` VALUES (500152, '潼南区');
INSERT INTO `district` VALUES (500120, '璧山区');
INSERT INTO `district` VALUES (500240, '石柱土家族自治县');
INSERT INTO `district` VALUES (500241, '秀山土家族苗族自治县');
INSERT INTO `district` VALUES (500110, '綦江区');
INSERT INTO `district` VALUES (500153, '荣昌区');
INSERT INTO `district` VALUES (500242, '酉阳土家族苗族自治县');
INSERT INTO `district` VALUES (500151, '铜梁区');
INSERT INTO `district` VALUES (500115, '长寿区');
INSERT INTO `district` VALUES (500114, '黔江区');

-- ----------------------------
-- Table structure for growth_audit
-- ----------------------------
DROP TABLE IF EXISTS `growth_audit`;
CREATE TABLE `growth_audit`  (
  `audit_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '审核主键ID(自增)',
  `growth_id` bigint(20) NOT NULL COMMENT '审核对象(关联herb_growth表)',
  `user_id` bigint(20) NOT NULL COMMENT '审核人(关联his_userinfo.user表)',
  `audit_result` int(11) NOT NULL COMMENT '审核结果(1-通过 2-不通过)',
  `audit_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核评语',
  `audit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  PRIMARY KEY (`audit_id`) USING BTREE,
  INDEX `idx_growth_id`(`growth_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_audit_time`(`audit_time` ASC) USING BTREE,
  CONSTRAINT `fk_growth_audit_growth` FOREIGN KEY (`growth_id`) REFERENCES `herb_growth` (`growth_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_growth_audit_user` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '2.8成长记录审核表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of growth_audit
-- ----------------------------
INSERT INTO `growth_audit` VALUES (1, 7, 2, 1, '违反规定', '2025-07-07 13:52:00');
INSERT INTO `growth_audit` VALUES (2, 8, 2, 1, '违反规定', '2025-07-07 13:45:53');

-- ----------------------------
-- Table structure for herb
-- ----------------------------
DROP TABLE IF EXISTS `herb`;
CREATE TABLE `herb`  (
  `herb_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `herb_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `herb_origin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `herb_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `herb_des1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `herb_des2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `herb_isvalid` tinyint(1) NOT NULL,
  PRIMARY KEY (`herb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 617 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of herb
-- ----------------------------
INSERT INTO `herb` VALUES (2, '111', NULL, NULL, NULL, NULL, 1);
INSERT INTO `herb` VALUES (3, '白芍', '广西', 'xxxx', '白色的,治病', NULL, 1);
INSERT INTO `herb` VALUES (4, '当归', '广西', 'xxxx', '不知道什么颜色的,治病', NULL, 1);
INSERT INTO `herb` VALUES (11, '人参', NULL, NULL, '不知道什么颜色的,治病', NULL, 1);
INSERT INTO `herb` VALUES (12, '陈皮', NULL, NULL, '不知道什么颜色的,治病', NULL, 1);
INSERT INTO `herb` VALUES (13, '黄连', NULL, NULL, NULL, NULL, 1);
INSERT INTO `herb` VALUES (14, '金银花', NULL, NULL, NULL, NULL, 1);
INSERT INTO `herb` VALUES (554, '当归', '高寒多雨山区', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Chinatown_Museum_Collections_in_Binondo_34.jpg/250px-Chinatown_Museum_Collections_in_Binondo_34.jpg', '当归（Angelica sinensis (Oliv.) Diels）是伞形科当归属多年生草本植物。根圆柱状，分枝，黄棕色；茎直立，绿白色或带紫色，有纵深沟纹；叶羽状分裂，紫色或绿色，卵形；花白色，花柄密被细柔毛，花瓣长卵形，花柱基圆锥形，花期6-7月。果实椭圆至卵形，翅边缘淡紫色，果期7-9月。《本草纲目》中记载：“古人娶妻，为嗣续也。当归调血，为女人要药，有思夫之意，故有当归之名。” 当归主产中国甘肃东南部，以岷县产量多，质量好，其次为云南、四川、陕西、湖北等省，均为栽培，有些省区也已引种栽培。', '黄芪、当归补气生血；杜仲补肝肾、强筋骨、安胎；枸杞子滋补肝肾、益精明目；黑枣与红枣成分、', 1);
INSERT INTO `herb` VALUES (555, '白芍', '大理北部及云南大部分地区', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Lactiflora1b.UME.jpg/250px-Lactiflora1b.UME.jpg', '白芍，Cynanchum otophyllum Schneid.是萝藦科鹅绒藤属植物青羊参的根。青羊参多年生草质藤本；根圆柱状,灰黑色。生于山地疏林或山坡灌木丛中；海拔1400-2800米分布于大理北部及云南大部分地区；西藏、四川、广西、湖南也有。药用根，味辛、苦；有小毒；具温阳祛湿、补体虚、健脾胃等功效。白芍民间用 于治疗风湿冷痛、风湿关节炎、腰肌劳损、体虚神衰、四肢抽搐、慢惊风、犬咬伤等病症。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (556, '甘草', '中国东北、华北、西北各省区', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Glycyrrhizauralensis.jpg/250px-Glycyrrhizauralensis.jpg', '甘草（Glycyrrhiza uralensis Fisch.），是豆科甘草属的多年生草本植物，别名乌拉尔甘草、甜根子、甜草、国老等。其根与根状茎粗壮，外皮褐色，有甜味。奇数羽状复叶，小叶两面均密被黄褐色腺点及短柔毛，边缘全缘或微呈波状。总状花序腋生，花萼钟状；花冠紫色、白色或黄色；荚果弯曲呈镰刀状或呈环状，密集成球，密生瘤状突起和刺毛状腺体。花期6~8月，果期7~10月。甘草分布于亚欧大陆的中国北部、蒙古、俄罗斯西伯利亚地区、哈萨克斯坦、巴基斯坦等国家和地区，在中国分布于东北、华北、西北各省区。', '【功效】功效，可治气短、腹痛、食物中毒等症\r\n', 1);
INSERT INTO `herb` VALUES (557, '黄芪', '西藏自治区，四川省、甘肃省、山东省、陕西省等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/0f/Astragalus_membranaceus.jpg/250px-Astragalus_membranaceus.jpg', '黄芪（学名：Astragalus membranaceus (Fisch.) Bunge），是豆科黄芪属植物，又名东北黄芪、绵芪、膜荚黄耆等，为多年生草本，高50-100厘米。主根肥厚，灰白色。茎直立，上部多分枝，有细棱，被白色柔毛；羽状复叶有13-27片小叶，叶柄长0.5-1厘米；托叶离生，披针形或线状披针形，下面被白色柔毛或近无毛；总状花序稍密，有10-20朵花，总花梗与叶近等长或较长，至果期显著伸长，花冠黄色或淡黄色，瓣片长圆形；荚果薄膜质，稍膨胀，半椭圆形，顶端具刺尖；花期6-8月，果期7-9月。', '“补气升阳、固表止汗、利水消肿、生津养血、行滞通痹、托毒排脓、敛疮生肌的', 1);
INSERT INTO `herb` VALUES (558, '党参', '中国、朝鲜、蒙古、俄罗斯有分布。', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Codonopsis_pilosula_flowers.jpg/250px-Codonopsis_pilosula_flowers.jpg', '党参（Codonopsis pilosula (Franch.) Nannf.），桔梗科党参属多年生草质藤本植物。党参的根部呈圆锥状，表面灰黄色，有环纹，根的头部有凸起的茎痕和芽，习称“狮子盘头”，从中部开始有分枝；根茎光滑，数量较多，都缠绕在一起；叶的下端像心形，边缘是锯齿形；花为淡黄绿色，有污紫色斑点；果实的下半部分为半球形，上半部分为短圆锥形，花期7—8月，果期8—9月。党参之名始见于清代 《本草从新》 。', '【主治】主治脾胃虚弱、中气不足、肺气亏虚、热病伤津、气短口渴、血虚萎黄和头晕心慌等症\r\n', 1);
INSERT INTO `herb` VALUES (559, '丹参', '产于中国和越南，后引种于韩国[11]，日本也有栽培；在中国，分布于河北，山西，陕西，山东，河南等省份', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Gardenology.org-IMG_2926_rbgs11jan.jpg/250px-Gardenology.org-IMG_2926_rbgs11jan.jpg', '丹参（Salvia miltiorrhiza Bunge）别名血参、紫丹参、五凤花、活血根等，为唇形科鼠尾草属的多年生直立草本植物，因其根皮赤而肉紫，形状似参，故名“丹参”。根肥厚，肉质，疏生支根；茎直立，四稜形，密被长柔毛，多分枝；奇数羽状复叶，小叶卵圆形或椭圆状卵圆形或宽披针形，先端锐尖或渐尖，基部圆形或偏斜，边缘具圆齿；轮伞花序，花萼钟形，带紫色，花冠紫蓝色，外被具腺短柔毛；小坚果黑色，椭圆形；花期4-8月，花后见果。', '【功效】功效，主治妇女月经不调，产后瘀滞腹痛，跌打损伤、烦躁不安等病症 [13]\r\n', 1);
INSERT INTO `herb` VALUES (560, '红花', '原产于中亚，在中国、印度、美国等国家均有栽培。', 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Safflower.jpg/250px-Safflower.jpg', '红花（学名：Carthamus tinctorius L.），属于菊科（Asteraceae）红花属（Carthamus）一年生草本植物，又名刺红花、红蓝花、草红花。其茎直立，上部分枝；中下部茎生叶，呈披针形、卵状披针形或长椭圆形，全部叶质地坚硬，革质，两面无毛无腺点，有光泽，基部无柄，半抱茎；头状花序多数，在茎枝顶端排成伞房花序，为苞叶所围绕，小花红色、桔红色；果实为瘦果倒卵圆形，乳白色；花果期5~8月。红花原产于中亚地区。', '活血通经，散瘀止痛。用于经闭，痛经，恶露不行，胸痹心痛，瘀滞腹痛，跌扑损伤等，孕妇慎用 [16]。食用', 1);
INSERT INTO `herb` VALUES (561, '地黄', '辽宁、河北、河南、山东、山西、陕西、甘肃、内蒙古、江苏等省。越南、朝鲜等有引种栽培[10]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/The_flower_of_Rehmannia_glutinosa.JPG/250px-The_flower_of_Rehmannia_glutinosa.JPG', '地黄（Rehmanniaglutinosa (Gaertn.) Libosch. ex Fisch. & C. A. Mey.）列当科地黄属多年生草本植物。植株密被灰白色长柔毛和腺毛。', '【主治】主治 温热病高热烦躁，吐血衄血，口干，咽喉肿痛，中耳炎，烫伤\r\n', 1);
INSERT INTO `herb` VALUES (562, '人参', '东亚', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Korea_Ginseng.jpg/250px-Korea_Ginseng.jpg', '人参（拉丁学名：Panax ginseng C. A. Mey.）是五加科、人参属多年生草本植物。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (563, '黄柏', '四川、贵州、湖北、云南等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Phellodendron_amurense0.jpg/250px-Phellodendron_amurense0.jpg', '黄柏（huáng bò），中药名。为芸香科植物黄皮树Phellodendron chinense Schneid.的干燥树皮。习称“川黄柏”。剥取树皮后，除去粗皮，晒干。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (564, '黄精', '黄精的主要产地为中国，朝鲜、蒙古和苏联西伯利亚东部地区也有分布[2]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Polygonatum_lasianthum.JPG/250px-Polygonatum_lasianthum.JPG', '黄精（Polygonatum sibiricum Delar. ex Redoute）是天门冬目、百合科、黄精属多年生草本，地下茎为根状茎，节膨大；叶轮生，每轮4~6枚，条状披针形，先端拳卷或弯曲成钩；花序常具2-4花，成伞状，花序梗长，俯垂；苞片生于花梗基部，膜质，钻形或条状披针形；花被乳白或淡黄色并且筒中部稍缢缩；浆果成熟时为黑色。花期5-6月，果期8-9月。[2]《抱朴子》中记载：“昔人以本品得坤土之气，获天地之精，故名。” 黄精的主要产地为中国，此外朝鲜、蒙古和苏联西伯利亚东部地区也有分布。', '黄精的根状茎含有黄精多糖甲、乙、丙，均由葡萄糖、甘仲夏夜的小月亮28药店很畅销的珍贵中药材，深受男同胞的欢迎，你家乡有吗？LILI743129小伙在山上发现，药店卖2000元的药材，原来长这样，你认识吗？F1314877360610黄精的土壤与阳光的要求👍种植： 黄精在种植时，喜欢疏松肥沃的土壤，保证土壤的透气性，可以适当加入泥沙石。黄精对土壤要求较高，在含丰富的腐殖质、土层深厚的土壤中，才可以有效促进植株的生长。将土壤进行消毒处理后使用，可以有效提高种植品质。', 1);
INSERT INTO `herb` VALUES (565, '白术', '安徽、浙江、江西、湖南、湖北、重庆、贵州、福建', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Baizhu_roots.jpg/250px-Baizhu_roots.jpg', '白术（学名：Atractylodes macrocephala Koidz.）是菊科苍术属多年生草本植物，在中国传统医学中占有重要地位。在《神农本草经》中白术被描述为具有健脾胃、消除水肿、增强免疫力等多种功效的药用植物。白术的根茎，经晒干和研磨后，是制作中药的主要成分。从魏晋南北朝开始，白术和苍术被明确区分，并在宋代进一步得到分类。白术的地下根粗大，灰黄色。叶子集中在茎中下部，3-5羽状全裂，纸质，绿色，边缘带细小刺。花序头状，单生于茎顶，苞片排成覆瓦状，小花紫红色。果实倒圆锥形，密被白色毛，冠毛羽毛状。', '白术还有着抗二手收藏品小梁9白术茯苓可以治脾虚，吃白术好还是吃茯苓好？茯苓白术怎么吃？脾虚是当下很多人都存在的问题，经常也可以听到周围的人说自己脾胃不好，也就是脾虚。人体的各个器官都是相互协调的，如果脾虚，就容易会引起其他器官无法正常运转，从而对身体健康造成影响。练锋1325练锋，超声科', 1);
INSERT INTO `herb` VALUES (566, '白芷', '产于中国东北及华北地区，在欧洲及北美洲部分地区也有分布，在中国北方各省多栽培[1][14]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Angelica_dahurica_%288855221294%29.jpg/250px-Angelica_dahurica_%288855221294%29.jpg', '白芷（学名：Angelica dahurica (Fisch. ex Hoffm.) Benth. & Hook. f. ex Franch. & Sav.），别名香白芷、走马芹、兴安白芷等，为伞形科当归属的多年生高大草本植物。', '【主治】主治感冒头痛，眉棱骨痛，牙痛等\r\n', 1);
INSERT INTO `herb` VALUES (567, '白及', '原产于中国，在世界范围内，主要分布于中国、日本、韩国等国家；在中国，主要分布于陕西、甘肃、江苏、安徽', 'https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Bletilla_striata_2007-05-13_375.jpg/250px-Bletilla_striata_2007-05-13_375.jpg', '白及（Bletilla striata (Thunb. ex A. Murray) Rchb. f.），是兰科白及属植物，又名白芨、甘根、白给、朱兰、紫兰等。白及植株高18-60厘米，茎粗壮，劲直。叶4-6枚，狭长圆形或披针形，先端渐尖，基部收狭成鞘并抱茎。花序具3-10朵花，花苞片长圆状披针形；花大，紫红色或粉红色。花期4-5月。', '【功效】功效，主要用来治疗咯血、吐血、蛆血、便血等病症\r\n', 1);
INSERT INTO `herb` VALUES (568, '白花蛇舌草', '在世界范围内分布于热带亚洲，西至尼泊尔，日本亦产。', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Oldenlandia_diffusa.jpg/250px-Oldenlandia_diffusa.jpg', '白花蛇舌草（拉丁学名：Scleromitrion diffusum (Willd.) R. J. Wang），又名：蛇舌草、竹叶草、定经草、矮脚白花蛇利草等。是茜草科蛇舌草属的植物，为无毛草本；其叶无柄，线形，顶端短尖，边缘干后常背卷，上面中脉凹下，侧脉不明显；花单生或双生叶，花冠白色，筒状，雄蕊生于冠筒喉部，花药伸出；果呈蒴果扁球形，无毛，成熟时顶部室背开裂；花期7~9月，果期8~10月。', '【功效】功效，有助于伤口的愈合；此外还具有轻微的利尿作用，可作为一种天然的解毒剂\r\n', 1);
INSERT INTO `herb` VALUES (569, '白蔹', '东北、华北、华东、华中及西南各地 在日本也有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/BaiLian.JPG/250px-BaiLian.JPG', '白蔹（Ampelopsis japonica (Thunb.) Makino）是葡萄科蛇葡萄属的木质藤本。小枝圆柱形，有纵棱纹；叶为掌状，由3-5片小叶组成，上面为绿色，下面为浅绿色；聚伞花序，与叶对生，通常集生于花序梗顶端，花序常呈卷须状卷曲，花瓣呈卵圆形，花蕾呈卵球形；果实呈球形，成熟后带白色，有种子1-3颗，种子呈倒卵形；花期5-6月；果期7-9月。首载于《神农本草经》。白蔹原产于中国，主要分布于东北、华北、华东、华中及西南各地，在日本也有分布。', '消肿止痛，舒筋活血，止血。可用于外伤出血，骨折，跌打损伤，风湿关节痛。光叶蛇葡萄光叶蛇葡萄，Ampelopsis heterophylla (Thunb.) Sieb. et Zucc. var. hancei Planch.，是属于葡萄科蛇葡萄属 的一种植物。浙江分布的蛇葡萄属植物共4个词条115阅读牛果藤牛果藤（学名：Nekemias cantoniensis (Hook. & Arn.) J. Wen & Z. L. Nie）是葡萄科牛果藤属下的一种植物。', 1);
INSERT INTO `herb` VALUES (570, '百合', '亚洲东部，欧洲，北美洲，北半球', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Glio_Lily1.jpg/250px-Glio_Lily1.jpg', '百合(Lilium brownie var.viridulum Baker)日常生活中提到百合，大多数时候是指统称的百合科百合属植物。而狭义的百合(Lilium brownie var.viridulum Baker)是指百合科百合属野百合（Lilium brownii）的变种，与野百合的区别在于叶呈倒披针形至倒卵形。百合又名山百合、香水百合、天香百合，也有人认为食用百合中的精品“龙牙百合”在分类学上就是此变种：Lilium brownie var.viridulum Baker。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (571, '百部', '中国浙江、江苏、安徽、江西等省 日本曾引入栽培', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/ManShengBaiBu.JPG/250px-ManShengBaiBu.JPG', '百部（Stemona japonica (Blume) Miq.）是百部科百部属多年生攀援性草本植物。地下根为块状根，成束，肉质，为长纺锤形；茎较长；叶为卵形、卵状披针形，顶端渐尖或锐尖，叶有叶柄；花梗紧贴叶片中脉生长，花单生或数朵排列成总状花序；蒴果卵形，稍扁，表面暗红棕色；种子椭圆形，紫褐色。花期5-7月，果期7-10月。百部分布于中国浙江、江苏、安徽、江西等省，日本曾引入栽培；生长于海拔300-400米的山坡草丛、路旁和林下。', '【功效】功效在历代本草著作，例如《本草纲目》《本草经疏》中均有记载\r\n', 1);
INSERT INTO `herb` VALUES (572, '半夏', '中国长江流域以及东北、华北等地区', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Puanh_ghrah_hrua.jpg/250px-Puanh_ghrah_hrua.jpg', '半夏（Pinellia ternata (Thunb.) Ten. ex Breitenb.），是天南星科半夏属多年生草本植物。具块茎，叶基出，有长柄，叶柄基部常有珠芽。肉穗花序具细长附属体；花雌雄同株，无花被；雌花部分与佛焰苞贴生。浆果小，熟时红色。半夏有两种繁殖方式，有性生殖通过种子完成，营养繁殖通过珠芽和块茎完成。因仲夏可采其块茎，故名“半夏”。半夏为广布物种，中国除内蒙古、新疆、青海、西藏未见野生外，其余各省区均有分布；人工栽培始于20世纪70年代中国山东和江苏等地，现日本、朝鲜等国也有分布。', '对治疗止咳、化痰、燥湿、毒虫咬伤、无名肿毒、痈疮疖肿、支气管炎、咳嗽痰多都有非常不错吃橘少年欢乐多赞👉', 1);
INSERT INTO `herb` VALUES (573, '巴豆', '四川、湖南、湖北、云南、贵州、广西、广东、福建、台湾、浙江等', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Croton_tiglium_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-197.jpg/250px-Croton_tiglium_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-197.jpg', '巴豆，（Croton tiglium L.）为大戟科巴豆属植物巴豆树的干燥成熟果实，其根及叶亦供药用。巴豆树为常绿乔木，高6～10米。中医药上以果实入药，性热，味辛，功能破积、逐水、涌吐痰涎，有助于治寒结便秘、腹水肿胀、寒邪食积所致的胸腹胀满急痛、大便不通、泄泻痢疾、水肿腹大、痰饮喘满、喉风喉痹、痈疽、恶疮疥癣。有大毒，须慎用。产于浙江南部、福建、江西、湖南、广东、海南、广西、贵州、四川和云南等省区。', '散寒除湿，祛风活血，用于寒湿痹痛，瘀血腹痛，产后风瘫，跌打肿痛，皮肤瘙痒。四川分布的巴豆属植物共3个词条152阅读石山巴豆石山巴豆（Croton euryphyllus W. W. Sm.）为大戟科、巴豆属的灌木，叶纸质，花期4-5月。分布于广西、四川西南部、贵州南部及云南等省区。巴豆巴豆，（Croton tiglium L.）为大戟科巴豆属植物巴豆树的干燥成熟果实，其根及叶亦供药用。巴豆树为常绿乔木，高6～10米。', 1);
INSERT INTO `herb` VALUES (574, '八角茴香', '福建、台湾、广西、广东、贵州、云南、越南等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Dried_Star_Anise_Fruit_Seeds.jpg/250px-Dried_Star_Anise_Fruit_Seeds.jpg', '八角茴香是木兰科八角属的常绿乔木。树皮灰褐色或红褐色；叶互生或叶簇生于枝端；叶片革质，椭圆状倒卵形或椭圆状倒披针形；花单生于叶腋或近顶生，花梗短，花被排成数轮，覆瓦状排列，内轮粉红色至深红色，花柱短于或近等长于子房；果实由8个骨突果放射排列成八角形的聚合果，红褐色或淡棕色；种子为扁卵形，红褐色，表面有光泽；花期4-5月；果期6-7月。 八角茴香原产于广西，分布于福建、广东、贵州等地，在西班牙、俄罗斯、日本等国也有分布。八角茴香喜温暖、潮湿气候，忌强光和干旱，怕强风。', '【功效】功效，有助于缓解痉挛、减轻疼痛的作用；茴香烯能促进骨髓细胞成熟并释放入外周血液，有明显的升高白细胞的作用，主要是升高中性粒细胞，可用于白细胞减少症的作用\r\n', 1);
INSERT INTO `herb` VALUES (575, '八角莲', '湖南、湖北、浙江、江西、安徽、广东、广西、云南、贵州、四川、河南、陕西等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Dysosma.jpg/250px-Dysosma.jpg', '八角莲（Dysosma versipellis (Hance) M. Cheng）小檗科鬼臼属的多年生草本。植株高40~150厘米；根状茎直立，不分枝，无毛，淡绿色；叶互生，薄纸质，呈盾状，叶脉明显隆起，边缘有细齿；花深红色，5~8朵簇生于叶下方，下垂，花瓣呈勺状倒卵形，无毛；子房呈椭圆形；浆果呈椭圆形，种子多数；花期3~6月；果期5~9月。八角莲原产于中国，分布于湖南、湖北、浙江、江西、安徽、广东、广西、云南、贵州、四川、河南、陕西等地。', '其主要有两种用途。第一种是因为它八角形的叶子非常美观，所以具有观赏性。同时它 根茎可做药用，治跌打损伤，半身不shichunhua2021赞八角莲的基本介绍💥植物外观：因为叶子是呈八角形的形状，看上去就像一朵莲花，所以被人们称之为八角莲。它的根状比较粗壮，部直立，上面有两枚叶子，有4-9浅裂。  🙌珍稀', 1);
INSERT INTO `herb` VALUES (576, '薄荷', '主要分布在中国、日本、韩国、俄罗斯及北美等地区，在中国南北各地均有分布[1][6]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/b3/Mentha_canadensis.jpg/250px-Mentha_canadensis.jpg', '薄荷（Mentha canadensis L.）又名南薄荷、土薄荷，是唇形科薄荷属的一种多年生草本植物。茎直立，高30-60厘米；叶片长圆状披针形、披针形、椭圆形或卵状披针形，边缘有牙齿状锯齿；轮伞花序腋生，花萼管状钟形，花冠淡紫；雄蕊4，花丝无毛，花药卵圆形，花柱略超出雄蕊，花盘平顶；小坚果卵珠形，黄褐色。花期7-9月，果期10月。薄荷喜阳，略耐阴， 对土壤要求不高，主要生长在温带生物群落中，多分布于山野湿地河旁，最高可在海拔3500米的地方生长。', '【主治】主治外感风热、头痛、咽喉肿痛等症，在侗族、瑶族、苗族传统医药中亦有应用\r\n', 1);
INSERT INTO `herb` VALUES (577, '苍术', '黑龙江、辽宁、吉林、内蒙古、河北、山西、甘肃、陕西、河南', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/%E3%83%9B%E3%82%BD%E3%83%90%E3%82%AA%E3%82%B1%E3%83%A92.JPG/250px-%E3%83%9B%E3%82%BD%E3%83%90%E3%82%AA%E3%82%B1%E3%83%A92.JPG', '苍术（Atractylodes Lancea (Thunb.) DC.）是菊科苍术属多年生草本植物。根状茎平卧或斜升，粗长或通常呈疙瘩状，生多数等粗等长或近等长的不定根。茎直立，单生或少数茎成簇生，全部叶质地硬，两面同色呈绿色，无毛。瘦果倒卵圆状，覆盖稠密的顺向贴伏的白色长直毛。苍术大体可以分为两大类，即北方产的北苍术和南方产的南苍术（又称茅苍术），北苍术产于中国内蒙古、山西、辽宁等地，南苍术产于中国江苏、湖北、河南等地，国外的朝鲜及俄罗斯远东地区均有分布；二者均生长于山坡草地、林下、灌丛及岩缝隙中。', '【主治】主治燥湿健脾；祛风散寒 [11]；明目\r\n', 1);
INSERT INTO `herb` VALUES (578, '苍耳', '中国黑龙江、辽宁、内蒙古及河北 日本及印度尼西亚也有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Xanthium_sibiricum.JPG/250px-Xanthium_sibiricum.JPG', '苍耳（Xanthium strumarium L.）是菊科苍耳属一年生草本植物。苍耳茎被灰白色糙伏毛；叶三角状卵形或心形，边缘有不规则的粗锯齿，下面苍白色；雄头状花序球形，雄花多数，花冠钟形，雌头状花序椭圆形，内层囊状，绿、淡黄绿或带红褐色；具瘦果的成熟总苞卵形或椭圆形；瘦果，倒卵圆形；花期为七月至八月；果期九月至十月。因其子如耳硝，得“耳”之名，熟后色青黑，即苍色，故名苍耳。苍耳产于中国黑龙江、辽宁、内蒙古及河北，日本及印度尼西亚也有分布。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (579, '车前', '中国吉林、辽宁、河北、山西等省及朝鲜，俄罗斯，日本等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Plantago_asiatica.jpg/250px-Plantago_asiatica.jpg', '车前（Plantago asiatica L.），车前科车前属的多年生草本植物。叶片呈卵形，叶片的前端尖或钝，基部逐渐狭窄成柄，叶柄与叶片长度相等；花亭有毛，花为淡绿色，苞片为三角形，花冠为管卵形，花柱为条形；霸果为圆锥形；种子细小，为黑褐色。4～10月开花结果。由于车前草长在战车前的荒地上，因此得名。车前产内蒙古各地，产量较多，乡土种，中国各地均有分布，朝鲜半岛、日本、马来西亚、印度尼西亚和南亚也有。车前喜温暖湿润、阳光充足环境，较耐寒，对土壤要求不严，一般土地、田边、房前屋后均可生长。', '【功效】功效在《神农本草经》《名医别录》《本草纲目》均有记载\r\n', 1);
INSERT INTO `herb` VALUES (580, '川贝母', '中国西藏、云南和四川 也见于甘肃、青海、宁夏、陕西和山西 也分布于尼泊尔', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Fritillaria_cirrhosa_%28Sikkim%2C_India%29_cropped_%26_sharpened.jpg/250px-Fritillaria_cirrhosa_%28Sikkim%2C_India%29_cropped_%26_sharpened.jpg', '川贝母（Fritillaria cirrhosa D. Don）是百合科贝母属多年生草本植物，植株可达50厘米。叶对生，叶片条形至条状披针形，花通常单朵，紫色至黄绿色，每花有叶状苞片，苞片狭长，花药近基着，蒴果长棱上有狭翅。5-7月开花，8-10月结果。主要分布于中国西藏、云南和四川，也见于甘肃、青海、宁夏、陕西和山西，也分布于尼泊尔。通常生于林中、灌丛下、草地或河滩、山谷等湿地或岩缝中。该种是药材“川贝”的主要来源之一。是润肺止咳的名贵中药材，应用历史悠久，驰名中外。（概述图参考来源：中国自然标本馆）', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (581, '穿心莲', '于广东、福建', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Andrographis_paniculata_%28Kalpa%29_in_Narshapur_forest%2C_AP_W2_IMG_0867.jpg/250px-Andrographis_paniculata_%28Kalpa%29_in_Narshapur_forest%2C_AP_W2_IMG_0867.jpg', '穿心莲（Andrographis paniculata (Burm. f.) Wall. ex Nees in Wallich），爵床科穿心莲属的一年生植物。穿心莲茎枝呈四棱形，多分枝，质地较脆，易折断；单叶成对生长，叶柄短或近无柄，叶片展开呈披针形或卵状披针形，全缘或浅波状；上面绿色，下面灰绿色，两面光滑；圆锥花序顶生或腋生花冠淡紫白色，唇形，上唇外弯，下唇直立；萌果长椭圆形至线形两侧呈压扁状，中央具一纵沟。穿心莲在5—9月开花，7—10月结果。', '【功效】功效，但因原料特性，孕妇需慎食且避免与萝卜等食物同食\r\n', 1);
INSERT INTO `herb` VALUES (582, '大青叶', '内蒙古、陕西、河北、山东、浙江、安徽、贵州等', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Clerodendrum_cyrtophyllum.JPG/250px-Clerodendrum_cyrtophyllum.JPG', '大青叶是十字花科大青属植物菘蓝、草大青、爵床科马蓝属植物马蓝、马鞭草科赧桐属植物大青木（路边青）和蓼科蓼属植物蓼蓝的叶。本草考证《神农本草经》列为上品，原名为“蓝实”。《名医别录》中始用大青叶之名。陶弘景谓：“此即今染襟碧所用者，以尖叶者为胜。”菘蓝一名最早见于《新修本草》。苏恭谓：“蓝有数种，陶氏所说乃是菘蓝，其汁为淀甚青者。”其根为药材板蓝根。《本草纲目》谓：“大青，其茎叶皆深青，故名。”大青叶在《本草纲目》中有记：“主热毒痴，黄痘，喉痹，丹毒。”主要用来治疗黄痘，喉咙肿痛，发热头痛和口舌生疮等病症。', '清热解毒，凉血消斑。\r\n用于热病高热烦渴；神昏；斑疹；吐血；衄血；黄疸；泻痢；丹毒；喉痹；口疮；痄腮。\r\n', 1);
INSERT INTO `herb` VALUES (583, '大蓟', '中国南北地区均有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/Cirsium_japonicum_var._maackii.JPG/250px-Cirsium_japonicum_var._maackii.JPG', '大蓟，中药名。为菊科植物蓟Cirsium japonicum Fisch. ex DC.的干燥地上部分。夏、秋二季花开时采割地上部分，除去杂质，晒干。', '凉血止血，祛瘀消肿。\r\n用于衄血，吐血，尿血，便血，崩漏，外伤出血，痈肿疮毒。\r\n', 1);
INSERT INTO `herb` VALUES (584, '枣', '原产中国，亚洲、欧洲和美洲常有栽培。', 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Ziziphus_zizyphus_foliage.jpg/250px-Ziziphus_zizyphus_foliage.jpg', '枣（拉丁学名：Ziziphus jujuba Mill.），别称枣子，大枣、刺枣，贯枣。鼠李科枣属植物，落叶小乔木，稀灌木，高达10余米，树皮褐色或灰褐色，叶柄长1-6毫米，或在长枝上的可达1厘米，无毛或有疏微毛，托叶刺纤细，后期常脱落。花黄绿色，两性，无毛，具短总花梗，单生或密集成腋生聚伞花序。核果矩圆形或是长卵圆形，长2-3.5厘米，直径1.5-2厘米，成熟后由红色变红紫色，中果皮肉质、厚、味甜。种子扁椭圆形，长约1厘米，宽8毫米。生长于海拔1700米以下的山区，丘陵或平原。广为栽培。', '枣味甘、性平温、入脾胃经。所以具有补脾和胃、益气生津的', 1);
INSERT INTO `herb` VALUES (585, '地骨皮', '中国', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Lycium_chinense%28siamak_sabet%29_%282%29.jpg/250px-Lycium_chinense%28siamak_sabet%29_%282%29.jpg', '地骨皮，别名枸杞皮，为茄科、枸杞属植物，是枸杞Lycium chinense Mill.的根皮。可入药，具有退热除蒸之效，凉血除蒸、清肺降火等功效。', '【功效】功效\r\n', 1);
INSERT INTO `herb` VALUES (586, '地黄', '辽宁、河北、河南、山东、山西、陕西、甘肃、内蒙古、江苏等省。越南、朝鲜等有引种栽培[10]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2b/The_flower_of_Rehmannia_glutinosa.JPG/250px-The_flower_of_Rehmannia_glutinosa.JPG', '地黄（Rehmanniaglutinosa (Gaertn.) Libosch. ex Fisch. & C. A. Mey.）列当科地黄属多年生草本植物。植株密被灰白色长柔毛和腺毛。', '【主治】主治 温热病高热烦躁，吐血衄血，口干，咽喉肿痛，中耳炎，烫伤\r\n', 1);
INSERT INTO `herb` VALUES (587, '防风', '中国东北、华北、西北', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/%E9%98%B2%E9%A3%8E.JPG/250px-%E9%98%B2%E9%A3%8E.JPG', '防风（Saposhnikovia divaricata (Turcz.) Schischk.），伞形科防风属多年生草本植物。主根为圆锥形，淡黄褐色。茎单生。花瓣白色，倒卵形。果窄椭圆形或椭圆形，背稍扁，幼果有海绵质瘤状突起。花期8~9月，果期9~10月。时珍曰：“屏风者，防风隐语也。”防者，御也。其功疗风最要，故名防风。防风在中国主产于东北及内蒙古东部，在朝鲜、蒙古及俄罗斯西伯利亚东部亦有分布。防风一般生于向阳山坡及草原，耐干旱、耐寒、怕水涝。适宜于在地势高、夏季凉爽的地方种植。对土壤要求通常不高。', '【主治】主治风寒感冒、头痛、发热、关节酸痛、破伤风；此外，防风叶、防风花也可供药用\r\n', 1);
INSERT INTO `herb` VALUES (588, '茯苓', '安徽、云南 、湖北等地', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3b/Pepper_-_Flora_Sinensis_1656_%282950795%29.jpg/250px-Pepper_-_Flora_Sinensis_1656_%282950795%29.jpg', '茯苓，中药名。为多孔菌科真菌茯苓Poria cocos(Schw.)Wolf的干燥菌核。多于7～9月采挖，挖出后除去泥沙，堆置“发汗”后，摊开晾至表面干燥，再“发汗”，反复数次至现皱纹、内部水分大部散失后，阴干，称为“茯苓个”；或将鲜茯苓按不同部位切制，阴干，分别称为“茯苓块”和“茯苓片”。', '自古以来都是入药上品《药品化义》称白茯苓“味独甘淡，甘则能补，淡则能渗，甘淡属土，用补脾阴，土旺生金，兼益肺气”，特别提出了白茯苓的专门药理规则。MK工作室179茯苓的人文历史：其貌不扬却大有来头著名大诗人杜甫就曾写下“知子松根长茯苓，迟暮有意来同煮。”的诗句，借着茯苓和松树紧密相连的关系，表达了自己对友人志趣相投的感情，也暗含着现在的自己知音难觅的忧伤。', 1);
INSERT INTO `herb` VALUES (589, '覆盆子', '华北中部、华南中部，日本、韩国、俄罗斯、北美、欧洲等[18]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Raspberries_%28Rubus_Idaeus%29.jpg/250px-Raspberries_%28Rubus_Idaeus%29.jpg', '覆盆子（Rubus idaeus L.），落叶灌木，高1—2米；枝褐色或红褐色，幼时被绒毛状短柔毛，疏生皮刺。小叶3-7枚，长卵形或椭圆形，顶生小叶常卵形，有时浅裂，顶端短渐尖，基部圆形，顶生小叶基部近心形，上面无毛或疏生柔毛，下面密被灰白色绒毛，边缘有不规则粗锯齿或重锯齿；叶柄均被绒毛状短柔毛和稀疏小刺；托叶线形，具短柔毛。', '【主治】主治气血虚弱，肺虚咳嗽，心悸盗汗，烦渴，风湿痹痛，淋病，水肿，痘疹不透\r\n', 1);
INSERT INTO `herb` VALUES (590, '葛根', '几乎遍布全国', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Flowering_kudzu.jpg/250px-Flowering_kudzu.jpg', '葛根，是一味中药。为豆科葛属植物葛（Pueraria montana (Lour.) Merr.）的干燥根。秋、冬二季采挖，趁鲜切成厚片或小块；干燥。甘、辛，凉，有升阳止泻，通经活络，解酒毒之功。中药葛根作为辛凉解表药，有清热、生津、止渴之效，药理学研究其具有抗氧化应激反应、减少炎症因子积聚、增强胰岛素敏感性、减少胰岛素抵抗等作用。常用于外感发热头痛，项背强痛，口渴，消渴，麻疹不透，热痢，泄泻，眩晕头痛，中风偏瘫，胸痹心痛，酒毒伤中。', '《神农本草经》《雷公炮制药性解》言：葛根“主消渴，身大热”，“发伤寒之表邪，止胃虚之消渴”，具有解肌退热、生津止渴、升阳止泻等功效。《医方考》和《幼幼集成》言：“消渴，无水也”，“夫消渴者，枯燥之病也”，且近代研究数据表明：中医药治疗糖尿病时，药物性味以甘辛为主，是以葛根从性味、归经及功效而言适用于治疗糖尿病及其并发症。且近代医者研究表明葛根是治疗糖尿病的核心高频单味药之一，其二阶、三阶关联药物使用频率均为最高。', 1);
INSERT INTO `herb` VALUES (591, '枸杞子', '宁夏', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/Lycium_chinense%28siamak_sabet%29_%282%29.jpg/250px-Lycium_chinense%28siamak_sabet%29_%282%29.jpg', '枸杞子，为茄科植物枸杞的成熟果实。夏、秋果实成熟时采摘，除去果柄，置阴凉处晾至果皮起皱纹后，再暴晒至外皮干硬、果肉柔软即得。遇阴雨可用微火烘干。枸杞子具有多种保健功效，是卫生部批准的药食两用食物。适量食用有益健康，配合菊杞茶有清肝明目的效果。', '历代医家治疗肝血不足、肾阴亏虚引起的视物昏花和夜盲症，常常使用枸杞子。枸杞子含有β一胡萝b素、叶黄素等，具有提高人体免疫功能、防止肿瘤形成及预防动脉粥样硬化等作用。中医认为枸杞味甘，性平，入肝肾经，具有补气强精、滋补肝肾、抗衰老、止消渴、暖身体的功效。\r\n', 1);
INSERT INTO `herb` VALUES (592, '贯众', '中国、日本、朝鲜、越南、泰国', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Cyrtomium_fortunei.jpg/250px-Cyrtomium_fortunei.jpg', '贯众（学名：Cyrtomium fortunei J. Sm.）是鳞毛蕨科、贯众属多年生蕨类植物。根茎直立，密被棕色鳞片。叶簇生，叶片矩圆披针形，奇数一回羽状；侧生羽片互生，披针形，多少上弯成镰状。叶为纸质，两面光滑；叶轴腹面有浅纵沟，疏生披针形及线形棕色鳞片。匏子囊群遍布羽片背面；囊群盖圆形，盾状，全缘。原产中国华北西南部、陕甘南部、东南沿海、华中、华南至西南东部区域及日本、朝鲜南部、越南北部、泰国。生于阴坡林下、沟谷和路边。贯众是一种奇特的古老蕨类。', '【功效】功效：贯众全草晒干后可入药，味苦、涩，性寒\r\n', 1);
INSERT INTO `herb` VALUES (593, '广藿香', '菲律宾等亚热带地区 中国广东等地有栽培', 'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Pogostemon_cablin_kz02.jpg/250px-Pogostemon_cablin_kz02.jpg', '广藿香（Pogostemon cablin (Blanco) Benth.）是唇形科刺蕊草属草本植物。茎直立，被柔毛；叶圆形或宽卵形；轮伞花序，下部的稍疏离。广藿香在中国栽培区的花期为4月。《纲目》云：“豆叶曰藿，其叶似之，而草味芳香，故曰藿香。枝香者，亦以香气得名”，因此得名广藿香。广藿香原产于菲律宾等亚热带地区，中国广东等地有栽培。', '【功效】功效，在历代本草著作，例如《本草纲目》《神农本草经》中均有记载\r\n', 1);
INSERT INTO `herb` VALUES (594, '麻黄', '主产于中国山西、河北、甘肃、内蒙古等地[2]', 'https://pic.baike.soso.com/ugc/baikepic2/770/cut-20180918111534-1423184657_jpg_491_327_47848.jpg/300', '麻黄（英文名：Ephedrae Herba）是草麻黄、中麻黄和木贼麻黄的统称，草本状小灌木，高20-40厘米。茎基部多分枝，丛生；木质茎短或成匍匐状。叶膜质鞘状，下部合生，上部2裂，裂片锐三角形，急尖。通常雌雄异株；雄球花为复穗状，长约14毫米；雌球花单生于当年生枝顶或老枝叶腋。雌球花成熟时苞片肉质、红色，近球形，成浆果状。种子常2粒，包于红色肉质苞片内。花期5-6月，种子成熟期8月。麻黄主产于中国山西、河北、甘肃、内蒙古等地。生长于干燥的山冈、高地、山田或干枯的河床中。麻黄用种子及分株繁殖。', '【功效】功效与作用有哪些♦︎麻黄是中药中发散风寒药，具有发汗解表、宣肺平喘、利水消肿的作用，主要用于外感风寒所导致的恶寒发热、头身疼痛等症状，搭配杏仁、甘草配伍组成三拗汤，有增强止咳平喘功效，可以说在临床方面\r\n', 1);
INSERT INTO `herb` VALUES (595, '海金沙', '亚洲暖温带至热带地区 中国各地均有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Lygodium_japonicum.jpg/250px-Lygodium_japonicum.jpg', '海金沙（Lygodium japonicum (Thunb.) Sw.）是海金沙科海金沙属草本植物，其叶轴具窄边，羽片多数，对生于叶轴短距两侧，不育羽片尖三角形，两侧有窄边，叶干后褐色，纸质；孢子囊穗长度过小羽片中央不育部分，排列稀疏，暗褐色；其果长度过小羽片中央不育部分，排列稀疏，暗褐色，无毛；孢子期5-11月。因其秋季采摘，黄如细沙，如海沙闪亮发光，故名。海金沙原产于亚洲暖温带至热带地区，中国各地均有分布。海金沙生于山坡草丛或灌木丛中，耐光，忌阳光直射，喜温暖湿润环境、喜散射光，喜排水良好的沙质土壤。', '通利小肠，疗伤寒热狂，治湿热肿毒，小便热淋膏淋血淋石淋经痛，解热毒气。中国四川用之治筋骨疼痛。 [1]海金沙用于治疗热淋、石淋、血淋、膏淋、尿道涩痛，现代药理研究表明还具有降血糖、抗氧化等', 1);
INSERT INTO `herb` VALUES (596, '海带', '北半球 同时南半球也有少量分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Kombu.jpg/250px-Kombu.jpg', '海带（学名：Saccharina japonica）是海带科糖海带属藻类。别名纶布，药用称昆布。多年生大型食用藻类。抱子体型大，褐色，扁平带状，最长可达20米。分叶片、柄部和固着器，固着器呈假根状。叶片为表皮、皮层和髓部组织所组成，叶片下部有抱子囊。具有黏液腔，可分泌滑性物质。固着器树状分支。海带属于亚寒带藻类，是北太平洋特有地方种类，大西洋也有一些海带分布。海带大部分都分布于北半球，同时南半球也有少量分布。中国北部沿海及浙江、福建沿海有大量栽培，产量居世界第一。一般多生长在潮下带海底岩石上。', '【主治】主治肺结核，浮肿，小便不利，荞麦中毒\r\n', 1);
INSERT INTO `herb` VALUES (597, '红景天', '中国新疆等地，欧洲北部至俄罗斯、蒙古、朝鲜、日本', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ef/Rhodiola_rosea_a2.jpg/250px-Rhodiola_rosea_a2.jpg', '红景天（Rhodiola rosea L.），为景天科红景天属多年生草本植物，高10-20厘米。根粗壮，圆锥形，肉质，褐黄色，根颈部具多数须根，根茎短，粗状，圆柱形，被多数覆瓦状排列的鳞片状的叶。花序伞房状，密集多花，长2厘米，宽3-6厘米；花瓣4，黄绿色，线状倒披针形或长圆形，长3毫米，钝；种子披针形，长2毫米，一侧有狭翅。花期4-6月，果期7-9月。红景天分布于中国新疆、山西、河北、吉林；在欧洲北部至俄罗斯、蒙古、朝鲜、日本亦有分布。', '有益气活血，通脉平喘。用于气虚血瘀，胸痹心痛，中风偏瘫，倦怠气喘等。 [3] [16] [17]中药-红景天观赏', 1);
INSERT INTO `herb` VALUES (598, '胡芦巴', '我国南北各地均有栽培，在西南、西北各地呈半野生状态', 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Trigonella_foenum-graecum_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-273.jpg/250px-Trigonella_foenum-graecum_-_K%C3%B6hler%E2%80%93s_Medizinal-Pflanzen-273.jpg', '胡芦巴，中药名。为豆科植物胡芦巴Trigonella foenum-graecum L.的干燥成熟种子。中国南北各地均有栽培，在西南、西北各地呈半野生状态。具有温肾助阳，祛寒止痛之功效。常用于肾阳不足，下元虚冷，小腹冷痛，寒疝腹痛，寒湿脚气。', '温肾助阳，祛寒止痛。\r\n用于肾阳不足，下元虚冷，小腹冷痛，寒疝腹痛，寒湿脚气。\r\n', 1);
INSERT INTO `herb` VALUES (599, '鹿茸', '吉林省长白山区', 'https://pic.baike.soso.com/ugc/baikepic2/0/20231023212013-464681643_jpeg_1180_780_816843.jpg/300', '鹿茸（cartialgenous），是指梅花鹿或马鹿的雄鹿未骨化而带茸毛的幼角，乃名贵中药材。鹿茸中含有磷脂、糖脂、胶脂、激素、脂肪酸、氨基酸、蛋白质及钙、磷、镁、钠等成分，其中氨基酸成分占总成分的一半以上。古代医家认为，鹿之精气全在于角，而茸为角之嫩芽，气体全而未发泄，故补阳益血之力最盛。明代李时珍在《本草纲目》上称鹿茸“善于补肾壮阳，生精益血，补髓健骨”。鹿是我国传统的名贵药用动物，在现存文献中，汉代文献就有“鹿身百宝”的说法，是灵丹妙药的象征。', '花鹿茸：Cornu Corvi nippon parvum 别名：黄毛茸。', 0);
INSERT INTO `herb` VALUES (600, '虎杖', '主要分布于中国、朝鲜、日本，在中国分布于陕西南部、甘肃南部、华东、华中、华南、西南地区[2]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Reynoutria_japonica_MdE_2.jpg/250px-Reynoutria_japonica_MdE_2.jpg', '虎杖（学名：Reynoutria japonica Houtt.），别名斑庄根、大接骨、酸桶芦、酸筒杆，为蓼科虎杖属的一种多年生草本植物。虎杖的根状茎粗壮，横走；茎直立，丛生，基部木质化，散生红色或紫红色斑点。叶有短柄，宽卵形或卵状椭圆形，顶端有短骤尖；托叶鞘膜质，褐色，早落。花单性，雌雄异株，成腋生的圆锥状花序；花梗细长，中部有关节，上部有翅。瘦果椭圆形，有3稜，黑褐色。花期8-9月，果期9-10月。虎杖主要分布于中国、朝鲜、日本，在中国分布于陕西南部、甘肃南部、华东、华中、华南、西南地区。', '【功效】功效，主治风湿关节疼痛，蛇咬伤等病症 [3]\r\n', 1);
INSERT INTO `herb` VALUES (601, '丁香', '东北、华北、西北（除新疆）以至西南达四川西北部', 'https://pic.baike.soso.com/ugc/baikepic2/0/20230316165609-437968202_jpeg_623_411_308540.jpg/300', '丁香（拉丁学名：Syringa oblata Lindl.），别名“紫丁香”，是唇形目木樨科丁香属植物', '其吸收二氧化碳的能力较强，对二氧化碳污染具有一定净化作用；花可提制芳香油；嫩叶可代茶', 0);
INSERT INTO `herb` VALUES (602, '花椒', '中国 原生于喜马拉雅山脉', 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Poivre4.jpg/250px-Poivre4.jpg', '花椒(Zanthoxylum bungeanum Maxim.)，是芸香科、花椒属落叶小乔木多年生植物。位列调料“十三香”之首，素有“调味之王”的美誉。具高茎，枝有短刺，叶为小叶片，叶片对生，无柄。花序顶生或侧枝顶生；花被片黄绿色，形状及大小大致相同；雌花很少有发育雄蕊。果成紫红色，有单个分果瓣散生微凸起的油点。4-5月开花，8-9月或10月结果。因果皮有细小突出的油点呈斑状形似花，故名“花椒”。花椒为广布物种，花椒原产于中国，原生于喜马拉雅山脉。现日本，韩国等国也有分布。', '还可以治胃腹冷痛，全身冰冷等病症；又可以表皮麻醉剂，在医学当中有重要的', 1);
INSERT INTO `herb` VALUES (603, '黄芩', '中国、苏联东西伯利亚、蒙古、朝鲜、日本', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Scutellaria_baicalensis_flowers.jpg/250px-Scutellaria_baicalensis_flowers.jpg', '黄芩（Scutellaria baicalensis Georgi）为唇形科黄芩属多年生草本植物；茎分枝，近无毛，或被向上至开展微柔毛；根茎肉质，分枝；叶披针形或线状披针形，先端钝，基部圆，全缘，两面无毛或疏被微柔毛；花梗被微柔毛；花萼密被微柔毛，具缘毛；花冠紫红或蓝色，密被腺柔毛，下唇中裂片三角状卵形；小坚果黑褐色，卵球形，腹面近基部具脐状突起；花期7至8月；果期8至9月。《本草纲目》云：“苓说文作菳，谓其色黄也。”黄芩即古人所谓之“芩”，因苓草色泛黄而得名。', '黄琴这种植物可以治虫，中药能够清热解毒。治疗嗓子痛，嗓子干等一些呼吸道问题，还能够治疗腹泻，但是脾胃功能不好的患者，最好不要大量的使用。 🍃食用方法：作为一种', 1);
INSERT INTO `herb` VALUES (604, '苦参', '中国 除青海、新疆外 国内各地均有分布 在印度、日本、朝鲜、俄罗斯也有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Sophora_flavescens.jpg/250px-Sophora_flavescens.jpg', '苦参（Sophora flavescens Aiton），豆科槐属多年生亚灌木，高1-2米。茎直立多分枝；叶卵状披针形，背面密生柔毛；总状花序顶生，花冠黄白色，旗瓣卵状匙形；果圆柱形，呈串珠状；种子褐色扁球状；花期6-7月，果期7-9月。《本草纲目》中记载了苦参名称的由来：“苦似味名，参以功名，槐似叶形名也。”苦参，原产于中国，除青海、新疆外，国内各地均有分布，在印度、日本、朝鲜、俄罗斯也有分布。常见于山坡、沙地草坡灌木林中或田野附近 。苦参喜温暖燥热气候，耐寒耐高温，喜肥怕涝耐盐碱沙质土壤。', '【功效】功效是清热燥湿、祛风、杀虫、利尿，在临床上多用于湿疹、阴囊潮湿、白带、湿疮的皮肤瘙痒\r\n', 1);
INSERT INTO `herb` VALUES (605, '雷公藤', '国内有台湾、福建、江苏、浙江、安徽等地，朝鲜、日本也有分布', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Tripterygium_regelii_1.JPG/250px-Tripterygium_regelii_1.JPG', '雷公藤是卫矛科雷公藤属的藤本灌木植物。株高达4米；小枝棕红色；叶椭圆形至卵形，先端急尖或短渐尖，基部阔楔形或圆形，边缘有细锯齿；圆锥聚伞花序较窄小，萼片先端急尖，花瓣长方卵形，边缘微蚀；翅果长圆状，中央果体较大，种子细柱状；花期6-7月；果期7-8月。“雷公藤”一名始载于《本草纲目拾遗》，得名于古代神话中的司雷之神雷公，示其毒性之烈。雷公藤产于台湾、福建、江苏、浙江、安徽等省地，朝鲜、日本也有分布。喜较阴凉的山坡、林木丛中或溪边。宜在偏酸性，肥沃土层深厚的砂质土壤或黄壤土中栽培。', '【功效】功效，主要用于外用，治疗风湿性关节炎、皮肤发痒等，不可内服\r\n', 1);
INSERT INTO `herb` VALUES (606, '连翘', '河北、山西、陕西、山东、安徽西部、河南、湖北、四川[8]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/6f/Forsythia_suspensa_SZ3.png/250px-Forsythia_suspensa_SZ3.png', '连翘（Forsythia suspensa (Thunb.) Vahl）木樨科连翘属灌木，枝开展或下垂，棕色或淡黄褐色；叶通常为单叶，叶片呈卵形或椭圆形，先端锐尖，叶缘上面呈深绿色，下面为淡黄绿色；花通常单生或2至数朵着生于叶腋，花萼绿色，裂片呈长圆形；果呈卵球形或长椭圆形；花期3—4月；果期7—9月。因为连翘的形态如古代的连车和翘车，故得此名。', '【主治】主治气阴两虚、烦热口渴、虚劳咳嗽、跌扑损伤、吐血、外伤出血等症\r\n', 1);
INSERT INTO `herb` VALUES (607, '龙胆草', '欧洲山脉 除最北部和最南部地区外 、高加索地区和亚洲西北部', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9f/Gentiana_scabra2.jpg/250px-Gentiana_scabra2.jpg', '龙胆草（学名：Gentiana cruciata L.）是龙胆科、龙胆属植物。高10-50厘米，无分枝，有棱角，基部有不育叶莲座丛。茎密叶，叶披针形，革质，横向对生，无柄，基部成对融合。上叶角有1-3朵花。冠紫蓝色，内浅蓝色，狭钟形，最上部裂成4部分，有3个角尖，长2-2.5 厘米。蒴果椭圆形，有4个短尖端。分布于欧洲山脉（除最北部和最南部地区外）、高加索地区和亚洲西北部。生长于干旱的草地、牧场、空地和灌木丛，喜欢石灰质土壤。在阿尔卑斯山上，它从海拔800米到2600米不等。', '【功效】功效与作用🍂1、镇静降压 龙胆草本身含有一定的龙胆碱，这类物质具有一定的镇静作用，在进入身体之后，可以快速的镇静效果，如果服用过量，就会导致身体出现麻醉的现象，具有一定的麻醉作用\r\n', 1);
INSERT INTO `herb` VALUES (608, '麦冬', '中国、日本、越南、印度', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/Ophiopogon_japonicus_%28fruits%29.jpg/250px-Ophiopogon_japonicus_%28fruits%29.jpg', '麦冬（拉丁学名：Ophiopogon japonicus (L. f.) Ker Gawl.），百合科沿阶草属草本植物，根较粗，中间或近末端具椭圆形或纺锤形小块根,小块根淡褐黄色；茎很短；花单生或成对生；种子球形；花期5—8月，果期8-9月。麦冬原名麦门冬，其名源于“虋冬”，最早见载于先秦著作《山海经—中山经》之条谷山：“其木多槐、桐，其草多芍药、虋冬”。', '麦冬草经常用于中药的植物。对治疗肺热，咳嗽，胃痛，失眠等都有帮助，除此之外，也可以作为熬粥的食材，搭配瘦告涤BI1矮麦冬草的基本养护💥种植方法：四季常绿，耐寒耐旱，还能观花，适用度非常高，种植一般来说要比草坪要复杂一些。市场上售卖的，需要经过修根、分拣，对于一些过长的叶片还需要剪短，然后才是种植。所以从工序上，种植比种植草坪要复杂一些，也更加耗时间。  🙌养护方法：虽然种植的时候比较麻烦，春日苦寒来3一定要给予充足的阳光💥形态特征：麦冬草是百合科的植物。', 1);
INSERT INTO `herb` VALUES (609, '牛蒡', '原产欧亚大陆，在亚洲、欧洲、美洲及大洋洲等地均有分布；在中国各地普遍栽培。', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/ArctiumLappa1.jpg/250px-ArctiumLappa1.jpg', '牛蒡（学名：Arctium lappa L.），又名大力子、恶实等，为菊科牛蒡属的二年生草本植物。其具有肉质直根；茎直立，粗壮，通常带紫红或淡紫红色；基生叶宽卵形，基部心形，两面异色，上面绿色，叶柄灰白色，被稠密的蛛丝状绒毛及黄色小腺点；头状花序多数或少数在茎枝顶端排成疏松的伞房花序或圆锥状伞房花序，花序梗粗壮，总苞卵形或卵球形，小花紫红色；瘦果倒长卵形或偏斜倒长卵形，两侧压扁，浅褐色。花果期6～9月。牛蒡原产欧亚大陆，在亚洲、欧洲、美洲及大洋洲等地均有分布。在中国，其在各地普遍栽培。', '【功效】功效，主治风寒感冒、胸闷呕吐、胎动不安等症状\r\n', 1);
INSERT INTO `herb` VALUES (610, '蒲公英', '世界的原生范围是西伯利亚到东亚地区[3]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Head_to_Head_-_geograph.org.uk_-_409345.jpg/250px-Head_to_Head_-_geograph.org.uk_-_409345.jpg', '蒲公英（学名：Taraxacum mongolicum Hand.-Mazz.），为菊科蒲公英属下一种多年生草本植物，别名蒙古蒲公英、黄花地丁、婆婆丁、灯笼草等。根圆柱状，黑褐色，粗壮。叶倒卵状披针形、倒披针形或长圆状披针形；花葶1至数个，与叶等长或稍长，头状花序，舌状花黄色，边缘花舌片背面具紫红色条纹，花药和柱头暗绿色；瘦果倒卵状披针形，暗褐色。花期4-9月，果期5-10月。蒲公英大多生长在于北半球温带和亚热带地区，少数生长在热带地区，常生长于中、低海拔地区的山坡草地、路边、田野、河滩。', '【主治】主治感冒，还可用作外伤的止血和促进创伤愈合的药物，所含的异黄酮类物质具有抗癌作用\r\n', 1);
INSERT INTO `herb` VALUES (611, '紫苏', '全国各地广泛栽培。不丹，印度，中南半岛，南至印度尼西亚（爪哇），东至日本，朝鲜也有。[1]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/Perilla_frutescens%27_flower.jpg/250px-Perilla_frutescens%27_flower.jpg', '紫苏（Perilla frutescens (L.) Britt.）为唇形科紫苏属一年生直立草本植物。植株高30～100厘米，茎绿色或紫色，具钝四棱；叶对生，阔卵形或近圆形，边缘具锯齿，基部圆形或阔楔形；花小，淡紫色或白色，组成轮伞花序，花萼钟形；苞片宽卵圆形或近圆形；小坚果近球形，灰褐色，花期8～11月，果期8～12月。因叶色、香气、用途不同，紫苏在栽培上分为叶用型、籽用型和药用型多个变种。紫苏原产于中国，已有两千多年栽培历史，现广泛分布于全国各地，在不丹、印度、朝鲜等地亦有栽培。', '【主治】主治风寒感冒、胸闷呕吐、胎动不安等症状 [11]\r\n', 1);
INSERT INTO `herb` VALUES (612, '泽泻', '亚洲、欧洲、非洲、北美洲、大洋洲等', 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/AlismaPlant1.jpg/250px-AlismaPlant1.jpg', '泽泻（Alisma plantago-aquatica L.）是泽泻科泽泻属多年生水生或沼生草本植物。泽泻茎块大；叶通常多数，呈水叶条形或披针形，挺水叶宽披针形、椭圆形至卵形；花两性，外轮花被卵形，内轮花被圆形，白色，粉红色或浅紫色，花柱直立，花药椭圆形，黄色，或淡绿色；花托平凸，近圆形；瘦果椭圆形，或近矩圆形；种子紫褐色；花果期5-10月。因其能利尿行水，如泽水之泻也，故名泽泻。', '利水渗湿，泄热，化浊降脂。用于小便不利，水肿胀满，泄泻尿少，痰饮眩晕，热淋涩痛，高脂血症。用药禁忌：肾虚滑精、无湿热者禁服。 [8]药理', 1);
INSERT INTO `herb` VALUES (613, '射干', '热带、亚热带及温带地区', 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Belamcanda_chinensis_2007.jpg/250px-Belamcanda_chinensis_2007.jpg', '射干（Belamcanda chinensis (L.) Redouté）是鸢尾科射干属多年生草本植物。根状茎为不规则的块状，斜黄色或黄褐色；花橙红色，散生紫褐色的斑点；花药呈条形；子房下位，倒卵形；蒴果倒卵形或长椭圆形；种子圆球形，黑紫色，有光泽。花期6~8月，果期7~9月。射干，在《荀子·劝学篇》中就有记载：“西方有木焉，名日射干，茎长四寸，生于高山之上，而临百仞之渊。', '【主治】主治小便短赤，黄疸，疮痈，胸膈烦热等病症\r\n', 1);
INSERT INTO `herb` VALUES (614, '桑白皮', '河南省、安徽省、四川省、湖南省、河北省、广东省', 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/MorusAlba.jpg/250px-MorusAlba.jpg', '桑白皮为双子叶植物桑科桑除去栓皮后的干燥根皮，始载于《神农本草经》，其性味甘寒，归肺脾经，桑白皮具有泻肺平喘，行水消肿的功效，桑白皮功用主要包括:泻肺平喘、利水消肿、补虚、活血祛瘀、化痰止嗽、止血、生津止渴、泻下通便、驱虫、消食、祛风、通淋、止痛等。', '【主治】主治风热感冒，头痛，咽喉肿痛，肺热咳嗽，黄疸，泄泻，热淋，痈肿疮疖，毒蛇咬伤\r\n', 1);
INSERT INTO `herb` VALUES (615, '石斛', '石斛分布在中国、印度、尼泊尔等地，在中国主要分布在台湾、福建、湖南等省市[1][11]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Dendrobium_nobile_%28BG_Zurich%29-01.JPG/250px-Dendrobium_nobile_%28BG_Zurich%29-01.JPG', '石斛（Dendrobium nobile Lindl.），别名金钗石斛、金钗花、千年润等，是兰科（Orchidaceae ）石斛属（Dendrobium）的植物。石斛茎直立，肉质状肥厚，稍扁的圆柱形。叶革质，长圆形。总状花序从具叶或落了叶的老茎中部以上部分发出，花瓣多少斜宽卵形，具绿色的蕊柱足，药帽紫红色，圆锥形。石斛果实为蒴果，呈长椭圆形或梨形，种子微小且多。花期4-5月。石斛分布在中国、印度、尼泊尔等地，在中国主要分布在台湾、福建、湖南等亚热带地区。', '【主治】主治益胃生津，滋阴清热\r\n', 1);
INSERT INTO `herb` VALUES (616, '天麻', '分布在中国、尼泊尔、不丹、印度、日本、朝鲜半岛至西伯利亚[5]', 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Gastrodia_elata_1.JPG/250px-Gastrodia_elata_1.JPG', '天麻（学名：Gastrodia elata Blume），是兰科天麻属植物，又名神草、赤箭、白龙皮、山地瓜等。植株高30-100厘米，有时可达2米；根状茎肥厚，块茎状，椭圆形至近哑铃形，肉质；茎直立，橙黄色、黄色、灰棕色或蓝绿色，无绿叶，下部被数枚膜质鞘。总状花序长5-30（-50）厘米，通常具30-50朵花；蒴果倒卵状椭圆形，长1.4-1.8厘米，宽8-9毫米。花果期5-7月。天麻在中国分布较为广泛，产吉林、河北、甘肃、江苏、江西、四川、贵州、云南、西藏等地。', '【功效】功效，主治肺虚咳嗽\r\n', 1);

-- ----------------------------
-- Table structure for herb_category
-- ----------------------------
DROP TABLE IF EXISTS `herb_category`;
CREATE TABLE `herb_category`  (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of herb_category
-- ----------------------------
INSERT INTO `herb_category` VALUES (1, '治人');
INSERT INTO `herb_category` VALUES (3, '祛湿');

-- ----------------------------
-- Table structure for herb_growth
-- ----------------------------
DROP TABLE IF EXISTS `herb_growth`;
CREATE TABLE `herb_growth`  (
  `growth_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `herb_id` bigint(20) NOT NULL,
  `batch_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `wet` decimal(4, 1) NOT NULL,
  `temperature` decimal(4, 1) NOT NULL,
  `growth_des` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `growth_longitude` decimal(9, 6) NOT NULL,
  `growth_latitude` decimal(8, 6) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `growth_time` datetime NOT NULL,
  `growth_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `growth_audit_status` int(11) NOT NULL,
  PRIMARY KEY (`growth_id`) USING BTREE,
  INDEX `herb_id`(`herb_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `herb_growth_ibfk_1` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`herb_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `herb_growth_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `his_userinfo`.`user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of herb_growth
-- ----------------------------
INSERT INTO `herb_growth` VALUES (6, 3, 'a212', 23.1, 31.2, '长得高', 21.243135, 12.542312, 4, '2025-06-30 17:58:31', 'xxxx.jpg', 1);
INSERT INTO `herb_growth` VALUES (7, 3, 'a212', 23.1, 31.2, '长得不高1', 21.243135, 12.542312, 4, '2025-07-07 11:47:29', 'xxxx.jpg', 1);
INSERT INTO `herb_growth` VALUES (8, 3, 'a212', 23.1, 31.2, '长得高', 21.243135, 12.542312, 4, '2025-06-30 18:05:23', 'xxxx.jpg', 1);
INSERT INTO `herb_growth` VALUES (15, 3, '321', 32.0, 2.0, '1', 32.000000, 12.000000, 7, '2025-06-30 18:43:12', '123', 0);
INSERT INTO `herb_growth` VALUES (46, 3, 'a2129', 23.1, 31.2, '长得高', 21.243135, 12.542312, 4, '2025-07-07 11:01:43', 'xxxx.jpg', 0);
INSERT INTO `herb_growth` VALUES (47, 4, '2', 30.0, 25.0, '状态很好', 103.858705, 30.783860, 22, '2025-07-08 10:27:11', 'https://qingkaka.oss-cn-hongkong.aliyuncs.com/herb_images/1751941611191.jpg', 0);
INSERT INTO `herb_growth` VALUES (48, 11, '2', 30.0, 25.0, '很好', 103.858705, 30.783860, 22, '2025-07-08 13:01:20', 'https://qingkaka.oss-cn-hongkong.aliyuncs.com/herb_images/1751950865825.jpg', 0);
INSERT INTO `herb_growth` VALUES (49, 4, '2', 30.0, 25.0, '生长状态良好', 103.858594, 30.783797, 22, '2025-07-10 11:42:57', 'https://qingkaka.oss-cn-hongkong.aliyuncs.com/herb_images/1752118976806.jpg', 0);

-- ----------------------------
-- Table structure for herb_link_category
-- ----------------------------
DROP TABLE IF EXISTS `herb_link_category`;
CREATE TABLE `herb_link_category`  (
  `hlc_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `herb_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`hlc_id`) USING BTREE,
  INDEX `herb_id`(`herb_id` ASC) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `herb_link_category_ibfk_1` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`herb_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `herb_link_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `herb_category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of herb_link_category
-- ----------------------------
INSERT INTO `herb_link_category` VALUES (3, 2, 1);

-- ----------------------------
-- Table structure for herb_location
-- ----------------------------
DROP TABLE IF EXISTS `herb_location`;
CREATE TABLE `herb_location`  (
  `location_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `herb_id` bigint(20) NOT NULL,
  `location_count` int(11) NOT NULL,
  `district_id` bigint(20) NOT NULL,
  `street_id` bigint(20) NOT NULL,
  `location_longitude` decimal(9, 6) NOT NULL,
  `location_latitude` decimal(8, 6) NOT NULL,
  PRIMARY KEY (`location_id`) USING BTREE,
  INDEX `herb_id`(`herb_id` ASC) USING BTREE,
  INDEX `herb_location_ibfk_2`(`district_id` ASC) USING BTREE,
  INDEX `herb_location_ibfk_3`(`street_id` ASC) USING BTREE,
  CONSTRAINT `herb_location_ibfk_1` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`herb_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `herb_location_ibfk_2` FOREIGN KEY (`district_id`) REFERENCES `district` (`district_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `herb_location_ibfk_3` FOREIGN KEY (`street_id`) REFERENCES `street` (`street_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of herb_location
-- ----------------------------
INSERT INTO `herb_location` VALUES (1, 3, 20, 500103, 500103001, 23.032911, 32.432531);
INSERT INTO `herb_location` VALUES (2, 4, 80, 500103, 500103002, 106.570421, 29.553672);
INSERT INTO `herb_location` VALUES (3, 11, 120, 500103, 500103003, 106.575892, 29.558921);
INSERT INTO `herb_location` VALUES (4, 3, 90, 500103, 500103004, 106.563211, 29.551234);
INSERT INTO `herb_location` VALUES (5, 4, 110, 500103, 500103005, 106.556782, 29.560123);
INSERT INTO `herb_location` VALUES (6, 11, 75, 500105, 500105001, 106.531234, 29.572345);
INSERT INTO `herb_location` VALUES (7, 3, 130, 500105, 500105002, 106.538765, 29.578456);
INSERT INTO `herb_location` VALUES (8, 4, 95, 500105, 500105003, 106.523456, 29.583567);
INSERT INTO `herb_location` VALUES (9, 11, 60, 500105, 500105004, 106.534567, 29.587678);
INSERT INTO `herb_location` VALUES (10, 3, 85, 500105, 500105005, 106.545678, 29.592789);
INSERT INTO `herb_location` VALUES (11, 4, 110, 500108, 500108001, 106.567890, 29.523456);
INSERT INTO `herb_location` VALUES (12, 11, 70, 500108, 500108002, 106.578901, 29.534567);
INSERT INTO `herb_location` VALUES (13, 3, 100, 500108, 500108003, 106.589012, 29.545678);
INSERT INTO `herb_location` VALUES (14, 4, 65, 500108, 500108004, 106.597834, 29.556789);
INSERT INTO `herb_location` VALUES (15, 11, 90, 500108, 500108005, 106.603456, 29.567890);
INSERT INTO `herb_location` VALUES (16, 3, 140, 500106, 500106001, 106.456789, 29.543210);
INSERT INTO `herb_location` VALUES (17, 4, 55, 500106, 500106002, 106.467890, 29.554321);
INSERT INTO `herb_location` VALUES (18, 11, 115, 500106, 500106003, 106.478901, 29.565432);
INSERT INTO `herb_location` VALUES (19, 3, 85, 500106, 500106004, 106.489012, 29.576543);
INSERT INTO `herb_location` VALUES (20, 4, 120, 500106, 500106005, 106.498765, 29.587654);
INSERT INTO `herb_location` VALUES (21, 11, 75, 500107, 500107001, 106.512345, 29.498765);
INSERT INTO `herb_location` VALUES (22, 3, 105, 500107, 500107002, 106.523456, 29.509876);
INSERT INTO `herb_location` VALUES (23, 4, 60, 500107, 500107003, 106.534567, 29.520987);
INSERT INTO `herb_location` VALUES (24, 11, 95, 500107, 500107004, 106.545678, 29.532098);
INSERT INTO `herb_location` VALUES (25, 3, 80, 500107, 500107005, 106.556789, 29.543209);
INSERT INTO `herb_location` VALUES (26, 4, 110, 500104, 500104001, 106.482345, 29.487654);
INSERT INTO `herb_location` VALUES (27, 11, 65, 500104, 500104002, 106.493456, 29.498765);
INSERT INTO `herb_location` VALUES (28, 3, 95, 500104, 500104003, 106.504567, 29.509876);
INSERT INTO `herb_location` VALUES (29, 4, 70, 500104, 500104004, 106.515678, 29.520987);
INSERT INTO `herb_location` VALUES (30, 11, 85, 500104, 500104005, 106.526789, 29.532098);
INSERT INTO `herb_location` VALUES (31, 12, 150, 500101, 500101001, 108.408691, 30.807987);
INSERT INTO `herb_location` VALUES (32, 13, 80, 500101, 500101002, 108.417532, 30.812345);
INSERT INTO `herb_location` VALUES (33, 14, 200, 500101, 500101003, 108.425671, 30.805432);
INSERT INTO `herb_location` VALUES (34, 12, 60, 500101, 500101004, 108.402345, 30.798765);
INSERT INTO `herb_location` VALUES (35, 13, 30, 500102, 500102001, 107.390284, 29.703512);
INSERT INTO `herb_location` VALUES (36, 14, 45, 500102, 500102002, 107.402156, 29.712343);
INSERT INTO `herb_location` VALUES (37, 13, 120, 500103, 500103001, 106.562342, 29.556678);
INSERT INTO `herb_location` VALUES (38, 13, 90, 500103, 500103002, 106.572341, 29.563412);
INSERT INTO `herb_location` VALUES (39, 13, 180, 500103, 500103003, 106.568923, 29.558765);
INSERT INTO `herb_location` VALUES (40, 12, 25, 500104, 500104001, 106.482345, 29.487654);
INSERT INTO `herb_location` VALUES (41, 14, 70, 500105, 500105001, 106.534567, 29.578901);
INSERT INTO `herb_location` VALUES (42, 12, 40, 500105, 500105002, 106.543218, 29.583456);
INSERT INTO `herb_location` VALUES (43, 13, 55, 500105, 500105003, 106.538765, 29.576543);
INSERT INTO `herb_location` VALUES (44, 14, 160, 500106, 500106001, 106.457812, 29.541234);
INSERT INTO `herb_location` VALUES (45, 14, 95, 500106, 500106002, 106.463456, 29.537891);
INSERT INTO `herb_location` VALUES (46, 14, 110, 500106, 500106003, 106.468923, 29.543218);
INSERT INTO `herb_location` VALUES (47, 12, 35, 500107, 500107001, 106.512345, 29.502341);
INSERT INTO `herb_location` VALUES (48, 13, 20, 500107, 500107002, 106.523456, 29.513452);
INSERT INTO `herb_location` VALUES (49, 12, 180, 500108, 500108001, 106.563456, 29.523456);
INSERT INTO `herb_location` VALUES (50, 12, 75, 500108, 500108002, 106.572341, 29.531234);
INSERT INTO `herb_location` VALUES (51, 12, 130, 500108, 500108003, 106.568765, 29.528901);
INSERT INTO `herb_location` VALUES (52, 14, 15, 500109, 500109001, 106.402341, 29.825678);
INSERT INTO `herb_location` VALUES (53, 13, 10, 500109, 500109002, 106.412345, 29.834567);
INSERT INTO `herb_location` VALUES (54, 12, 85, 500112, 500112001, 106.634567, 29.718901);
INSERT INTO `herb_location` VALUES (55, 13, 65, 500112, 500112002, 106.642341, 29.723456);
INSERT INTO `herb_location` VALUES (56, 14, 50, 500112, 500112003, 106.638765, 29.715432);
INSERT INTO `herb_location` VALUES (57, 12, 95, 500112, 500112004, 106.647812, 29.728901);
INSERT INTO `herb_location` VALUES (58, 14, 30, 500113, 500113001, 106.523456, 29.402341);
INSERT INTO `herb_location` VALUES (59, 13, 200, 500115, 500115001, 107.082345, 29.834567);
INSERT INTO `herb_location` VALUES (60, 12, 10, 500116, 500116001, 106.253456, 29.293452);

-- ----------------------------
-- Table structure for street
-- ----------------------------
DROP TABLE IF EXISTS `street`;
CREATE TABLE `street`  (
  `street_id` bigint(20) NOT NULL,
  `district_id` bigint(20) NOT NULL,
  `street_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`street_id`) USING BTREE,
  INDEX `idx_street_district`(`district_id` ASC) USING BTREE,
  CONSTRAINT `street_ibfk_1` FOREIGN KEY (`district_id`) REFERENCES `district` (`district_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of street
-- ----------------------------
INSERT INTO `street` VALUES (500101001, 500101, '高笋塘街道');
INSERT INTO `street` VALUES (500101002, 500101, '太白街道');
INSERT INTO `street` VALUES (500101003, 500101, '牌楼街道');
INSERT INTO `street` VALUES (500101004, 500101, '双河口街道');
INSERT INTO `street` VALUES (500101005, 500101, '龙都街道');
INSERT INTO `street` VALUES (500101006, 500101, '周家坝街道');
INSERT INTO `street` VALUES (500101007, 500101, '沙河街道');
INSERT INTO `street` VALUES (500101008, 500101, '钟鼓楼街道');
INSERT INTO `street` VALUES (500101009, 500101, '百安坝街道');
INSERT INTO `street` VALUES (500101010, 500101, '五桥街道');
INSERT INTO `street` VALUES (500101011, 500101, '陈家坝街道');
INSERT INTO `street` VALUES (500101012, 500101, '天城街道');
INSERT INTO `street` VALUES (500101013, 500101, '熊家镇');
INSERT INTO `street` VALUES (500101014, 500101, '小周镇');
INSERT INTO `street` VALUES (500101015, 500101, '大周镇');
INSERT INTO `street` VALUES (500102001, 500102, '敦仁街道');
INSERT INTO `street` VALUES (500102002, 500102, '崇义街道');
INSERT INTO `street` VALUES (500102003, 500102, '荔枝街道');
INSERT INTO `street` VALUES (500102004, 500102, '江北街道');
INSERT INTO `street` VALUES (500102005, 500102, '江东街道');
INSERT INTO `street` VALUES (500102006, 500102, '李渡街道');
INSERT INTO `street` VALUES (500102007, 500102, '龙桥街道');
INSERT INTO `street` VALUES (500102008, 500102, '白涛街道');
INSERT INTO `street` VALUES (500102009, 500102, '马鞍街道');
INSERT INTO `street` VALUES (500102010, 500102, '蔺市镇');
INSERT INTO `street` VALUES (500102011, 500102, '新妙镇');
INSERT INTO `street` VALUES (500102012, 500102, '清溪镇');
INSERT INTO `street` VALUES (500102013, 500102, '珍溪镇');
INSERT INTO `street` VALUES (500102014, 500102, '南沱镇');
INSERT INTO `street` VALUES (500102015, 500102, '义和镇');
INSERT INTO `street` VALUES (500103001, 500103, '七星岗街道');
INSERT INTO `street` VALUES (500103002, 500103, '解放碑街道');
INSERT INTO `street` VALUES (500103003, 500103, '两路口街道');
INSERT INTO `street` VALUES (500103004, 500103, '上清寺街道');
INSERT INTO `street` VALUES (500103005, 500103, '菜园坝街道');
INSERT INTO `street` VALUES (500103006, 500103, '南纪门街道');
INSERT INTO `street` VALUES (500103007, 500103, '朝天门街道');
INSERT INTO `street` VALUES (500103008, 500103, '大溪沟街道');
INSERT INTO `street` VALUES (500103009, 500103, '大坪街道');
INSERT INTO `street` VALUES (500103010, 500103, '化龙桥街道');
INSERT INTO `street` VALUES (500103011, 500103, '石油路街道');
INSERT INTO `street` VALUES (500104001, 500104, '新山村街道');
INSERT INTO `street` VALUES (500104002, 500104, '跃进村街道');
INSERT INTO `street` VALUES (500104003, 500104, '九宫庙街道');
INSERT INTO `street` VALUES (500104004, 500104, '茄子溪街道');
INSERT INTO `street` VALUES (500104005, 500104, '春晖路街道');
INSERT INTO `street` VALUES (500104006, 500104, '八桥镇');
INSERT INTO `street` VALUES (500104007, 500104, '建胜镇');
INSERT INTO `street` VALUES (500104008, 500104, '跳磴镇');
INSERT INTO `street` VALUES (500105001, 500105, '观音桥街道');
INSERT INTO `street` VALUES (500105002, 500105, '华新街街道');
INSERT INTO `street` VALUES (500105003, 500105, '五里店街道');
INSERT INTO `street` VALUES (500105004, 500105, '石马河街道');
INSERT INTO `street` VALUES (500105005, 500105, '大石坝街道');
INSERT INTO `street` VALUES (500105006, 500105, '寸滩街道');
INSERT INTO `street` VALUES (500105007, 500105, '铁山坪街道');
INSERT INTO `street` VALUES (500105008, 500105, '郭家沱街道');
INSERT INTO `street` VALUES (500105009, 500105, '鱼嘴镇');
INSERT INTO `street` VALUES (500105010, 500105, '复盛镇');
INSERT INTO `street` VALUES (500105011, 500105, '五宝镇');
INSERT INTO `street` VALUES (500106001, 500106, '小龙坎街道');
INSERT INTO `street` VALUES (500106002, 500106, '沙坪坝街道');
INSERT INTO `street` VALUES (500106003, 500106, '磁器口街道');
INSERT INTO `street` VALUES (500106004, 500106, '天星桥街道');
INSERT INTO `street` VALUES (500106005, 500106, '土湾街道');
INSERT INTO `street` VALUES (500106006, 500106, '新桥街道');
INSERT INTO `street` VALUES (500106007, 500106, '覃家岗街道');
INSERT INTO `street` VALUES (500106008, 500106, '井口街道');
INSERT INTO `street` VALUES (500106009, 500106, '歌乐山街道');
INSERT INTO `street` VALUES (500106010, 500106, '山洞街道');
INSERT INTO `street` VALUES (500106011, 500106, '詹家溪街道');
INSERT INTO `street` VALUES (500106012, 500106, '石井坡街道');
INSERT INTO `street` VALUES (500106013, 500106, '童家桥街道');
INSERT INTO `street` VALUES (500106014, 500106, '渝碚路街道');
INSERT INTO `street` VALUES (500106015, 500106, '陈家桥街道');
INSERT INTO `street` VALUES (500106016, 500106, '虎溪街道');
INSERT INTO `street` VALUES (500106017, 500106, '西永街道');
INSERT INTO `street` VALUES (500106018, 500106, '联芳街道');
INSERT INTO `street` VALUES (500106019, 500106, '丰文街道');
INSERT INTO `street` VALUES (500106020, 500106, '香炉山街道');
INSERT INTO `street` VALUES (500107001, 500107, '杨家坪街道');
INSERT INTO `street` VALUES (500107002, 500107, '谢家湾街道');
INSERT INTO `street` VALUES (500107003, 500107, '石坪桥街道');
INSERT INTO `street` VALUES (500107004, 500107, '黄桷坪街道');
INSERT INTO `street` VALUES (500107005, 500107, '中梁山街道');
INSERT INTO `street` VALUES (500107006, 500107, '渝州路街道');
INSERT INTO `street` VALUES (500107007, 500107, '石桥铺街道');
INSERT INTO `street` VALUES (500107008, 500107, '二郎街道');
INSERT INTO `street` VALUES (500107009, 500107, '九龙镇');
INSERT INTO `street` VALUES (500107010, 500107, '华岩镇');
INSERT INTO `street` VALUES (500107011, 500107, '含谷镇');
INSERT INTO `street` VALUES (500107012, 500107, '金凤镇');
INSERT INTO `street` VALUES (500107013, 500107, '白市驿镇');
INSERT INTO `street` VALUES (500107014, 500107, '走马镇');
INSERT INTO `street` VALUES (500107015, 500107, '石板镇');
INSERT INTO `street` VALUES (500107016, 500107, '巴福镇');
INSERT INTO `street` VALUES (500107017, 500107, '陶家镇');
INSERT INTO `street` VALUES (500107018, 500107, '西彭镇');
INSERT INTO `street` VALUES (500107019, 500107, '铜罐驿镇');
INSERT INTO `street` VALUES (500108001, 500108, '南坪街道');
INSERT INTO `street` VALUES (500108002, 500108, '花园路街道');
INSERT INTO `street` VALUES (500108003, 500108, '海棠溪街道');
INSERT INTO `street` VALUES (500108004, 500108, '龙门浩街道');
INSERT INTO `street` VALUES (500108005, 500108, '弹子石街道');
INSERT INTO `street` VALUES (500108006, 500108, '铜元局街道');
INSERT INTO `street` VALUES (500108007, 500108, '南山街道');
INSERT INTO `street` VALUES (500108008, 500108, '天文街道');
INSERT INTO `street` VALUES (500108009, 500108, '涂山镇');
INSERT INTO `street` VALUES (500108010, 500108, '鸡冠石镇');
INSERT INTO `street` VALUES (500108011, 500108, '峡口镇');
INSERT INTO `street` VALUES (500108012, 500108, '长生桥镇');
INSERT INTO `street` VALUES (500108013, 500108, '迎龙镇');
INSERT INTO `street` VALUES (500108014, 500108, '广阳镇');
INSERT INTO `street` VALUES (500109001, 500109, '天生街道');
INSERT INTO `street` VALUES (500109002, 500109, '朝阳街道');
INSERT INTO `street` VALUES (500109003, 500109, '北温泉街道');
INSERT INTO `street` VALUES (500109004, 500109, '东阳街道');
INSERT INTO `street` VALUES (500109005, 500109, '龙凤桥街道');
INSERT INTO `street` VALUES (500109006, 500109, '歇马街道');
INSERT INTO `street` VALUES (500109007, 500109, '蔡家岗街道');
INSERT INTO `street` VALUES (500109008, 500109, '童家溪镇');
INSERT INTO `street` VALUES (500109009, 500109, '施家梁镇');
INSERT INTO `street` VALUES (500109010, 500109, '澄江镇');
INSERT INTO `street` VALUES (500109011, 500109, '天府镇');
INSERT INTO `street` VALUES (500109012, 500109, '静观镇');
INSERT INTO `street` VALUES (500109013, 500109, '柳荫镇');
INSERT INTO `street` VALUES (500109014, 500109, '复兴镇');
INSERT INTO `street` VALUES (500109015, 500109, '三圣镇');
INSERT INTO `street` VALUES (500109016, 500109, '金刀峡镇');
INSERT INTO `street` VALUES (500110001, 500110, '古南街道');
INSERT INTO `street` VALUES (500110002, 500110, '文龙街道');
INSERT INTO `street` VALUES (500110003, 500110, '三江街道');
INSERT INTO `street` VALUES (500110004, 500110, '万盛街道');
INSERT INTO `street` VALUES (500110005, 500110, '东林街道');
INSERT INTO `street` VALUES (500110006, 500110, '新盛街道');
INSERT INTO `street` VALUES (500110007, 500110, '通惠街道');
INSERT INTO `street` VALUES (500110008, 500110, '石角镇');
INSERT INTO `street` VALUES (500110009, 500110, '东溪镇');
INSERT INTO `street` VALUES (500110010, 500110, '赶水镇');
INSERT INTO `street` VALUES (500110011, 500110, '打通镇');
INSERT INTO `street` VALUES (500110012, 500110, '石壕镇');
INSERT INTO `street` VALUES (500110013, 500110, '永新镇');
INSERT INTO `street` VALUES (500110014, 500110, '三角镇');
INSERT INTO `street` VALUES (500110015, 500110, '隆盛镇');
INSERT INTO `street` VALUES (500110016, 500110, '郭扶镇');
INSERT INTO `street` VALUES (500110017, 500110, '篆塘镇');
INSERT INTO `street` VALUES (500110018, 500110, '丁山镇');
INSERT INTO `street` VALUES (500110019, 500110, '安稳镇');
INSERT INTO `street` VALUES (500110020, 500110, '扶欢镇');
INSERT INTO `street` VALUES (500110021, 500110, '永城镇');
INSERT INTO `street` VALUES (500110022, 500110, '中峰镇');
INSERT INTO `street` VALUES (500110023, 500110, '横山镇');
INSERT INTO `street` VALUES (500111001, 500111, '龙岗街道');
INSERT INTO `street` VALUES (500111002, 500111, '棠香街道');
INSERT INTO `street` VALUES (500111003, 500111, '龙滩子街道');
INSERT INTO `street` VALUES (500111004, 500111, '双路街道');
INSERT INTO `street` VALUES (500111005, 500111, '通桥街道');
INSERT INTO `street` VALUES (500111006, 500111, '智凤街道');
INSERT INTO `street` VALUES (500111007, 500111, '龙水镇');
INSERT INTO `street` VALUES (500111008, 500111, '邮亭镇');
INSERT INTO `street` VALUES (500111009, 500111, '宝顶镇');
INSERT INTO `street` VALUES (500111010, 500111, '万古镇');
INSERT INTO `street` VALUES (500111011, 500111, '珠溪镇');
INSERT INTO `street` VALUES (500111012, 500111, '中敖镇');
INSERT INTO `street` VALUES (500111013, 500111, '三驱镇');
INSERT INTO `street` VALUES (500111014, 500111, '石马镇');
INSERT INTO `street` VALUES (500111015, 500111, '拾万镇');
INSERT INTO `street` VALUES (500111016, 500111, '回龙镇');
INSERT INTO `street` VALUES (500111017, 500111, '金山镇');
INSERT INTO `street` VALUES (500111018, 500111, '铁山镇');
INSERT INTO `street` VALUES (500111019, 500111, '玉龙镇');
INSERT INTO `street` VALUES (500111020, 500111, '雍溪镇');
INSERT INTO `street` VALUES (500111021, 500111, '高升镇');
INSERT INTO `street` VALUES (500111022, 500111, '季家镇');
INSERT INTO `street` VALUES (500111023, 500111, '龙石镇');
INSERT INTO `street` VALUES (500111024, 500111, '高坪镇');
INSERT INTO `street` VALUES (500111025, 500111, '古龙镇');
INSERT INTO `street` VALUES (500112001, 500112, '双凤桥街道');
INSERT INTO `street` VALUES (500112002, 500112, '两路街道');
INSERT INTO `street` VALUES (500112003, 500112, '龙溪街道');
INSERT INTO `street` VALUES (500112004, 500112, '回兴街道');
INSERT INTO `street` VALUES (500112005, 500112, '双龙湖街道');
INSERT INTO `street` VALUES (500112006, 500112, '仙桃街道');
INSERT INTO `street` VALUES (500112007, 500112, '宝圣湖街道');
INSERT INTO `street` VALUES (500112008, 500112, '鸳鸯街道');
INSERT INTO `street` VALUES (500112009, 500112, '人和街道');
INSERT INTO `street` VALUES (500112010, 500112, '天宫殿街道');
INSERT INTO `street` VALUES (500112011, 500112, '翠云街道');
INSERT INTO `street` VALUES (500112012, 500112, '大竹林街道');
INSERT INTO `street` VALUES (500112013, 500112, '礼嘉街道');
INSERT INTO `street` VALUES (500112014, 500112, '悦来街道');
INSERT INTO `street` VALUES (500112015, 500112, '木耳镇');
INSERT INTO `street` VALUES (500112016, 500112, '兴隆镇');
INSERT INTO `street` VALUES (500112017, 500112, '茨竹镇');
INSERT INTO `street` VALUES (500112018, 500112, '大湾镇');
INSERT INTO `street` VALUES (500112019, 500112, '洛碛镇');
INSERT INTO `street` VALUES (500112020, 500112, '龙兴镇');
INSERT INTO `street` VALUES (500112021, 500112, '石船镇');
INSERT INTO `street` VALUES (500112022, 500112, '统景镇');
INSERT INTO `street` VALUES (500112023, 500112, '古路镇');
INSERT INTO `street` VALUES (500112024, 500112, '玉峰山镇');
INSERT INTO `street` VALUES (500113001, 500113, '鱼洞街道');
INSERT INTO `street` VALUES (500113002, 500113, '李家沱街道');
INSERT INTO `street` VALUES (500113003, 500113, '花溪街道');
INSERT INTO `street` VALUES (500113004, 500113, '龙洲湾街道');
INSERT INTO `street` VALUES (500113005, 500113, '南泉街道');
INSERT INTO `street` VALUES (500113006, 500113, '一品街道');
INSERT INTO `street` VALUES (500113007, 500113, '南彭街道');
INSERT INTO `street` VALUES (500113008, 500113, '惠民街道');
INSERT INTO `street` VALUES (500113009, 500113, '界石镇');
INSERT INTO `street` VALUES (500113010, 500113, '木洞镇');
INSERT INTO `street` VALUES (500113011, 500113, '双河口镇');
INSERT INTO `street` VALUES (500113012, 500113, '麻柳嘴镇');
INSERT INTO `street` VALUES (500113013, 500113, '丰盛镇');
INSERT INTO `street` VALUES (500113014, 500113, '二圣镇');
INSERT INTO `street` VALUES (500113015, 500113, '东泉镇');
INSERT INTO `street` VALUES (500113016, 500113, '姜家镇');
INSERT INTO `street` VALUES (500113017, 500113, '天星寺镇');
INSERT INTO `street` VALUES (500113018, 500113, '接龙镇');
INSERT INTO `street` VALUES (500113019, 500113, '石滩镇');
INSERT INTO `street` VALUES (500113020, 500113, '石龙镇');
INSERT INTO `street` VALUES (500113021, 500113, '圣灯山镇');
INSERT INTO `street` VALUES (500114001, 500114, '城东街道');
INSERT INTO `street` VALUES (500114002, 500114, '城南街道');
INSERT INTO `street` VALUES (500114003, 500114, '城西街道');
INSERT INTO `street` VALUES (500114004, 500114, '舟白街道');
INSERT INTO `street` VALUES (500114005, 500114, '正阳街道');
INSERT INTO `street` VALUES (500114006, 500114, '冯家街道');
INSERT INTO `street` VALUES (500114007, 500114, '阿蓬江镇');
INSERT INTO `street` VALUES (500114008, 500114, '石会镇');
INSERT INTO `street` VALUES (500114009, 500114, '黑溪镇');
INSERT INTO `street` VALUES (500114010, 500114, '黄溪镇');
INSERT INTO `street` VALUES (500114011, 500114, '黎水镇');
INSERT INTO `street` VALUES (500114012, 500114, '金溪镇');
INSERT INTO `street` VALUES (500114013, 500114, '马喇镇');
INSERT INTO `street` VALUES (500114014, 500114, '濯水镇');
INSERT INTO `street` VALUES (500114015, 500114, '石家镇');
INSERT INTO `street` VALUES (500114016, 500114, '鹅池镇');
INSERT INTO `street` VALUES (500114017, 500114, '小南海镇');
INSERT INTO `street` VALUES (500114018, 500114, '沙坝镇');
INSERT INTO `street` VALUES (500114019, 500114, '白石镇');
INSERT INTO `street` VALUES (500114020, 500114, '杉岭乡');
INSERT INTO `street` VALUES (500114021, 500114, '太极乡');
INSERT INTO `street` VALUES (500114022, 500114, '水田乡');
INSERT INTO `street` VALUES (500114023, 500114, '白土乡');
INSERT INTO `street` VALUES (500114024, 500114, '金洞乡');
INSERT INTO `street` VALUES (500114025, 500114, '五里乡');
INSERT INTO `street` VALUES (500114026, 500114, '水市乡');
INSERT INTO `street` VALUES (500114027, 500114, '蓬东乡');
INSERT INTO `street` VALUES (500115001, 500115, '凤城街道');
INSERT INTO `street` VALUES (500115002, 500115, '晏家街道');
INSERT INTO `street` VALUES (500115003, 500115, '江南街道');
INSERT INTO `street` VALUES (500115004, 500115, '渡舟街道');
INSERT INTO `street` VALUES (500115005, 500115, '新市街道');
INSERT INTO `street` VALUES (500115006, 500115, '八颗街道');
INSERT INTO `street` VALUES (500115007, 500115, '菩提街道');
INSERT INTO `street` VALUES (500115008, 500115, '邻封镇');
INSERT INTO `street` VALUES (500115009, 500115, '但渡镇');
INSERT INTO `street` VALUES (500115010, 500115, '云集镇');
INSERT INTO `street` VALUES (500115011, 500115, '长寿湖镇');
INSERT INTO `street` VALUES (500115012, 500115, '双龙镇');
INSERT INTO `street` VALUES (500115013, 500115, '龙河镇');
INSERT INTO `street` VALUES (500115014, 500115, '石堰镇');
INSERT INTO `street` VALUES (500115015, 500115, '云台镇');
INSERT INTO `street` VALUES (500115016, 500115, '海棠镇');
INSERT INTO `street` VALUES (500115017, 500115, '葛兰镇');
INSERT INTO `street` VALUES (500115018, 500115, '洪湖镇');
INSERT INTO `street` VALUES (500115019, 500115, '万顺镇');
INSERT INTO `street` VALUES (500115020, 500115, '称沱镇');
INSERT INTO `street` VALUES (500116001, 500116, '几江街道');
INSERT INTO `street` VALUES (500116002, 500116, '德感街道');
INSERT INTO `street` VALUES (500116003, 500116, '双福街道');
INSERT INTO `street` VALUES (500116004, 500116, '鼎山街道');
INSERT INTO `street` VALUES (500116005, 500116, '圣泉街道');
INSERT INTO `street` VALUES (500116101, 500116, '油溪镇');
INSERT INTO `street` VALUES (500116102, 500116, '吴滩镇');
INSERT INTO `street` VALUES (500116103, 500116, '石门镇');
INSERT INTO `street` VALUES (500116104, 500116, '朱杨镇');
INSERT INTO `street` VALUES (500116105, 500116, '永兴镇');
INSERT INTO `street` VALUES (500116106, 500116, '塘河镇');
INSERT INTO `street` VALUES (500116107, 500116, '白沙镇');
INSERT INTO `street` VALUES (500116108, 500116, '龙华镇');
INSERT INTO `street` VALUES (500116109, 500116, '李市镇');
INSERT INTO `street` VALUES (500116110, 500116, '慈云镇');
INSERT INTO `street` VALUES (500116111, 500116, '蔡家镇');
INSERT INTO `street` VALUES (500116112, 500116, '中山镇');
INSERT INTO `street` VALUES (500116113, 500116, '嘉平镇');
INSERT INTO `street` VALUES (500116114, 500116, '柏林镇');
INSERT INTO `street` VALUES (500116115, 500116, '先锋镇');
INSERT INTO `street` VALUES (500116116, 500116, '珞璜镇');
INSERT INTO `street` VALUES (500116117, 500116, '贾嗣镇');
INSERT INTO `street` VALUES (500116118, 500116, '夏坝镇');
INSERT INTO `street` VALUES (500116119, 500116, '西湖镇');
INSERT INTO `street` VALUES (500116120, 500116, '杜市镇');
INSERT INTO `street` VALUES (500116121, 500116, '广兴镇');
INSERT INTO `street` VALUES (500116122, 500116, '四面山镇');
INSERT INTO `street` VALUES (500116123, 500116, '支坪镇');
INSERT INTO `street` VALUES (500116124, 500116, '四屏镇');
INSERT INTO `street` VALUES (500117001, 500117, '合阳城街道');
INSERT INTO `street` VALUES (500117002, 500117, '钓鱼城街道');
INSERT INTO `street` VALUES (500117003, 500117, '南津街街道');
INSERT INTO `street` VALUES (500117004, 500117, '盐井街道');
INSERT INTO `street` VALUES (500117005, 500117, '草街街道');
INSERT INTO `street` VALUES (500117006, 500117, '云门街道');
INSERT INTO `street` VALUES (500117007, 500117, '大石街道');
INSERT INTO `street` VALUES (500117100, 500117, '沙鱼镇');
INSERT INTO `street` VALUES (500117101, 500117, '官渡镇');
INSERT INTO `street` VALUES (500117102, 500117, '涞滩镇');
INSERT INTO `street` VALUES (500117103, 500117, '肖家镇');
INSERT INTO `street` VALUES (500117104, 500117, '古楼镇');
INSERT INTO `street` VALUES (500117105, 500117, '三庙镇');
INSERT INTO `street` VALUES (500117106, 500117, '二郎镇');
INSERT INTO `street` VALUES (500117107, 500117, '龙凤镇');
INSERT INTO `street` VALUES (500117108, 500117, '隆兴镇');
INSERT INTO `street` VALUES (500117109, 500117, '铜溪镇');
INSERT INTO `street` VALUES (500117112, 500117, '双凤镇');
INSERT INTO `street` VALUES (500117113, 500117, '狮滩镇');
INSERT INTO `street` VALUES (500117114, 500117, '清平镇');
INSERT INTO `street` VALUES (500117115, 500117, '土场镇');
INSERT INTO `street` VALUES (500117116, 500117, '小沔镇');
INSERT INTO `street` VALUES (500117117, 500117, '三汇镇');
INSERT INTO `street` VALUES (500117118, 500117, '香龙镇');
INSERT INTO `street` VALUES (500117120, 500117, '钱塘镇');
INSERT INTO `street` VALUES (500117121, 500117, '龙市镇');
INSERT INTO `street` VALUES (500117123, 500117, '燕窝镇');
INSERT INTO `street` VALUES (500117124, 500117, '太和镇');
INSERT INTO `street` VALUES (500117125, 500117, '渭沱镇');
INSERT INTO `street` VALUES (500117126, 500117, '双槐镇');
INSERT INTO `street` VALUES (500118001, 500118, '中山路街道');
INSERT INTO `street` VALUES (500118002, 500118, '胜利路街道');
INSERT INTO `street` VALUES (500118003, 500118, '南大街街道');
INSERT INTO `street` VALUES (500118004, 500118, '茶山竹海街道');
INSERT INTO `street` VALUES (500118005, 500118, '大安街道');
INSERT INTO `street` VALUES (500118006, 500118, '陈食街道');
INSERT INTO `street` VALUES (500118007, 500118, '卫星湖街道');
INSERT INTO `street` VALUES (500118100, 500118, '青峰镇');
INSERT INTO `street` VALUES (500118102, 500118, '金龙镇');
INSERT INTO `street` VALUES (500118103, 500118, '临江镇');
INSERT INTO `street` VALUES (500118104, 500118, '何埂镇');
INSERT INTO `street` VALUES (500118105, 500118, '松溉镇');
INSERT INTO `street` VALUES (500118106, 500118, '仙龙镇');
INSERT INTO `street` VALUES (500118107, 500118, '吉安镇');
INSERT INTO `street` VALUES (500118108, 500118, '五间镇');
INSERT INTO `street` VALUES (500118109, 500118, '来苏镇');
INSERT INTO `street` VALUES (500118110, 500118, '宝峰镇');
INSERT INTO `street` VALUES (500118111, 500118, '双石镇');
INSERT INTO `street` VALUES (500118112, 500118, '红炉镇');
INSERT INTO `street` VALUES (500118113, 500118, '永荣镇');
INSERT INTO `street` VALUES (500118114, 500118, '三教镇');
INSERT INTO `street` VALUES (500118115, 500118, '板桥镇');
INSERT INTO `street` VALUES (500118116, 500118, '朱沱镇');
INSERT INTO `street` VALUES (500119001, 500119, '东城街道');
INSERT INTO `street` VALUES (500119002, 500119, '南城街道');
INSERT INTO `street` VALUES (500119003, 500119, '西城街道');
INSERT INTO `street` VALUES (500119004, 500119, '三泉镇');
INSERT INTO `street` VALUES (500119005, 500119, '南平镇');
INSERT INTO `street` VALUES (500119006, 500119, '神童镇');
INSERT INTO `street` VALUES (500119007, 500119, '鸣玉镇');
INSERT INTO `street` VALUES (500119008, 500119, '大观镇');
INSERT INTO `street` VALUES (500119009, 500119, '兴隆镇');
INSERT INTO `street` VALUES (500119010, 500119, '太平场镇');
INSERT INTO `street` VALUES (500119011, 500119, '白沙镇');
INSERT INTO `street` VALUES (500119012, 500119, '水江镇');
INSERT INTO `street` VALUES (500119013, 500119, '石墙镇');
INSERT INTO `street` VALUES (500119014, 500119, '金山镇');
INSERT INTO `street` VALUES (500119015, 500119, '头渡镇');
INSERT INTO `street` VALUES (500119016, 500119, '大有镇');
INSERT INTO `street` VALUES (500119017, 500119, '合溪镇');
INSERT INTO `street` VALUES (500119018, 500119, '黎香湖镇');
INSERT INTO `street` VALUES (500119019, 500119, '山王坪镇');
INSERT INTO `street` VALUES (500119020, 500119, '石溪镇');
INSERT INTO `street` VALUES (500119021, 500119, '德隆镇');
INSERT INTO `street` VALUES (500119022, 500119, '民主镇');
INSERT INTO `street` VALUES (500119023, 500119, '福寿镇');
INSERT INTO `street` VALUES (500119024, 500119, '河图镇');
INSERT INTO `street` VALUES (500119025, 500119, '庆元镇');
INSERT INTO `street` VALUES (500119026, 500119, '古花镇');
INSERT INTO `street` VALUES (500119027, 500119, '石莲镇');
INSERT INTO `street` VALUES (500119028, 500119, '乾丰镇');
INSERT INTO `street` VALUES (500119029, 500119, '骑龙镇');
INSERT INTO `street` VALUES (500119030, 500119, '冷水关镇');
INSERT INTO `street` VALUES (500119031, 500119, '楠竹山镇');
INSERT INTO `street` VALUES (500120001, 500120, '璧城街道');
INSERT INTO `street` VALUES (500120002, 500120, '璧泉街道');
INSERT INTO `street` VALUES (500120003, 500120, '青杠街道');
INSERT INTO `street` VALUES (500120004, 500120, '来凤街道');
INSERT INTO `street` VALUES (500120005, 500120, '丁家街道');
INSERT INTO `street` VALUES (500120006, 500120, '大路街道');
INSERT INTO `street` VALUES (500120007, 500120, '八塘镇');
INSERT INTO `street` VALUES (500120008, 500120, '七塘镇');
INSERT INTO `street` VALUES (500120009, 500120, '河边镇');
INSERT INTO `street` VALUES (500120010, 500120, '福禄镇');
INSERT INTO `street` VALUES (500120011, 500120, '大兴镇');
INSERT INTO `street` VALUES (500120012, 500120, '正兴镇');
INSERT INTO `street` VALUES (500120013, 500120, '广普镇');
INSERT INTO `street` VALUES (500120014, 500120, '三合镇');
INSERT INTO `street` VALUES (500120015, 500120, '健龙镇');
INSERT INTO `street` VALUES (500151001, 500151, '巴川街道');
INSERT INTO `street` VALUES (500151002, 500151, '东城街道');
INSERT INTO `street` VALUES (500151003, 500151, '南城街道');
INSERT INTO `street` VALUES (500151004, 500151, '蒲吕街道');
INSERT INTO `street` VALUES (500151005, 500151, '旧县街道');
INSERT INTO `street` VALUES (500151006, 500151, '土桥镇');
INSERT INTO `street` VALUES (500151007, 500151, '二坪镇');
INSERT INTO `street` VALUES (500151008, 500151, '水口镇');
INSERT INTO `street` VALUES (500151009, 500151, '安居镇');
INSERT INTO `street` VALUES (500151010, 500151, '白羊镇');
INSERT INTO `street` VALUES (500151011, 500151, '平滩镇');
INSERT INTO `street` VALUES (500151012, 500151, '小林镇');
INSERT INTO `street` VALUES (500151013, 500151, '双山镇');
INSERT INTO `street` VALUES (500151014, 500151, '虎峰镇');
INSERT INTO `street` VALUES (500151015, 500151, '福果镇');
INSERT INTO `street` VALUES (500151016, 500151, '庆隆镇');
INSERT INTO `street` VALUES (500151017, 500151, '石鱼镇');
INSERT INTO `street` VALUES (500151018, 500151, '少云镇');
INSERT INTO `street` VALUES (500151019, 500151, '维新镇');
INSERT INTO `street` VALUES (500151020, 500151, '高楼镇');
INSERT INTO `street` VALUES (500151021, 500151, '大庙镇');
INSERT INTO `street` VALUES (500151022, 500151, '围龙镇');
INSERT INTO `street` VALUES (500151023, 500151, '华兴镇');
INSERT INTO `street` VALUES (500151024, 500151, '永嘉镇');
INSERT INTO `street` VALUES (500151025, 500151, '安溪镇');
INSERT INTO `street` VALUES (500151026, 500151, '西河镇');
INSERT INTO `street` VALUES (500151027, 500151, '侣俸镇');
INSERT INTO `street` VALUES (500151028, 500151, '太平镇');
INSERT INTO `street` VALUES (500152001, 500152, '桂林街道');
INSERT INTO `street` VALUES (500152002, 500152, '梓潼街道');
INSERT INTO `street` VALUES (500152003, 500152, '大佛街道');
INSERT INTO `street` VALUES (500152004, 500152, '上和镇');
INSERT INTO `street` VALUES (500152005, 500152, '龙形镇');
INSERT INTO `street` VALUES (500152006, 500152, '古溪镇');
INSERT INTO `street` VALUES (500152007, 500152, '宝龙镇');
INSERT INTO `street` VALUES (500152008, 500152, '玉溪镇');
INSERT INTO `street` VALUES (500152009, 500152, '米心镇');
INSERT INTO `street` VALUES (500152010, 500152, '群力镇');
INSERT INTO `street` VALUES (500152011, 500152, '双江镇');
INSERT INTO `street` VALUES (500152012, 500152, '花岩镇');
INSERT INTO `street` VALUES (500152013, 500152, '柏梓镇');
INSERT INTO `street` VALUES (500152014, 500152, '崇龛镇');
INSERT INTO `street` VALUES (500152015, 500152, '塘坝镇');
INSERT INTO `street` VALUES (500152016, 500152, '新胜镇');
INSERT INTO `street` VALUES (500152017, 500152, '太安镇');
INSERT INTO `street` VALUES (500152018, 500152, '小渡镇');
INSERT INTO `street` VALUES (500152019, 500152, '卧佛镇');
INSERT INTO `street` VALUES (500152020, 500152, '五桂镇');
INSERT INTO `street` VALUES (500152021, 500152, '田家镇');
INSERT INTO `street` VALUES (500152022, 500152, '别口镇');
INSERT INTO `street` VALUES (500152023, 500152, '寿桥镇');
INSERT INTO `street` VALUES (500153001, 500153, '昌元街道');
INSERT INTO `street` VALUES (500153002, 500153, '昌州街道');
INSERT INTO `street` VALUES (500153003, 500153, '广顺街道');
INSERT INTO `street` VALUES (500153004, 500153, '双河街道');
INSERT INTO `street` VALUES (500153005, 500153, '安富街道');
INSERT INTO `street` VALUES (500153006, 500153, '峰高街道');
INSERT INTO `street` VALUES (500153007, 500153, '荣隆镇');
INSERT INTO `street` VALUES (500153008, 500153, '仁义镇');
INSERT INTO `street` VALUES (500153009, 500153, '盘龙镇');
INSERT INTO `street` VALUES (500153010, 500153, '吴家镇');
INSERT INTO `street` VALUES (500153011, 500153, '直升镇');
INSERT INTO `street` VALUES (500153012, 500153, '万灵镇');
INSERT INTO `street` VALUES (500153013, 500153, '清升镇');
INSERT INTO `street` VALUES (500153014, 500153, '清江镇');
INSERT INTO `street` VALUES (500153015, 500153, '古昌镇');
INSERT INTO `street` VALUES (500153016, 500153, '河包镇');
INSERT INTO `street` VALUES (500153017, 500153, '观胜镇');
INSERT INTO `street` VALUES (500153018, 500153, '铜鼓镇');
INSERT INTO `street` VALUES (500153019, 500153, '清流镇');
INSERT INTO `street` VALUES (500153020, 500153, '远觉镇');
INSERT INTO `street` VALUES (500153021, 500153, '龙集镇');
INSERT INTO `street` VALUES (500154001, 500154, '汉丰街道');
INSERT INTO `street` VALUES (500154002, 500154, '文峰街道');
INSERT INTO `street` VALUES (500154003, 500154, '云枫街道');
INSERT INTO `street` VALUES (500154004, 500154, '镇东街道');
INSERT INTO `street` VALUES (500154005, 500154, '丰乐街道');
INSERT INTO `street` VALUES (500154006, 500154, '白鹤街道');
INSERT INTO `street` VALUES (500154007, 500154, '赵家街道');
INSERT INTO `street` VALUES (500154008, 500154, '大德镇');
INSERT INTO `street` VALUES (500154009, 500154, '镇安镇');
INSERT INTO `street` VALUES (500154010, 500154, '厚坝镇');
INSERT INTO `street` VALUES (500154011, 500154, '金峰镇');
INSERT INTO `street` VALUES (500154012, 500154, '温泉镇');
INSERT INTO `street` VALUES (500154013, 500154, '郭家镇');
INSERT INTO `street` VALUES (500154014, 500154, '白桥镇');
INSERT INTO `street` VALUES (500154015, 500154, '和谦镇');
INSERT INTO `street` VALUES (500154016, 500154, '河堰镇');
INSERT INTO `street` VALUES (500154017, 500154, '大进镇');
INSERT INTO `street` VALUES (500154018, 500154, '谭家镇');
INSERT INTO `street` VALUES (500154019, 500154, '高桥镇');
INSERT INTO `street` VALUES (500154020, 500154, '九龙山镇');
INSERT INTO `street` VALUES (500154021, 500154, '天和镇');
INSERT INTO `street` VALUES (500154022, 500154, '中和镇');
INSERT INTO `street` VALUES (500154023, 500154, '义和镇');
INSERT INTO `street` VALUES (500154024, 500154, '临江镇');
INSERT INTO `street` VALUES (500154025, 500154, '竹溪镇');
INSERT INTO `street` VALUES (500154026, 500154, '铁桥镇');
INSERT INTO `street` VALUES (500154027, 500154, '南雅镇');
INSERT INTO `street` VALUES (500154028, 500154, '巫山镇');
INSERT INTO `street` VALUES (500154029, 500154, '岳溪镇');
INSERT INTO `street` VALUES (500154030, 500154, '长沙镇');
INSERT INTO `street` VALUES (500154031, 500154, '南门镇');
INSERT INTO `street` VALUES (500154032, 500154, '渠口镇');
INSERT INTO `street` VALUES (500154033, 500154, '满月镇');
INSERT INTO `street` VALUES (500154034, 500154, '雪宝山镇');
INSERT INTO `street` VALUES (500154035, 500154, '关面乡');
INSERT INTO `street` VALUES (500154036, 500154, '白泉乡');
INSERT INTO `street` VALUES (500154037, 500154, '麻柳乡');
INSERT INTO `street` VALUES (500155001, 500155, '梁山街道');
INSERT INTO `street` VALUES (500155002, 500155, '双桂街道');
INSERT INTO `street` VALUES (500155003, 500155, '仁贤街道');
INSERT INTO `street` VALUES (500155004, 500155, '金带街道');
INSERT INTO `street` VALUES (500155005, 500155, '合兴街道');
INSERT INTO `street` VALUES (500155006, 500155, '礼让镇');
INSERT INTO `street` VALUES (500155007, 500155, '云龙镇');
INSERT INTO `street` VALUES (500155008, 500155, '屏锦镇');
INSERT INTO `street` VALUES (500155009, 500155, '袁驿镇');
INSERT INTO `street` VALUES (500155010, 500155, '新盛镇');
INSERT INTO `street` VALUES (500155011, 500155, '福禄镇');
INSERT INTO `street` VALUES (500155012, 500155, '聚奎镇');
INSERT INTO `street` VALUES (500155013, 500155, '明达镇');
INSERT INTO `street` VALUES (500155014, 500155, '荫平镇');
INSERT INTO `street` VALUES (500155015, 500155, '和林镇');
INSERT INTO `street` VALUES (500155016, 500155, '回龙镇');
INSERT INTO `street` VALUES (500155017, 500155, '碧山镇');
INSERT INTO `street` VALUES (500155018, 500155, '虎城镇');
INSERT INTO `street` VALUES (500155019, 500155, '七星镇');
INSERT INTO `street` VALUES (500155020, 500155, '龙门镇');
INSERT INTO `street` VALUES (500155021, 500155, '文化镇');
INSERT INTO `street` VALUES (500155022, 500155, '石安镇');
INSERT INTO `street` VALUES (500155023, 500155, '柏家镇');
INSERT INTO `street` VALUES (500155024, 500155, '大观镇');
INSERT INTO `street` VALUES (500155025, 500155, '竹山镇');
INSERT INTO `street` VALUES (500155026, 500155, '蟠龙镇');
INSERT INTO `street` VALUES (500155027, 500155, '安胜镇');
INSERT INTO `street` VALUES (500155028, 500155, '复平镇');
INSERT INTO `street` VALUES (500155029, 500155, '星桥镇');
INSERT INTO `street` VALUES (500155030, 500155, '曲水镇');
INSERT INTO `street` VALUES (500155031, 500155, '铁门乡');
INSERT INTO `street` VALUES (500155032, 500155, '龙胜乡');
INSERT INTO `street` VALUES (500155033, 500155, '紫照乡');
INSERT INTO `street` VALUES (500156001, 500156, '凤山街道');
INSERT INTO `street` VALUES (500156002, 500156, '芙蓉街道');
INSERT INTO `street` VALUES (500156003, 500156, '仙女山街道');
INSERT INTO `street` VALUES (500156004, 500156, '羊角街道');
INSERT INTO `street` VALUES (500156005, 500156, '白马镇');
INSERT INTO `street` VALUES (500156006, 500156, '江口镇');
INSERT INTO `street` VALUES (500156007, 500156, '火炉镇');
INSERT INTO `street` VALUES (500156008, 500156, '鸭江镇');
INSERT INTO `street` VALUES (500156009, 500156, '长坝镇');
INSERT INTO `street` VALUES (500156010, 500156, '平桥镇');
INSERT INTO `street` VALUES (500156011, 500156, '桐梓镇');
INSERT INTO `street` VALUES (500156012, 500156, '和顺镇');
INSERT INTO `street` VALUES (500156013, 500156, '双河镇');
INSERT INTO `street` VALUES (500156014, 500156, '土坎镇');
INSERT INTO `street` VALUES (500156015, 500156, '黄莺乡');
INSERT INTO `street` VALUES (500156016, 500156, '沧沟乡');
INSERT INTO `street` VALUES (500156017, 500156, '文复乡');
INSERT INTO `street` VALUES (500156018, 500156, '后坪乡');
INSERT INTO `street` VALUES (500156019, 500156, '浩口乡');
INSERT INTO `street` VALUES (500156020, 500156, '接龙乡');
INSERT INTO `street` VALUES (500156021, 500156, '庙垭乡');
INSERT INTO `street` VALUES (500156022, 500156, '石桥乡');
INSERT INTO `street` VALUES (500156023, 500156, '白云乡');
INSERT INTO `street` VALUES (500156024, 500156, '大洞河乡');
INSERT INTO `street` VALUES (500156025, 500156, '赵家乡');
INSERT INTO `street` VALUES (500229001, 500229, '葛城街道');
INSERT INTO `street` VALUES (500229002, 500229, '复兴街道');
INSERT INTO `street` VALUES (500229003, 500229, '巴山镇');
INSERT INTO `street` VALUES (500229004, 500229, '坪坝镇');
INSERT INTO `street` VALUES (500229005, 500229, '庙坝镇');
INSERT INTO `street` VALUES (500229006, 500229, '明通镇');
INSERT INTO `street` VALUES (500229007, 500229, '修齐镇');
INSERT INTO `street` VALUES (500229008, 500229, '高观镇');
INSERT INTO `street` VALUES (500229009, 500229, '高燕镇');
INSERT INTO `street` VALUES (500229010, 500229, '东安镇');
INSERT INTO `street` VALUES (500229011, 500229, '咸宜镇');
INSERT INTO `street` VALUES (500229012, 500229, '高楠镇');
INSERT INTO `street` VALUES (500229013, 500229, '龙田乡');
INSERT INTO `street` VALUES (500229014, 500229, '北屏乡');
INSERT INTO `street` VALUES (500229015, 500229, '左岚乡');
INSERT INTO `street` VALUES (500229016, 500229, '沿河乡');
INSERT INTO `street` VALUES (500229017, 500229, '双河乡');
INSERT INTO `street` VALUES (500229018, 500229, '蓼子乡');
INSERT INTO `street` VALUES (500229019, 500229, '鸡鸣乡');
INSERT INTO `street` VALUES (500229020, 500229, '周溪乡');
INSERT INTO `street` VALUES (500229021, 500229, '明中乡');
INSERT INTO `street` VALUES (500229022, 500229, '治平乡');
INSERT INTO `street` VALUES (500229023, 500229, '岚天乡');
INSERT INTO `street` VALUES (500229024, 500229, '厚坪乡');
INSERT INTO `street` VALUES (500229025, 500229, '河鱼乡');
INSERT INTO `street` VALUES (500230001, 500230, '三合街道');
INSERT INTO `street` VALUES (500230002, 500230, '名山街道');
INSERT INTO `street` VALUES (500230003, 500230, '虎威镇');
INSERT INTO `street` VALUES (500230004, 500230, '社坛镇');
INSERT INTO `street` VALUES (500230005, 500230, '三元镇');
INSERT INTO `street` VALUES (500230006, 500230, '许明寺镇');
INSERT INTO `street` VALUES (500230007, 500230, '董家镇');
INSERT INTO `street` VALUES (500230008, 500230, '树人镇');
INSERT INTO `street` VALUES (500230009, 500230, '十直镇');
INSERT INTO `street` VALUES (500230010, 500230, '高家镇');
INSERT INTO `street` VALUES (500230011, 500230, '兴义镇');
INSERT INTO `street` VALUES (500230012, 500230, '双路镇');
INSERT INTO `street` VALUES (500230013, 500230, '江池镇');
INSERT INTO `street` VALUES (500230014, 500230, '龙河镇');
INSERT INTO `street` VALUES (500230015, 500230, '武平镇');
INSERT INTO `street` VALUES (500230016, 500230, '包鸾镇');
INSERT INTO `street` VALUES (500230017, 500230, '湛普镇');
INSERT INTO `street` VALUES (500230018, 500230, '南天湖镇');
INSERT INTO `street` VALUES (500230019, 500230, '保合镇');
INSERT INTO `street` VALUES (500230020, 500230, '兴龙镇');
INSERT INTO `street` VALUES (500230021, 500230, '仁沙镇');
INSERT INTO `street` VALUES (500230022, 500230, '龙孔镇');
INSERT INTO `street` VALUES (500230023, 500230, '暨龙镇');
INSERT INTO `street` VALUES (500230024, 500230, '双龙镇');
INSERT INTO `street` VALUES (500230025, 500230, '仙女湖镇');
INSERT INTO `street` VALUES (500230026, 500230, '青龙乡');
INSERT INTO `street` VALUES (500230027, 500230, '太平坝乡');
INSERT INTO `street` VALUES (500230028, 500230, '都督乡');
INSERT INTO `street` VALUES (500230029, 500230, '栗子乡');
INSERT INTO `street` VALUES (500230030, 500230, '三建乡');
INSERT INTO `street` VALUES (500231001, 500231, '桂溪街道');
INSERT INTO `street` VALUES (500231002, 500231, '桂阳街道');
INSERT INTO `street` VALUES (500231003, 500231, '新民镇');
INSERT INTO `street` VALUES (500231004, 500231, '沙坪镇');
INSERT INTO `street` VALUES (500231005, 500231, '周嘉镇');
INSERT INTO `street` VALUES (500231006, 500231, '普顺镇');
INSERT INTO `street` VALUES (500231007, 500231, '永安镇');
INSERT INTO `street` VALUES (500231008, 500231, '高安镇');
INSERT INTO `street` VALUES (500231009, 500231, '高峰镇');
INSERT INTO `street` VALUES (500231010, 500231, '五洞镇');
INSERT INTO `street` VALUES (500231011, 500231, '澄溪镇');
INSERT INTO `street` VALUES (500231012, 500231, '太平镇');
INSERT INTO `street` VALUES (500231013, 500231, '鹤游镇');
INSERT INTO `street` VALUES (500231014, 500231, '坪山镇');
INSERT INTO `street` VALUES (500231015, 500231, '砚台镇');
INSERT INTO `street` VALUES (500231016, 500231, '曹回镇');
INSERT INTO `street` VALUES (500231017, 500231, '杠家镇');
INSERT INTO `street` VALUES (500231018, 500231, '包家镇');
INSERT INTO `street` VALUES (500231019, 500231, '白家镇');
INSERT INTO `street` VALUES (500231020, 500231, '永平镇');
INSERT INTO `street` VALUES (500231021, 500231, '三溪镇');
INSERT INTO `street` VALUES (500231022, 500231, '裴兴镇');
INSERT INTO `street` VALUES (500231023, 500231, '黄沙镇');
INSERT INTO `street` VALUES (500231024, 500231, '长龙镇');
INSERT INTO `street` VALUES (500231025, 500231, '沙河乡');
INSERT INTO `street` VALUES (500231026, 500231, '大石乡');
INSERT INTO `street` VALUES (500233001, 500233, '忠州街道');
INSERT INTO `street` VALUES (500233002, 500233, '白公街道');
INSERT INTO `street` VALUES (500233003, 500233, '乌杨街道');
INSERT INTO `street` VALUES (500233004, 500233, '新生街道');
INSERT INTO `street` VALUES (500233005, 500233, '任家镇');
INSERT INTO `street` VALUES (500233006, 500233, '洋渡镇');
INSERT INTO `street` VALUES (500233007, 500233, '东溪镇');
INSERT INTO `street` VALUES (500233008, 500233, '复兴镇');
INSERT INTO `street` VALUES (500233009, 500233, '石宝镇');
INSERT INTO `street` VALUES (500233010, 500233, '汝溪镇');
INSERT INTO `street` VALUES (500233011, 500233, '野鹤镇');
INSERT INTO `street` VALUES (500233012, 500233, '官坝镇');
INSERT INTO `street` VALUES (500233013, 500233, '石黄镇');
INSERT INTO `street` VALUES (500233014, 500233, '马灌镇');
INSERT INTO `street` VALUES (500233015, 500233, '金鸡镇');
INSERT INTO `street` VALUES (500233016, 500233, '新立镇');
INSERT INTO `street` VALUES (500233017, 500233, '双桂镇');
INSERT INTO `street` VALUES (500233018, 500233, '拔山镇');
INSERT INTO `street` VALUES (500233019, 500233, '花桥镇');
INSERT INTO `street` VALUES (500233020, 500233, '永丰镇');
INSERT INTO `street` VALUES (500233021, 500233, '三汇镇');
INSERT INTO `street` VALUES (500233022, 500233, '白石镇');
INSERT INTO `street` VALUES (500233023, 500233, '黄金镇');
INSERT INTO `street` VALUES (500233024, 500233, '善广乡');
INSERT INTO `street` VALUES (500233025, 500233, '石子乡');
INSERT INTO `street` VALUES (500233026, 500233, '磨子土家族乡');
INSERT INTO `street` VALUES (500233027, 500233, '涂井乡');
INSERT INTO `street` VALUES (500233028, 500233, '金声乡');
INSERT INTO `street` VALUES (500233029, 500233, '兴峰乡');
INSERT INTO `street` VALUES (500235001, 500235, '双江街道');
INSERT INTO `street` VALUES (500235002, 500235, '青龙街道');
INSERT INTO `street` VALUES (500235003, 500235, '人和街道');
INSERT INTO `street` VALUES (500235004, 500235, '盘龙街道');
INSERT INTO `street` VALUES (500235101, 500235, '龙角镇');
INSERT INTO `street` VALUES (500235102, 500235, '故陵镇');
INSERT INTO `street` VALUES (500235103, 500235, '红狮镇');
INSERT INTO `street` VALUES (500235104, 500235, '路阳镇');
INSERT INTO `street` VALUES (500235105, 500235, '农坝镇');
INSERT INTO `street` VALUES (500235106, 500235, '渠马镇');
INSERT INTO `street` VALUES (500235107, 500235, '黄石镇');
INSERT INTO `street` VALUES (500235108, 500235, '巴阳镇');
INSERT INTO `street` VALUES (500235109, 500235, '沙市镇');
INSERT INTO `street` VALUES (500235110, 500235, '鱼泉镇');
INSERT INTO `street` VALUES (500235111, 500235, '凤鸣镇');
INSERT INTO `street` VALUES (500235112, 500235, '宝坪镇');
INSERT INTO `street` VALUES (500235113, 500235, '南溪镇');
INSERT INTO `street` VALUES (500235114, 500235, '双土镇');
INSERT INTO `street` VALUES (500235115, 500235, '桑坪镇');
INSERT INTO `street` VALUES (500235116, 500235, '江口镇');
INSERT INTO `street` VALUES (500235117, 500235, '高阳镇');
INSERT INTO `street` VALUES (500235118, 500235, '平安镇');
INSERT INTO `street` VALUES (500235119, 500235, '云阳镇');
INSERT INTO `street` VALUES (500235120, 500235, '云安镇');
INSERT INTO `street` VALUES (500235121, 500235, '栖霞镇');
INSERT INTO `street` VALUES (500235122, 500235, '双龙镇');
INSERT INTO `street` VALUES (500235123, 500235, '泥溪镇');
INSERT INTO `street` VALUES (500235124, 500235, '蔈草镇');
INSERT INTO `street` VALUES (500235125, 500235, '养鹿镇');
INSERT INTO `street` VALUES (500235126, 500235, '水口镇');
INSERT INTO `street` VALUES (500235127, 500235, '堰坪镇');
INSERT INTO `street` VALUES (500235128, 500235, '龙洞镇');
INSERT INTO `street` VALUES (500235129, 500235, '后叶镇');
INSERT INTO `street` VALUES (500235130, 500235, '耀灵镇');
INSERT INTO `street` VALUES (500235131, 500235, '大阳镇');
INSERT INTO `street` VALUES (500235201, 500235, '外郎乡');
INSERT INTO `street` VALUES (500235202, 500235, '新津乡');
INSERT INTO `street` VALUES (500235203, 500235, '普安乡');
INSERT INTO `street` VALUES (500235204, 500235, '洞鹿乡');
INSERT INTO `street` VALUES (500235205, 500235, '石门乡');
INSERT INTO `street` VALUES (500235206, 500235, '上坝乡');
INSERT INTO `street` VALUES (500235207, 500235, '清水土家族乡');
INSERT INTO `street` VALUES (500236001, 500236, '永安街道');
INSERT INTO `street` VALUES (500236002, 500236, '鱼复街道');
INSERT INTO `street` VALUES (500236003, 500236, '夔门街道');
INSERT INTO `street` VALUES (500236004, 500236, '夔州街道');
INSERT INTO `street` VALUES (500236101, 500236, '白帝镇');
INSERT INTO `street` VALUES (500236102, 500236, '草堂镇');
INSERT INTO `street` VALUES (500236103, 500236, '汾河镇');
INSERT INTO `street` VALUES (500236104, 500236, '康乐镇');
INSERT INTO `street` VALUES (500236105, 500236, '大树镇');
INSERT INTO `street` VALUES (500236106, 500236, '竹园镇');
INSERT INTO `street` VALUES (500236107, 500236, '公平镇');
INSERT INTO `street` VALUES (500236108, 500236, '朱衣镇');
INSERT INTO `street` VALUES (500236109, 500236, '甲高镇');
INSERT INTO `street` VALUES (500236110, 500236, '羊市镇');
INSERT INTO `street` VALUES (500236111, 500236, '吐祥镇');
INSERT INTO `street` VALUES (500236112, 500236, '兴隆镇');
INSERT INTO `street` VALUES (500236113, 500236, '青龙镇');
INSERT INTO `street` VALUES (500236114, 500236, '新民镇');
INSERT INTO `street` VALUES (500236115, 500236, '永乐镇');
INSERT INTO `street` VALUES (500236116, 500236, '安坪镇');
INSERT INTO `street` VALUES (500236117, 500236, '五马镇');
INSERT INTO `street` VALUES (500236118, 500236, '青莲镇');
INSERT INTO `street` VALUES (500236201, 500236, '岩湾乡');
INSERT INTO `street` VALUES (500236202, 500236, '平安乡');
INSERT INTO `street` VALUES (500236203, 500236, '红土乡');
INSERT INTO `street` VALUES (500236204, 500236, '石岗乡');
INSERT INTO `street` VALUES (500236205, 500236, '康坪乡');
INSERT INTO `street` VALUES (500236206, 500236, '太和土家族乡');
INSERT INTO `street` VALUES (500236207, 500236, '鹤峰乡');
INSERT INTO `street` VALUES (500236208, 500236, '冯坪乡');
INSERT INTO `street` VALUES (500236209, 500236, '长安土家族乡');
INSERT INTO `street` VALUES (500236210, 500236, '龙桥土家族乡');
INSERT INTO `street` VALUES (500236211, 500236, '云雾土家族乡');
INSERT INTO `street` VALUES (500237001, 500237, '高唐街道');
INSERT INTO `street` VALUES (500237002, 500237, '龙门街道');
INSERT INTO `street` VALUES (500237101, 500237, '庙宇镇');
INSERT INTO `street` VALUES (500237102, 500237, '大昌镇');
INSERT INTO `street` VALUES (500237103, 500237, '福田镇');
INSERT INTO `street` VALUES (500237104, 500237, '龙溪镇');
INSERT INTO `street` VALUES (500237105, 500237, '双龙镇');
INSERT INTO `street` VALUES (500237106, 500237, '官阳镇');
INSERT INTO `street` VALUES (500237107, 500237, '骡坪镇');
INSERT INTO `street` VALUES (500237108, 500237, '抱龙镇');
INSERT INTO `street` VALUES (500237109, 500237, '官渡镇');
INSERT INTO `street` VALUES (500237110, 500237, '铜鼓镇');
INSERT INTO `street` VALUES (500237111, 500237, '巫峡镇');
INSERT INTO `street` VALUES (500237201, 500237, '红椿乡');
INSERT INTO `street` VALUES (500237202, 500237, '两坪乡');
INSERT INTO `street` VALUES (500237203, 500237, '曲尺乡');
INSERT INTO `street` VALUES (500237204, 500237, '建平乡');
INSERT INTO `street` VALUES (500237205, 500237, '大溪乡');
INSERT INTO `street` VALUES (500237206, 500237, '金坪乡');
INSERT INTO `street` VALUES (500237207, 500237, '平河乡');
INSERT INTO `street` VALUES (500237208, 500237, '当阳乡');
INSERT INTO `street` VALUES (500237209, 500237, '竹贤乡');
INSERT INTO `street` VALUES (500237210, 500237, '三溪乡');
INSERT INTO `street` VALUES (500237211, 500237, '培石乡');
INSERT INTO `street` VALUES (500237212, 500237, '笃坪乡');
INSERT INTO `street` VALUES (500237213, 500237, '邓家乡');
INSERT INTO `street` VALUES (500238001, 500238, '宁河街道');
INSERT INTO `street` VALUES (500238002, 500238, '柏杨街道');
INSERT INTO `street` VALUES (500238101, 500238, '城厢镇');
INSERT INTO `street` VALUES (500238102, 500238, '凤凰镇');
INSERT INTO `street` VALUES (500238103, 500238, '宁厂镇');
INSERT INTO `street` VALUES (500238104, 500238, '上磺镇');
INSERT INTO `street` VALUES (500238105, 500238, '古路镇');
INSERT INTO `street` VALUES (500238106, 500238, '文峰镇');
INSERT INTO `street` VALUES (500238107, 500238, '徐家镇');
INSERT INTO `street` VALUES (500238108, 500238, '白鹿镇');
INSERT INTO `street` VALUES (500238109, 500238, '尖山镇');
INSERT INTO `street` VALUES (500238110, 500238, '下堡镇');
INSERT INTO `street` VALUES (500238111, 500238, '峰灵镇');
INSERT INTO `street` VALUES (500238112, 500238, '塘坊镇');
INSERT INTO `street` VALUES (500238113, 500238, '朝阳镇');
INSERT INTO `street` VALUES (500238114, 500238, '田坝镇');
INSERT INTO `street` VALUES (500238115, 500238, '通城镇');
INSERT INTO `street` VALUES (500238116, 500238, '菱角镇');
INSERT INTO `street` VALUES (500238117, 500238, '蒲莲镇');
INSERT INTO `street` VALUES (500238118, 500238, '土城镇');
INSERT INTO `street` VALUES (500238119, 500238, '红池坝镇');
INSERT INTO `street` VALUES (500238201, 500238, '胜利乡');
INSERT INTO `street` VALUES (500238202, 500238, '大河乡');
INSERT INTO `street` VALUES (500238203, 500238, '天星乡');
INSERT INTO `street` VALUES (500238204, 500238, '长桂乡');
INSERT INTO `street` VALUES (500238205, 500238, '鱼鳞乡');
INSERT INTO `street` VALUES (500238206, 500238, '乌龙乡');
INSERT INTO `street` VALUES (500238207, 500238, '花台乡');
INSERT INTO `street` VALUES (500238208, 500238, '兰英乡');
INSERT INTO `street` VALUES (500238209, 500238, '双阳乡');
INSERT INTO `street` VALUES (500238210, 500238, '中梁乡');
INSERT INTO `street` VALUES (500238211, 500238, '天元乡');
INSERT INTO `street` VALUES (500240001, 500240, '南宾街道');
INSERT INTO `street` VALUES (500240002, 500240, '万安街道');
INSERT INTO `street` VALUES (500240003, 500240, '下路街道');
INSERT INTO `street` VALUES (500240101, 500240, '西沱镇');
INSERT INTO `street` VALUES (500240102, 500240, '悦崃镇');
INSERT INTO `street` VALUES (500240103, 500240, '临溪镇');
INSERT INTO `street` VALUES (500240104, 500240, '黄水镇');
INSERT INTO `street` VALUES (500240105, 500240, '马武镇');
INSERT INTO `street` VALUES (500240106, 500240, '沙子镇');
INSERT INTO `street` VALUES (500240107, 500240, '王场镇');
INSERT INTO `street` VALUES (500240108, 500240, '沿溪镇');
INSERT INTO `street` VALUES (500240109, 500240, '龙沙镇');
INSERT INTO `street` VALUES (500240110, 500240, '鱼池镇');
INSERT INTO `street` VALUES (500240111, 500240, '三河镇');
INSERT INTO `street` VALUES (500240112, 500240, '大歇镇');
INSERT INTO `street` VALUES (500240113, 500240, '桥头镇');
INSERT INTO `street` VALUES (500240114, 500240, '万朝镇');
INSERT INTO `street` VALUES (500240115, 500240, '冷水镇');
INSERT INTO `street` VALUES (500240116, 500240, '黄鹤镇');
INSERT INTO `street` VALUES (500240117, 500240, '枫木镇');
INSERT INTO `street` VALUES (500240201, 500240, '黎场乡');
INSERT INTO `street` VALUES (500240202, 500240, '三星乡');
INSERT INTO `street` VALUES (500240203, 500240, '六塘乡');
INSERT INTO `street` VALUES (500240204, 500240, '三益乡');
INSERT INTO `street` VALUES (500240205, 500240, '王家乡');
INSERT INTO `street` VALUES (500240206, 500240, '河嘴乡');
INSERT INTO `street` VALUES (500240207, 500240, '石家乡');
INSERT INTO `street` VALUES (500240208, 500240, '中益乡');
INSERT INTO `street` VALUES (500240209, 500240, '洗新乡');
INSERT INTO `street` VALUES (500240210, 500240, '龙潭乡');
INSERT INTO `street` VALUES (500240211, 500240, '新乐乡');
INSERT INTO `street` VALUES (500240212, 500240, '金铃乡');
INSERT INTO `street` VALUES (500240213, 500240, '金竹乡');
INSERT INTO `street` VALUES (500241001, 500241, '中和街道');
INSERT INTO `street` VALUES (500241002, 500241, '乌杨街道');
INSERT INTO `street` VALUES (500241003, 500241, '平凯街道');
INSERT INTO `street` VALUES (500241004, 500241, '官庄街道');
INSERT INTO `street` VALUES (500241101, 500241, '清溪场镇');
INSERT INTO `street` VALUES (500241102, 500241, '隘口镇');
INSERT INTO `street` VALUES (500241103, 500241, '溶溪镇');
INSERT INTO `street` VALUES (500241104, 500241, '龙池镇');
INSERT INTO `street` VALUES (500241105, 500241, '石堤镇');
INSERT INTO `street` VALUES (500241106, 500241, '峨溶镇');
INSERT INTO `street` VALUES (500241107, 500241, '洪安镇');
INSERT INTO `street` VALUES (500241108, 500241, '雅江镇');
INSERT INTO `street` VALUES (500241109, 500241, '石耶镇');
INSERT INTO `street` VALUES (500241110, 500241, '梅江镇');
INSERT INTO `street` VALUES (500241111, 500241, '兰桥镇');
INSERT INTO `street` VALUES (500241112, 500241, '膏田镇');
INSERT INTO `street` VALUES (500241113, 500241, '溪口镇');
INSERT INTO `street` VALUES (500241114, 500241, '妙泉镇');
INSERT INTO `street` VALUES (500241115, 500241, '宋农镇');
INSERT INTO `street` VALUES (500241116, 500241, '里仁镇');
INSERT INTO `street` VALUES (500241117, 500241, '钟灵镇');
INSERT INTO `street` VALUES (500241201, 500241, '孝溪乡');
INSERT INTO `street` VALUES (500241202, 500241, '海洋乡');
INSERT INTO `street` VALUES (500241203, 500241, '大溪乡');
INSERT INTO `street` VALUES (500241204, 500241, '涌洞乡');
INSERT INTO `street` VALUES (500241205, 500241, '中平乡');
INSERT INTO `street` VALUES (500241206, 500241, '岑溪乡');
INSERT INTO `street` VALUES (500242001, 500242, '桃花源街道');
INSERT INTO `street` VALUES (500242002, 500242, '钟多街道');
INSERT INTO `street` VALUES (500242101, 500242, '龙潭镇');
INSERT INTO `street` VALUES (500242102, 500242, '麻旺镇');
INSERT INTO `street` VALUES (500242103, 500242, '酉酬镇');
INSERT INTO `street` VALUES (500242104, 500242, '大溪镇');
INSERT INTO `street` VALUES (500242105, 500242, '兴隆镇');
INSERT INTO `street` VALUES (500242106, 500242, '黑水镇');
INSERT INTO `street` VALUES (500242107, 500242, '丁市镇');
INSERT INTO `street` VALUES (500242108, 500242, '龚滩镇');
INSERT INTO `street` VALUES (500242109, 500242, '李溪镇');
INSERT INTO `street` VALUES (500242110, 500242, '泔溪镇');
INSERT INTO `street` VALUES (500242111, 500242, '酉水河镇');
INSERT INTO `street` VALUES (500242112, 500242, '苍岭镇');
INSERT INTO `street` VALUES (500242113, 500242, '小河镇');
INSERT INTO `street` VALUES (500242114, 500242, '板溪镇');
INSERT INTO `street` VALUES (500242115, 500242, '涂市镇');
INSERT INTO `street` VALUES (500242116, 500242, '铜鼓镇');
INSERT INTO `street` VALUES (500242117, 500242, '五福镇');
INSERT INTO `street` VALUES (500242118, 500242, '万木镇');
INSERT INTO `street` VALUES (500242119, 500242, '南腰界镇');
INSERT INTO `street` VALUES (500242201, 500242, '可大乡');
INSERT INTO `street` VALUES (500242202, 500242, '偏柏乡');
INSERT INTO `street` VALUES (500242203, 500242, '木叶乡');
INSERT INTO `street` VALUES (500242204, 500242, '毛坝乡');
INSERT INTO `street` VALUES (500242205, 500242, '花田乡');
INSERT INTO `street` VALUES (500242206, 500242, '后坪乡');
INSERT INTO `street` VALUES (500242207, 500242, '天馆乡');
INSERT INTO `street` VALUES (500242208, 500242, '宜居乡');
INSERT INTO `street` VALUES (500242209, 500242, '两罾乡');
INSERT INTO `street` VALUES (500242210, 500242, '板桥乡');
INSERT INTO `street` VALUES (500242211, 500242, '官清乡');
INSERT INTO `street` VALUES (500242212, 500242, '车田乡');
INSERT INTO `street` VALUES (500242213, 500242, '腴地乡');
INSERT INTO `street` VALUES (500242214, 500242, '清泉乡');
INSERT INTO `street` VALUES (500242215, 500242, '庙溪乡');
INSERT INTO `street` VALUES (500242216, 500242, '浪坪乡');
INSERT INTO `street` VALUES (500242217, 500242, '双泉乡');
INSERT INTO `street` VALUES (500242218, 500242, '楠木乡');
INSERT INTO `street` VALUES (500243001, 500243, '汉葭街道');
INSERT INTO `street` VALUES (500243002, 500243, '绍庆街道');
INSERT INTO `street` VALUES (500243003, 500243, '靛水街道');
INSERT INTO `street` VALUES (500243101, 500243, '保家镇');
INSERT INTO `street` VALUES (500243102, 500243, '郁山镇');
INSERT INTO `street` VALUES (500243103, 500243, '高谷镇');
INSERT INTO `street` VALUES (500243104, 500243, '桑柘镇');
INSERT INTO `street` VALUES (500243105, 500243, '鹿角镇');
INSERT INTO `street` VALUES (500243106, 500243, '黄家镇');
INSERT INTO `street` VALUES (500243107, 500243, '普子镇');
INSERT INTO `street` VALUES (500243108, 500243, '龙射镇');
INSERT INTO `street` VALUES (500243109, 500243, '连湖镇');
INSERT INTO `street` VALUES (500243110, 500243, '万足镇');
INSERT INTO `street` VALUES (500243111, 500243, '平安镇');
INSERT INTO `street` VALUES (500243112, 500243, '长生镇');
INSERT INTO `street` VALUES (500243113, 500243, '新田镇');
INSERT INTO `street` VALUES (500243114, 500243, '鞍子镇');
INSERT INTO `street` VALUES (500243115, 500243, '太原镇');
INSERT INTO `street` VALUES (500243116, 500243, '龙溪镇');
INSERT INTO `street` VALUES (500243117, 500243, '梅子垭镇');
INSERT INTO `street` VALUES (500243118, 500243, '大同镇');
INSERT INTO `street` VALUES (500243201, 500243, '岩东乡');
INSERT INTO `street` VALUES (500243202, 500243, '鹿鸣乡');
INSERT INTO `street` VALUES (500243203, 500243, '棣棠乡');
INSERT INTO `street` VALUES (500243204, 500243, '三义乡');
INSERT INTO `street` VALUES (500243205, 500243, '联合乡');
INSERT INTO `street` VALUES (500243206, 500243, '石柳乡');
INSERT INTO `street` VALUES (500243207, 500243, '走马乡');
INSERT INTO `street` VALUES (500243208, 500243, '芦塘乡');
INSERT INTO `street` VALUES (500243209, 500243, '乔梓乡');
INSERT INTO `street` VALUES (500243210, 500243, '诸佛乡');
INSERT INTO `street` VALUES (500243211, 500243, '桐楼乡');
INSERT INTO `street` VALUES (500243212, 500243, '善感乡');
INSERT INTO `street` VALUES (500243213, 500243, '双龙乡');
INSERT INTO `street` VALUES (500243214, 500243, '石盘乡');
INSERT INTO `street` VALUES (500243215, 500243, '大垭乡');
INSERT INTO `street` VALUES (500243216, 500243, '润溪乡');
INSERT INTO `street` VALUES (500243217, 500243, '朗溪乡');
INSERT INTO `street` VALUES (500243218, 500243, '龙塘乡');

SET FOREIGN_KEY_CHECKS = 1;
