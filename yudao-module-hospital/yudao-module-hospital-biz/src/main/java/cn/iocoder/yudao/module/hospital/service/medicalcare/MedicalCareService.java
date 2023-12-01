package cn.iocoder.yudao.module.hospital.service.medicalcare;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppMedicalCarePageReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppMedicalCarePerfectVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppRealNameReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.CareFavoritePageReqVO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareStatusEnum;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 医护管理 Service 接口
 *
 * @author 芋道源码
 */
public interface MedicalCareService extends IService<MedicalCareDO>{

    /**
     * 创建医护管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMedicalCare(@Valid MedicalCareCreateReqVO createReqVO);

    /**
     * 更新医护管理
     *
     * @param updateReqVO 更新信息
     */
    void updateMedicalCare(@Valid MedicalCareUpdateReqVO updateReqVO);

    /**
     * 删除医护管理
     *
     * @param id 编号
     */
    void deleteMedicalCare(Long id);

    /**
     * 获得医护管理
     *
     * @param id 编号
     * @return 医护管理
     */
    MedicalCareDO getMedicalCare(Long id);

    /**
     * 获得医护管理列表
     *
     * @param ids 编号
     * @return 医护管理列表
     */
    List<MedicalCareDO> getMedicalCareList(Collection<Long> ids);

    /**
     * 获得医护管理分页
     *
     * @param pageReqVO 分页查询
     * @return 医护管理分页
     */
    PageResult<MedicalCareDO> getMedicalCarePage(MedicalCarePageReqVO pageReqVO);

    /**
     * 获得医护管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 医护管理列表
     */
    List<MedicalCareDO> getMedicalCareList(MedicalCareExportReqVO exportReqVO);

    /**
     * 根据手机号查询医护人员
     * @param mobile 手机号
     * @return 返回医护信息
     */
    MedicalCareDO selectOneByMobile(String mobile);

    /**
     * 根据会员 编号查询医护信息
     * @param memberId 会员编号
     * @return 医护信息
     */
    MedicalCareDO getByMemberId(Long memberId);

    /**
     * 创建医护人员
     * @param memberId 会员ID
     * @param mobile 手机号
     * @return
     */
    long createMedicalCare(Long memberId, String mobile);

    /**
     * 禁用或启用医护
     * @param id 医护编号
     * @param status 状态
     */
    void closeOrOpenMedicalCare(Long id, MedicalCareStatusEnum status);

    /**
     * 审核医护
     * @param auditVO 审核请求
     */
    void auditMedicalCare(MedicalCareAuditVO auditVO);

    /**
     * 完善医护信息
     * @param perfectVO
     */
    void perfectMedicalCare(AppMedicalCarePerfectVO perfectVO);

    void realNameMedicalCare(AppRealNameReqVO realNameReqVO);

    /**
     * 更新医护资质认证
     * @param careId 医护编号
     */
    void updateMedicalCareAptitude(Long careId);

    /**
     * APP端分页查询
     * @param pageVO 查询条件
     * @return
     */
    PageResult<MedicalCareDO> getCareAptitudePage(AppMedicalCarePageReqVO pageVO);


    /**
     * 更新医护人员评价得分
     * @param id 医护编号
     * @param commentScore 此次评分
     */
    void updateCareComment(Long id, Integer commentScore);

    /**
     * 查询我的收藏
     * @param pageVO 查询条件
     * @return
     */
    PageResult<MedicalCareDO> getCareFavoritePage(CareFavoritePageReqVO pageVO);
}
