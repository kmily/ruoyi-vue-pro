package cn.iocoder.yudao.framework.easytrans.core.aop;

import cn.iocoder.yudao.framework.easytrans.core.annotations.DataTranslation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


/**
 * 数据翻译 AOP 切面
 *
 * @author HUIHUI
 */
@Aspect
@Slf4j
public class DataTranslationAspect {

    //@Resource
    //public List<DataTranslationHandler> translationHandlers;

    @Before("@annotation(dataTranslation)")
    public void doBefore(JoinPoint joinPoint, DataTranslation dataTranslation) {
        log.info("DataTranslationAspect triggered for method: {}", joinPoint.getSignature().toShortString());
        System.out.println(joinPoint);
    }

}
