package cn.iocoder.yudao.module.hospital.service.medicalcarechecklog;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.medicalcarechecklog.MedicalCareCheckLogConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.medicalcarechecklog.MedicalCareCheckLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 医护审核记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MedicalCareCheckLogServiceImpl extends ServiceImpl<MedicalCareCheckLogMapper, MedicalCareCheckLogDO> implements MedicalCareCheckLogService {

    @Resource
    private MedicalCareCheckLogMapper medicalCareCheckLogMapper;

    @Override
    public Long createMedicalCareCheckLog(MedicalCareCheckLogCreateReqVO createReqVO) {
        // 插入
        MedicalCareCheckLogDO medicalCareCheckLog = MedicalCareCheckLogConvert.INSTANCE.convert(createReqVO);
        medicalCareCheckLogMapper.insert(medicalCareCheckLog);
        // 返回
        return medicalCareCheckLog.getId();
    }

    @Override
    public void updateMedicalCareCheckLog(MedicalCareCheckLogUpdateReqVO updateReqVO) {
        // 校验存在
        validateMedicalCareCheckLogExists(updateReqVO.getId());
        // 更新
        MedicalCareCheckLogDO updateObj = MedicalCareCheckLogConvert.INSTANCE.convert(updateReqVO);
        medicalCareCheckLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteMedicalCareCheckLog(Long id) {
        // 校验存在
        validateMedicalCareCheckLogExists(id);
        // 删除
        medicalCareCheckLogMapper.deleteById(id);
    }

    private void validateMedicalCareCheckLogExists(Long id) {
        if (medicalCareCheckLogMapper.selectById(id) == null) {
           //throw exception(MEDICAL_CARE_CHECK_LOG_NOT_EXISTS);
        }
    }

    @Override
    public MedicalCareCheckLogDO getMedicalCareCheckLog(Long id) {
        return medicalCareCheckLogMapper.selectById(id);
    }

    @Override
    public List<MedicalCareCheckLogDO> getMedicalCareCheckLogList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return medicalCareCheckLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MedicalCareCheckLogDO> getMedicalCareCheckLogPage(MedicalCareCheckLogPageReqVO pageReqVO) {
        return medicalCareCheckLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MedicalCareCheckLogDO> getMedicalCareCheckLogList(MedicalCareCheckLogExportReqVO exportReqVO) {
        return medicalCareCheckLogMapper.selectList(exportReqVO);
    }

}
