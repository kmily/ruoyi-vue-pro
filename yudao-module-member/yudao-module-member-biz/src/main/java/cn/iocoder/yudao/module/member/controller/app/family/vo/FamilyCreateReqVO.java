package cn.iocoder.yudao.module.member.controller.app.family.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 用户家庭创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FamilyCreateReqVO extends FamilyBaseVO {

}
