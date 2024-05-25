package cn.iocoder.yudao.module.therapy.service.impl;

import cn.iocoder.yudao.framework.common.util.date.DateUtils;
import cn.iocoder.yudao.module.therapy.controller.VO.SetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.controller.admin.VO.AdminSetAppointmentTimeVO;
import cn.iocoder.yudao.module.therapy.controller.app.VO.AppMemberUserSetAppointmentTimeReqVO;
import cn.iocoder.yudao.module.therapy.dal.dataobject.user.MemberUserExtDO;
import cn.iocoder.yudao.module.therapy.dal.mysql.definition.MemberUserExtMapper;
import cn.iocoder.yudao.module.therapy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import javax.annotation.Resource;

/**
 * @Author:lidongw_1
 * @Date 2024/5/23
 * @Description: 患者服务相关
 **/
@Service
public class UserServiceImpl implements UserService {

    @Resource
    MemberUserExtMapper memberUserExtMapper;

    @Override
    public boolean setAppointmentTime(Long userId, SetAppointmentTimeReqVO reqVO) {
        QueryWrapper<MemberUserExtDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        //如果用户存就修改，如果不存在就添加
        MemberUserExtDO memberUserExtDO = memberUserExtMapper.selectOne(queryWrapper);
        if (memberUserExtDO != null) {
            memberUserExtDO.setAppointmentDate(reqVO.getAppointmentDate());
            memberUserExtDO.setAppointmentTimeRange(reqVO.getAppointmentPeriodTime());
            return memberUserExtMapper.updateById(memberUserExtDO) > 0;
        } else {
            return memberUserExtMapper.insert(new MemberUserExtDO()) > 0;
        }
    }
}
