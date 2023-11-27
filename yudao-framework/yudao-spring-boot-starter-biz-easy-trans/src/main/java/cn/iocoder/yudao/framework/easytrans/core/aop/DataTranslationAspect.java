package cn.iocoder.yudao.framework.easytrans.core.aop;

import cn.iocoder.yudao.framework.easytrans.core.DataTranslationHandler;
import cn.iocoder.yudao.framework.easytrans.core.annotations.DataTranslation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 数据翻译 AOP 切面
 *
 * @author HUIHUI
 */
@Component
@Aspect
@Slf4j
public class DataTranslationAspect {

    @Resource
    public List<DataTranslationHandler> translationHandlers;

    @After("@annotation(dataTranslation)")
    public void doBefore(JoinPoint joinPoint, DataTranslation dataTranslation) {

    }

}
