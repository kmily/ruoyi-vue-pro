package cn.iocoder.yudao.module.steam.service.hotwords;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.hotwords.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.hotwords.HotWordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.hotwords.HotWordsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 热词搜索 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class HotWordsServiceImpl implements HotWordsService {

    @Resource
    private HotWordsMapper hotWordsMapper;

    @Override
    public Integer createHotWords(HotWordsSaveReqVO createReqVO) {
        // 插入
        HotWordsDO hotWords = BeanUtils.toBean(createReqVO, HotWordsDO.class);
        hotWordsMapper.insert(hotWords);
        // 返回
        return hotWords.getId();
    }

    @Override
    public void updateHotWords(HotWordsSaveReqVO updateReqVO) {
        // 校验存在
        validateHotWordsExists(updateReqVO.getId());
        // 更新
        HotWordsDO updateObj = BeanUtils.toBean(updateReqVO, HotWordsDO.class);
        hotWordsMapper.updateById(updateObj);
    }

    @Override
    public void deleteHotWords(Integer id) {
        // 校验存在
        validateHotWordsExists(id);
        // 删除
        hotWordsMapper.deleteById(id);
    }

    private void validateHotWordsExists(Integer id) {
        if (hotWordsMapper.selectById(id) == null) {
            throw exception(HOT_WORDS_NOT_EXISTS);
        }
    }

    @Override
    public HotWordsDO getHotWords(Integer id) {
        return hotWordsMapper.selectById(id);
    }

    @Override
    public PageResult<HotWordsDO> getHotWordsPage(HotWordsPageReqVO pageReqVO) {
        return hotWordsMapper.selectPage(pageReqVO);
    }

}