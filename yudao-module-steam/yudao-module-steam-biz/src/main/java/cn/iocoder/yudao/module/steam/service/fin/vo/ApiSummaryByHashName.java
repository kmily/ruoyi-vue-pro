package cn.iocoder.yudao.module.steam.service.fin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiSummaryByHashName implements Serializable {
    private String marketHashName;
    private Integer price;
}
