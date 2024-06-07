package cn.iocoder.yudao.module.therapy.dal.mysql.survey;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyPageReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jodd.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TreatmentSurveyMapper extends BaseMapperX<TreatmentSurveyDO> {

    default PageResult<TreatmentSurveyDO> selectPage(SurveyPageReqVO reqVO) {

        // 分页查询
        return selectPage(reqVO, new LambdaQueryWrapperX<TreatmentSurveyDO>()
                .likeIfPresent(TreatmentSurveyDO::getTitle, reqVO.getTitle())
                .betweenIfPresent(TreatmentSurveyDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(TreatmentSurveyDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(TreatmentSurveyDO::getSurveyType, reqVO.getSurveyType())
                .eqIfPresent(TreatmentSurveyDO::getCreator, reqVO.getCreator())
                .apply(StringUtil.isNotBlank(reqVO.getTag()), String.format("JSON_CONTAINS(tags, '[\"%s\"]')", reqVO.getTag()))
                .orderByDesc(TreatmentSurveyDO::getId));
    }

    default TreatmentSurveyDO selectByCode(String code){
        LambdaQueryWrapper<TreatmentSurveyDO> queryWrapper= Wrappers.lambdaQuery(TreatmentSurveyDO.class)
                .eq(TreatmentSurveyDO::getCode,code);
        return selectOne(queryWrapper);
    }

    default TreatmentSurveyDO selectFirstByType(Integer type){
        LambdaQueryWrapper<TreatmentSurveyDO> queryWrapper= Wrappers.lambdaQuery(TreatmentSurveyDO.class)
                .eq(TreatmentSurveyDO::getSurveyType,type)
                .orderByDesc(TreatmentSurveyDO::getId)
                .last("limit 1");

        return selectOne(queryWrapper);
    }
}
