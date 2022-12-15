package cn.iocoder.yudao.module.infra.controller.admin.codegen.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "管理后台 - 代码生成预览 Response VO", description ="注意，每个文件都是一个该对象")
@Data
public class CodegenPreviewRespVO {

    @Schema(title = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "java/cn/iocoder/yudao/adminserver/modules/system/controller/test/SysTestDemoController.java")
    private String filePath;

    @Schema(title = "代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "Hello World")
    private String code;

}
