package cn.iocoder.yudao.module.infra.service.demo.demo04;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo.Demo04StudentPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04CourseDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04GradeDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04StudentDO;
import cn.iocoder.yudao.module.infra.dal.mysql.demo.demo04.Demo04CourseMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.demo.demo04.Demo04GradeMapper;
import cn.iocoder.yudao.module.infra.dal.mysql.demo.demo04.Demo04StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 学生 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class Demo04StudentServiceImpl implements Demo04StudentService {

    @Resource
    private Demo04StudentMapper demo04StudentMapper;
    @Resource
    private Demo04CourseMapper demo04CourseMapper;
    @Resource
    private Demo04GradeMapper demo04GradeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDemo04Student() {
        // 插入
        Demo04StudentDO demo04Student = new Demo04StudentDO();
        demo04Student.setId(0L);
        demo04Student.setName("");
        demo04Student.setSex(0);
        demo04Student.setBirthday(LocalDateTime.now());
        demo04Student.setDescription("");
        demo04Student.setGradeId(0L);
        HashSet<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
        demo04Student.setCourseIds(ids);
        demo04Student.setCreateTime(LocalDateTime.now());
        demo04Student.setUpdateTime(LocalDateTime.now());
        demo04Student.setCreator("1");
        demo04Student.setUpdater("1");
        demo04Student.setDeleted(false);

        demo04StudentMapper.insert(demo04Student);
        // 返回
        return demo04Student.getId();
    }

    @Override
    public List<Demo04StudentDO> getDemo04StudentList() {
        return demo04StudentMapper.selectList();
    }

    @Override
    public List<Demo04StudentDO> getDemo04StudentList(Collection<Long> ids) {
        return demo04StudentMapper.selectBatchIds(ids);
    }

    @Override
    public List<Demo04CourseDO> getDemo04CourseList(Collection<Long> ids) {
        return demo04CourseMapper.selectBatchIds(ids);
    }

    @Override
    public Demo04GradeDO getDemo04GradeById(Long id) {
        return demo04GradeMapper.selectById(id);
    }

    @Override
    public Demo04StudentDO getDemo04StudentById(Long id) {
        return demo04StudentMapper.selectById(id);
    }

    @Override
    public PageResult<Demo04StudentDO> getDemo04StudentPage(Demo04StudentPageReqVO pageReqVO) {
        return demo04StudentMapper.selectPage(pageReqVO, null);
    }

}