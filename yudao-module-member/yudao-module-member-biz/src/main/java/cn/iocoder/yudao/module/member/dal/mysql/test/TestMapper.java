package cn.iocoder.yudao.module.member.dal.mysql.test;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.test.TestDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.test.vo.*;

/**
 * 会员标签 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TestMapper extends BaseMapperX<TestDO> {

    default PageResult<TestDO> selectPage(TestPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TestDO>()
                .likeIfPresent(TestDO::getName, reqVO.getName())
                .betweenIfPresent(TestDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TestDO::getId));
    }

    default List<TestDO> selectList(TestExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<TestDO>()
                .likeIfPresent(TestDO::getName, reqVO.getName())
                .betweenIfPresent(TestDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TestDO::getId));
    }

}
