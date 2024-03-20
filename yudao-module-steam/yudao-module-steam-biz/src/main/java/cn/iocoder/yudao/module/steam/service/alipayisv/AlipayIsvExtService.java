package cn.iocoder.yudao.module.steam.service.alipayisv;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.alipay.vo.CreateIsvVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.alipayisv.AlipayIsvMapper;
import cn.iocoder.yudao.module.steam.enums.PayApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 签约ISV用户 Service 实现类
 *
 * @author 管理员
 */
@Service
@Slf4j
public class AlipayIsvExtService {

    @Resource
    private AlipayIsvMapper alipayIsvMapper;

    public AlipayIsvDO initIsv(CreateIsvVo createIsvVo, LoginUser loginUser){
        AlipayIsvDO alipayIsvDO = new AlipayIsvDO().setIsvBizId(createIsvVo.getIsvBizId()).setSystemUserId(loginUser.getId()).setSystemUserType(loginUser.getUserType());
        Long aLong = alipayIsvMapper.selectCount(new LambdaQueryWrapperX<AlipayIsvDO>()
                .eq(AlipayIsvDO::getSystemUserId, loginUser.getId())
                .eq(AlipayIsvDO::getSystemUserType, loginUser.getUserType())
        );
        if(aLong>0){
            throw new ServiceException(PayApiCode.PAY_ISV_EXISTS);
        }
        alipayIsvMapper.insert(alipayIsvDO);
        return alipayIsvDO;
    }

}