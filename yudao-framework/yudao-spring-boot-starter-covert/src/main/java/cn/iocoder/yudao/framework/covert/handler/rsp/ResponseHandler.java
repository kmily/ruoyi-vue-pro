package cn.iocoder.yudao.framework.covert.handler.rsp;

/**
 * 返回处理第三方响应
 */
public interface ResponseHandler {
    Object handle(String bean, String method, Object rsp, Object... params);
}
