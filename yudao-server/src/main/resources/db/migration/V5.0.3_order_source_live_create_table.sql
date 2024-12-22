-- 将该建表 SQL 语句，添加到 yudao-module-haoka-biz 模块的 test/resources/sql/create_tables.sql 文件里
CREATE TABLE IF NOT EXISTS "haoka_order_source_live" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "author_id" bigint NOT NULL,
    "source_id" bigint NOT NULL,
    "source" varchar,
    "shop_id" bigint,
    "user_id" bigint,
    "dept_id" bigint,
    "team_name" varchar,
    "nick_name" varchar,
    "creator" varchar DEFAULT '',
    "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar DEFAULT '',
    "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    "tenant_id" bigint NOT NULL DEFAULT 0,
    PRIMARY KEY ("id")
) COMMENT '订单来源-直播间配置';

-- 将该删表 SQL 语句，添加到 yudao-module-haoka-biz 模块的 test/resources/sql/clean.sql 文件里
DELETE FROM "haoka_order_source_live";