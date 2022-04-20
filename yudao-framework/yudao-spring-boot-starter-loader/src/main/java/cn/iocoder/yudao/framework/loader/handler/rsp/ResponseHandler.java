package cn.iocoder.yudao.framework.loader.handler.rsp;

/**
 * 返回处理第三方响应
 */
public interface ResponseHandler {

    /**
     *  返回值处理
     * @param bean bean
     * @param method 方法
     * @param rsp 返回值
     * @param writeClass 写入类型
     * @param params 参数
     * @return
     */
    Object handle(String bean, String method, Object rsp, Class<?> writeClass, Object... params);
}