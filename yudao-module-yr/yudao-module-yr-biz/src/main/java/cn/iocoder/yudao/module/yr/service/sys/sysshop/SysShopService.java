package cn.iocoder.yudao.module.yr.service.sys.sysshop;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopExportReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopPageReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.sysshop.vo.SysShopUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.sysshop.SysShopDO;

/**
 * 店面 Service 接口
 *
 * @author 芋道源码
 */
public interface SysShopService {

    /**
     * 创建店面
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSysShop(@Valid SysShopCreateReqVO createReqVO);

    /**
     * 更新店面
     *
     * @param updateReqVO 更新信息
     */
    void updateSysShop(@Valid SysShopUpdateReqVO updateReqVO);

    /**
     * 删除店面
     *
     * @param id 编号
     */
    void deleteSysShop(Long id);

    /**
     * 获得店面
     *
     * @param id 编号
     * @return 店面
     */
    SysShopDO getSysShop(Long id);

    /**
     * 获得店面列表
     *
     * @param ids 编号
     * @return 店面列表
     */
    List<SysShopDO> getSysShopList(Collection<Long> ids);

    /**
     * 获得店面分页
     *
     * @param pageReqVO 分页查询
     * @return 店面分页
     */
    PageResult<SysShopDO> getSysShopPage(SysShopPageReqVO pageReqVO);

    /**
     * 获得店面列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 店面列表
     */
    List<SysShopDO> getSysShopList(SysShopExportReqVO exportReqVO);

}
