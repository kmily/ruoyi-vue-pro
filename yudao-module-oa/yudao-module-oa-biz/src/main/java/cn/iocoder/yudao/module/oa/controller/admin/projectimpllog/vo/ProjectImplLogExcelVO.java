package cn.iocoder.yudao.module.oa.controller.admin.projectimpllog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * 工程日志列表 Excel VO
 *
 * @author 管理员
 */
@Data
public class ProjectImplLogExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("内容")
    private String logContent;

    @ExcelProperty("合同id")
    private Long contractId;

    @ExcelProperty(value = "工程进度", converter = DictConvert.class)
    @DictFormat("oa_contract_impl_state") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private String implStatus;

    @ExcelProperty("创建者")
    private String createBy;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
