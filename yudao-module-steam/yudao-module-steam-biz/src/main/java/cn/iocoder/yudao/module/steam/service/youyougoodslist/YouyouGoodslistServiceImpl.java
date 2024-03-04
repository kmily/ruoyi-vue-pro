package cn.iocoder.yudao.module.steam.service.youyougoodslist;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyougoodslist.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyougoodslist.YouyouGoodslistDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.youyougoodslist.YouyouGoodslistMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 查询商品列 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class YouyouGoodslistServiceImpl implements YouyouGoodslistService {

    @Resource
    private YouyouGoodslistMapper youyouGoodslistMapper;

    @Override
    public Integer createYouyouGoodslist(YouyouGoodslistSaveReqVO createReqVO) {
        // 插入
        YouyouGoodslistDO youyouGoodslist = BeanUtils.toBean(createReqVO, YouyouGoodslistDO.class);
        youyouGoodslistMapper.insert(youyouGoodslist);
        // 返回
        return youyouGoodslist.getId();
    }

    @Override
    public void updateYouyouGoodslist(YouyouGoodslistSaveReqVO updateReqVO) {
        // 校验存在
        validateYouyouGoodslistExists(updateReqVO.getId());
        // 更新
        YouyouGoodslistDO updateObj = BeanUtils.toBean(updateReqVO, YouyouGoodslistDO.class);
        youyouGoodslistMapper.updateById(updateObj);
    }

    @Override
    public void deleteYouyouGoodslist(Integer id) {
        // 校验存在
        validateYouyouGoodslistExists(id);
        // 删除
        youyouGoodslistMapper.deleteById(id);
    }

    private void validateYouyouGoodslistExists(Integer id) {
        if (youyouGoodslistMapper.selectById(id) == null) {
            throw exception(YOUYOU_GOODSLIST_NOT_EXISTS);
        }
    }

    @Override
    public YouyouGoodslistDO getYouyouGoodslist(Integer id) {
        return youyouGoodslistMapper.selectById(id);
    }

    @Override
    public PageResult<YouyouGoodslistDO> getYouyouGoodslistPage(YouyouGoodslistPageReqVO pageReqVO) {
        return youyouGoodslistMapper.selectPage(pageReqVO);
    }

}