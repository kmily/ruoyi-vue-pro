package cn.iocoder.yudao.module.member.service.room;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.member.controller.app.room.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.room.RoomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 用户房间 Service 接口
 *
 * @author 芋道源码
 */
public interface RoomService {

    /**
     * 创建用户房间
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRoom(@Valid RoomCreateReqVO createReqVO);

    /**
     * 更新用户房间
     *
     * @param updateReqVO 更新信息
     */
    void updateRoom(@Valid RoomUpdateReqVO updateReqVO);

    /**
     * 删除用户房间
     *
     * @param id 编号
     */
    void deleteRoom(Long id);

    /**
     * 获得用户房间
     *
     * @param id 编号
     * @return 用户房间
     */
    RoomDO getRoom(Long id);

    /**
     * 获得用户房间列表
     *
     * @param ids 编号
     * @return 用户房间列表
     */
    List<RoomDO> getRoomList(Collection<Long> ids);

    /**
     * 获得用户房间分页
     *
     * @param pageReqVO 分页查询
     * @return 用户房间分页
     */
    PageResult<RoomDO> getRoomPage(RoomPageReqVO pageReqVO);

    /**
     * 获得用户房间列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户房间列表
     */
    List<RoomDO> getRoomList(RoomExportReqVO exportReqVO);

}
