package cn.iocoder.yudao.framework.ai.core.exception;

// TODO @fan：这个有办法干掉么？
/**
 * ai 异常
 *
 * @author fansili
 * @time 2024/4/13 17:05
 * @since 1.0
 */
public class AiException extends RuntimeException {

    public AiException(String message) {
        super(message);
    }
}