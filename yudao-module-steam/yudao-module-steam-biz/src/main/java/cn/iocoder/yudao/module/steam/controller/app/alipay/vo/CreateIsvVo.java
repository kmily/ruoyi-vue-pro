package cn.iocoder.yudao.module.steam.controller.app.alipay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@Data
public class CreateIsvVo implements Serializable {
    /**
     * isv业务系统的申请单id
     */
    @JsonProperty("isv_biz_id")
    private String isvBizId;
    /**
     * isv平台的回跳地址。商户从伙伴平台跳转过来签约提交申请后，在结果页可以回跳到合作伙伴指定的页面
     */
    @JsonProperty("isv_return_url")
    private String isvReturnUrl;
}
