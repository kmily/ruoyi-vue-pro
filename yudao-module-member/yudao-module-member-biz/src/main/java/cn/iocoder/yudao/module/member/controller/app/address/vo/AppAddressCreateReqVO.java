package cn.iocoder.yudao.module.member.controller.app.address.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("用户 APP - 用户收件地址创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppAddressCreateReqVO extends AppAddressBaseVO {

}
