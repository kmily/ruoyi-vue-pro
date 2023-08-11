package cn.iocoder.yudao.module.radar.controller.app.healthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: AppHealthStatisticsResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/119:21
 */

@Schema(description = "用户APP - 睡眠统计记录创建 Request VO")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class AppHealthStatisticsResVO extends HealthStatisticsBaseVO{

    @Schema(description = "详细信息")
    private List<HealthStatisticsRespVO> respVOList;

}
