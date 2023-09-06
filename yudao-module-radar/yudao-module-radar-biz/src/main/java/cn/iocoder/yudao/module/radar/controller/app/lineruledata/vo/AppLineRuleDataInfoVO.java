package cn.iocoder.yudao.module.radar.controller.app.lineruledata.vo;

import cn.iocoder.yudao.module.radar.dal.dataobject.lineruledata.LineRuleDataDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: AppLineRuleDataInfoVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/69:31
 */

@Schema(description = "用户 APP - 离回家数据 Response VO")
@Data
@ToString(callSuper = true)
public class AppLineRuleDataInfoVO {

    public AppLineRuleDataInfoVO(){}

    @Schema(description = "时间")
    private LocalDateTime createTime;

    @Schema(description = "回家")
    private Boolean enter;

    @Schema(description = "离家")
    private Boolean goOut;


    public AppLineRuleDataInfoVO(LineRuleDataDO dataDO){
        this.createTime = dataDO.getCreateTime();
        this.enter = dataDO.getEnter() > 0;
        this.goOut = dataDO.getGoOut() > 0;
    }

}
