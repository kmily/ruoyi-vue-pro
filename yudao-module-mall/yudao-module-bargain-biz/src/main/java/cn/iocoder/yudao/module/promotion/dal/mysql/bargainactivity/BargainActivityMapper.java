package cn.iocoder.yudao.module.promotion.dal.mysql.bargainactivity;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityExportReqVO;
import cn.iocoder.yudao.module.promotion.controller.admin.bargainactivity.vo.BargainActivityPageReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.bargainactivity.BargainActivityDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 砍价 Mapper
 *
 * @author WangBosheng
 */
@Mapper
public interface BargainActivityMapper extends BaseMapperX<BargainActivityDO> {

    default PageResult<BargainActivityDO> selectPage(BargainActivityPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BargainActivityDO>()
                .likeIfPresent(BargainActivityDO::getName, reqVO.getName())
                .betweenIfPresent(BargainActivityDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(BargainActivityDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(BargainActivityDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BargainActivityDO::getBargainCount, reqVO.getBargainCount())
                .eqIfPresent(BargainActivityDO::getStock, reqVO.getStock())
                .betweenIfPresent(BargainActivityDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BargainActivityDO::getPeopleNum, reqVO.getPeopleNum())
                .orderByDesc(BargainActivityDO::getId));
    }

    default List<BargainActivityDO> selectList(BargainActivityExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<BargainActivityDO>()
                .likeIfPresent(BargainActivityDO::getName, reqVO.getName())
                .betweenIfPresent(BargainActivityDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(BargainActivityDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(BargainActivityDO::getStatus, reqVO.getStatus())
                .eqIfPresent(BargainActivityDO::getBargainCount, reqVO.getBargainCount())
                .eqIfPresent(BargainActivityDO::getStock, reqVO.getStock())
                .betweenIfPresent(BargainActivityDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(BargainActivityDO::getPeopleNum, reqVO.getPeopleNum())
                .orderByDesc(BargainActivityDO::getId));
    }

}
