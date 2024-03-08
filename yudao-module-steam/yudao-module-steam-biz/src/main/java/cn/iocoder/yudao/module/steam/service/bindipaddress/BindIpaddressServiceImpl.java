package cn.iocoder.yudao.module.steam.service.bindipaddress;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.steam.controller.admin.bindipaddress.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.bindipaddress.BindIpaddressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.steam.dal.mysql.bindipaddress.BindIpaddressMapper;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.*;

/**
 * 绑定用户IP地址池 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class BindIpaddressServiceImpl implements BindIpaddressService {

    @Resource
    private BindIpaddressMapper bindIpaddressMapper;

    @Override
    public Long createBindIpaddress(BindIpaddressSaveReqVO createReqVO) {
        // 插入
        BindIpaddressDO bindIpaddress = BeanUtils.toBean(createReqVO, BindIpaddressDO.class);
        bindIpaddressMapper.insert(bindIpaddress);
        // 返回
        return bindIpaddress.getId();
    }

    @Override
    public void updateBindIpaddress(BindIpaddressSaveReqVO updateReqVO) {
        // 校验存在
        validateBindIpaddressExists(updateReqVO.getId());
        // 更新
        BindIpaddressDO updateObj = BeanUtils.toBean(updateReqVO, BindIpaddressDO.class);
        bindIpaddressMapper.updateById(updateObj);
    }

    @Override
    public void deleteBindIpaddress(Long id) {
        // 校验存在
        validateBindIpaddressExists(id);
        // 删除
        bindIpaddressMapper.deleteById(id);
    }

    private void validateBindIpaddressExists(Long id) {
        if (bindIpaddressMapper.selectById(id) == null) {
            throw exception(BIND_IPADDRESS_NOT_EXISTS);
        }
    }

    @Override
    public BindIpaddressDO getBindIpaddress(Long id) {
        return bindIpaddressMapper.selectById(id);
    }

    @Override
    public PageResult<BindIpaddressDO> getBindIpaddressPage(BindIpaddressPageReqVO pageReqVO) {
        return bindIpaddressMapper.selectPage(pageReqVO);
    }

}