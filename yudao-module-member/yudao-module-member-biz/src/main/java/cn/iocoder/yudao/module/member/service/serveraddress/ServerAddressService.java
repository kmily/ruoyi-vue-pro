package cn.iocoder.yudao.module.member.service.serveraddress;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 服务地址 Service 接口
 *
 * @author 芋道源码
 */
public interface ServerAddressService extends IService<ServerAddressDO>{

    /**
     * 创建服务地址
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createServerAddress(@Valid ServerAddressCreateReqVO createReqVO);

    /**
     * 更新服务地址
     *
     * @param updateReqVO 更新信息
     */
    void updateServerAddress(@Valid ServerAddressUpdateReqVO updateReqVO);

    /**
     * 删除服务地址
     *
     * @param id 编号
     */
    void deleteServerAddress(Long id);

    /**
     * 获得服务地址
     *
     * @param id 编号
     * @return 服务地址
     */
    ServerAddressDO getServerAddress(Long id);

    /**
     * 获得服务地址列表
     *
     * @param ids 编号
     * @return 服务地址列表
     */
    List<ServerAddressDO> getServerAddressList(Collection<Long> ids);

    /**
     * 获得服务地址分页
     *
     * @param pageReqVO 分页查询
     * @return 服务地址分页
     */
    PageResult<ServerAddressDO> getServerAddressPage(ServerAddressPageReqVO pageReqVO);

    /**
     * 获得服务地址列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 服务地址列表
     */
    List<ServerAddressDO> getServerAddressList(ServerAddressExportReqVO exportReqVO);

    /**
     * 根据id和userId获取服务地址
     */
    ServerAddressDO getServerAddressApiDTO(Long id,Long usrId);
}
