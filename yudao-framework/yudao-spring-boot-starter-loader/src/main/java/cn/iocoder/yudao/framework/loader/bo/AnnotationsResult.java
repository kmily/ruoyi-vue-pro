package cn.iocoder.yudao.framework.loader.bo;

import lombok.Data;

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
