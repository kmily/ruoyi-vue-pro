package cn.iocoder.yudao.module.steam.controller.app.binduser.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 -  用户解绑")
@Data
@ToString(callSuper = true)
public class AppUnBindUserReqVO {
    @Schema(description = "bindId", example = "123456")
    @NotNull(message = "bindId不能为空")
    private Long bindId;

}