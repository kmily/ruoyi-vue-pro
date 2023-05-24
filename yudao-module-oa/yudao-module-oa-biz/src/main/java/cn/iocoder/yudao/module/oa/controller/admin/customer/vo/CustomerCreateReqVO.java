package cn.iocoder.yudao.module.oa.controller.admin.customer.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 客户管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerCreateReqVO extends CustomerBaseVO {

}
