package cn.iocoder.yudao.module.radar.bean.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by l09655 on 2022/7/28.
 * 注册成功的设备实体类
 */
@Data
@Getter
@Setter
public class Device {

    private String vendor;

    private String deviceType;

    private String deviceCode;

    private String nonce;

    private String sign;

    private String cnonce;

    private String resign;

    private String sessionID;

    private String ip;

    /**
     * 当前设备订阅周期
     */
    private long subDuration;

    /**
     * 当前设备订阅ID
     */
    private long id = 0;

    private int type;
    
}
