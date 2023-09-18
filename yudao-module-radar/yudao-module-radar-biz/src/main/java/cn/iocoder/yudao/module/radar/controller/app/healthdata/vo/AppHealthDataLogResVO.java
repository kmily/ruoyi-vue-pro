package cn.iocoder.yudao.module.radar.controller.app.healthdata.vo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author whycode
 * @title: AppHealthDetalLogVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/1513:51
 */

@Schema(description = "用户 APP - 体征检测数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppHealthDataLogResVO {

    @Schema(description = "呼吸数据", required = true)
    private Long breath;

    @Schema(description = "心跳数据", required = true)
    private Long heart;

    @Schema(description = "分页数据")
    private PageResult<AppHealthDataLogItemVO> pageResult;
}
