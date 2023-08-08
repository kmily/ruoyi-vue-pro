package cn.iocoder.yudao.module.member.service.family;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.family.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 用户家庭 Service 接口
 *
 * @author 芋道源码
 */
public interface FamilyService {

    /**
     * 创建用户家庭
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFamily(@Valid FamilyCreateReqVO createReqVO);

    /**
     * 更新用户家庭
     *
     * @param updateReqVO 更新信息
     */
    void updateFamily(@Valid FamilyUpdateReqVO updateReqVO);

    /**
     * 删除用户家庭
     *
     * @param id 编号
     */
    void deleteFamily(Long id);

    /**
     * 获得用户家庭
     *
     * @param id 编号
     * @return 用户家庭
     */
    FamilyDO getFamily(Long id);

    /**
     * 获得用户家庭列表
     *
     * @param ids 编号
     * @return 用户家庭列表
     */
    List<FamilyDO> getFamilyList(Collection<Long> ids);

    /**
     * 获得用户家庭分页
     *
     * @param pageReqVO 分页查询
     * @return 用户家庭分页
     */
    PageResult<FamilyDO> getFamilyPage(FamilyPageReqVO pageReqVO);

    /**
     * 获得用户家庭列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户家庭列表
     */
    List<FamilyDO> getFamilyList(FamilyExportReqVO exportReqVO);

    /**
     * 新增预警手机号
     * @return
     */
    Collection<String> addMobile(FamilyAddMobileVO mobileVO);

    /**
     * 修改手机号
     * @return
     */
    Collection<String> updateMobile(FamilyUpdateMobileVO mobileVO);

    /**
     * 删除手机号
     * @param mobileVO
     * @return
     */
    Collection<String> deleteMobile(FamilyAddMobileVO mobileVO);

    /**
     * 创建家庭
     * @param userId 会员ID
     * @param mobile 手机号
     */
    Long createFamily(Long userId, String nickName, String mobile);
}
