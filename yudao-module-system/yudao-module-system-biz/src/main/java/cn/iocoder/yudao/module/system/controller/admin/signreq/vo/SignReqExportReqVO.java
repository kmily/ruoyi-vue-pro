package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 注册申请 Excel 导出 Request VO，参数和 SignReqPageReqVO 是一致的")
@Data
public class SignReqExportReqVO {

    @Schema(description = "openId", example = "18895")
    private String openId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "姓名", example = "张三")
    private String userName;

}
