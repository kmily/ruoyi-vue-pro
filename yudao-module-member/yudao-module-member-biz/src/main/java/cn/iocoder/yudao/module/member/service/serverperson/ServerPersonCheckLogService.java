package cn.iocoder.yudao.module.member.service.serverperson;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 被户人审核记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ServerPersonCheckLogService extends IService<ServerPersonCheckLogDO>{

    /**
     * 获得被户人审核记录
     *
     * @param id 编号
     * @return 被户人审核记录
     */
    ServerPersonCheckLogDO getServerPersonCheckLog(Long id);

    /**
     * 获得被户人审核记录列表
     *
     * @param ids 编号
     * @return 被户人审核记录列表
     */
    List<ServerPersonCheckLogDO> getServerPersonCheckLogList(Collection<Long> ids);

    /**
     * 获得被户人审核记录分页
     *
     * @param pageReqVO 分页查询
     * @return 被户人审核记录分页
     */
    PageResult<ServerPersonCheckLogDO> getServerPersonCheckLogPage(ServerPersonCheckLogPageReqVO pageReqVO);

}
