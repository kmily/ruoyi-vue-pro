package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: BootUpResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/119:36
 */
@Schema(description = "管理后台 - 用户启动数据 Response VO")
@Data
@ToString(callSuper = true)
public class BootUpResVO {

    @Schema(description = "启动总次数")
    private Integer total;

    @Schema(description = "启动一次的次数")
    private Integer once;

    @Schema(description = "占用百分比", example = "12.1%")
    private String percentage;

    @Schema(description = "每天详情")
    private List<BootUpListResVO> resVOList;



}
