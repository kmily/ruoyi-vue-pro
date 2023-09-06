package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author whycode
 * @title: SubScribeObj
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/410:35
 */

@Data
public class SubScribeObj {

    @JSONField(name = "EventType")
    private String eventType;

    @JSONField(name = "SourceList")
    private List<Source> sourceList;

}
