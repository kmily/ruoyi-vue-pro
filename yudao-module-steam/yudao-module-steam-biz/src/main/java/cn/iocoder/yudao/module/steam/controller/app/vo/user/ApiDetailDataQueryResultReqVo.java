package cn.iocoder.yudao.module.steam.controller.app.vo.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDetailDataQueryResultReqVo implements Serializable {
    @Schema(description = "申请标识", example = "2a187e2711c0418c80f3acb8fd4ef04b")
    @NotNull(message = "申请标识不能为空")
    private String applyCode;
}
