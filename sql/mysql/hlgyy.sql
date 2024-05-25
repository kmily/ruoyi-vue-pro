create table hlgyy_member_user_ext
(
    id                     int comment '自增ID',
    appointment_date       date                                null comment '预约日期',
    appointment_time_range varchar(20)                         not null comment '预约时间段范围',
    user_id                bigint                              null comment '用户 ID',
    created_at             timestamp default current_timestamp null comment '创建时间',
    updated_at             timestamp default current_timestamp null on update CURRENT_TIMESTAMP,
    constraint `uniq_idx_user_id`
        primary key (user_id)
)
    comment '治疗用户扩展表' collate = utf8mb4_unicode_ci;

alter table hlgyy_member_user_ext
    modify id int auto_increment comment '自增ID';

