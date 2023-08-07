package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;


/**
 * 首页配置 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class AppHomePageExcelVO {

    //@ExcelProperty("自增编号")
    private Long id;

    //@ExcelProperty("用户ID")
    private Long userId;

    //@ExcelProperty("家庭ID")
    private Long familyId;

    //@ExcelProperty("房间名称")
    private String name;

    //@ExcelProperty("数据类型 0-睡眠,1-如厕,2-跌倒,3-离/回家")
    private Byte type;

    //@ExcelProperty("状态（0正常 1停用）")
    private Byte status;

    //@ExcelProperty("排序")
    private Byte sort;

    //@ExcelProperty("模式，0-系统, 1-自定义")
    private Byte mold;

    //@ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
