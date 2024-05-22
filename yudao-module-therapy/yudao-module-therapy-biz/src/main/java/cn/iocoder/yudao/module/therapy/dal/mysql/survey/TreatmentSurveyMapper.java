package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.stream.Collectors;

@Mapper
public interface TreatmentSurveyMapper extends BaseMapperX<TreatmentSurveyDO> {

    default PageResult<TreatmentSurveyDO> selectPage(SurveyPageReqVO reqVO) {

        // 分页查询
        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentSurveyDO>()
                .likeIfPresent(TreatmentSurveyDO::getTitle, reqVO.getTitle())
                .betweenIfPresent(TreatmentSurveyDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(TreatmentSurveyDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(TreatmentSurveyDO::getSurveyType, reqVO.getSurveyType())
                .likeIfPresent(TreatmentSurveyDO::getTags, reqVO.getTags())
                .eqIfPresent(TreatmentSurveyDO::getCreator,reqVO.getCreator())
                .orderByDesc(TreatmentSurveyDO::getId));
    }
}
