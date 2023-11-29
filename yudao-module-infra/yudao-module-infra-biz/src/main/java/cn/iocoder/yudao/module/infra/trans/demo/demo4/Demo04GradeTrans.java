package cn.iocoder.yudao.module.infra.trans.demo.demo4;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.datatranslation.core.DataTranslationHandler;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04GradeDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 学生班级翻译
 *
 * TransType.AUTO_TRANS 用于自己写代码实现翻译数据源，Easy trans不在负责查询DB
 *
 * @author HUIHUI
 */
@Component
public class Demo04GradeTrans implements DataTranslationHandler {

    @Resource
    private Demo04StudentService studentService;

    @Override
    public Map<String, Object> selectById(Object o) {
        Demo04GradeDO demo04Grade = studentService.getDemo04GradeById((Long) o);
        return BeanUtil.beanToMap(demo04Grade);
    }

}
