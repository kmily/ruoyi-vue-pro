package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

@Data
public class LoginRespVO {
    private Integer code;
    private String msg;
    private LoginBackVO data;

    @Data
    public static class LoginBackVO{
        private String account;
        private String merchantName;
        private String merchantBalance;
        private String tradeToken;
    }
}
