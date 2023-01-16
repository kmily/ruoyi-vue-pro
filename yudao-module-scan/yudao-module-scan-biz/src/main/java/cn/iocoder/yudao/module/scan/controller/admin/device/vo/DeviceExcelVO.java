package cn.iocoder.yudao.module.scan.controller.admin.device.vo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import io.swagger.annotations.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 设备 Excel VO
 *
 * @author lyz
 */
@Data
public class DeviceExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("设备名称")
    private String name;

    @ExcelProperty("设备编号")
    private String code;

    @ExcelProperty("联系人")
    private String contact;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("激活序号")
    private Integer serialNo;

    @ExcelProperty("省名称")
    private String provinceName;

    @ExcelProperty("省编号")
    private String provinceCode;

    @ExcelProperty("市名称")
    private String cityName;

    @ExcelProperty("市编号")
    private String cityCode;

    @ExcelProperty("区名称")
    private String areaName;

    @ExcelProperty("区编号")
    private String areaCode;

    @ExcelProperty("详细地址")
    private String address;

    @ExcelProperty("设备管理密码")
    private String password;

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("门店名称")
    private String storeName;

}
