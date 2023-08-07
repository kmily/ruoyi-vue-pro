package cn.iocoder.yudao.module.radar.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 雷达模块banner图 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class BannerExcelVO {

    @ExcelProperty("自增编号")
    private Long id;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("跳转连接")
    private String url;

    @ExcelProperty("图片连接")
    private String picUrl;

    @ExcelProperty("排序")
    private Byte sort;

    @ExcelProperty("部门状态（0正常 1停用）")
    private Byte status;

    @ExcelProperty("备注")
    private String memo;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
