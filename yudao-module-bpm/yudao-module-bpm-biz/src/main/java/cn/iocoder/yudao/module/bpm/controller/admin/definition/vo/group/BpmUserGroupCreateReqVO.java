package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.group;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ApiModel("管理后台 - 用户组创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupCreateReqVO extends BpmUserGroupBaseVO {

}
