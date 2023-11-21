package cn.iocoder.yudao.module.hospital.api.medicalcare;

/**
 * @author whycode
 * @title: MedicalCareApi
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/2011:31
 */
public interface MedicalCareApi {

    long createMedicalCare(Long memberId, String mobile);
}
