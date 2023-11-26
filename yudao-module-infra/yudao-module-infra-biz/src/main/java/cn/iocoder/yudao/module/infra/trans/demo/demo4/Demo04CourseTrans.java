package cn.iocoder.yudao.module.infra.trans.demo.demo4;

import cn.iocoder.yudao.framework.easytrans.core.VOAutoTransable;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04CourseDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import com.fhs.core.trans.anno.AutoTrans;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 学生课程信息翻译
 *
 * TransType.AUTO_TRANS 用于自己写代码实现翻译数据源，Easy trans不在负责查询DB
 *
 * @author HUIHUI
 */
@Component // TODO puhui999: 问题：翻译结果只能返回一个 courseName，不符合预期的返回 courseNames
@AutoTrans(namespace = "demo04course", fields = "name", defaultAlias = "course")
public class Demo04CourseTrans implements VOAutoTransable<Demo04CourseDO> {

    @Resource
    private Demo04StudentService studentService;

    @Override
    public List<Demo04CourseDO> selectByIds(List<?> ids) {
        return studentService.getDemo04CourseList(convertSet(ids, i -> Long.parseLong(i.toString().trim())));
    }

}
