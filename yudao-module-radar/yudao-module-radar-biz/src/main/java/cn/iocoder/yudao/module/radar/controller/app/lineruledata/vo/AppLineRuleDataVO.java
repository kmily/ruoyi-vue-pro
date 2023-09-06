package cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author whycode
 * @title: AppLineRuleDataVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/69:41
 */

@Schema(description = "用户 APP - 离回家数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppLineRuleDataVO {

    @Schema(description = "最近一次离家时间")
    private Long lastGoOut;

    @Schema(description = "离/回家详细信息")
    private List<AppLineRuleDataInfoVO> details;

}
