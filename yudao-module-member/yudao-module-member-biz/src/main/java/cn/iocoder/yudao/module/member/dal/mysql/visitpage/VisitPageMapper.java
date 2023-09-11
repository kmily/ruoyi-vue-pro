package cn.iocoder.yudao.module.member.dal.mysql.visitpage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.dal.dataobject.visitpage.VisitPageDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.member.controller.admin.visitpage.vo.*;

/**
 * 页面访问数据 Mapper
 *
 * @author 和尘同光
 */
@Mapper
public interface VisitPageMapper extends BaseMapperX<VisitPageDO> {

//    default PageResult<VisitPageDO> selectPage(VisitPagePageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<VisitPageDO>()
//                .betweenIfPresent(VisitPageDO::getStartTime, reqVO.getStartTime())
//                //.betweenIfPresent(VisitPageDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(VisitPageDO::getId));
//    }

    default List<VisitPageDO> selectList(VisitPageExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<VisitPageDO>()
                .eqIfPresent(VisitPageDO::getUserId, reqVO.getUserId())
                .likeIfPresent(VisitPageDO::getPageName, reqVO.getPageName())
                .betweenIfPresent(VisitPageDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(VisitPageDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(VisitPageDO::getUseTime, reqVO.getUseTime())
                .betweenIfPresent(VisitPageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VisitPageDO::getId));
    }

}
