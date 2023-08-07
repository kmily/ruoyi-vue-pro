package cn.iocoder.yudao.module.radar.controller.admin.banner.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 雷达模块banner图 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class BannerBaseVO {

    @Schema(description = "标题", required = true)
    @NotNull(message = "标题不能为空")
    private String title;

    @Schema(description = "跳转连接", example = "https://www.iocoder.cn")
    private String url;

    @Schema(description = "图片连接", required = true, example = "https://www.iocoder.cn")
    @NotNull(message = "图片连接不能为空")
    private String picUrl;

    @Schema(description = "排序")
    private Byte sort;

    @Schema(description = "部门状态（0正常 1停用）", required = true, example = "2")
    @NotNull(message = "部门状态（0正常 1停用）不能为空")
    private Byte status;

    @Schema(description = "备注", example = "随便")
    private String memo;

}
