package cn.iocoder.yudao.module.scan.service.appversion;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.scan.controller.admin.appversion.vo.*;
import cn.iocoder.yudao.module.scan.dal.dataobject.appversion.AppVersionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 应用版本记录 Service 接口
 *
 * @author lyz
 */
public interface AppVersionService {

    /**
     * 创建应用版本记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAppVersion(@Valid AppVersionCreateReqVO createReqVO);

    /**
     * 更新应用版本记录
     *
     * @param updateReqVO 更新信息
     */
    void updateAppVersion(@Valid AppVersionUpdateReqVO updateReqVO);

    /**
     * 删除应用版本记录
     *
     * @param id 编号
     */
    void deleteAppVersion(Long id);

    /**
     * 获得应用版本记录
     *
     * @param id 编号
     * @return 应用版本记录
     */
    AppVersionDO getAppVersion(Long id);

    /**
     * 获得应用版本记录列表
     *
     * @param ids 编号
     * @return 应用版本记录列表
     */
    List<AppVersionDO> getAppVersionList(Collection<Long> ids);

    /**
     * 获得应用版本记录分页
     *
     * @param pageReqVO 分页查询
     * @return 应用版本记录分页
     */
    PageResult<AppVersionDO> getAppVersionPage(AppVersionPageReqVO pageReqVO);

    /**
     * 获得应用版本记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 应用版本记录列表
     */
    List<AppVersionDO> getAppVersionList(AppVersionExportReqVO exportReqVO);

    /**
     * 获得应用最新版本
     *
     * @return 应用版本最新版本
     */
    AppVersionDO getNewVersion();
}
