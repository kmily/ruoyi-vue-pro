package cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 业务字典分类 Excel VO
 *
 * @author alex
 */
@Data
public class QgDictTreeExcelVO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("父id")
    private Long parentId;

    @ExcelProperty("显示顺序")
    private Integer sort;

    @ExcelProperty(value = "状态（0正常 1停用）", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private Date createTime;

}
