package cn.iocoder.yudao.module.member.service.serverperson;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonAuditVO;
import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonCheckLogConvert;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonConvert;
import cn.iocoder.yudao.module.member.dal.mysql.serverperson.ServerPersonMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 被服务人 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ServerPersonServiceImpl extends ServiceImpl<ServerPersonMapper, ServerPersonDO> implements ServerPersonService {

    @Resource
    private ServerPersonMapper serverPersonMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private ServerPersonCheckLogService serverPersonCheckLogService;

    @Override
    public Long createServerPerson(AppServerPersonCreateReqVO createReqVO) {
        // 插入
        ServerPersonDO serverPerson = ServerPersonConvert.INSTANCE.convert(createReqVO);

        Long count = serverPersonMapper.selectCount(ServerPersonDO::getIdCard, createReqVO.getIdCard());
        if(count > 0){
            throw exception(SERVER_PERSON_HAS_EXISTS, createReqVO.getIdCard());
        }

        if(StrUtil.isNotBlank(createReqVO.getIdCard())){
            serverPerson.setBirthday(IdcardUtil.getBirthDate(createReqVO.getIdCard()).toLocalDateTime().toLocalDate());
            serverPerson.setSex(IdcardUtil.getGenderByIdCard(createReqVO.getIdCard()) == 1? (byte)1: (byte)2);
        }
        serverPerson.setStatus(CommonStatusEnum.APPLYING.name());
        serverPersonMapper.insert(serverPerson);
        // 返回
        return serverPerson.getId();
    }

    @Override
    public void updateServerPerson(AppServerPersonUpdateReqVO updateReqVO) {
        // 校验存在
        ServerPersonDO serverPerson = validateServerPersonExists(updateReqVO.getId());
        // 更新
        ServerPersonDO updateObj = ServerPersonConvert.INSTANCE.convert(updateReqVO);
        updateObj.setStatus(CommonStatusEnum.APPLYING.name());
        if(StrUtil.isNotBlank(updateObj.getIdCard())){
            serverPerson.setBirthday(IdcardUtil.getBirthDate(updateObj.getIdCard()).toLocalDateTime().toLocalDate());
            serverPerson.setSex(IdcardUtil.getGenderByIdCard(updateObj.getIdCard()) == 1? (byte)1: (byte)2);
        }
        serverPersonMapper.updateById(updateObj);
    }

    @Override
    public void deleteServerPerson(Long id) {
        // 校验存在
        validateServerPersonExists(id);
        // 删除
        serverPersonMapper.deleteById(id);
    }

    private ServerPersonDO validateServerPersonExists(Long id) {
        ServerPersonDO serverPersonDO = serverPersonMapper.selectById(id);
        if (serverPersonDO == null) {
            throw exception(SERVER_PERSON_NOT_EXISTS);
        }
        return serverPersonDO;
    }

    @Override
    public ServerPersonDO getServerPerson(Long id) {
        return serverPersonMapper.selectById(id);
    }

    @Override
    public List<ServerPersonDO> getServerPersonList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return serverPersonMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ServerPersonDO> getServerPersonPage(AppServerPersonPageReqVO pageReqVO) {
        return serverPersonMapper.selectPage(pageReqVO);
    }

    @Override
    public void auditServerPerson(ServerPersonAuditVO auditVO) {

        String status = auditVO.getStatus();
        AdminUserRespDTO user = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        this.updateById(new ServerPersonDO().setCheckTime(LocalDateTime.now())
                .setStatus(status).setCheckName(user.getNickname()).setOpinion(auditVO.getOpinion())
                .setId(auditVO.getId()));

        serverPersonCheckLogService.save(ServerPersonCheckLogConvert.INSTANCE.convert(auditVO, auditVO.getId(),user.getNickname()));
    }

}
