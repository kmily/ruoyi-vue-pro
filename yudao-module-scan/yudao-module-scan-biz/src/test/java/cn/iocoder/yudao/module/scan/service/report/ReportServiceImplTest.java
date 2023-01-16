package cn.iocoder.yudao.module.scan.service.report;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.scan.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.module.scan.dal.mysql.report.ReportMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.scan.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
* {@link ReportServiceImpl} 的单元测试类
*
* @author lyz
*/
@Import(ReportServiceImpl.class)
public class ReportServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ReportServiceImpl reportService;

    @Resource
    private ReportMapper reportMapper;

    @Test
    public void testCreateReport_success() {
        // 准备参数
        ReportCreateReqVO reqVO = randomPojo(ReportCreateReqVO.class);

        // 调用
        Long reportId = reportService.createReport(reqVO);
        // 断言
        assertNotNull(reportId);
        // 校验记录的属性是否正确
        ReportDO report = reportMapper.selectById(reportId);
        assertPojoEquals(reqVO, report);
    }

    @Test
    public void testUpdateReport_success() {
        // mock 数据
        ReportDO dbReport = randomPojo(ReportDO.class);
        reportMapper.insert(dbReport);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ReportUpdateReqVO reqVO = randomPojo(ReportUpdateReqVO.class, o -> {
            o.setId(dbReport.getId()); // 设置更新的 ID
        });

        // 调用
        reportService.updateReport(reqVO);
        // 校验是否更新正确
        ReportDO report = reportMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, report);
    }

    @Test
    public void testUpdateReport_notExists() {
        // 准备参数
        ReportUpdateReqVO reqVO = randomPojo(ReportUpdateReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> reportService.updateReport(reqVO), REPORT_NOT_EXISTS);
    }

    @Test
    public void testDeleteReport_success() {
        // mock 数据
        ReportDO dbReport = randomPojo(ReportDO.class);
        reportMapper.insert(dbReport);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbReport.getId();

        // 调用
        reportService.deleteReport(id);
       // 校验数据不存在了
       assertNull(reportMapper.selectById(id));
    }

    @Test
    public void testDeleteReport_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> reportService.deleteReport(id), REPORT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetReportPage() {
       // mock 数据
       ReportDO dbReport = randomPojo(ReportDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setCode(null);
           o.setName(null);
           o.setSex(null);
           o.setHeight(null);
           o.setAge(null);
           o.setHealthScore(null);
           o.setHighLowShoulders(null);
           o.setHeadAskew(null);
           o.setLeftLegKokd(null);
           o.setRightLegKokd(null);
           o.setLongShortLeg(null);
           o.setPokingChin(null);
           o.setPelvicAnteversion(null);
           o.setLeftKneeAnalysis(null);
           o.setRightKneeAnalysis(null);
           o.setLeftRoundShoulder(null);
           o.setRightRoundShoulder(null);
           o.setFocusTiltLeftRight(null);
           o.setFocusTiltForthBack(null);
           o.setWeight(null);
           o.setProteinRate(null);
           o.setBoneMassMositureContent(null);
           o.setFatPercentage(null);
           o.setMusclePercentage(null);
           o.setSkeletalMuscleVolume(null);
           o.setBmi(null);
           o.setVisceralFatGrade(null);
           o.setIdealWeight(null);
           o.setPhysicalAge(null);
           o.setBasalMetabolicRate(null);
           o.setBust(null);
           o.setWaistline(null);
           o.setHipline(null);
           o.setLeftHipline(null);
           o.setRightHipline(null);
           o.setLeftThigCircumference(null);
           o.setRightThigCircumference(null);
           o.setLeftCalfCircumference(null);
           o.setRightCalfCircumference(null);
           o.setModelFileId(null);
           o.setUserId(null);
           o.setShapeValue(null);
           o.setShapeId(null);
           o.setReportFilePath(null);
       });
       reportMapper.insert(dbReport);
       // 测试 createTime 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setCreateTime(null)));
       // 测试 code 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setCode(null)));
       // 测试 name 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setName(null)));
       // 测试 sex 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setSex(null)));
       // 测试 height 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHeight(null)));
       // 测试 age 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setAge(null)));
       // 测试 healthScore 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHealthScore(null)));
       // 测试 highLowShoulders 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHighLowShoulders(null)));
       // 测试 headAskew 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHeadAskew(null)));
       // 测试 leftLegKokd 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftLegKokd(null)));
       // 测试 rightLegKokd 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightLegKokd(null)));
       // 测试 longShortLeg 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLongShortLeg(null)));
       // 测试 pokingChin 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPokingChin(null)));
       // 测试 pelvicAnteversion 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPelvicAnteversion(null)));
       // 测试 leftKneeAnalysis 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftKneeAnalysis(null)));
       // 测试 rightKneeAnalysis 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightKneeAnalysis(null)));
       // 测试 leftRoundShoulder 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftRoundShoulder(null)));
       // 测试 rightRoundShoulder 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightRoundShoulder(null)));
       // 测试 focusTiltLeftRight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFocusTiltLeftRight(null)));
       // 测试 focusTiltForthBack 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFocusTiltForthBack(null)));
       // 测试 weight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setWeight(null)));
       // 测试 proteinRate 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setProteinRate(null)));
       // 测试 boneMassMositureContent 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBoneMassMositureContent(null)));
       // 测试 fatPercentage 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFatPercentage(null)));
       // 测试 musclePercentage 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setMusclePercentage(null)));
       // 测试 skeletalMuscleVolume 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setSkeletalMuscleVolume(null)));
       // 测试 bmi 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBmi(null)));
       // 测试 visceralFatGrade 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setVisceralFatGrade(null)));
       // 测试 idealWeight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setIdealWeight(null)));
       // 测试 physicalAge 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPhysicalAge(null)));
       // 测试 basalMetabolicRate 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBasalMetabolicRate(null)));
       // 测试 bust 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBust(null)));
       // 测试 waistline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setWaistline(null)));
       // 测试 hipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHipline(null)));
       // 测试 leftHipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftHipline(null)));
       // 测试 rightHipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightHipline(null)));
       // 测试 leftThigCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftThigCircumference(null)));
       // 测试 rightThigCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightThigCircumference(null)));
       // 测试 leftCalfCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftCalfCircumference(null)));
       // 测试 rightCalfCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightCalfCircumference(null)));
       // 测试 modelFileId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setModelFileId(null)));
       // 测试 userId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setUserId(null)));
       // 测试 shapeValue 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setShapeValue(null)));
       // 测试 shapeId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setShapeId(null)));
       // 测试 reportFilePath 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setReportFilePath(null)));
       // 准备参数
       ReportPageReqVO reqVO = new ReportPageReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setCode(null);
       reqVO.setName(null);
       reqVO.setSex(null);
       reqVO.setHeight(null);
       reqVO.setAge(null);
       reqVO.setHealthScore(null);
       reqVO.setHighLowShoulders(null);
       reqVO.setHeadAskew(null);
       reqVO.setLeftLegKokd(null);
       reqVO.setRightLegKokd(null);
       reqVO.setLongShortLeg(null);
       reqVO.setPokingChin(null);
       reqVO.setPelvicAnteversion(null);
       reqVO.setLeftKneeAnalysis(null);
       reqVO.setRightKneeAnalysis(null);
       reqVO.setLeftRoundShoulder(null);
       reqVO.setRightRoundShoulder(null);
       reqVO.setFocusTiltLeftRight(null);
       reqVO.setFocusTiltForthBack(null);
       reqVO.setWeight(null);
       reqVO.setProteinRate(null);
       reqVO.setBoneMassMositureContent(null);
       reqVO.setFatPercentage(null);
       reqVO.setMusclePercentage(null);
       reqVO.setSkeletalMuscleVolume(null);
       reqVO.setBmi(null);
       reqVO.setVisceralFatGrade(null);
       reqVO.setIdealWeight(null);
       reqVO.setPhysicalAge(null);
       reqVO.setBasalMetabolicRate(null);
       reqVO.setBust(null);
       reqVO.setWaistline(null);
       reqVO.setHipline(null);
       reqVO.setLeftHipline(null);
       reqVO.setRightHipline(null);
       reqVO.setLeftThigCircumference(null);
       reqVO.setRightThigCircumference(null);
       reqVO.setLeftCalfCircumference(null);
       reqVO.setRightCalfCircumference(null);
       reqVO.setModelFileId(null);
       reqVO.setUserId(null);
       reqVO.setShapeValue(null);
       reqVO.setShapeId(null);
       reqVO.setReportFilePath(null);

       // 调用
       PageResult<ReportDO> pageResult = reportService.getReportPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbReport, pageResult.getList().get(0));
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetReportList() {
       // mock 数据
       ReportDO dbReport = randomPojo(ReportDO.class, o -> { // 等会查询到
           o.setCreateTime(null);
           o.setCode(null);
           o.setName(null);
           o.setSex(null);
           o.setHeight(null);
           o.setAge(null);
           o.setHealthScore(null);
           o.setHighLowShoulders(null);
           o.setHeadAskew(null);
           o.setLeftLegKokd(null);
           o.setRightLegKokd(null);
           o.setLongShortLeg(null);
           o.setPokingChin(null);
           o.setPelvicAnteversion(null);
           o.setLeftKneeAnalysis(null);
           o.setRightKneeAnalysis(null);
           o.setLeftRoundShoulder(null);
           o.setRightRoundShoulder(null);
           o.setFocusTiltLeftRight(null);
           o.setFocusTiltForthBack(null);
           o.setWeight(null);
           o.setProteinRate(null);
           o.setBoneMassMositureContent(null);
           o.setFatPercentage(null);
           o.setMusclePercentage(null);
           o.setSkeletalMuscleVolume(null);
           o.setBmi(null);
           o.setVisceralFatGrade(null);
           o.setIdealWeight(null);
           o.setPhysicalAge(null);
           o.setBasalMetabolicRate(null);
           o.setBust(null);
           o.setWaistline(null);
           o.setHipline(null);
           o.setLeftHipline(null);
           o.setRightHipline(null);
           o.setLeftThigCircumference(null);
           o.setRightThigCircumference(null);
           o.setLeftCalfCircumference(null);
           o.setRightCalfCircumference(null);
           o.setModelFileId(null);
           o.setUserId(null);
           o.setShapeValue(null);
           o.setShapeId(null);
           o.setReportFilePath(null);
       });
       reportMapper.insert(dbReport);
       // 测试 createTime 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setCreateTime(null)));
       // 测试 code 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setCode(null)));
       // 测试 name 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setName(null)));
       // 测试 sex 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setSex(null)));
       // 测试 height 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHeight(null)));
       // 测试 age 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setAge(null)));
       // 测试 healthScore 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHealthScore(null)));
       // 测试 highLowShoulders 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHighLowShoulders(null)));
       // 测试 headAskew 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHeadAskew(null)));
       // 测试 leftLegKokd 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftLegKokd(null)));
       // 测试 rightLegKokd 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightLegKokd(null)));
       // 测试 longShortLeg 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLongShortLeg(null)));
       // 测试 pokingChin 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPokingChin(null)));
       // 测试 pelvicAnteversion 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPelvicAnteversion(null)));
       // 测试 leftKneeAnalysis 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftKneeAnalysis(null)));
       // 测试 rightKneeAnalysis 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightKneeAnalysis(null)));
       // 测试 leftRoundShoulder 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftRoundShoulder(null)));
       // 测试 rightRoundShoulder 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightRoundShoulder(null)));
       // 测试 focusTiltLeftRight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFocusTiltLeftRight(null)));
       // 测试 focusTiltForthBack 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFocusTiltForthBack(null)));
       // 测试 weight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setWeight(null)));
       // 测试 proteinRate 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setProteinRate(null)));
       // 测试 boneMassMositureContent 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBoneMassMositureContent(null)));
       // 测试 fatPercentage 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setFatPercentage(null)));
       // 测试 musclePercentage 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setMusclePercentage(null)));
       // 测试 skeletalMuscleVolume 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setSkeletalMuscleVolume(null)));
       // 测试 bmi 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBmi(null)));
       // 测试 visceralFatGrade 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setVisceralFatGrade(null)));
       // 测试 idealWeight 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setIdealWeight(null)));
       // 测试 physicalAge 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setPhysicalAge(null)));
       // 测试 basalMetabolicRate 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBasalMetabolicRate(null)));
       // 测试 bust 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setBust(null)));
       // 测试 waistline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setWaistline(null)));
       // 测试 hipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setHipline(null)));
       // 测试 leftHipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftHipline(null)));
       // 测试 rightHipline 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightHipline(null)));
       // 测试 leftThigCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftThigCircumference(null)));
       // 测试 rightThigCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightThigCircumference(null)));
       // 测试 leftCalfCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setLeftCalfCircumference(null)));
       // 测试 rightCalfCircumference 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setRightCalfCircumference(null)));
       // 测试 modelFileId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setModelFileId(null)));
       // 测试 userId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setUserId(null)));
       // 测试 shapeValue 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setShapeValue(null)));
       // 测试 shapeId 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setShapeId(null)));
       // 测试 reportFilePath 不匹配
       reportMapper.insert(cloneIgnoreId(dbReport, o -> o.setReportFilePath(null)));
       // 准备参数
       ReportExportReqVO reqVO = new ReportExportReqVO();
       reqVO.setCreateTime((new LocalDateTime[]{}));
       reqVO.setCode(null);
       reqVO.setName(null);
       reqVO.setSex(null);
       reqVO.setHeight(null);
       reqVO.setAge(null);
       reqVO.setHealthScore(null);
       reqVO.setHighLowShoulders(null);
       reqVO.setHeadAskew(null);
       reqVO.setLeftLegKokd(null);
       reqVO.setRightLegKokd(null);
       reqVO.setLongShortLeg(null);
       reqVO.setPokingChin(null);
       reqVO.setPelvicAnteversion(null);
       reqVO.setLeftKneeAnalysis(null);
       reqVO.setRightKneeAnalysis(null);
       reqVO.setLeftRoundShoulder(null);
       reqVO.setRightRoundShoulder(null);
       reqVO.setFocusTiltLeftRight(null);
       reqVO.setFocusTiltForthBack(null);
       reqVO.setWeight(null);
       reqVO.setProteinRate(null);
       reqVO.setBoneMassMositureContent(null);
       reqVO.setFatPercentage(null);
       reqVO.setMusclePercentage(null);
       reqVO.setSkeletalMuscleVolume(null);
       reqVO.setBmi(null);
       reqVO.setVisceralFatGrade(null);
       reqVO.setIdealWeight(null);
       reqVO.setPhysicalAge(null);
       reqVO.setBasalMetabolicRate(null);
       reqVO.setBust(null);
       reqVO.setWaistline(null);
       reqVO.setHipline(null);
       reqVO.setLeftHipline(null);
       reqVO.setRightHipline(null);
       reqVO.setLeftThigCircumference(null);
       reqVO.setRightThigCircumference(null);
       reqVO.setLeftCalfCircumference(null);
       reqVO.setRightCalfCircumference(null);
       reqVO.setModelFileId(null);
       reqVO.setUserId(null);
       reqVO.setShapeValue(null);
       reqVO.setShapeId(null);
       reqVO.setReportFilePath(null);

       // 调用
       List<ReportDO> list = reportService.getReportList(reqVO);
       // 断言
       assertEquals(1, list.size());
       assertPojoEquals(dbReport, list.get(0));
    }

}
