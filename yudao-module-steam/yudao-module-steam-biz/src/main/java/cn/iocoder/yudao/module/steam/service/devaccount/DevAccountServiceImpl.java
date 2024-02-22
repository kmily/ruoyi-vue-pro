package cn.iocoder.yudao.module.steam.service.devaccount;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.DevAccountPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.devaccount.vo.DevAccountSaveReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.devaccount.DevAccountDO;
import cn.iocoder.yudao.module.steam.dal.mysql.devaccount.DevAccountMapper;
import cn.iocoder.yudao.module.steam.utils.RSAUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.security.KeyPair;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.steam.enums.ErrorCodeConstants.DEV_ACCOUNT_NOT_EXISTS;

/**
 * 开放平台用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DevAccountServiceImpl implements DevAccountService {

    @Resource
    private DevAccountMapper devAccountMapper;

    @Override
    public Long createDevAccount(DevAccountSaveReqVO createReqVO) {
        // 插入
        DevAccountDO devAccount = BeanUtils.toBean(createReqVO, DevAccountDO.class);
        devAccountMapper.insert(devAccount);
        // 返回
        return devAccount.getId();
    }

    @Override
    public void updateDevAccount(DevAccountSaveReqVO updateReqVO) {
        // 校验存在
        validateDevAccountExists(updateReqVO.getId());
        // 更新
        DevAccountDO updateObj = BeanUtils.toBean(updateReqVO, DevAccountDO.class);
        devAccountMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevAccount(Long id) {
        // 校验存在
        validateDevAccountExists(id);
        // 删除
        devAccountMapper.deleteById(id);
    }

    private void validateDevAccountExists(Long id) {
        if (devAccountMapper.selectById(id) == null) {
            throw exception(DEV_ACCOUNT_NOT_EXISTS);
        }
    }

    @Override
    public DevAccountDO getDevAccount(Long id) {
        return devAccountMapper.selectById(id);
    }

    @Override
    public PageResult<DevAccountDO> getDevAccountPage(DevAccountPageReqVO pageReqVO) {
        return devAccountMapper.selectPage(pageReqVO);
    }

    @Override
    public Long apply(DevAccountSaveReqVO createReqVO) {
        try {
            KeyPair keyPair = RSAUtils.genKey();
            createReqVO.setApiPublicKey(RSAUtils.encryptBASE64(keyPair.getPublic().getEncoded()));
            createReqVO.setApiPrivateKey(RSAUtils.encryptBASE64(keyPair.getPrivate().getEncoded()));
            // 插入
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            createReqVO.setUserId(loginUser.getId());
            createReqVO.setStatus(0);
            DevAccountDO devAccount = BeanUtils.toBean(createReqVO, DevAccountDO.class);
            devAccountMapper.insert(devAccount);
            // 返回
            return devAccount.getId();
        } catch (Exception e) {
            throw new ServiceException(new ErrorCode(01,"申请权限失败"));
        }

    }

    public DevAccountDO selectByUserName (String userName, UserTypeEnum userTypeEnum) {
        try {
            DevAccountPageReqVO devAccountPageReqVO=new DevAccountPageReqVO();
            devAccountPageReqVO.setUserName(userName);
            devAccountPageReqVO.setUserType(userTypeEnum.getValue());
            DevAccountDO devAccountDO = devAccountMapper.selectByUserName(devAccountPageReqVO);
            // 返回
            return devAccountDO;
        } catch (Exception e) {
            throw new ServiceException(new ErrorCode(01,"申请权限失败"));
        }

    }
}