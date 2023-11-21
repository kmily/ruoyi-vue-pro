package cn.iocoder.yudao.module.hospital.service.medicalcarechecklog;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcarechecklog.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 医护审核记录 Service 接口
 *
 * @author 芋道源码
 */
public interface MedicalCareCheckLogService extends IService<MedicalCareCheckLogDO>{

    /**
     * 创建医护审核记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMedicalCareCheckLog(@Valid MedicalCareCheckLogCreateReqVO createReqVO);

    /**
     * 更新医护审核记录
     *
     * @param updateReqVO 更新信息
     */
    void updateMedicalCareCheckLog(@Valid MedicalCareCheckLogUpdateReqVO updateReqVO);

    /**
     * 删除医护审核记录
     *
     * @param id 编号
     */
    void deleteMedicalCareCheckLog(Long id);

    /**
     * 获得医护审核记录
     *
     * @param id 编号
     * @return 医护审核记录
     */
    MedicalCareCheckLogDO getMedicalCareCheckLog(Long id);

    /**
     * 获得医护审核记录列表
     *
     * @param ids 编号
     * @return 医护审核记录列表
     */
    List<MedicalCareCheckLogDO> getMedicalCareCheckLogList(Collection<Long> ids);

    /**
     * 获得医护审核记录分页
     *
     * @param pageReqVO 分页查询
     * @return 医护审核记录分页
     */
    PageResult<MedicalCareCheckLogDO> getMedicalCareCheckLogPage(MedicalCareCheckLogPageReqVO pageReqVO);

    /**
     * 获得医护审核记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 医护审核记录列表
     */
    List<MedicalCareCheckLogDO> getMedicalCareCheckLogList(MedicalCareCheckLogExportReqVO exportReqVO);

}
