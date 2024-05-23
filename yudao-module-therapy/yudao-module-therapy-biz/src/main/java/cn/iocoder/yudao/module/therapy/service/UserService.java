package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.VO.AdminSetAppointmentTimeVO;
import cn.iocoder.yudao.module.therapy.controller.app.VO.AppMemberUserSetAppointmentTimeReqVO;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: TODO
 **/
public interface UserService {

    boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO);
}
