package cn.iocoder.yudao.module.member.api.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemberUserExtDTO {
    private Date appointmentDate;
    private Integer appointmentTimeRange;
    private Long userId;
}