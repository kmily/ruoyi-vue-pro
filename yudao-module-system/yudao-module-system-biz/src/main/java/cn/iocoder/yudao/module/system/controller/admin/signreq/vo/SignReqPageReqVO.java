package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 注册申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SignReqPageReqVO extends PageParam {

    @Schema(description = "openId", example = "18895")
    private String openId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "姓名", example = "张三")
    private String userName;

}
