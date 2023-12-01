package cn.iocoder.yudao.module.member.service.serveraddress;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.serveraddress.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serveraddress.ServerAddressDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.serveraddress.ServerAddressConvert;
import cn.iocoder.yudao.module.member.dal.mysql.serveraddress.ServerAddressMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 服务地址 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ServerAddressServiceImpl extends ServiceImpl<ServerAddressMapper, ServerAddressDO> implements ServerAddressService {

    @Resource
    private ServerAddressMapper serverAddressMapper;

    @Override
    public Long createServerAddress(ServerAddressCreateReqVO createReqVO) {
        // 插入
        ServerAddressDO serverAddress = ServerAddressConvert.INSTANCE.convert(createReqVO);
        serverAddressMapper.insert(serverAddress);
        // 返回
        return serverAddress.getId();
    }

    @Override
    public void updateServerAddress(ServerAddressUpdateReqVO updateReqVO) {
        // 校验存在
        validateServerAddressExists(updateReqVO.getId());
        // 更新
        ServerAddressDO updateObj = ServerAddressConvert.INSTANCE.convert(updateReqVO);
        serverAddressMapper.updateById(updateObj);
    }

    @Override
    public void deleteServerAddress(Long id) {
        // 校验存在
        validateServerAddressExists(id);
        // 删除
        serverAddressMapper.deleteById(id);
    }

    private void validateServerAddressExists(Long id) {
        if (serverAddressMapper.selectById(id) == null) {
            throw exception(SERVER_ADDRESS_NOT_EXISTS);
        }
    }

    @Override
    public ServerAddressDO getServerAddress(Long id) {
        return serverAddressMapper.selectById(id);
    }

    @Override
    public List<ServerAddressDO> getServerAddressList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return serverAddressMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ServerAddressDO> getServerAddressPage(ServerAddressPageReqVO pageReqVO) {
        return serverAddressMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ServerAddressDO> getServerAddressList(ServerAddressExportReqVO exportReqVO) {
        return serverAddressMapper.selectList(exportReqVO);
    }

    @Override
    public ServerAddressDO getServerAddress4Api(Long id, Long usrId) {
        return serverAddressMapper.selectOne("id", id,
                "user_id", usrId);
    }

}
