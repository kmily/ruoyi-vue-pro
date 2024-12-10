package cn.iocoder.yudao.module.haoka.dal.mysql.demo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.DemoPageReqVO;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.DemoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好卡案例 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DemoMapper extends BaseMapperX<DemoDO> {

    default PageResult<DemoDO> selectPage(DemoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DemoDO>()
                .likeIfPresent(DemoDO::getName, reqVO.getName())
                .eqIfPresent(DemoDO::getAge, reqVO.getAge())
                .eqIfPresent(DemoDO::getAgent, reqVO.getAgent())
                .betweenIfPresent(DemoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DemoDO::getId));
    }

}
