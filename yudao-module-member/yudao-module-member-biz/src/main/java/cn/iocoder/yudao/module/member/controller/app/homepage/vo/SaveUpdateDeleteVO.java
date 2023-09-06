package cn.iocoder.yudao.module.member.controller.app.homepage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * @author whycode
 * @title: SaveUpdateDeleteVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3017:21
 */

@Schema(description = "用户 APP - 首页配置更新 Request VO")
@Data
public class SaveUpdateDeleteVO {
    @Schema(description = "更新数据项")
    @NotNull(message = "更新数据项不能为空")
    @Size(min = 1)
    List<AppHomePageUpdateReqVO> updateReqVOS;

    @Schema(description = "删除ID", required = true, example = "5597")
    private Set<Long> delIds;

}
