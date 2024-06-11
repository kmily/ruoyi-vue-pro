DROP TABLE IF EXISTS `hlgyy_treatment_flow`;  # 治疗流程模板
CREATE TABLE `hlgyy_treatment_flow`  (
      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程id',
      `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '治疗流程名称',
      `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '治疗流程编码',
      `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（1未发布 0已发布）',
      `tenant_id` bigint NOT NULL COMMENT '租户id',
      `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
      `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
      `type` tinyint NOT NULL DEFAULT 0 COMMENT '方案类型',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程模板';



DROP TABLE IF EXISTS `hlgyy_treatment_flow_days`; # 治疗流程模板的每日
CREATE TABLE `hlgyy_treatment_flow_days`  (
         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
         `flow_id` bigint NOT NULL COMMENT '治疗流程id',
         `sequence` int NOT NULL DEFAULT 0 COMMENT '顺序',
         `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
         `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
         `tenant_id` bigint NOT NULL COMMENT '租户id',
         `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
         `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
         `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
         `has_break` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否休息',
         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程模板';

DROP TABLE IF EXISTS `hlgyy_treatment_flow_dayitem`; # 治疗流程模板的每日的项目
CREATE TABLE `hlgyy_treatment_flow_dayitem`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
     `day_id` bigint NOT NULL COMMENT 'day id',
     `flow_id` bigint NOT NULL COMMENT '方案id',
     `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
     `settings` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '设置',
     `item_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '类型',
     `dependent_item_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '依赖的dayitems',
     `agroup` int NOT NULL DEFAULT 0 COMMENT '顺序组',
     `group_seq` int NOT NULL DEFAULT 0 COMMENT '顺序组内的顺序',
     `group_settings` text NOT NULL DEFAULT '' COMMENT '顺序组的设置',
     `task_flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '任务流id',
     `tenant_id` bigint NOT NULL COMMENT '租户id',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     `required` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否必须',
     `type` tinyint NOT NULL DEFAULT 0 COMMENT '任务类型',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程模板';

delete from hlgyy_treatment_flow where id = 1;
insert into hlgyy_treatment_flow(id, name, tenant_id, code, status, creator, create_time, updater, update_time, deleted, `type`)
values(1, '治疗流程1', 1 ,'main', 1, '287', now(), '287', now(), 0, 1);

delete from hlgyy_treatment_flow_days where id <= 4;
insert into hlgyy_treatment_flow_days(id, name, flow_id, tenant_id, sequence,  has_break) values(1, 'Day 1', 1, 1 , 0, 0);
insert into hlgyy_treatment_flow_days(id, name, flow_id, tenant_id, sequence,  has_break) values(2, 'Day 2', 1, 1 , 1, 0);
insert into hlgyy_treatment_flow_days(id, name, flow_id, tenant_id, sequence,  has_break) values(3, 'Day 3', 1, 1 , 2, 1);  # 休息日
insert into hlgyy_treatment_flow_days(id, name, flow_id, tenant_id, sequence,  has_break) values(4, 'Day 4', 1, 1 ,3, 0);

delete from hlgyy_treatment_flow_dayitem where id <= 10;
# day 1
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id)
values (1, 1, 2, '{"content": "欢迎来到回龙观青少年心理康复中心线上治疗中心"}', '', 1, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id)
values (2, 1, 2, '{"content": "请你做好准备"}', '', 2, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id)
values (3, 1, 1, '{"survey_codes": ["a", "b", "c", "d"]}', '', 3, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id, required)
values (4, 1, 3, '{}', '', 4, 1, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, group_seq, tenant_id, required)
values (5, 1, 4, '{}', '', 5, 0, 1, 0);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, group_seq, tenant_id, required)
values (6, 1, 5, '{}', '', 5, 1, 1, 0);
# day 2
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, group_seq, tenant_id)
values (7, 2, 7, '{}', '', 1, 0, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, group_seq, tenant_id)
values (8, 2, 7, '{}', '[7]', 1, 1, 1);
# day 4
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id)
values (9, 4, 7, '{}', '', 1, 1);
insert into hlgyy_treatment_flow_dayitem(id, day_id, `type`, settings, dependent_item_ids, `agroup`, tenant_id)
values (10, 4, 7, '{}', '', 2, 1);
# user_id = 287 is a test user
delete from hlgyy_treatment_instance where user_id = 287;
delete from hlgyy_treatment_dayitem_instance where user_id = 287;
delete from hlgyy_treatment_day_instance where user_id = 287;
delete from hlgyy_treatment_user_progress where user_id = 287;

DROP TABLE IF EXISTS `hlgyy_treatment_instance`;
CREATE TABLE `hlgyy_treatment_instance`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程实例id',
     `user_id` bigint not null COMMENT '用户id',
     `flow_id` bigint NOT NULL COMMENT '治疗流程id',
     `tenant_id` bigint NOT NULL COMMENT '租户id',
     `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（0未开始 1进行中 2已完成 3已取消）',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程实例';

DROP TABLE IF EXISTS `hlgyy_treatment_day_instance`;
CREATE TABLE `hlgyy_treatment_day_instance`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程实例每日id',
     `user_id` bigint not null COMMENT '用户id',
     `flow_instance_id` bigint NOT NULL COMMENT '治疗流程id',
     `tenant_id` bigint NOT NULL COMMENT '租户id',
     `day_id` bigint NOT NULL COMMENT '治疗流程模板的每日id',
     `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（0未开始 1进行中 2已完成 3已取消）',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程实例';



DROP TABLE IF EXISTS `hlgyy_treatment_dayitem_instance`;
CREATE TABLE `hlgyy_treatment_dayitem_instance`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程实例每日项目 id',
     `user_id` bigint not null COMMENT '用户id',
     `tenant_id` bigint NOT NULL COMMENT '租户id',
     `flow_instance_id` bigint not null COMMENT '治疗day instance id',
     `day_instance_id` bigint not null COMMENT '治疗day instance id',
     `dayitem_id` bigint NOT NULL COMMENT '治疗流程模板的每日的项目id',
     `ext_attr` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '扩展属性',
     `task_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '任务流任务实例id',
     `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（0未开始 1进行中 2已完成 3已取消）',
     `user_input` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户输入',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     `required` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否必须',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程实例';



DROP TABLE IF EXISTS `hlgyy_treatment_user_progress`;
CREATE TABLE `hlgyy_treatment_user_progress`  (
      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程实例每日项目 id',
      `user_id` bigint not null COMMENT '用户id',
      `tenant_id` bigint NOT NULL COMMENT '租户id',
      `treatment_instance_id` bigint not null COMMENT '治疗day instance id',
      `day_instance_id` bigint not null COMMENT '治疗day instance id',
      `day_agroup` bigint not null COMMENT '治疗dayitem instance i',
      `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程实例';


DROP TABLE IF EXISTS `hlgyy_treatment_chat_history`;
CREATE TABLE `hlgyy_treatment_chat_history`  (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程的聊天消息',
`message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息',
`user_id` bigint NOT NULL COMMENT '用户id',
`treatment_instance_id` bigint NOT NULL COMMENT '治疗流程实例id',
`is_system` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否系统消息',
`treatment_dayitem_instance_id` bigint NOT NULL COMMENT '治疗流程实例每日项目 id',
`source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'system' COMMENT '消息来源',

`tenant_id` bigint NOT NULL COMMENT '租户id',
`creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程历史消息表';



