/*
 编码规则相关表和测试数据
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 编号规则表头表结构
-- ----------------------------
DROP TABLE IF EXISTS `system_coding_rules`;
CREATE TABLE `system_coding_rules`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_code`(`code`) USING BTREE COMMENT '编码唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编码规则表头' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 编码规则表头测试数据
-- ----------------------------
INSERT INTO `system_coding_rules` VALUES ('1797829879208181762', 'CRM_CONTRACT_NO_RULES', 'CRM合同编号规则', 'CRM合同编号规则', '1', '2024-06-04 11:17:00', '1', '2024-06-04 17:44:28', b'0', 1);
INSERT INTO `system_coding_rules` VALUES ('1797829879208181763', 'CRM_RECEIVABLE', 'CRM回款编号规则', 'CRM回款编号规则', '1', '2024-06-04 11:17:00', '1', '2024-06-04 18:02:18', b'0', 1);
INSERT INTO `system_coding_rules` VALUES ('1798189491874811905', 'TEST_A_002', '测试规则2', '测试规则2', '1', '2024-06-05 11:05:58', '1', '2024-07-10 10:27:31', b'1', 1);
INSERT INTO `system_coding_rules` VALUES ('1811697637008089090', 'TEST_001', '测试编码', '', '1', '2024-07-12 17:42:31', '1', '2024-07-12 17:42:31', b'0', 1);

-- ----------------------------
--  编码规则明细表结构
-- ----------------------------
DROP TABLE IF EXISTS `system_coding_rules_details`;
CREATE TABLE `system_coding_rules_details`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `rule_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码规则头id',
  `order_num` int NOT NULL COMMENT '序号',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '类型，字典coding_rules_type，1-常量 2-日期 3-日流水号 4-月流水号 5-年流水号',
  `value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设置值',
  `len` int NULL DEFAULT NULL COMMENT '长度',
  `initial` int NULL DEFAULT NULL COMMENT '起始值',
  `step_size` int NULL DEFAULT NULL COMMENT '步长',
  `fill_key` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补位符',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编码规则明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 编码规则明细表测试数据
-- ----------------------------
INSERT INTO `system_coding_rules_details` VALUES ('1797830382197506049', '1797829879208181762', 1, '1', 'HT', NULL, NULL, NULL, NULL, NULL, '1', '2024-06-04 11:19:00', '1', '2024-06-04 17:44:58', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1797830551970349057', '1797829879208181762', 2, '2', 'yyyyMMdd', NULL, NULL, NULL, NULL, NULL, '1', '2024-06-04 11:19:40', '1', '2024-06-04 11:21:32', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1797830739279577090', '1797829879208181762', 3, '3', NULL, 6, 1, 1, '0', NULL, '1', '2024-06-04 11:20:25', '1', '2024-06-04 17:45:07', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811005408333627394', '1797829879208181763', 0, '1', '2', 0, 0, 0, '', '测试数据', '1', '2024-07-10 19:51:51', '1', '2024-07-10 20:10:03', b'1', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811010244277981185', '1797829879208181763', 0, '1', 'mmhhh', 0, 0, 0, '', '测试数据1', '1', '2024-07-10 20:11:04', '1', '2024-07-12 17:46:29', b'1', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811011146019786753', '1797829879208181763', 0, '1', '22', 0, 0, 0, '', '22', '1', '2024-07-10 20:14:39', '1', '2024-07-10 20:15:13', b'1', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811697735372906497', '1811697637008089090', 1, '1', 'TT-', 0, 0, 0, '', '', '1', '2024-07-12 17:42:54', '1', '2024-07-12 17:42:54', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811697796706213890', '1811697637008089090', 2, '2', 'yyyyMMdd', 0, 0, 0, '', '', '1', '2024-07-12 17:43:09', '1', '2024-07-12 17:43:09', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811697878956515330', '1811697637008089090', 4, '3', '', 4, 1, 1, '0', '', '1', '2024-07-12 17:43:29', '1', '2024-07-12 17:43:58', b'1', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811697935088885761', '1811697637008089090', 3, '1', '-', 0, 0, 0, '', '', '1', '2024-07-12 17:43:42', '1', '2024-07-12 17:44:00', b'1', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811698491102601217', '1811697637008089090', 3, '1', '-', 0, 0, 0, '', '', '1', '2024-07-12 17:45:55', '1', '2024-07-12 17:45:55', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811698539970437122', '1811697637008089090', 4, '3', '', 4, 1, 1, '0', '', '1', '2024-07-12 17:46:06', '1', '2024-07-12 17:46:06', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811698664901976065', '1797829879208181763', 1, '1', 'HTHK', 0, 0, 0, '', '', '1', '2024-07-12 17:46:36', '1', '2024-07-12 17:48:41', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811698696451530753', '1797829879208181763', 2, '2', 'yyyyMMdd', 0, 0, 0, '', '', '1', '2024-07-12 17:46:44', '1', '2024-07-12 17:46:44', b'0', 1);
INSERT INTO `system_coding_rules_details` VALUES ('1811698789242118146', '1797829879208181763', 3, '3', 'yyyyMM', 6, 1, 1, '0', '', '1', '2024-07-12 17:47:06', '1', '2024-07-12 17:47:06', b'0', 1);

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2758, '编码规则', '', 2, 0, 1, 'coding-rules', 'ep:calendar', 'system/codingrules/index', 'CodingRules', 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '1', '2024-07-19 09:20:54', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2759, '编码规则表头查询', 'system:coding-rules:query', 3, 1, 2758, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '', '2024-07-19 09:20:55', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2760, '编码规则表头创建', 'system:coding-rules:create', 3, 2, 2758, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '', '2024-07-19 09:20:57', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2761, '编码规则表头更新', 'system:coding-rules:update', 3, 3, 2758, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '', '2024-07-19 09:20:59', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2762, '编码规则表头删除', 'system:coding-rules:delete', 3, 4, 2758, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '', '2024-07-19 09:21:02', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2763, '编码规则表头导出', 'system:coding-rules:export', 3, 5, 2758, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-06-12 16:37:59', '', '2024-07-19 09:21:04', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2764, '编码规则明细', '', 2, 0, 1, 'coding-rules-details', '', 'system/codingrules/details/index', 'CodingRulesDetails', 0, b'0', b'1', b'1', '', '2024-07-10 11:38:22', '1', '2024-07-10 13:47:46', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2765, '编码规则明细查询', 'system:coding-rules-details:query', 3, 1, 2764, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-07-10 11:38:22', '', '2024-07-10 11:38:22', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2766, '编码规则明细创建', 'system:coding-rules-details:create', 3, 2, 2764, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-07-10 11:38:22', '', '2024-07-10 11:38:22', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2767, '编码规则明细更新', 'system:coding-rules-details:update', 3, 3, 2764, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-07-10 11:38:22', '', '2024-07-10 11:38:22', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2768, '编码规则明细删除', 'system:coding-rules-details:delete', 3, 4, 2764, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-07-10 11:38:22', '', '2024-07-10 11:38:22', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2769, '编码规则明细导出', 'system:coding-rules-details:export', 3, 5, 2764, '', '', '', NULL, 0, b'1', b'1', b'1', '', '2024-07-10 11:38:22', '', '2024-07-10 11:38:22', b'0');

-- ----------------------------
-- 字典数据
-- ----------------------------
INSERT INTO `ruoyi-vue-pro`.`system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (621, '编码规则类型', 'coding_rules_type', 0, '', '1', '2024-06-06 17:10:45', '1', '2024-06-06 17:10:45', b'0', '1970-01-01 00:00:00');
INSERT INTO `ruoyi-vue-pro`.`system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (622, '日期格式', 'coding_rules_date_format', 0, '', '1', '2024-06-06 17:18:54', '1', '2024-06-06 17:18:54', b'0', '1970-01-01 00:00:00');

INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1539, 1, '常量', '1', 'coding_rules_type', 0, '', '', '', '1', '2024-06-06 17:12:23', '1', '2024-06-06 17:12:23', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1540, 2, '日期', '2', 'coding_rules_type', 0, '', '', '', '1', '2024-06-06 17:12:33', '1', '2024-06-06 17:12:33', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1541, 3, '日流水号', '3', 'coding_rules_type', 0, '', '', '', '1', '2024-06-06 17:12:50', '1', '2024-06-06 17:12:50', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1542, 4, '月流水号', '4', 'coding_rules_type', 0, '', '', '', '1', '2024-06-06 17:13:10', '1', '2024-06-06 17:13:10', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1543, 5, '年流水号', '5', 'coding_rules_type', 0, '', '', '', '1', '2024-06-06 17:13:23', '1', '2024-06-06 17:13:23', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1544, 1, 'yyyyMMdd', 'yyyyMMdd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:19:52', '1', '2024-06-06 17:19:52', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1545, 2, 'yyyyMM', 'yyyyMM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:20:01', '1', '2024-06-06 17:20:01', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1546, 3, 'yyyy', 'yyyy', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:20:12', '1', '2024-06-06 17:20:12', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1547, 4, 'yyyy-MM-dd', 'yyyy-MM-dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:22:38', '1', '2024-06-06 17:22:38', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1548, 5, 'yyyy-MM', 'yyyy-MM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:22:49', '1', '2024-06-06 17:22:49', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1549, 6, 'MMdd', 'MMdd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:25:31', '1', '2024-06-06 17:25:31', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1550, 7, 'MM-dd', 'MM-dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:25:42', '1', '2024-06-06 17:25:42', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1551, 9, 'MM', 'MM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:25:56', '1', '2024-06-06 17:31:16', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1552, 9, 'dd', 'dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:26:06', '1', '2024-06-06 17:31:38', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1553, 10, 'yyyy/MM/dd', 'yyyy/MM/dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:26:25', '1', '2024-06-06 17:26:25', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1554, 11, 'yyyy/MM', 'yyyy/MM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:26:34', '1', '2024-06-06 17:26:34', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1555, 12, 'MM/dd', 'MM/dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:26:46', '1', '2024-06-06 17:26:46', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1556, 13, 'yyyy.MM.dd', 'yyyy.MM.dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:28:37', '1', '2024-06-06 17:28:37', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1557, 14, 'yyyy.MM', 'yyyy.MM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:28:46', '1', '2024-06-06 17:28:46', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1558, 15, 'MM.dd', 'MM.dd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:29:06', '1', '2024-06-06 17:29:06', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1559, 8, 'yy', 'yy', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:31:05', '1', '2024-06-06 17:31:05', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1560, 16, 'yyMMdd', 'yyMMdd', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:32:00', '1', '2024-06-06 17:32:00', b'0');
INSERT INTO `ruoyi-vue-pro`.`system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1561, 17, 'yyMM', 'yyMM', 'coding_rules_date_format', 0, '', '', '', '1', '2024-06-06 17:32:19', '1', '2024-06-06 17:32:19', b'0');


