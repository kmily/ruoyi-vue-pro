package cn.iocoder.yudao.module.radar.dal.mysql.banner;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.radar.dal.dataobject.banner.BannerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.radar.controller.admin.banner.vo.*;

/**
 * 雷达模块banner图 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BannerMapper extends BaseMapperX<BannerDO> {

    default PageResult<BannerDO> selectPage(BannerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getUrl, reqVO.getUrl())
                .eqIfPresent(BannerDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(BannerDO::getSort, reqVO.getSort())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BannerDO::getMemo, reqVO.getMemo())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getId));
    }

    default List<BannerDO> selectList(BannerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getUrl, reqVO.getUrl())
                .eqIfPresent(BannerDO::getPicUrl, reqVO.getPicUrl())
                .eqIfPresent(BannerDO::getSort, reqVO.getSort())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BannerDO::getMemo, reqVO.getMemo())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getId));
    }

}
