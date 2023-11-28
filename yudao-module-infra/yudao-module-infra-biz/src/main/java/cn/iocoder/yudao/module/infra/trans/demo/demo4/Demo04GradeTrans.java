package cn.iocoder.yudao.module.infra.trans.demo.demo4;

import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04GradeDO;
import cn.iocoder.yudao.module.infra.service.demo.demo04.Demo04StudentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 学生班级翻译
 *
 * TransType.AUTO_TRANS 用于自己写代码实现翻译数据源，Easy trans不在负责查询DB
 *
 * @author HUIHUI
 */
@Component
public class Demo04GradeTrans {

    @Resource
    private Demo04StudentService studentService;

    public Demo04GradeDO selectById(Object o) {
        return studentService.getDemo04GradeById((Long) o);
    }

}
