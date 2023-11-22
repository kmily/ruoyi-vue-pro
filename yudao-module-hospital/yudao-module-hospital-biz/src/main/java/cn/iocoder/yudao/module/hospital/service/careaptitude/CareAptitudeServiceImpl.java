package cn.iocoder.yudao.module.hospital.service.careaptitude;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.CareAptitudeAuditReqVO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;

import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;
import cn.iocoder.yudao.module.hospital.service.aptitude.AptitudeService;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.careaptitude.CareAptitudeConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.careaptitude.CareAptitudeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareStatusEnum.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 医护资质 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CareAptitudeServiceImpl extends ServiceImpl<CareAptitudeMapper, CareAptitudeDO> implements CareAptitudeService {

    @Resource
    private CareAptitudeMapper careAptitudeMapper;

    @Resource
    private AptitudeService aptitudeService;

    @Resource
    private MedicalCareService medicalCareService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CareAptitudeCheckLogService checkLogService;

    @Override
    public Long createCareAptitude(AppCareAptitudeCreateReqVO createReqVO) {
        // 插入
        CareAptitudeDO careAptitude = CareAptitudeConvert.INSTANCE.convert(createReqVO);
        AptitudeDO aptitude = aptitudeService.getAptitude(careAptitude.getAptitudeId());
        if(Objects.isNull(aptitude)){
            throw exception(APTITUDE_NOT_EXISTS);
        }else if(Objects.equals(aptitude.getStatus().intValue(), CommonStatusEnum.DISABLE.getStatus())){
            throw exception(APTITUDE_NOT_ENABLE);
        }
        careAptitude.setAptitudeName(aptitude.getName());
        careAptitude.setCheckStatus(APPLYING.value());

        CareAptitudeDO aptitudeDO = this.getOne(new LambdaQueryWrapper<CareAptitudeDO>().eq(CareAptitudeDO::getCareId, careAptitude.getCareId())
                .eq(CareAptitudeDO::getAptitudeId, careAptitude.getAptitudeId()));
        if(Objects.nonNull(aptitudeDO)){
            careAptitude.setId(aptitudeDO.getId());
            careAptitudeMapper.updateById(careAptitude);

            // TODO  更新资质时是否要处理如果没有存在已认证的资质，把医护资质认证设置为 NO

        }else{
            careAptitudeMapper.insert(careAptitude);
        }

        // 返回
        return careAptitude.getId();
    }

    @Override
    public void updateCareAptitude(AppCareAptitudeUpdateReqVO updateReqVO) {
        // 校验存在
        validateCareAptitudeExists(updateReqVO.getId());
        // 更新
        CareAptitudeDO updateObj = CareAptitudeConvert.INSTANCE.convert(updateReqVO);
        careAptitudeMapper.updateById(updateObj);
    }

    @Override
    public void deleteCareAptitude(Long id) {
        // 校验存在
        validateCareAptitudeExists(id);
        // 删除
        careAptitudeMapper.deleteById(id);
    }

    private CareAptitudeDO validateCareAptitudeExists(Long id) {
        CareAptitudeDO careAptitudeDO = careAptitudeMapper.selectById(id);
        if ( careAptitudeDO == null) {
            throw exception(CARE_APTITUDE_NOT_EXISTS);
        }
        return careAptitudeDO;
    }

    @Override
    public CareAptitudeDO getCareAptitude(Long id) {
        return careAptitudeMapper.selectById(id);
    }

    @Override
    public List<CareAptitudeDO> getCareAptitudeList(Long careId) {
        return careAptitudeMapper.selectList(CareAptitudeDO::getCareId, careId);
    }

    @Override
    public PageResult<CareAptitudeDO> getCareAptitudePage(AppCareAptitudePageReqVO pageReqVO) {
        return careAptitudeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CareAptitudeDO> getCareAptitudeList(AppCareAptitudeExportReqVO exportReqVO) {
        return careAptitudeMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditCareAptitude(CareAptitudeAuditReqVO auditVO) {
        Long id = auditVO.getId();
        CareAptitudeDO aptitudeDO = validateCareAptitudeExists(id);
        AdminUserRespDTO user = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        CareAptitudeDO careAptitudeDO = new CareAptitudeDO()
                .setId(auditVO.getId())
                .setOpinion(auditVO.getOpinion())
                .setCheckStatus(auditVO.getStatus())
                .setCheckName(user.getNickname())
                .setCheckTime(LocalDateTime.now());
        this.updateById(careAptitudeDO);
        if(Objects.equals(auditVO.getStatus(), OPEN.value())){
            // 审核通过
            medicalCareService.updateMedicalCareAptitude(aptitudeDO.getCareId());
        }

        checkLogService.save(new CareAptitudeCheckLogDO()
                .setCareId(aptitudeDO.getCareId())
                .setAptitudeId(aptitudeDO.getAptitudeId())
                .setOpinion(auditVO.getOpinion())
                .setCheckStatus(auditVO.getStatus())
                .setCheckName(user.getNickname())
                .setCheckTime(LocalDateTime.now()));
    }

}
