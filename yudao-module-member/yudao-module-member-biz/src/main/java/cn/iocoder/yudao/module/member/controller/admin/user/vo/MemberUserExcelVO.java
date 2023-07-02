package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import lombok.*;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 用户 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class MemberUserExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("用户昵称")
    private String nickname;

    @ExcelProperty("用户真实名称")
    private String realName;

    @ExcelProperty("头像")
    private String avatar;

    @ExcelProperty("状态")
    private Byte status;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("注册 IP")
    private String registerIp;

    @ExcelProperty("最后登录IP")
    private String loginIp;

    @ExcelProperty("支付密码")
    private String payPassword;

    @ExcelProperty("最后登录时间")
    private LocalDateTime loginDate;

    @ExcelProperty("区域id")
    private Long areaId;

    @ExcelProperty("用户组id")
    private Long userGroupId;

    @ExcelProperty("积分")
    private Long point;

    @ExcelProperty("推荐人")
    private Long referrer;

    @ExcelProperty("性别")
    private Byte gender;

    @ExcelProperty("标签")
    private Long labelId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
