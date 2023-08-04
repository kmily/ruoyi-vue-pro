package cn.iocoder.yudao.module.member.controller.app.family.vo;

import lombok.*;

import java.time.LocalDateTime;


/**
 * 用户家庭 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class FamilyExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

   // @ExcelProperty("用户ID")
    private Long userId;

   // @ExcelProperty("家庭名称")
    private String name;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
