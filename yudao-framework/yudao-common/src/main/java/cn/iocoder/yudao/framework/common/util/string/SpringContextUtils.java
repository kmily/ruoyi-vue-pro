package cn.iocoder.yudao.framework.common.util.string;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Spring Bean 容器相关工具类
 */
public class SpringContextUtils {
    private static ApplicationContext applicationContext = null;

    /**
     * 在Spring Boot启动时，获取ApplicationContext全局设置
     *
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 获取全局applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取 Spring Environment
     *
     * @return
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 通过name获取 Spring Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取 Spring Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的 Spring Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
