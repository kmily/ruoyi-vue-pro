package cn.iocoder.yudao.module.member.controller.app.user.vo;

import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/6/21
 * @Description: 热线
 **/
@Data
public class HotlineRespVO {

    //全国热线
    private String nationalHotline;
    //城市热线
    private String cityHotline;
}
