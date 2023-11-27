package cn.iocoder.yudao.module.member.service.serverperson;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.member.controller.admin.serverperson.vo.ServerPersonAuditVO;
import cn.iocoder.yudao.module.member.controller.app.serverperson.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.serverperson.ServerPersonDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 被服务人 Service 接口
 *
 * @author 芋道源码
 */
public interface ServerPersonService extends IService<ServerPersonDO>{

    /**
     * 创建被服务人
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createServerPerson(@Valid AppServerPersonCreateReqVO createReqVO);

    /**
     * 更新被服务人
     *
     * @param updateReqVO 更新信息
     */
    void updateServerPerson(@Valid AppServerPersonUpdateReqVO updateReqVO);

    /**
     * 删除被服务人
     *
     * @param id 编号
     */
    void deleteServerPerson(Long id);

    /**
     * 获得被服务人
     *
     * @param id 编号
     * @return 被服务人
     */
    ServerPersonDO getServerPerson(Long id);

    /**
     * 获得被服务人列表
     *
     * @param ids 编号
     * @return 被服务人列表
     */
    List<ServerPersonDO> getServerPersonList(Collection<Long> ids);

    /**
     * 获得被服务人分页
     *
     * @param pageReqVO 分页查询
     * @return 被服务人分页
     */
    PageResult<ServerPersonDO> getServerPersonPage(AppServerPersonPageReqVO pageReqVO);


    /**
     * 审核被户人
     * @param auditVO 审核参数
     */
    void auditServerPerson(ServerPersonAuditVO auditVO);

}
