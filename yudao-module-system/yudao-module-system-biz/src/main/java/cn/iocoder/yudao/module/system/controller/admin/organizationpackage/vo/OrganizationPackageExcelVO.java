package cn.iocoder.yudao.module.system.controller.admin.organizationpackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 机构套餐 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class OrganizationPackageExcelVO {

    @ExcelProperty("套餐编号")
    private Long id;

    @ExcelProperty("套餐名")
    private String name;

    @ExcelProperty("租户状态（0正常 1停用）")
    private Byte status;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("关联的菜单编号")
    private Set<Long> menuIds;

    @ExcelProperty("是否为默认")
    private Boolean isDefault;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
