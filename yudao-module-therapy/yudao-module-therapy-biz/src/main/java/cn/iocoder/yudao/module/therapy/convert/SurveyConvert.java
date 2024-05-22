package cn.iocoder.yudao.module.therapy.convert;

import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyRespVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SurveyConvert {
    SurveyConvert INSTANCE = Mappers.getMapper(SurveyConvert.class);
    List<SurveyRespVO> convertList(List<TreatmentSurveyDO> tsDO);
}
