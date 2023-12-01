package cn.iocoder.yudao.module.system.service.helpcenter;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterCreateReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.helpcenter.vo.HelpCenterUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.helpcenter.HelpCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 常见问题 Service 接口
 *
 * @author 芋道源码
 */
public interface HelpCenterService extends IService<HelpCenterDO>{

    /**
     * 创建常见问题
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHelpCenter(@Valid HelpCenterCreateReqVO createReqVO);

    /**
     * 更新常见问题
     *
     * @param updateReqVO 更新信息
     */
    void updateHelpCenter(@Valid HelpCenterUpdateReqVO updateReqVO);

    /**
     * 删除常见问题
     *
     * @param id 编号
     */
    void deleteHelpCenter(Long id);

    /**
     * 获得常见问题
     *
     * @param id 编号
     * @return 常见问题
     */
    HelpCenterDO getHelpCenter(Long id);

    /**
     * 获得常见问题列表
     *
     * @param ids 编号
     * @return 常见问题列表
     */
    List<HelpCenterDO> getHelpCenterList(Collection<Long> ids);

    /**
     * 获得常见问题分页
     *
     * @param pageReqVO 分页查询
     * @return 常见问题分页
     */
    PageResult<HelpCenterDO> getHelpCenterPage(HelpCenterPageReqVO pageReqVO);

    /**
     * 获得常见问题列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 常见问题列表
     */
    List<HelpCenterDO> getHelpCenterList(HelpCenterExportReqVO exportReqVO);

}
