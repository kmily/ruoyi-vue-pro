package cn.iocoder.yudao.framework.common.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhenan
 */
@Component
public class ApplicationUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationUtils.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

}
