package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author whycode
 * @title: Source
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/410:36
 */

@Data
public class Source {

    @JSONField(name = "IDType")
    private Long iDType;

    @JSONField(name = "ID")
    private String id;

    @JSONField(name = "SourceType")
    private String sourceType;

}
