package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: TODO
 **/
public interface UserService {

    boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO);
}
