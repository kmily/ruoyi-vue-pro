package cn.iocoder.yudao.module.scan.dal.mysql.report;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;

/**
 * 报告 Mapper
 *
 * @author lyz
 */
@Mapper
public interface ReportMapper extends BaseMapperX<ReportDO> {

    default PageResult<ReportDO> selectPage(ReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReportDO>()
                .betweenIfPresent(ReportDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ReportDO::getCode, reqVO.getCode())
                .likeIfPresent(ReportDO::getName, reqVO.getName())
                .eqIfPresent(ReportDO::getSex, reqVO.getSex())
                .eqIfPresent(ReportDO::getHeight, reqVO.getHeight())
                .eqIfPresent(ReportDO::getAge, reqVO.getAge())
                .eqIfPresent(ReportDO::getHealthScore, reqVO.getHealthScore())
                .eqIfPresent(ReportDO::getHighLowShoulders, reqVO.getHighLowShoulders())
                .eqIfPresent(ReportDO::getHeadAskew, reqVO.getHeadAskew())
                .eqIfPresent(ReportDO::getLeftLegKokd, reqVO.getLeftLegKokd())
                .eqIfPresent(ReportDO::getRightLegKokd, reqVO.getRightLegKokd())
                .eqIfPresent(ReportDO::getLongShortLeg, reqVO.getLongShortLeg())
                .eqIfPresent(ReportDO::getPokingChin, reqVO.getPokingChin())
                .eqIfPresent(ReportDO::getPelvicAnteversion, reqVO.getPelvicAnteversion())
                .eqIfPresent(ReportDO::getLeftKneeAnalysis, reqVO.getLeftKneeAnalysis())
                .eqIfPresent(ReportDO::getRightKneeAnalysis, reqVO.getRightKneeAnalysis())
                .eqIfPresent(ReportDO::getLeftRoundShoulder, reqVO.getLeftRoundShoulder())
                .eqIfPresent(ReportDO::getRightRoundShoulder, reqVO.getRightRoundShoulder())
                .eqIfPresent(ReportDO::getFocusTiltLeftRight, reqVO.getFocusTiltLeftRight())
                .eqIfPresent(ReportDO::getFocusTiltForthBack, reqVO.getFocusTiltForthBack())
                .eqIfPresent(ReportDO::getWeight, reqVO.getWeight())
                .eqIfPresent(ReportDO::getProteinRate, reqVO.getProteinRate())
                .eqIfPresent(ReportDO::getBoneMassMositureContent, reqVO.getBoneMassMositureContent())
                .eqIfPresent(ReportDO::getFatPercentage, reqVO.getFatPercentage())
                .eqIfPresent(ReportDO::getMusclePercentage, reqVO.getMusclePercentage())
                .eqIfPresent(ReportDO::getSkeletalMuscleVolume, reqVO.getSkeletalMuscleVolume())
                .eqIfPresent(ReportDO::getBmi, reqVO.getBmi())
                .eqIfPresent(ReportDO::getVisceralFatGrade, reqVO.getVisceralFatGrade())
                .eqIfPresent(ReportDO::getIdealWeight, reqVO.getIdealWeight())
                .eqIfPresent(ReportDO::getPhysicalAge, reqVO.getPhysicalAge())
                .eqIfPresent(ReportDO::getBasalMetabolicRate, reqVO.getBasalMetabolicRate())
                .eqIfPresent(ReportDO::getBust, reqVO.getBust())
                .eqIfPresent(ReportDO::getWaistline, reqVO.getWaistline())
                .eqIfPresent(ReportDO::getHipline, reqVO.getHipline())
                .eqIfPresent(ReportDO::getLeftHipline, reqVO.getLeftHipline())
                .eqIfPresent(ReportDO::getRightHipline, reqVO.getRightHipline())
                .eqIfPresent(ReportDO::getLeftThigCircumference, reqVO.getLeftThigCircumference())
                .eqIfPresent(ReportDO::getRightThigCircumference, reqVO.getRightThigCircumference())
                .eqIfPresent(ReportDO::getLeftCalfCircumference, reqVO.getLeftCalfCircumference())
                .eqIfPresent(ReportDO::getRightCalfCircumference, reqVO.getRightCalfCircumference())
                .eqIfPresent(ReportDO::getModelFileId, reqVO.getModelFileId())
                .eqIfPresent(ReportDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ReportDO::getShapeValue, reqVO.getShapeValue())
                .eqIfPresent(ReportDO::getShapeId, reqVO.getShapeId())
                .eqIfPresent(ReportDO::getReportFilePath, reqVO.getReportFilePath())
                .orderByDesc(ReportDO::getId));
    }

    default List<ReportDO> selectList(ReportExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ReportDO>()
                .betweenIfPresent(ReportDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ReportDO::getCode, reqVO.getCode())
                .likeIfPresent(ReportDO::getName, reqVO.getName())
                .eqIfPresent(ReportDO::getSex, reqVO.getSex())
                .eqIfPresent(ReportDO::getHeight, reqVO.getHeight())
                .eqIfPresent(ReportDO::getAge, reqVO.getAge())
                .eqIfPresent(ReportDO::getHealthScore, reqVO.getHealthScore())
                .eqIfPresent(ReportDO::getHighLowShoulders, reqVO.getHighLowShoulders())
                .eqIfPresent(ReportDO::getHeadAskew, reqVO.getHeadAskew())
                .eqIfPresent(ReportDO::getLeftLegKokd, reqVO.getLeftLegKokd())
                .eqIfPresent(ReportDO::getRightLegKokd, reqVO.getRightLegKokd())
                .eqIfPresent(ReportDO::getLongShortLeg, reqVO.getLongShortLeg())
                .eqIfPresent(ReportDO::getPokingChin, reqVO.getPokingChin())
                .eqIfPresent(ReportDO::getPelvicAnteversion, reqVO.getPelvicAnteversion())
                .eqIfPresent(ReportDO::getLeftKneeAnalysis, reqVO.getLeftKneeAnalysis())
                .eqIfPresent(ReportDO::getRightKneeAnalysis, reqVO.getRightKneeAnalysis())
                .eqIfPresent(ReportDO::getLeftRoundShoulder, reqVO.getLeftRoundShoulder())
                .eqIfPresent(ReportDO::getRightRoundShoulder, reqVO.getRightRoundShoulder())
                .eqIfPresent(ReportDO::getFocusTiltLeftRight, reqVO.getFocusTiltLeftRight())
                .eqIfPresent(ReportDO::getFocusTiltForthBack, reqVO.getFocusTiltForthBack())
                .eqIfPresent(ReportDO::getWeight, reqVO.getWeight())
                .eqIfPresent(ReportDO::getProteinRate, reqVO.getProteinRate())
                .eqIfPresent(ReportDO::getBoneMassMositureContent, reqVO.getBoneMassMositureContent())
                .eqIfPresent(ReportDO::getFatPercentage, reqVO.getFatPercentage())
                .eqIfPresent(ReportDO::getMusclePercentage, reqVO.getMusclePercentage())
                .eqIfPresent(ReportDO::getSkeletalMuscleVolume, reqVO.getSkeletalMuscleVolume())
                .eqIfPresent(ReportDO::getBmi, reqVO.getBmi())
                .eqIfPresent(ReportDO::getVisceralFatGrade, reqVO.getVisceralFatGrade())
                .eqIfPresent(ReportDO::getIdealWeight, reqVO.getIdealWeight())
                .eqIfPresent(ReportDO::getPhysicalAge, reqVO.getPhysicalAge())
                .eqIfPresent(ReportDO::getBasalMetabolicRate, reqVO.getBasalMetabolicRate())
                .eqIfPresent(ReportDO::getBust, reqVO.getBust())
                .eqIfPresent(ReportDO::getWaistline, reqVO.getWaistline())
                .eqIfPresent(ReportDO::getHipline, reqVO.getHipline())
                .eqIfPresent(ReportDO::getLeftHipline, reqVO.getLeftHipline())
                .eqIfPresent(ReportDO::getRightHipline, reqVO.getRightHipline())
                .eqIfPresent(ReportDO::getLeftThigCircumference, reqVO.getLeftThigCircumference())
                .eqIfPresent(ReportDO::getRightThigCircumference, reqVO.getRightThigCircumference())
                .eqIfPresent(ReportDO::getLeftCalfCircumference, reqVO.getLeftCalfCircumference())
                .eqIfPresent(ReportDO::getRightCalfCircumference, reqVO.getRightCalfCircumference())
                .eqIfPresent(ReportDO::getModelFileId, reqVO.getModelFileId())
                .eqIfPresent(ReportDO::getUserId, reqVO.getUserId())
                .eqIfPresent(ReportDO::getShapeValue, reqVO.getShapeValue())
                .eqIfPresent(ReportDO::getShapeId, reqVO.getShapeId())
                .eqIfPresent(ReportDO::getReportFilePath, reqVO.getReportFilePath())
                .orderByDesc(ReportDO::getId));
    }

}
