--liquibase formatted sql

--changeset sumht:insert_system_login_log_master_202207111039 context:dev,local,pro
--comment 日志测试添加 2022-07-11 10:39
INSERT INTO `ruoyi-vue-pro`.`system_login_log` (`id`, `log_type`, `trace_id`, `user_id`, `user_type`, `username`, `result`, `user_ip`, `user_agent`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES (99999, 100, '', 1, 2, 'admin', 0, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36', NULL, '2022-07-11 10:01:58', NULL, '2022-07-11 10:01:58', b'0', 1);


--changeset sumht:insert_system_login_log_master_202207111048 context:pro
--comment 日志测试添加 2022-07-11 10:48
INSERT INTO `ruoyi-vue-pro`.`system_login_log` (`id`, `log_type`, `trace_id`, `user_id`, `user_type`, `username`, `result`, `user_ip`, `user_agent`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES (99998, 100, '', 1, 2, 'admin', 0, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36', NULL, '2022-07-11 10:01:58', NULL, '2022-07-11 10:01:58', b'0', 1);
