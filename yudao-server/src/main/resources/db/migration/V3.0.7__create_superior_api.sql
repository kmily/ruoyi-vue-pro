DROP TABLE IF EXISTS `haoka_superior_api`;
CREATE TABLE `haoka_superior_api`
(
    `id`                           bigint(20) NOT NULL PRIMARY KEY COMMENT 'ID',
    `name`                         varchar(512) NOT NULL DEFAULT '' COMMENT '名字',
    `enable_select_num`            bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否有选号功能',
    `abnormal_order_handle_method` int(11) NOT NULL COMMENT '异常订单处理方式', -- 枚举 abnormal_order_handle_method：异常订单人工处理，异常订单自动处理
    `status`                       int(11) NOT NULL COMMENT '接口状态',         -- 枚举 haoka_superior_api_status: 开发中,完成,
    `publish_time`                 datetime COMMENT '发布日期',
    `api_doc`                      longtext COMMENT 'API文档',
    `is_dev_confined`              bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否已配置开发',
    `is_sku_confined`              bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否已配置产品',

    `dept_id`                      bigint(20) COMMENT '部门ID',
    `creator`                      varchar(64)  NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time`                  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                      varchar(64)  NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time`                  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`                      bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`                    bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
    UNIQUE (tenant_id, name)
) COMMENT = '上游API接口';
