package cn.iocoder.yudao.module.hospital.service.careaptitude;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.CareAptitudeAuditReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 医护资质 Service 接口
 *
 * @author 芋道源码
 */
public interface CareAptitudeService extends IService<CareAptitudeDO>{

    /**
     * 创建医护资质
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCareAptitude(@Valid AppCareAptitudeCreateReqVO createReqVO);

    /**
     * 更新医护资质
     *
     * @param updateReqVO 更新信息
     */
    void updateCareAptitude(@Valid AppCareAptitudeUpdateReqVO updateReqVO);

    /**
     * 删除医护资质
     *
     * @param id 编号
     */
    void deleteCareAptitude(Long id);

    /**
     * 获得医护资质
     *
     * @param id 编号
     * @return 医护资质
     */
    CareAptitudeDO getCareAptitude(Long id);

    /**
     * 获得医护资质列表
     *
     * @param careId 编号
     * @return 医护资质列表
     */
    List<CareAptitudeDO> getCareAptitudeList(Long careId);

    /**
     * 获得医护资质分页
     *
     * @param pageReqVO 分页查询
     * @return 医护资质分页
     */
    PageResult<CareAptitudeDO> getCareAptitudePage(AppCareAptitudePageReqVO pageReqVO);

    /**
     * 获得医护资质列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 医护资质列表
     */
    List<CareAptitudeDO> getCareAptitudeList(AppCareAptitudeExportReqVO exportReqVO);

    /**
     * 医护资质审核
     * @param auditVO 审核信息
     */
    void auditCareAptitude(CareAptitudeAuditReqVO auditVO);
}
