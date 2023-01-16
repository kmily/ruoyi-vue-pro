package cn.iocoder.yudao.module.scan.controller.app.users.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("扫描APP - 扫描用户创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppUsersCreateReqVO extends AppUsersBaseVO {

}
