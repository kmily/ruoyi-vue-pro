package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author whycode
 * @title: CareFavoritePageReqVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2810:11
 */

@Schema(description = "用户 APP - 我的收藏 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CareFavoritePageReqVO extends PageParam {

    @Schema(description = "护理专家姓名")
    private String name;

    @Schema(description = "会员编号", deprecated = true)
    private Long userId;

}
