package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 首页配置 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class AppHomePageBaseVO {

    @Schema(description = "用户ID", required = true, example = "10081")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "家庭ID", required = true, example = "23700")
    @NotNull(message = "家庭ID不能为空")
    private Long familyId;

//    @Schema(description = "页面名称", required = true, example = "王五")
//    @NotNull(message = "房间名称不能为空")
    private String name;

    @Schema(description = "数据类型 0-睡眠,1-如厕,2-跌倒,3-离/回家", example = "1")
    private Byte type;

    @Schema(description = "状态（0正常 1停用）", required = true, example = "2")
    @NotNull(message = "状态（0正常 1停用）不能为空")
    private Byte status;

    @Schema(description = "排序")
    private Byte sort;

    @Schema(description = "模式，0-系统, 1-自定义")
    private Byte mold;

    @Schema(description = "绑定设备, 多个','隔开", example = "11,22")
    private String devices;

}
