package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author whycode
 * @title: UserStatisticsDetailVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3013:42
 */
@Schema(description = "管理后台 - 会员统计明细 Response VO")
@Data
@ToString(callSuper = true)
public class UserStatisticsDetailVO {

    private String month;

    private Long quantity;

}
