package cn.iocoder.yudao.framework.datatranslation.core.aop;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.datatranslation.core.DataTranslationHandler;
import cn.iocoder.yudao.framework.datatranslation.core.annotations.DataTrans;
import cn.iocoder.yudao.framework.datatranslation.core.annotations.DataTranslation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 数据翻译 AOP 切面
 *
 * @author HUIHUI
 */
@Aspect
@Slf4j
public class DataTranslationAspect {

    private final Map<String, DataTranslationHandler> translationHandlers = new HashMap<>();

    public DataTranslationAspect(ApplicationContext applicationContext) {
        Map<String, DataTranslationHandler> beansOfType = applicationContext.getBeansOfType(DataTranslationHandler.class);
        translationHandlers.putAll(beansOfType);
    }

    @Around("@annotation(dataTranslation)")
    public Object doBefore(ProceedingJoinPoint joinPoint, DataTranslation dataTranslation) throws Throwable {
        // 执行目标方法，获取返回值
        CommonResult<?> result = (CommonResult<?>) joinPoint.proceed();
        Object data = result.getData();
        if (data instanceof Collection) { // 情况一：返回的是 list
            Collection<?> collection = (Collection<?>) data;
            for (Object obj : collection) {
                if (obj == null) {
                    break; // 遇到 null 直接结束
                }
                handlerFields(obj);
            }
        } else if (data instanceof PageResult) { // 情况二：返回的是 PageResult
            PageResult<?> pageResult = (PageResult<?>) data;
            List<?> list = pageResult.getList();
            for (Object obj : list) {
                if (obj == null) {
                    break; // 遇到 null 直接结束
                }
                handlerFields(obj);
            }
        } else { // 情况三：返回的是一个 respVO
            handlerFields(data);
        }
        return result;
    }

    private void handlerFields(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] classFields = clazz.getDeclaredFields();
        for (Field field : classFields) {
            // 检测是否存在数据翻译注解
            if (field.isAnnotationPresent(DataTrans.class)) {
                DataTrans dataTrans = field.getAnnotation(DataTrans.class);
                List<Map<String, Object>> maps = handlerField(obj, field, dataTrans);
                setFieldValue(obj, clazz, field, dataTrans, maps);
            }
        }
    }

    /**
     * 处理属性注解获取翻译数据源
     *
     * @param obj       操作对象
     * @param field     对象字段
     * @param dataTrans 翻译属性注解
     * @return 翻译数据源
     */
    private List<Map<String, Object>> handlerField(Object obj, Field field, DataTrans dataTrans) {
        String type = dataTrans.type(); // 翻译数据处理器类型
        DataTranslationHandler translationHandler = translationHandlers.get(type);
        if (translationHandler == null) {
            return Collections.emptyList();
        }
        // 使用反射获取属性值
        field.setAccessible(true);
        try {
            // 获取翻译数据源
            Object value = field.get(obj);
            if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                return translationHandler.selectByIds(collection);
            }
            return Collections.singletonList(translationHandler.selectById(value));
        } catch (IllegalAccessException ignored) {
        }
        return Collections.emptyList();
    }

    /**
     * 数据翻译映射属性值
     *
     * @param obj       操作的对象
     * @param clazz     对象类型
     * @param dataTrans 属性注解
     * @param maps      翻译数据源
     */
    private static void setFieldValue(Object obj, Class<?> clazz, Field classField, DataTrans dataTrans, List<Map<String, Object>> maps) {
        if (CollUtil.isEmpty(maps)) {
            log.warn("没有获取到【{}】数据源", dataTrans.type());
            return;
        }
        String[] fields = dataTrans.fields(); // 需要获取的字段
        String[] resultMapping = dataTrans.resultMapping(); // 映射在哪些属性上（与 fields 一一对应）
        try {
            Object value = classField.get(obj);
            if (value instanceof Collection) {  // 多数据翻译,比如说 postIds 翻译
                for (int i = 0; i < fields.length; i++) {
                    String field = fields[i]; // 需要获取的值
                    // 获取属性字段
                    Field targetField = clazz.getDeclaredField(resultMapping[i]);
                    // 设置字段可访问（如果是私有字段）
                    targetField.setAccessible(true);
                    // 获取目标属性类型
                    Type fieldType = targetField.getGenericType();
                    // 检查属性类型是否为 Collection
                    if (fieldType instanceof ParameterizedType) { // 情况一：集合类型
                        ParameterizedType parameterizedType = (ParameterizedType) fieldType;
                        if (List.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) { // List
                            // 设置属性值
                            targetField.set(obj, CollectionUtils.convertList(maps, item -> item.get(field)));
                        } else if (Set.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) { // Set
                            // 设置属性值
                            targetField.set(obj, CollectionUtils.convertSet(maps, item -> item.get(field)));
                        }
                    } else if (fieldType == String.class) { // 情况二：如果目标是字符串类型则将值已 "," 拼接后设置
                        List<Object> objects = CollectionUtils.convertList(maps, item -> item.get(field));
                        String collect = objects.stream()
                                .filter(Objects::nonNull)
                                .map(Object::toString)
                                .collect(Collectors.joining(","));
                        // 设置属性值
                        targetField.set(obj, collect);
                    }
                }
            } else { // 单数据翻译,比如说 userId 翻译
                // 获取数据
                Map<String, Object> objectMap = maps.get(0);
                for (int i = 0; i < fields.length; i++) {
                    String field = fields[i]; // 需要获取的值
                    // 获取属性字段
                    Field targetField = clazz.getDeclaredField(resultMapping[i]);
                    // 设置字段可访问（如果是私有字段）
                    targetField.setAccessible(true);
                    targetField.set(obj, objectMap.get(field));
                }
            }
        } catch (Exception e) {
            log.warn("存在注解【{}】的属性数据翻译失败", dataTrans);
        }
    }

}
