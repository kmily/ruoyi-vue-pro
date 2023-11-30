package cn.iocoder.yudao.module.infra.service.demo.demo04;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.infra.controller.admin.demo.demo04.vo.Demo04StudentPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04CourseDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04GradeDO;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04StudentDO;

import java.util.Collection;
import java.util.List;

/**
 * 学生 Service 接口
 *
 * @author 芋道源码
 */
public interface Demo04StudentService {

    /**
     * 创建学生
     *
     * @return 编号
     */
    Long createDemo04Student();

    /**
     * 获得学生列表
     *
     * @return 学生列表
     */
    List<Demo04StudentDO> getDemo04StudentList();

    /**
     * 获得学生列表
     *
     * @param ids 学生编号
     * @return 学生列表
     */
    List<Demo04StudentDO> getDemo04StudentList(Collection<Long> ids);

    /**
     * 获得课程列表
     *
     * @param ids 课程编号
     * @return 课程列表
     */
    List<Demo04CourseDO> getDemo04CourseList(Collection<Long> ids);

    /**
     * 获得学生班级
     *
     * @param id 编号
     * @return 学生班级
     */
    Demo04GradeDO getDemo04GradeById(Long id);

    /**
     * 获得学生详情
     *
     * @param id 编号
     * @return 学生DO
     */
    Demo04StudentDO getDemo04StudentById(Long id);

    /**
     * 获得学生分页
     *
     * @param pageReqVO 分页条件
     * @return 学生分页数据
     */
    PageResult<Demo04StudentDO> getDemo04StudentPage(Demo04StudentPageReqVO pageReqVO);

}