package cn.iocoder.yudao.module.member.controller.admin.group.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商城会员分组 Excel VO
 *
 * @author ChikaWang
 */
@Data
public class MemberGroupExcelVO {

    @ExcelProperty("编号")
    private Long id;

    @ExcelProperty("分组名称")
    private String name;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
