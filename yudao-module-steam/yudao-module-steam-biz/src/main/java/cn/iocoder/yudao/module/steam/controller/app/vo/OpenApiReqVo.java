package cn.iocoder.yudao.module.steam.controller.app.vo;

import lombok.Data;

@Data
public class OpenApiReqVo {
    private String method;
    private String userName;
    private String data;
}
