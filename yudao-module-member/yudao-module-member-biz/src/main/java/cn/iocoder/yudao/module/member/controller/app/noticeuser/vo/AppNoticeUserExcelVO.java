package cn.iocoder.yudao.module.member.controller.app.noticeuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 用户消息关联 Excel VO
 *
 * @author 和尘同光
 */
@Data
public class AppNoticeUserExcelVO {

    private Long id;

    private Long noticeId;

    private Long userId;

    private LocalDateTime readTime;

    private Byte status;

    private LocalDateTime createTime;

}
