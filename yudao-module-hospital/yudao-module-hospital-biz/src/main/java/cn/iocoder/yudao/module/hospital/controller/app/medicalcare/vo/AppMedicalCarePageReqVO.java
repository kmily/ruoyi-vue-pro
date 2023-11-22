package cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Set;

/**
 * @author whycode
 * @title: AppMedicalCarePageReqVO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2214:08
 */

@Schema(description = "用户 APP - 医护分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppMedicalCarePageReqVO extends PageParam {

    @Schema(description = "查询条件")
    private String name;

    @Schema(description = "需要资质")
    private Set<Long> aptitudes;

}
