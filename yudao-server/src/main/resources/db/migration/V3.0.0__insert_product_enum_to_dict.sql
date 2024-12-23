-- '身份证号码验证', -- 枚举 id_card_num_verify:不需要，需要，国政同校验

INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('身份证号码验证', 'id_card_num_verify', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '不需要', '1', 'id_card_num_verify', 0, '', '', NULL, '', '', b'0'),
       (2, '需要', '2', 'id_card_num_verify', 0, '', '', NULL, '', '', b'0'),
       (3, '国政同校验', '3', 'id_card_num_verify', 0, '', '', NULL, '', '', b'0');

-- '身份证图片验证' -- 枚举 id_card_img_verify:不需要，需要，需要但不审核，智能校验
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('身份证图片验证', 'id_card_img_verify', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '不需要', '1', 'id_card_img_verify', 0, '', '', NULL, '', '', b'0'),
       (2, '需要', '2', 'id_card_img_verify', 0, '', '', NULL, '', '', b'0'),
       (3, '需要但不审核', '3', 'id_card_img_verify', 0, '', '', NULL, '', '', b'0'),
       (4, '智能校验', '4', 'id_card_img_verify', 0, '', '', NULL, '', '', b'0');

-- '运营商' -- 枚举 haoka_operator:中国移动，中国联通，中国电信，中国广电，虚拟运营商
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('运营商', 'haoka_operator', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '中国移动', '1', 'haoka_operator', 0, '', '', NULL, '', '', b'0'),
       (2, '中国联通', '2', 'haoka_operator', 0, '', '', NULL, '', '', b'0'),
       (3, '中国电信', '3', 'haoka_operator', 0, '', '', NULL, '', '', b'0'),
       (4, '中国广电', '4', 'haoka_operator', 0, '', '', NULL, '', '', b'0'),
       (5, '虚拟运营商', '5', 'haoka_operator', 0, '', '', NULL, '', '', b'0');

-- '异常订单处理方式' -- 枚举 abnormal_order_handle_method：异常订单人工处理，异常订单自动处理
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('异常订单处理方式', 'abnormal_order_handle_method', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '异常订单人工处理', '1', 'abnormal_order_handle_method', 0, '', '', NULL, '', '', b'0'),
       (2, '异常订单自动处理', '2', 'abnormal_order_handle_method', 0, '', '', NULL, '', '', b'0');

-- '接口状态' -- 枚举 haoka_superior_api_status: 开发中,完成
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('接口状态', 'haoka_superior_api_status', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '开发中', '1', 'haoka_superior_api_status', 0, '', '', NULL, '', '', b'0'),
       (2, '完成', '2', 'haoka_superior_api_status', 0, '', '', NULL, '', '', b'0');

-- '配置输入类型' -- 枚举 haoka_superior_api_input_type：输入，单选，多选
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `updater`, `deleted`)
VALUES ('配置输入类型', 'haoka_superior_api_input_type', 0, '', '1', '1', b'0');

INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`,
                                `css_class`, `remark`, `creator`, `updater`,
                                `deleted`)
VALUES (1, '输入', '1', 'haoka_superior_api_input_type', 0, '', '', NULL, '', '', b'0'),
       (2, '单选', '2', 'haoka_superior_api_input_type', 0, '', '', NULL, '', '', b'0'),
       (3, '多选', '3', 'haoka_superior_api_input_type', 0, '', '', NULL, '', '', b'0');
