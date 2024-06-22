package cn.iocoder.yudao.module.therapy.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.boot.module.therapy.enums.SurveyQuestionType;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.therapy.controller.admin.survey.vo.*;
import cn.iocoder.yudao.module.therapy.controller.admin.vo.UserRespVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.AnAnswerReqVO;
import cn.iocoder.yudao.module.therapy.controller.app.vo.SubmitSurveyReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.AnswerDetailDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.QuestionDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.SurveyAnswerDO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.survey.TreatmentSurveyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_NOT_EXISTS;
import static cn.iocoder.boot.module.therapy.enums.ErrorCodeConstants.SURVEY_QUESTION_TYPE_CHANGE;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Mapper
public interface SurveyConvert {
    SurveyConvert INSTANCE = Mappers.getMapper(SurveyConvert.class);

    List<SurveyRespVO> convertList(List<TreatmentSurveyDO> tsDO);

    TreatmentSurveyDO convert(SurveySaveReqVO reqVO);

    default QuestionDO convertQst(JSONObject vo) {
        QuestionDO qst = new QuestionDO();
        QuestSaveReqVO newVO = JSONUtil.toBean(vo, QuestSaveReqVO.class);
        qst.setQstContext(vo);
        qst.setTitle(newVO.getTitle());
        qst.setQstType(SurveyQuestionType.getByCode(newVO.getType()).getType());
        qst.setRequired(vo.getBool("$required", true));
        qst.setCode(newVO.getField());
        return qst;
    }

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
            vo.getQuestions().add(item.getQstContext().toString());
        }

        return vo;
    }

    default List<AnswerDetailDO> convert(Map<String, QuestionDO> qstMap, SubmitSurveyReqVO reqVO) {
        List<AnswerDetailDO> anAnswerDOS = new ArrayList<>();
        for (AnAnswerReqVO item : reqVO.getQstList()) {
            AnswerDetailDO detailDO = new AnswerDetailDO();
            if (!qstMap.containsKey(item.getQstCode())) throw exception(SURVEY_QUESTION_NOT_EXISTS);

            QuestionDO qst = qstMap.get(item.getQstCode());
//            if (!item.getQstType().equals(qst.getQstType())) throw exception(SURVEY_QUESTION_TYPE_CHANGE);
            detailDO.setAnswerId(reqVO.getId());
            detailDO.setBelongSurveyId(qst.getBelongSurveyId());
//            detailDO.setBelongSurveyCode(qst.getBelongSurveyCode());
//            detailDO.setQstType(item.getQstType());
            detailDO.setCreator(getLoginUserId().toString());
            detailDO.setUpdater(getLoginUserId().toString());
            detailDO.setQstContext(qst.getQstContext());
            detailDO.setAnswer(item.getAnswer());
            detailDO.setQstId(qst.getId());
            detailDO.setBelongQstCode(qst.getCode());
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

    PageResult<UserRespVO> convertUserDTOPage(PageResult<MemberUserRespDTO> pageResult);

}
