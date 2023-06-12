package cn.iocoder.yudao.module.oa.controller.admin.opportunityfollowlog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 商机-跟进日志创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OpportunityFollowLogCreateReqVO extends OpportunityFollowLogBaseVO {

}
