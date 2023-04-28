package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 工程实施列 Excel VO
 *
 * @author 管理员
 */
@Data
public class ProjectImplExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("合同id")
    private Long contractId;

    @ExcelProperty("实施内容")
    private String implContent;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("实施范围")
    private String implScope;

}
