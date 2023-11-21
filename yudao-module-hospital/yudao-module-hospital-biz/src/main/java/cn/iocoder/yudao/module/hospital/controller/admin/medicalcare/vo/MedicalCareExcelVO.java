package cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 医护管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class MedicalCareExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("组织编号")
    private Long orgId;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("身份证号")
    private String idCard;

    @ExcelProperty("身份证图片[正,反]")
    private String cardPath;

    @ExcelProperty("联系方式")
    private String mobile;

    @ExcelProperty("出生日期")
    private LocalDateTime birthday;

    @ExcelProperty("用户性别")
    private Byte sex;

    @ExcelProperty("紧急联系人")
    private String critical;

    @ExcelProperty("是否实名 NO-未实名，YES-实名")
    private String realname;

    @ExcelProperty("职称")
    private Byte title;

    @ExcelProperty("所属机构")
    private String organization;

    @ExcelProperty("服务人数")
    private Integer quantity;

    @ExcelProperty("服务时长，单位分钟")
    private Integer timeLength;

    @ExcelProperty("从业时间")
    private LocalDateTime practiceTime;

    @ExcelProperty("主要擅长")
    private String genius;

    @ExcelProperty("是否完善 NO-未实名，YES-实名")
    private String perfect;

    @ExcelProperty("是否资质认证 NO-未实名，YES-实名")
    private String aptitude;

    @ExcelProperty("个人简介")
    private String introduction;

    @ExcelProperty("最终审核状态")
    private String status;

    @ExcelProperty("最终审核人")
    private String checkName;

    @ExcelProperty("最终审核时间")
    private LocalDateTime checkTime;

    @ExcelProperty("审核意见")
    private String opinion;

    @ExcelProperty("是否是店主")
    private Boolean shopkeeper;

    @ExcelProperty("是否是超级管理员 超级管理员/普通管理员")
    private Boolean isSuper;

    @ExcelProperty("是否可以提现")
    private Byte cashOut;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
