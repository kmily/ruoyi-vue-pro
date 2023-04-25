package cn.iocoder.yudao.module.oa.convert.feedback;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import cn.iocoder.yudao.module.oa.controller.admin.feedback.vo.*;
import cn.iocoder.yudao.module.oa.dal.dataobject.feedback.FeedbackDO;

/**
 * 产品反馈 Convert
 *
 * @author 管理员
 */
@Mapper
public interface FeedbackConvert {

    FeedbackConvert INSTANCE = Mappers.getMapper(FeedbackConvert.class);

    FeedbackDO convert(FeedbackCreateReqVO bean);

    FeedbackDO convert(FeedbackUpdateReqVO bean);

    FeedbackRespVO convert(FeedbackDO bean);

    List<FeedbackRespVO> convertList(List<FeedbackDO> list);

    PageResult<FeedbackRespVO> convertPage(PageResult<FeedbackDO> page);

    List<FeedbackExcelVO> convertList02(List<FeedbackDO> list);

}
