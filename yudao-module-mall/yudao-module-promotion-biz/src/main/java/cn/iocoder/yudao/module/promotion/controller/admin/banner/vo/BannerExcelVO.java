package cn.iocoder.yudao.module.promotion.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;


/**
 * Banner Excel VO
 *
 * @author 芋道源码
 */
@Data
public class BannerExcelVO {

    @ExcelProperty("Banner 编号")
    private Long id;

    @ExcelProperty("Banner 标题")
    private String title;

    @ExcelProperty("图片 URL")
    private String picUrl;

    @ExcelProperty("跳转地址")
    private String url;

    @ExcelProperty(value = "活动状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Byte status;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty(value = "位置", converter = DictConvert.class)
    @DictFormat("promotion_banner_position") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Byte position;

    @ExcelProperty("描述")
    private String memo;

    @ExcelProperty("Banner 点击次数")
    private Integer browseCount;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
