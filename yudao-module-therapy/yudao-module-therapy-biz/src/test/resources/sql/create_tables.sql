-- 创建表
CREATE TABLE hlgyy_member_user_ext
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    appointment_date       DATE,
    appointment_time_range VARCHAR(20) NOT NULL,
    user_id                BIGINT,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
