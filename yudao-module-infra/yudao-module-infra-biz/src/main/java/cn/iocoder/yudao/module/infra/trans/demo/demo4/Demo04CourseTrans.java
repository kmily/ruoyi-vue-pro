package cn.iocoder.yudao.module.infra.trans.demo.demo4;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.datatranslation.core.DataTranslationHandler;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04CourseDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 学生课程信息翻译
 *
 * TransType.AUTO_TRANS 用于自己写代码实现翻译数据源，Easy trans不在负责查询DB
 *
 * @author HUIHUI
 */
@Component
public class Demo04CourseTrans implements DataTranslationHandler {

    @Resource
    private Demo04StudentService studentService;

    @Override
    public List<Map<String, Object>> selectByIds(Collection<?> ids) {
        List<Demo04CourseDO> demo04CourseList = studentService.getDemo04CourseList(convertSet(ids, id -> (Long) id));
        return convertList(demo04CourseList, BeanUtil::beanToMap);
    }

}
