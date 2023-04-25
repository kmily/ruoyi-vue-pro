package cn.iocoder.yudao.module.system.controller.admin.signreq.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
* 注册申请 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
*/
@Data
public class SignReqBaseVO {

    @Schema(description = "备注信息", example = "你猜")
    private String remark;

    @Schema(description = "处理状态 1.未绑定 2.已绑定 3.已拒绝", example = "1")
    private String status;

    @Schema(description = "openId", example = "18895")
    private String openId;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "姓名", example = "张三")
    private String userName;

    @Schema(description = "性别(0男 1女 2未知）")
    private String sex;

}
