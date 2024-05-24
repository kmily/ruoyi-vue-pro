package cn.iocoder.yudao.module.therapy.convert;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyAnswerRespVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyQstSaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveyRespVO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.SurveySaveReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.enums.ErrorCodeConstants.SURVEY_QUESTION_NOT_EXISTS;
import static cn.iocoder.boot.enums.ErrorCodeConstants.SURVEY_QUESTION_TYPE_CHANGE;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

@Mapper
public interface SurveyConvert {
    SurveyConvert INSTANCE = Mappers.getMapper(SurveyConvert.class);

    List<SurveyRespVO> convertList(List<TreatmentSurveyDO> tsDO);

    TreatmentSurveyDO convert(SurveySaveReqVO reqVO);

    QuestionDO convert(SurveyQstSaveReqVO vo);

    default List<SurveyRespVO> convertList(List<TreatmentSurveyDO> tsDO, Map<Long, AdminUserRespDTO> userMap) {
        List<SurveyRespVO> list = this.convertList(tsDO);
        for (SurveyRespVO item : list) {
            Long userId = Long.parseLong(item.getCreator());
            if (CollUtil.isEmpty(userMap) && userMap.containsKey(userId)) {
                item.setCreator(userMap.get(userId).getNickname());
            }
        }

        return list;
    }

    SurveySaveReqVO convert(TreatmentSurveyDO tsdo);

    SurveyQstSaveReqVO convert(QuestionDO qst);

    default SurveySaveReqVO convert(TreatmentSurveyDO tsdo, List<QuestionDO> list) {
        SurveySaveReqVO vo = this.convert(tsdo);
        vo.setQuestions(new ArrayList<>());
        for (QuestionDO item : list) {
            vo.getQuestions().add(convert(item));
        }

        return vo;
    }

    default List<AnswerDetailDO> convert(Map<Long, QuestionDO> qstMap, List<AnAnswerReqVO> reqVOS, Long userId) {
        List<AnswerDetailDO> anAnswerDOS = new ArrayList<>();
        for (AnAnswerReqVO item : reqVOS) {
            AnswerDetailDO detailDO = new AnswerDetailDO();
            if (!qstMap.containsKey(item.getId())) throw exception(SURVEY_QUESTION_NOT_EXISTS);

            QuestionDO qst = qstMap.get(item.getId());
            if (!item.getQstType().equals(qst.getQstType())) throw exception(SURVEY_QUESTION_TYPE_CHANGE);
            detailDO.setAnswer(item.getAnswer());
            detailDO.setBelongSurveyId(qst.getBelongSurveyId());
            detailDO.setQstType(item.getQstType());
            detailDO.setCreator(userId.toString());
            detailDO.setUpdater(userId.toString());
            detailDO.setQstContext(qst.getQstContext());
            detailDO.setQstId(qst.getId());
            anAnswerDOS.add(detailDO);
        }
        return anAnswerDOS;
    }

    default List<SurveyAnswerRespVO> convertList(Map<Long, TreatmentSurveyDO> treatmentSurveyDOMap, List<SurveyAnswerDO> answerDOS) {
        List<SurveyAnswerRespVO> respVOS = new ArrayList<>();
        for (SurveyAnswerDO item : answerDOS) {
            SurveyAnswerRespVO res = convert(item);
            res.setSurveyId(item.getBelongSurveyId());
            TreatmentSurveyDO temp = treatmentSurveyDOMap.get(item.getBelongSurveyId());
            res.setTitle(temp.getTitle());
            res.setSurveyType(temp.getSurveyType());
            respVOS.add(res);
        }
        return respVOS;
    }

    SurveyAnswerRespVO convert(SurveyAnswerDO surveyAnswerDO);

}
