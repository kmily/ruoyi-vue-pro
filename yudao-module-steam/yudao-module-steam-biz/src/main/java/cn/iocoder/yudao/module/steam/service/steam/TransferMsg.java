package cn.iocoder.yudao.module.steam.service.steam;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class TransferMsg implements Serializable {
    private String tradeofferid;
    private String msg;
}
