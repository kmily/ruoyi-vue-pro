package cn.iocoder.yudao.framework.covert.bo;

import lombok.Data;

import java.util.List;

/**
 * 注解处理结果
 */
@Data
public class AnnotationsResult {

    /**
     * 远程调用额外参数
     */
    private Object[] remoteParams;

    /**
     * 写入字段
     */
    private String writeField;
}
