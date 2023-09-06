package cn.iocoder.yudao.module.member.controller.admin.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author whycode
 * @title: AdminIndexVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3110:10
 */
@Schema(description = "管理后台 - 首页统计 Response VO")
@Data
public class AdminIndexVO {

    private Long peoples;

    private Long messages;

    private Long devices;

    private Long order;

}
