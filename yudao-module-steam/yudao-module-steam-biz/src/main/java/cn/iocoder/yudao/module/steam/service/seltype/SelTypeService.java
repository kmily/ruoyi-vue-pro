package cn.iocoder.yudao.module.steam.service.seltype;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.seltype.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelTypeDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.seltype.SelWeaponDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 类型选择 Service 接口
 *
 * @author glzaboy
 */
public interface SelTypeService {

    /**
     * 创建类型选择
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSelType(@Valid SelTypeSaveReqVO createReqVO);

    /**
     * 更新类型选择
     *
     * @param updateReqVO 更新信息
     */
    void updateSelType(@Valid SelTypeSaveReqVO updateReqVO);

    /**
     * 删除类型选择
     *
     * @param id 编号
     */
    void deleteSelType(Long id);

    /**
     * 获得类型选择
     *
     * @param id 编号
     * @return 类型选择
     */
    SelTypeDO getSelType(Long id);

    /**
     * 获得类型选择分页
     *
     * @param pageReqVO 分页查询
     * @return 类型选择分页
     */
    PageResult<SelTypeDO> getSelTypePage(SelTypePageReqVO pageReqVO);

    // ==================== 子表（武器选择） ====================

    /**
     * 获得武器选择分页
     *
     * @param pageReqVO 分页查询
     * @param typeId 类型选择编号
     * @return 武器选择分页
     */
    PageResult<SelWeaponDO> getSelWeaponPage(PageParam pageReqVO, Long typeId);

    /**
     * 创建武器选择
     *
     * @param selWeapon 创建信息
     * @return 编号
     */
    Long createSelWeapon(@Valid SelWeaponDO selWeapon);

    /**
     * 更新武器选择
     *
     * @param selWeapon 更新信息
     */
    void updateSelWeapon(@Valid SelWeaponDO selWeapon);

    /**
     * 删除武器选择
     *
     * @param id 编号
     */
    void deleteSelWeapon(Long id);

	/**
	 * 获得武器选择
	 *
	 * @param id 编号
     * @return 武器选择
	 */
    SelWeaponDO getSelWeapon(Long id);

}