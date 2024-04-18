package cn.iocoder.yudao.module.steam.service.fin.c5.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderCancelRes implements Serializable {
    private boolean success;
    private Object data;
    private int errorCode;
    private String errorMsg;
    private Object errorData;

}
