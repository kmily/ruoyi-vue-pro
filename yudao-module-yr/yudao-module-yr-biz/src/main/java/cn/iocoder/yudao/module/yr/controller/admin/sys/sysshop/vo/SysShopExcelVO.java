package cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo;

import lombok.*;
import java.util.*;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 店面 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class SysShopExcelVO {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("店面名称")
    private String shopName;

    @ExcelProperty("店面城市")
    private String shopCity;

    @ExcelProperty("店面地址")
    private String shopAddress;

    @ExcelProperty("门牌号")
    private String shopAddressNum;

    @ExcelProperty("店面分组")
    private String shopGroup;

    @ExcelProperty("状态（0正常 1停用）")
    private Integer status;

    @ExcelProperty("创建时间")
    private Date createTime;

}
