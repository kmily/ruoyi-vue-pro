package cn.iocoder.yudao.module.member.api.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemberUserExtDTO {
    private Date appointmentDate;
    private Integer appointmentTimeRange;
    private Long userId;
    /**
     * ab测试组
     * 字典类型:user_test_group
     */
    private Integer testGroup;
}
