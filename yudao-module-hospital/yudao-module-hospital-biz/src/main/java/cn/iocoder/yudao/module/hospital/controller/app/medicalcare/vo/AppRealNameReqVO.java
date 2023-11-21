package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author whycode
 * @title: AppRealNameReqVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2115:54
 */

@Schema(description = "APP - 医护管理更新 Request VO")
@Data
@ToString(callSuper = true)
public class AppRealNameReqVO {

    @Schema(description = "医护编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "医护编号不能为空")
    private Long id;

    @Schema(description = "正面", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "身份证正面照不能为空")
    private String front;

    @Schema(description = "反面", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "身份证反面照不能为空")
    private String back;

    @Schema(description = "手持", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "身份证手持照不能为空")
    private String hand;


    public List<String> picPaths(){
        return Arrays.asList(getFront(), getBack(), getHand());
    }
}
