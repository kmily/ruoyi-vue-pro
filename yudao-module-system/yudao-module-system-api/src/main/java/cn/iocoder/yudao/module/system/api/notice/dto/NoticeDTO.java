package cn.iocoder.yudao.module.system.api.notice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author whycode
 * @title: NoticeDTO
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/1813:48
 */

@Data
public class NoticeDTO {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息状态
     */
    private Integer status;

    private LocalDateTime createTime;

}
