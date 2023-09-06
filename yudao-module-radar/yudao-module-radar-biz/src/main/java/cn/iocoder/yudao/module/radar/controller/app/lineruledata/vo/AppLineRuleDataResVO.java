package cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: AppLineRuleDataResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/69:27
 */
@Schema(description = "用户 APP - 离回家数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppLineRuleDataResVO {


    @Schema(description = "日期", example = "2023-09-01")
    private String date;

    @Schema(description = "详细信息")
    private AppLineRuleDataVO ruleDataVO;
}
