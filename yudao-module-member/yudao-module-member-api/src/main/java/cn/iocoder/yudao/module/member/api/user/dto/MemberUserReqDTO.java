package cn.iocoder.yudao.module.member.api.user.dto;

import lombok.Data;

/**
 * @author whycode
 * @title: MemberUserReqDTO
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2010:16
 */

@Data
public class MemberUserReqDTO {
    /**
     * 手机
     */
    private String mobile;
    /**
     * 加密后的密码
     *
     * 因为目前使用 {@link BCryptPasswordEncoder} 加密器，所以无需自己处理 salt 盐
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 真实名字
     */
    private String name;


    private Integer status;

}
