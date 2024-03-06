package cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kotlin.jvm.internal.SerializedIr;
import lombok.Data;

import java.io.Serializable;

@Data
public class YouyouTemplatedownloadRespVO implements Serializable {

    @Schema(description = "code", example = "200")
    private String code;
    @Schema(description = "msg", example = "成功")
    private String msg;
    @Schema(description = "时间", example = "1669626806902")
    private String timestamp;
    @Schema(description = "下载链接", example = "https://youpin898-images.oss-cn-shenzhen.aliyuncs.com/youpin/commodity_template/6yN%2Bg%3")
    private String data;
}
