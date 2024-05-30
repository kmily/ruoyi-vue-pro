package cn.iocoder.yudao.module.therapy.service.dto;

import lombok.Data;

/**
 * @Author:lidongw_1
 * @Date 2024/5/29
 * @Description: sse消息
 **/
@Data
public class SSEMsgDTO {

    int code;
    String msg;
    String id;
    boolean finished;
    String content;
}
