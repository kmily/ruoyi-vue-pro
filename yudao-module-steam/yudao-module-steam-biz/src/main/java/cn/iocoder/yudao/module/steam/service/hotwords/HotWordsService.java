package cn.iocoder.yudao.module.steam.service.hotwords;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 热词搜索 Service 接口
 *
 * @author 管理员
 */
public interface HotWordsService {

    /**
     * 创建热词搜索
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createHotWords(@Valid HotWordsSaveReqVO createReqVO);

    /**
     * 更新热词搜索
     *
     * @param updateReqVO 更新信息
     */
    void updateHotWords(@Valid HotWordsSaveReqVO updateReqVO);

    /**
     * 删除热词搜索
     *
     * @param id 编号
     */
    void deleteHotWords(Integer id);

    /**
     * 获得热词搜索
     *
     * @param id 编号
     * @return 热词搜索
     */
    HotWordsDO getHotWords(Integer id);

    /**
     * 获得热词搜索分页
     *
     * @param pageReqVO 分页查询
     * @return 热词搜索分页
     */
    PageResult<HotWordsDO> getHotWordsPage(HotWordsPageReqVO pageReqVO);

}