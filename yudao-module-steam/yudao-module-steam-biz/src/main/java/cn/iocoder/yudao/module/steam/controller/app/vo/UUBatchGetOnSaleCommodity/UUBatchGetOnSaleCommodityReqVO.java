package cn.iocoder.yudao.module.steam.controller.app.vo.UUBatchGetOnSaleCommodity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UUBatchGetOnSaleCommodityReqVO implements Serializable {

    private List<List1> requestList;

    @Data
    private static class List1{

        private Integer templateId;
        private String templateHashName;

    }


}
