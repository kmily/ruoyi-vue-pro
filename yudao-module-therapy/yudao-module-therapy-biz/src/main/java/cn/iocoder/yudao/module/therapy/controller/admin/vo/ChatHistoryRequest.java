package cn.iocoder.yudao.module.therapy.controller.admin.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author:lidongw_1
 * @Date 2024/6/7
 * @Description: 自动化思维识别聊天记录
 **/

@Data
public class ChatHistoryRequest extends PageBaseRequest {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Min(value = 1,message = "用户编号不能为空")
    private Long userId;


}
