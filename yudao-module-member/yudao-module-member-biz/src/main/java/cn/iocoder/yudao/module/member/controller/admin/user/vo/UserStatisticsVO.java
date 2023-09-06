package cn.iocoder.yudao.module.member.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author whycode
 * @title: UserStatisticsVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/3013:40
 */
@Schema(description = "管理后台 - 会员统计 Response VO")
@Data
@ToString(callSuper = true)
public class UserStatisticsVO {

    private Long total;

    private Long month;

    private Long sevenDays;

    private List<UserStatisticsDetailVO> detail;

}
