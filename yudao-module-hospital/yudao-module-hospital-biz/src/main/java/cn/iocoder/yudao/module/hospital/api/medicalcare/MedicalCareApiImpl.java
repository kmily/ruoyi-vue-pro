package cn.iocoder.yudao.module.hospital.api.medicalcare;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.hospital.api.medicalcare.dto.MedicalCareRepsDTO;
import cn.iocoder.yudao.module.hospital.convert.medicalcare.MedicalCareConvert;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.MEDICAL_CARE_NOT_EXISTS;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.MEDICAL_CARE_STATUS_ERROR;

/**
 * @author whycode
 * @title: MedicalCareApiImpl
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2011:32
 */

@Service
public class MedicalCareApiImpl implements MedicalCareApi{

    @Resource
    private MedicalCareService medicalCareService;


    @Override
    public long createMedicalCare(Long memberId, String mobile) {
        return medicalCareService.createMedicalCare(memberId, mobile);
    }

    @Override
    public MedicalCareRepsDTO getMedicalCare(Long id) {
        MedicalCareDO medicalCare = medicalCareService.getMedicalCare(id);

        return MedicalCareConvert.INSTANCE.convert03(medicalCare);
    }

    @Override
    public MedicalCareRepsDTO validateMedicalCare(Long careId) {
        MedicalCareDO medicalCare = medicalCareService.getMedicalCare(careId);
        if (medicalCare == null) {
            throw exception(MEDICAL_CARE_NOT_EXISTS);
        }else if(!CommonStatusEnum.OPEN.name().equals(medicalCare.getStatus())){
            throw exception(MEDICAL_CARE_STATUS_ERROR);
        }
        return MedicalCareConvert.INSTANCE.convert03(medicalCare);
    }

    @Override
    public Map<Long, MedicalCareRepsDTO> getMedicalCareMap(Set<Long> ids) {
        List<MedicalCareRepsDTO> dtoList = MedicalCareConvert.INSTANCE.convertList03(medicalCareService.getMedicalCareList(ids));
        return CollectionUtils.convertMap(dtoList, MedicalCareRepsDTO::getId);
    }

    @Override
    public MedicalCareRepsDTO getMedicalCareByMember(Long userId) {
        return MedicalCareConvert.INSTANCE.convert03(medicalCareService.getByMemberId(userId));
    }

    @Override
    public void updateComment(Long careId, Integer scores) {

        medicalCareService.updateCareComment(careId, scores);

    }
}
