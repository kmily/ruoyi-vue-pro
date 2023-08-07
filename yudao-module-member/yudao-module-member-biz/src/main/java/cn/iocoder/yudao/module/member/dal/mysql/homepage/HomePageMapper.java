package cn.iocoder.yudao.module.member.dal.mysql.homepage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.homepage.HomePageDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.app.homepage.vo.*;

/**
 * 首页配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface HomePageMapper extends BaseMapperX<HomePageDO> {

    default PageResult<HomePageDO> selectPage(AppHomePagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HomePageDO>()
                .eqIfPresent(HomePageDO::getUserId, reqVO.getUserId())
                .eqIfPresent(HomePageDO::getFamilyId, reqVO.getFamilyId())
                .likeIfPresent(HomePageDO::getName, reqVO.getName())
                .eqIfPresent(HomePageDO::getType, reqVO.getType())
                .eqIfPresent(HomePageDO::getStatus, reqVO.getStatus())
                .eqIfPresent(HomePageDO::getSort, reqVO.getSort())
                .eqIfPresent(HomePageDO::getMold, reqVO.getMold())
                .betweenIfPresent(HomePageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HomePageDO::getId));
    }

    default List<HomePageDO> selectList(AppHomePageExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<HomePageDO>()
                .eqIfPresent(HomePageDO::getUserId, reqVO.getUserId())
                .eqIfPresent(HomePageDO::getFamilyId, reqVO.getFamilyId())
                .likeIfPresent(HomePageDO::getName, reqVO.getName())
                .eqIfPresent(HomePageDO::getType, reqVO.getType())
                .eqIfPresent(HomePageDO::getStatus, reqVO.getStatus())
                .eqIfPresent(HomePageDO::getSort, reqVO.getSort())
                .eqIfPresent(HomePageDO::getMold, reqVO.getMold())
                .betweenIfPresent(HomePageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HomePageDO::getId));
    }

}
