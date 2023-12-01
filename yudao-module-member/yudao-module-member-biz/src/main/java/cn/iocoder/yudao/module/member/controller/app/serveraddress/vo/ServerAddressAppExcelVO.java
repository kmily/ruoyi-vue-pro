package cn.iocoder.yudao.module.member.controller.app.serveraddress.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务地址 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class ServerAddressAppExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("用户编号")
    private Long userId;

    @ExcelProperty("地区编码")
    private Long areaId;

    @ExcelProperty("省市县/区")
    private String address;

    @ExcelProperty("收件详细地址")
    private String detailAddress;

    @ExcelProperty("是否默认")
    private Boolean defaultStatus;

    @ExcelProperty("经纬度")
    private String coordinate;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
