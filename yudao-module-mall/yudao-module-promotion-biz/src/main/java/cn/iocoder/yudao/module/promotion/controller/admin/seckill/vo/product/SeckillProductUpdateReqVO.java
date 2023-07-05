package cn.iocoder.yudao.module.promotion.controller.admin.seckill.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 管理后台 - 秒杀参与商品更新 Request VO
 *
 * @author HUIHUI
 */
@Schema(description = "管理后台 - 秒杀参与商品更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SeckillProductUpdateReqVO extends SeckillProductBaseVO {

}
