package cn.iocoder.yudao.module.steam.service.youyoudetails;

import cn.iocoder.yudao.module.steam.service.youyouorder.YouyouOrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.youyoudetails.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoudetails.YouyouDetailsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoudetails.YouyouDetailsMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 用户查询明细 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class YouyouDetailsServiceImpl implements YouyouDetailsService {

    @Resource
    private YouyouDetailsMapper youyouDetailsMapper;


    @Override
    public Integer createYouyouDetails(YouyouDetailsSaveReqVO createReqVO) {
        // 插入
        YouyouDetailsDO youyouDetails = BeanUtils.toBean(createReqVO, YouyouDetailsDO.class);
        youyouDetailsMapper.insert(youyouDetails);
        // 返回
        return youyouDetails.getId();
    }

    @Override
    public void updateYouyouDetails(YouyouDetailsSaveReqVO updateReqVO) {
        // 校验存在
        validateYouyouDetailsExists(updateReqVO.getId());
        // 更新
        YouyouDetailsDO updateObj = BeanUtils.toBean(updateReqVO, YouyouDetailsDO.class);
        youyouDetailsMapper.updateById(updateObj);
    }

    @Override
    public void deleteYouyouDetails(Integer id) {
        // 校验存在
        validateYouyouDetailsExists(id);
        // 删除
        youyouDetailsMapper.deleteById(id);
    }

    private void validateYouyouDetailsExists(Integer id) {
        if (youyouDetailsMapper.selectById(id) == null) {
            throw exception(YOUYOU_DETAILS_NOT_EXISTS);
        }
    }

    @Override
    public YouyouDetailsDO getYouyouDetails(Integer id) {
        return youyouDetailsMapper.selectById(id);
    }

    @Override
    public PageResult<YouyouDetailsDO> getYouyouDetailsPage(YouyouDetailsPageReqVO pageReqVO) {
        return youyouDetailsMapper.selectPage(pageReqVO);
    }

}