package cn.iocoder.yudao.module.therapy.controller.admin.VO;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Author:lidongw_1
 * @Date 2024/6/8
 * @Description: 分页基础查询
 **/

@Data
public class PageBaseRequest {

    @Min(value = 1, message = "页码必须大于 1")
    private Integer pageNo;

    @Min(value = 1, message = "每页条数必须大于 1")
    private Integer pageSize;
}
