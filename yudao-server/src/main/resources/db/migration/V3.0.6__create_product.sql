DROP TABLE IF EXISTS `haoka_product`;
CREATE TABLE `haoka_product`
(
    `id`                       bigint(20) NOT NULL PRIMARY KEY COMMENT '产品ID',
    `operator`                 int(11) NOT NULL COMMENT '运营商',         -- 枚举 haoka_operator:中国移动，中国联通，中国电信，中国广电，虚拟运营商
    `sku`                      varchar(512) NOT NULL COMMENT '产品编码',
    `name`                     varchar(512) NOT NULL COMMENT '产品名称',
    `haoka_product_type_id`    bigint(20) COMMENT '产品类型',
    `belong_area_code`         int(11) NOT NULL COMMENT '归属地',
    `haoka_product_channel_id` bigint(20) COMMENT '产品渠道',
    `haoka_product_limit_id`   bigint(20) NOT NULL COMMENT '产品限制',
    `id_card_num_verify`       int(11) NOT NULL COMMENT '身份证号码验证', -- 枚举 id_card_num_verify:不需要，需要，国政同校验
    `id_card_img_verify`       int(11) NOT NULL COMMENT '身份证图片验证', -- 枚举 id_card_img_verify:不需要，需要，需要但不审核，智能校验
    `produce_address`          varchar(512) NOT NULL DEFAULT '' COMMENT '生产地址',
    `need_blacklist_filter`    bit(1)       NOT NULL DEFAULT b'1' COMMENT '黑名单过滤',

    `enable_stock_limit`       bit(1) COMMENT '是否启用库存限制',
    `stock_num`                int(11) COMMENT '库存数量',
    `stock_warn_num`           int(11) COMMENT '库存报警数量',
    `produce_remarks`          text COMMENT '生产备注',
    `settlement_rules`         text COMMENT '结算规则',
    `estimated_revenue`        text COMMENT '预估收益',

    `on_sale`                  bit(1)       NOT NULL DEFAULT b'1' COMMENT '上架',
    `is_top`                   bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否顶置',

    `dept_id`                  bigint(20) COMMENT '部门ID',
    `creator`                  varchar(64)  NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                  varchar(64)  NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time`              datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`                  bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`                bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
    UNIQUE (tenant_id, sku)
) COMMENT = '产品/渠道';
