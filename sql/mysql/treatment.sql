DROP TABLE IF EXISTS `hlgyy_treatment_flow`;
CREATE TABLE `hlgyy_treatment_flow`  (
      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程id',
      `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '治疗流程名称',
      `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '治疗流程编码',
      `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（0未发布 1已发布）',
      `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
      `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程模板';

insert into hlgyy_treatment_flow(name, code, status, creator, create_time, updater, update_time, deleted)
values('治疗流程1', 'treatment_flow_1', 1, 'admin', now(), 'admin', now(), 0);

DROP TABLE IF EXISTS `hlgyy_treatment_instance`;
CREATE TABLE `hlgyy_treatment_instance`  (
         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '治疗流程实例id',
         `user_id` bigint not null COMMENT '用户id',
         `flow_id` bigint NOT NULL COMMENT '治疗流程id',
         `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
         `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
         `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程实例';


DROP TABLE IF EXISTS `hlgyy_treatment_flow_dayitem`;
CREATE TABLE `hlgyy_treatment_flow_dayitem`  (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
     `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
     `status` tinyint NOT NULL DEFAULT 0 COMMENT '流程状态（0开始 1进行中 2已完成 3未完成）',
     `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
     `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
     `is_break` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否休息',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '治疗流程模板';
