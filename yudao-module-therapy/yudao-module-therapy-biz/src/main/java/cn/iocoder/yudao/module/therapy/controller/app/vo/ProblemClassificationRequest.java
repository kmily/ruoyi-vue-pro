package cn.iocoder.yudao.module.therapy.controller.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/5/28
 * @Description: C端 - AI 问题分类
 **/
@Schema(description = "C端 - AI 智能问题分类")
@Data
public class ProblemClassificationRequest {

        @Schema(description = "问题", required = true, example = "你好")
        private String question;
}
