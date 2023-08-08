package cn.iocoder.yudao.module.member.service.homepage;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.homepage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.homepage.HomePageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 首页配置 Service 接口
 *
 * @author 芋道源码
 */
public interface HomePageService {

    /**
     * 创建首页配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createHomePage(@Valid AppHomePageCreateReqVO createReqVO);

    /**
     * 更新首页配置
     *
     * @param updateReqVO 更新信息
     */
    void updateHomePage(@Valid AppHomePageUpdateReqVO updateReqVO);

    /**
     * 删除首页配置
     *
     * @param id 编号
     */
    void deleteHomePage(Long id);

    /**
     * 获得首页配置
     *
     * @param id 编号
     * @return 首页配置
     */
    HomePageDO getHomePage(Long id);

    /**
     * 获得首页配置列表
     *
     * @param ids 编号
     * @return 首页配置列表
     */
    List<HomePageDO> getHomePageList(Collection<Long> ids);

    /**
     * 获得首页配置分页
     *
     * @param pageReqVO 分页查询
     * @return 首页配置分页
     */
    PageResult<HomePageDO> getHomePagePage(AppHomePagePageReqVO pageReqVO);

    /**
     * 获得首页配置列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 首页配置列表
     */
    List<HomePageDO> getHomePageList(AppHomePageExportReqVO exportReqVO);


    /**
     * 初始化首页配置
     * @param familyId 家庭ID
     */
    List<HomePageDO> initialization(Long familyId);


    /**
     * 根据家庭查询首页数据
     * @param familyId 家庭类别
     * @return
     */
    List<HomePageDO> getHomePageList(Long familyId);

    /**
     * 首页数据卡片，绑定设备
     * @param id  卡片ID
     * @param devices 绑定的设备
     */
    void bindDevice(Long id, Set<Long> devices);

    /**
     * 修改或保存数据
     * @param updateReqVOS
     */
    void saveOrUpdate(List<AppHomePageUpdateReqVO> updateReqVOS);

}
