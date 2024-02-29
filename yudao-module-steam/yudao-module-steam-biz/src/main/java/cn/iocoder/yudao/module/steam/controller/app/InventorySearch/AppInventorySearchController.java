package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;


import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.inv.vo.InvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.binduser.BindUserService;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * @description: 库存查询
 */
@RestController
@RequestMapping("/steam-app/inventory")
@Validated
@Slf4j
public class AppInventorySearchController {

    @Resource
    private SteamInvService steamInvService;
    @Resource
    private InvService invService;
    @Resource
    private BindUserMapper bindUserMapper;




    // 后续查询库存
    @GetMapping("/after_search")
    @Operation(summary = "查询库存")
    public CommonResult<PageResult<InvDO>> afterSearch(@Valid InvPageReqVO invPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        loginUser.getId();
        PageResult<InvDO> inv = invService.getInvPage(invPageReqVO);
        List<BindUserDO> collect = bindUserMapper.selectList()
                .stream()
                .filter(o -> o.getSteamId().equals(invPageReqVO.getSteamId()))
                .collect(Collectors.toList());
        for(BindUserDO bindUserDO:collect){
            if(!Objects.equals(loginUser.getId(), bindUserDO.getUserId())){
                throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
            }
        }
        return success(inv);
    }

}

