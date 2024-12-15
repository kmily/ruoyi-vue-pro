DROP TABLE IF EXISTS `haoka_superior_product_config`;
CREATE TABLE `haoka_superior_product_config`
(
    `id`                    bigint(20) NOT NULL PRIMARY KEY COMMENT 'ID',
    `haoka_superior_api_id` bigint(20) NOT NULL COMMENT '上游接口ID',
    `haoka_product_id`      bigint(20) NOT NULL COMMENT '产品ID',

    `is_confined`           bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否已配置',
    `config`                text        NOT NULL COMMENT '值',
    `remarks`               text        COMMENT '说明',

    `dept_id`               bigint(20) COMMENT '部门ID',
    `creator`               varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time`           datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`               varchar(64) NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time`           datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`               bit(1)      NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`             bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
    UNIQUE (haoka_superior_api_id, haoka_product_id)
) COMMENT = '产品对接上游配置';
