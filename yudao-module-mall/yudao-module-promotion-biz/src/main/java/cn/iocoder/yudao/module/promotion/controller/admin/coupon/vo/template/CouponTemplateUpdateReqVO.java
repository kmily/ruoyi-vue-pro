package cn.iocoder.yudao.module.promotion.controller.admin.coupon.vo.template;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(title = "管理后台 - 优惠劵模板更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CouponTemplateUpdateReqVO extends CouponTemplateBaseVO {

    @Schema(title  = "模板编号", required = true, example = "1024")
    @NotNull(message = "模板编号不能为空")
    private Long id;

}
