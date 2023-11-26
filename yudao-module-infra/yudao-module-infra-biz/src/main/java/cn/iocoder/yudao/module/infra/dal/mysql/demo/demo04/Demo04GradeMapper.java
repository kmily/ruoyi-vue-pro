package cn.iocoder.yudao.module.infra.dal.mysql.demo.demo04;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.infra.dal.dataobject.demo.demo04.Demo04GradeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生班级 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface Demo04GradeMapper extends BaseMapperX<Demo04GradeDO> {

}