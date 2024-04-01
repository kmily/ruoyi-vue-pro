package cn.iocoder.yudao.module.steam.controller.app.vo.UUBatchGetOnSaleCommodity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchGetCommodity implements Serializable {

    @JsonProperty("data")
    private List<UUSaleTemplateRespVO> data;
}
