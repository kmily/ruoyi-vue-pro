package cn.iocoder.yudao.module.radar.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author whycode
 * @title: SubscribeEvent
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/410:24
 */

@Data
@Getter
@Setter
public class SubscribeEvent {
    @JSONField(name = "Num")
    private Long num;

    @JSONField(name = "SubScribeObjList")
    private List<SubScribeObj> subScribeObjList;

}
