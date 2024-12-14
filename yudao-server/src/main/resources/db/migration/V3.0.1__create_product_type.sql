DROP TABLE IF EXISTS `haoka_product_type`;
CREATE TABLE `haoka_product_type`
(
    `id`          bigint(20) NOT NULL PRIMARY KEY COMMENT '产品类型ID',
    `name`        varchar(512) NOT NULL COMMENT '产品类型名称',

    `dept_id`     bigint(20) COMMENT '部门ID',
    `creator`     varchar(64)  NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)  NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
    UNIQUE (tenant_id, name)
) COMMENT = '产品类型';
