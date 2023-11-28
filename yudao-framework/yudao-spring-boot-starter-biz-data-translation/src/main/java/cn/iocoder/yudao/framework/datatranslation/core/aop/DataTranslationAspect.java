package cn.iocoder.yudao.framework.datatranslation.core.aop;

import cn.iocoder.yudao.framework.datatranslation.core.DataTranslationHandler;
import cn.iocoder.yudao.framework.datatranslation.core.annotations.DataTranslation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;
import java.util.List;


/**
 * 数据翻译 AOP 切面
 *
 * @author HUIHUI
 */
@Aspect
@Slf4j
public class DataTranslationAspect {

    @Resource
    public List<DataTranslationHandler> translationHandlers;

    @After("@annotation(dataTranslation)")
    public void doBefore(JoinPoint joinPoint, DataTranslation dataTranslation) {
        log.info("DataTranslationAspect triggered for method: {}", joinPoint.getSignature().toShortString());
        System.out.println(joinPoint);
    }

}
