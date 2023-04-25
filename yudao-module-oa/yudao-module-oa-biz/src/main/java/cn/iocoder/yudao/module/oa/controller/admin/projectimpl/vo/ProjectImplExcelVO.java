package cn.iocoder.yudao.module.oa.controller.admin.projectimpl.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


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

    @ExcelProperty(value = "实施范围", converter = DictConvert.class)
    @DictFormat("oa_contract_impl_scope") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String implType;

    @ExcelProperty("实施内容")
    private String implContent;

    @ExcelProperty("备注")
    private String remark;

}
