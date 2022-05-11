package cn.iocoder.yudao.module.system.controller.admin.area.vo;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 行政区域 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AreaExcelVO {

    @ExcelProperty("自增主键id")
    private Long id;

    @ExcelProperty("数据版本")
    private String dataVersion;

    @ExcelProperty("省直辖市编码")
    private String provinceCode;

    @ExcelProperty("省直辖市")
    private String province;

    @ExcelProperty("市编码")
    private String cityCode;

    @ExcelProperty("市")
    private String city;

    @ExcelProperty("区县编码")
    private String countyCode;

    @ExcelProperty("区县")
    private String county;

    @ExcelProperty("乡镇编码")
    private String townCode;

    @ExcelProperty("乡镇街道")
    private String town;

    @ExcelProperty("村社区编码")
    private String villageCode;

    @ExcelProperty("村,社区")
    private String village;

    @ExcelProperty("经度")
    private BigDecimal longitude;

    @ExcelProperty("纬度")
    private BigDecimal latitude;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("使用标识 1使用 0不使用")
    private Integer useFlag;

}
