package cn.iocoder.yudao.module.steam.service.alipay.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateIsvVo implements Serializable {
    private String isv_biz_id;
    private String isv_return_url;
}
