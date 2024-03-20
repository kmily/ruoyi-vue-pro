package cn.iocoder.yudao.module.steam.service.alipayisv;

import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.module.steam.controller.app.alipay.vo.CreateIsvVo;
import cn.iocoder.yudao.module.steam.dal.dataobject.alipayisv.AlipayIsvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.alipayisv.AlipayIsvMapper;
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
        alipayIsvMapper.insert(alipayIsvDO);
        return alipayIsvDO;
    }

}