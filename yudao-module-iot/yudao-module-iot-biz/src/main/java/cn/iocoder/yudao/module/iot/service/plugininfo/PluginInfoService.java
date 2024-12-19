package cn.iocoder.yudao.module.iot.service.plugininfo;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.iot.controller.admin.plugininfo.vo.PluginInfoPageReqVO;
import cn.iocoder.yudao.module.iot.controller.admin.plugininfo.vo.PluginInfoSaveReqVO;
import cn.iocoder.yudao.module.iot.dal.dataobject.plugininfo.PluginInfoDO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

/**
 * IoT 插件信息 Service 接口
 *
 * @author 芋道源码
 */
public interface PluginInfoService {

    /**
     * 创建IoT 插件信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPluginInfo(@Valid PluginInfoSaveReqVO createReqVO);

    /**
     * 更新IoT 插件信息
     *
     * @param updateReqVO 更新信息
     */
    void updatePluginInfo(@Valid PluginInfoSaveReqVO updateReqVO);

    /**
     * 删除IoT 插件信息
     *
     * @param id 编号
     */
    void deletePluginInfo(Long id);

    /**
     * 获得IoT 插件信息
     *
     * @param id 编号
     * @return IoT 插件信息
     */
    PluginInfoDO getPluginInfo(Long id);

    /**
     * 获得IoT 插件信息分页
     *
     * @param pageReqVO 分页查询
     * @return IoT 插件信息分页
     */
    PageResult<PluginInfoDO> getPluginInfoPage(PluginInfoPageReqVO pageReqVO);

    /**
     * 上传插件的 JAR 包
     *
     * @param id   插件id
     * @param file 文件
     */
    void uploadJar(Long id, MultipartFile file);

    /**
     * 更新插件的状态
     *
     * @param id     插件id
     * @param status 状态
     */
    void updatePluginStatus(Long id, Integer status);
}