package cn.iocoder.yudao.module.steam.service.youyoucommodity;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.YouyouCommodityMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 悠悠商品列表 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class YouyouCommodityServiceImpl implements YouyouCommodityService {

    @Resource
    private YouyouCommodityMapper youyouCommodityMapper;

    @Override
    public Integer createYouyouCommodity(YouyouCommoditySaveReqVO createReqVO) {
        // 插入
        YouyouCommodityDO youyouCommodity = BeanUtils.toBean(createReqVO, YouyouCommodityDO.class);
        youyouCommodityMapper.insert(youyouCommodity);
        // 返回
        return youyouCommodity.getId();
    }

    @Override
    public void updateYouyouCommodity(YouyouCommoditySaveReqVO updateReqVO) {
        // 校验存在
        validateYouyouCommodityExists(updateReqVO.getId());
        // 更新
        YouyouCommodityDO updateObj = BeanUtils.toBean(updateReqVO, YouyouCommodityDO.class);
        youyouCommodityMapper.updateById(updateObj);
    }

    @Override
    public void deleteYouyouCommodity(Integer id) {
        // 校验存在
        validateYouyouCommodityExists(id);
        // 删除
        youyouCommodityMapper.deleteById(id);
    }

    private void validateYouyouCommodityExists(Integer id) {
        if (youyouCommodityMapper.selectById(id) == null) {
            throw exception(YOUYOU_COMMODITY_NOT_EXISTS);
        }
    }

    @Override
    public YouyouCommodityDO getYouyouCommodity(Integer id) {
        return youyouCommodityMapper.selectById(id);
    }

    @Override
    public PageResult<YouyouCommodityDO> getYouyouCommodityPage(YouyouCommodityPageReqVO pageReqVO) {
        return youyouCommodityMapper.selectPage(pageReqVO);
    }

}