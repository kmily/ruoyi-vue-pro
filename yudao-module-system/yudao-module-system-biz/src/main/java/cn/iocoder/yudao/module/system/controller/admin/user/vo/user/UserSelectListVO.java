package cn.iocoder.yudao.module.system.controller.admin.user.vo.user;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * UserSelectListVo类 :
 * 这里不展示用户名,只展示昵称,为了防止重名,展示ID、部门和手机号
 * <p>
 * 创建日期: 2022/6/13 11:13
 *
 * @author Timeless小帅
 * @version 1.0
 */
@Data
@ApiModel("管理后台 - 用户选择框 Response VO")
public class UserSelectListVO {

    @ApiModelProperty(value = "用户编号", required = true, example = "1")
    private Long id;

    private Dept dept;

    @ApiModelProperty(value = "用户昵称", required = true, example = "芋艿")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickname;


    @ApiModelProperty(value = "手机号码", example = "15601691300")
    @Mobile
    private String mobile;

    @ApiModel("部门")
    @Data
    public static class Dept {

        @ApiModelProperty(value = "部门编号", required = true, example = "1")
        private Long id;

        @ApiModelProperty(value = "部门名称", required = true, example = "研发部")
        private String name;

    }
}
