package cn.iocoder.yudao.module.hospital.api.medicalcare;

import cn.iocoder.yudao.module.hospital.api.medicalcare.dto.MedicalCareRepsDTO;

import java.util.Map;
import java.util.Set;

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


    /**
     * 查询医护信息
     * @param ids 医护Id
     * @return
     */
    Map<Long, MedicalCareRepsDTO> getMedicalCareMap(Set<Long> ids);

    /**
     * 根据用户ID 查询医护ID
     * @param userId 用户ID
     * @return
     */
    MedicalCareRepsDTO getMedicalCareByMember(Long userId);

    /**
     * 更新医护的评分
     * @param scores 分数
     * @param careId 医护编号
     */
    void updateComment(Long careId, Integer scores);
}
