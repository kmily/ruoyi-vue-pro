package cn.iocoder.yudao.module.oa.dal.mysql.feedback;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.oa.dal.dataobject.feedback.FeedbackDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.oa.controller.admin.feedback.vo.*;

/**
 * 产品反馈 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface FeedbackMapper extends BaseMapperX<FeedbackDO> {

    default PageResult<FeedbackDO> selectPage(FeedbackPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FeedbackDO>()
                .eqIfPresent(FeedbackDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(FeedbackDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FeedbackDO::getId));
    }

    default List<FeedbackDO> selectList(FeedbackExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<FeedbackDO>()
                .eqIfPresent(FeedbackDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(FeedbackDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FeedbackDO::getId));
    }

}
