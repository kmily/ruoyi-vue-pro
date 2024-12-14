DROP TABLE IF EXISTS `haoka_superior_api_sku_config`;
CREATE TABLE `haoka_superior_api_sku_config`
(
    `id`                    bigint(20) NOT NULL PRIMARY KEY COMMENT 'ID',
    `haoka_superior_api_id` bigint(20) NOT NULL COMMENT 'ID',
    `code`                  varchar(512)  NOT NULL DEFAULT '' COMMENT '标识',
    `name`                  varchar(512)  NOT NULL DEFAULT '' COMMENT '名字',
    `required`              bit(1)        NOT NULL DEFAULT b'0' COMMENT '是否必填',
    `remarks`               varchar(1024) NOT NULL DEFAULT '' COMMENT '说明',
    `input_type`            int(11) NOT NULL DEFAULT 1 COMMENT '输入类型', -- 枚举：haoka_superior_api_input_type：输入，单选，多选
    `input_select_values`   varchar(1024) NOT NULL DEFAULT '' COMMENT '选项(逗号,分割)',

    `dept_id`               bigint(20) COMMENT '部门ID',
    `creator`               varchar(64)   NOT NULL DEFAULT '' COMMENT '创建者',
    `create_time`           datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`               varchar(64)   NOT NULL DEFAULT '' COMMENT '更新者',
    `update_time`           datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`               bit(1)        NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`             bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
    UNIQUE (haoka_superior_api_id, name),
    UNIQUE (haoka_superior_api_id, code)
) COMMENT = '上游API接口SKU要求配置';
