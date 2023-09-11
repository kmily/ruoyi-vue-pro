package cn.iocoder.yudao.module.member.controller.admin.bootup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Comparator;

/**
 * @author whycode
 * @title: BootUpListResVO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/9/119:38
 */

@Schema(description = "管理后台 - 用户启动数据 Response VO")
@Data
@ToString(callSuper = true)
public class BootUpListResVO implements Comparable<BootUpListResVO> {

    @Schema(description = "日期", example = "2023-09-11")
    private String date;

    @Schema(description = "次数", example = "100")
    private Integer times;

    @Schema(description = "占用百分比", example = "12.1%")
    private String percentage;




    @Override
    public int compareTo(BootUpListResVO o) {
        return this.getDate().compareTo(o.getDate());
    }
}
