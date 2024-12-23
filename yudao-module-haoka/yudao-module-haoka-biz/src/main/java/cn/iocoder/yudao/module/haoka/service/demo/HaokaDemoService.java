package cn.iocoder.yudao.module.haoka.service.demo;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.haoka.controller.admin.demo.vo.*;
import cn.iocoder.yudao.module.haoka.dal.dataobject.demo.HaokaDemoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 好卡案例 Service 接口
 *
 * @author 芋道源码
 */
public interface HaokaDemoService {

    /**
     * 创建好卡案例
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDemo(@Valid HaokaDemoSaveReqVO createReqVO);

    /**
     * 更新好卡案例
     *
     * @param updateReqVO 更新信息
     */
    void updateDemo(@Valid HaokaDemoSaveReqVO updateReqVO);

    /**
     * 删除好卡案例
     *
     * @param id 编号
     */
    void deleteDemo(Long id);

    /**
     * 获得好卡案例
     *
     * @param id 编号
     * @return 好卡案例
     */
    HaokaDemoDO getDemo(Long id);

    /**
     * 获得好卡案例分页
     *
     * @param pageReqVO 分页查询
     * @return 好卡案例分页
     */
    PageResult<HaokaDemoDO> getDemoPage(HaokaDemoPageReqVO pageReqVO);

}