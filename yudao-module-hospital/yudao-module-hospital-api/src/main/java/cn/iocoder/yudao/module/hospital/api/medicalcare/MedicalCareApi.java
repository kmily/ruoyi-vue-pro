package cn.iocoder.yudao.module.hospital.api.medicalcare;

import cn.iocoder.yudao.module.hospital.api.medicalcare.dto.MedicalCareRepsDTO;

/**
 * @author whycode
 * @title: MedicalCareApi
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2011:31
 */
public interface MedicalCareApi {

    long createMedicalCare(Long memberId, String mobile);


    /**
     * 根据 编号查询 医护信息
     * @param id 医护编号
     * @return 医护信息
     */
    MedicalCareRepsDTO getMedicalCare(Long id);

    /**
     * 校验 医护人员
     * @param careId 医护编号
     * @return
     */
    MedicalCareRepsDTO validateMedicalCare(Long careId);


}
