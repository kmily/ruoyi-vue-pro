package cn.iocoder.yudao.module.hospital.api.medicalcare;

import cn.iocoder.yudao.module.hospital.service.medicalcare.MedicalCareService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
