package cn.iocoder.yudao.module.steam.service.fin.v5.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class V5callBackResult implements Serializable {


    private String notifyMsgNo;

    private CallBackInfoVO callBackInfo;

    private String sign;
}

