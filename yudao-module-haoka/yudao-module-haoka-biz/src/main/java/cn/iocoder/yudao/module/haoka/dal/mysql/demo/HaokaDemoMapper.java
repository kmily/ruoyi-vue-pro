package cn.iocoder.yudao.module.haoka.dal.mysql.demo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.HaokaDemoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.*;

/**
 * 好卡案例 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HaokaDemoMapper extends BaseMapperX<HaokaDemoDO> {

    default PageResult<HaokaDemoDO> selectPage(HaokaDemoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HaokaDemoDO>()
                .likeIfPresent(HaokaDemoDO::getName, reqVO.getName())
                .eqIfPresent(HaokaDemoDO::getAge, reqVO.getAge())
                .eqIfPresent(HaokaDemoDO::getAgent, reqVO.getAgent())
                .betweenIfPresent(HaokaDemoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HaokaDemoDO::getId));
    }

}