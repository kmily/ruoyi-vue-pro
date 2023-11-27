package cn.iocoder.yudao.module.promotion.dal.mysql.banner;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.promotion.dal.dataobject.banner.BannerDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.promotion.controller.admin.banner.vo.*;

/**
 * Banner Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BannerMapper extends BaseMapperX<BannerDO>,BaseMapper<BannerDO> {

    default PageResult<BannerDO> selectPage(BannerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BannerDO::getPosition, reqVO.getPosition())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getId));
    }

    default List<BannerDO> selectList(BannerExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BannerDO>()
                .eqIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BannerDO::getPosition, reqVO.getPosition())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getId));
    }

}
