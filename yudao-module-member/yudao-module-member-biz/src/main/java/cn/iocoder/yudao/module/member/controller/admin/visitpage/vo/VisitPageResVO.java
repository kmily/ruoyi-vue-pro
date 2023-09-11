package cn.iocoder.yudao.module.member.controller.admin.visitpage.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author whycode
 * @title: VisitPageResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1115:37
 */
@Schema(description = "管理后台 - 页面访问数据 Response VO")
@Data
@ToString(callSuper = true)
public class VisitPageResVO {

    @ExcelProperty("页面名称")
    @Schema(description = "页面名称", example = "首页")
    private String pageName;

    @ExcelProperty("访问次数")
    @Schema(description = "访问次数", example = "12")
    private Integer count;

    @ExcelProperty("访问次数占比")
    @Schema(description = "访问次数占比", example = "12.1%")
    private String percentage;

    @ExcelProperty("平均访问时长")
    @Schema(description = "平均访问时长", example = "12秒")
    private String average;


}
