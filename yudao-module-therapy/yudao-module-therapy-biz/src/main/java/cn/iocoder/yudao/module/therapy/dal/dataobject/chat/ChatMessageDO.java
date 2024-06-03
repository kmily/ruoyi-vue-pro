package cn.iocoder.yudao.module.therapy.dal.dataobject.chat;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:lidongw_1
 * @Date 2024/5/30
 * @Description: 聊天消息 DO
 **/

@TableName("hlgyy_ai_chat_message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDO {

//    CREATE TABLE `hlgyy_ai_chat_message` (
//            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto id',
//            `conversation_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
//            `send_user_id` bigint(20) NOT NULL COMMENT '发送人 ID',
//            `content` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话内容',
//            `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
//            `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
//            `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
//    PRIMARY KEY (`id`) USING BTREE,
//    KEY `idx_create_time` (`created_at`) USING BTREE
//) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息'

    @TableId
    private Long id;
    private String conversationId;
    private Long sendUserId;
    private String sendUserType;
    private String content;
    private Long tenantId;
    private Boolean deleted;
    private Long receiveUserId;

}
