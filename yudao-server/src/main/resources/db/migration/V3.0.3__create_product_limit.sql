DROP TABLE IF EXISTS `haoka_product_limit`;
CREATE TABLE `haoka_product_limit`
(
    `id`                         bigint(20) NOT NULL PRIMARY KEY COMMENT '产品类型ID',
    `name`                       varchar(512) NOT NULL DEFAULT '' COMMENT '产品类型名称',
    `use_only_send_area`         bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否使用只发货地址',
    `use_not_send_area`          bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否使用不发货地址',
    `use_card_limit`             bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否使用身份证限制',
    `age_max`                    int(11) COMMENT '最大年龄限制',
    `age_min`                    int(11) COMMENT '最小年龄限制',
    `person_card_quantity_limit` int(11) COMMENT '单人开卡数量限制',
    `detection_cycle`            int(11) COMMENT '检测周期(月)',

    `dept_id`                    bigint(20) COMMENT '部门ID',
    `creator`                    varchar(64)  NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time`                datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                    varchar(64)  NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time`                datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`                    bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`                  bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号'
) COMMENT = '产品限制条件';
