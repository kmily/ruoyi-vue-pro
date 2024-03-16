/*
 Navicat Premium Data Transfer

 Source Server         : mysql8_root
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : chaojiniu.top:23306
 Source Schema         : ruoyi-vue-pro

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 16/03/2024 15:45:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for im_conversation
-- ----------------------------
DROP TABLE IF EXISTS `im_conversation`;
CREATE TABLE `im_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '所属用户',
  `conversation_type` tinyint NOT NULL COMMENT '类型：1 单聊；2 群聊；4 通知会话（预留）',
  `target_id` bigint NOT NULL COMMENT '单聊时，用户编号；群聊时，群编号',
  `no` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话标志 单聊：s_{userId}_{targetId}，需要排序 userId 和 targetId 群聊：g_groupId',
  `pinned` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否置顶 0否 1是',
  `last_read_time` datetime DEFAULT NULL COMMENT '最后已读时间',
  `creator` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- ----------------------------
-- Table structure for im_group
-- ----------------------------
DROP TABLE IF EXISTS `im_group`;
CREATE TABLE `im_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `group_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群名字',
  `owner_id` bigint NOT NULL COMMENT '群主id',
  `head_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群头像',
  `head_image_thumb` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群头像缩略图',
  `notice` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群公告',
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群';

-- ----------------------------
-- Table structure for im_group_member
-- ----------------------------
DROP TABLE IF EXISTS `im_group_member`;
CREATE TABLE `im_group_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `group_id` bigint DEFAULT NULL COMMENT '群 id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `alias_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组内显示名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群成员';

-- ----------------------------
-- Table structure for im_inbox
-- ----------------------------
DROP TABLE IF EXISTS `im_inbox`;
CREATE TABLE `im_inbox` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `message_id` bigint NOT NULL COMMENT '消息编号',
  `sequence` bigint NOT NULL COMMENT '序号，按照 user 递增',
  `creator` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收件箱表';

-- ----------------------------
-- Table structure for im_message
-- ----------------------------
DROP TABLE IF EXISTS `im_message`;
CREATE TABLE `im_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `client_message_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户端消息编号 uuid，用于排重',
  `sender_id` bigint NOT NULL COMMENT '发送人编号',
  `receiver_id` bigint NOT NULL COMMENT '接收人编号',
  `sender_nickname` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送人昵称',
  `sender_avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发送人头像',
  `conversation_type` tinyint NOT NULL COMMENT '会话类型',
  `conversation_no` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话标志',
  `content_type` tinyint NOT NULL COMMENT '消息类型',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `send_from` tinyint NOT NULL COMMENT '消息来源 100-用户发送；200-系统发送（一般是通知）；',
  `message_status` tinyint DEFAULT NULL COMMENT '消息状态',
  `creator` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

SET FOREIGN_KEY_CHECKS = 1;
