package cn.iocoder.yudao.module.member.service.serverperson;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.serverperson.ServerPersonCheckLogConvert;
import cn.iocoder.yudao.module.member.dal.mysql.serverperson.ServerPersonCheckLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 被户人审核记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ServerPersonCheckLogServiceImpl extends ServiceImpl<ServerPersonCheckLogMapper, ServerPersonCheckLogDO> implements ServerPersonCheckLogService {

    @Resource
    private ServerPersonCheckLogMapper serverPersonCheckLogMapper;


    @Override
    public ServerPersonCheckLogDO getServerPersonCheckLog(Long id) {
        return serverPersonCheckLogMapper.selectById(id);
    }

    @Override
    public List<ServerPersonCheckLogDO> getServerPersonCheckLogList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return serverPersonCheckLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ServerPersonCheckLogDO> getServerPersonCheckLogPage(ServerPersonCheckLogPageReqVO pageReqVO) {
        return serverPersonCheckLogMapper.selectPage(pageReqVO);
    }

}
