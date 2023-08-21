package cn.iocoder.yudao.module.member.controller.admin.deviceuser.vo;

import cn.iocoder.yudao.module.member.controller.app.deviceuser.vo.AppDeviceUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author whycode
 * @title: AllFamilyDeviceVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1710:58
 */
@Schema(description = "后台管理 - 家庭设备 Response VO")
@Data
public class AllFamilyDeviceVO {

    @Schema(description = "家庭ID")
    private Long id;

    @Schema(description = "家庭名称")
    private String name;

    @Schema(description = "家庭对应设备")
    private List<AppDeviceUserVO> deviceUserVOS;

}
