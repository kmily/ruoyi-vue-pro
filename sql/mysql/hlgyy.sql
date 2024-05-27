DROP TABLE IF EXISTS `hlgyy_member_user_ext`;
create table hlgyy_member_user_ext
(
    `id`                     bigint auto_increment comment '自增ID',
    `appointment_date`       date                                null comment '预约日期',
    `appointment_time_range` varchar(20)                         not null comment '预约时间段范围',
    `user_id`                bigint                              null comment '用户 ID',
    `created_at`             timestamp default current_timestamp null comment '创建时间',
    `updated_at`             timestamp default current_timestamp null on update CURRENT_TIMESTAMP,
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    constraint `uniq_idx_user_id` unique key (user_id)
)ENGINE = InnoDB comment '治疗用户扩展表' CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `hlgyy_ai_chat_message`;
create table hlgyy_ai_chat_message
(
    `id`              bigint auto_increment comment 'auto id',
    `conversation_id` varchar(50)   not null comment '会话ID',
    `send_user_id`    bigint        not null comment '发送人 ID',
    `content`         varchar(1000) not null comment '会话内容',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_create_time`(`created_at` ASC) USING BTREE
)ENGINE = InnoDB comment '聊天消息' CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;