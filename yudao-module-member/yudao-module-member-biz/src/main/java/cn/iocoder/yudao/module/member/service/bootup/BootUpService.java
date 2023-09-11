package cn.iocoder.yudao.module.member.service.bootup;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.bootup.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.bootup.BootUpDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 用户启动数据 Service 接口
 *
 * @author 和尘同光
 */
public interface BootUpService {

    /**
     * 创建用户启动数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBootUp(@Valid BootUpCreateReqVO createReqVO);

    /**
     * 更新用户启动数据
     *
     * @param updateReqVO 更新信息
     */
    void updateBootUp(@Valid BootUpUpdateReqVO updateReqVO);

    /**
     * 删除用户启动数据
     *
     * @param id 编号
     */
    void deleteBootUp(Long id);

    /**
     * 获得用户启动数据
     *
     * @param id 编号
     * @return 用户启动数据
     */
    BootUpDO getBootUp(Long id);

    /**
     * 获得用户启动数据列表
     *
     * @param ids 编号
     * @return 用户启动数据列表
     */
    List<BootUpDO> getBootUpList(Collection<Long> ids);

    /**
     * 获得用户启动数据分页
     *
     * @param pageReqVO 分页查询
     * @return 用户启动数据分页
     */
    PageResult<BootUpDO> getBootUpPage(BootUpPageReqVO pageReqVO);

    /**
     * 获得用户启动数据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户启动数据列表
     */
    List<BootUpDO> getBootUpList(BootUpExportReqVO exportReqVO);

    /**
     * 查询数据
     * @param bootUpReqVO
     * @return
     */
    List<BootUpDO> getBootUpList(BootUpReqVO bootUpReqVO);
}
