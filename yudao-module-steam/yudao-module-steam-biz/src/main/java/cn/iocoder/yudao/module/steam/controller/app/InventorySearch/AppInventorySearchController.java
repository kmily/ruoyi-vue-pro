package cn.iocoder.yudao.module.steam.controller.app.InventorySearch;


import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.steam.controller.admin.invorder.vo.AppMergeToSellReq;
import cn.iocoder.yudao.module.steam.controller.app.InventorySearch.vo.AppInvPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.binduser.BindUserDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.binduser.BindUserMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.service.SteamInvService;
import cn.iocoder.yudao.module.steam.service.inv.InvService;
import cn.iocoder.yudao.module.steam.service.ioinvupdate.IOInvUpdateService;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.steam.InventoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private BindUserMapper bindUserMapper;
    @Resource
    private InvMapper invMapper;

    @Resource
    private InvDescMapper invDescMapper;

    @Resource
    private SellingMapper sellingMapper;

    @Resource
    private IOInvUpdateService ioInvUpdateService;

    /**
     * 用户手动查询自己的 steam_inv 库存（从数据库中获取数据）
     *  入参 steamid
     * @param inv
     * @return
     */
    @GetMapping("/after_SearchInDB")
    @Operation(summary = "从数据库中查询数据")
    public List<AppInvPageReqVO> SearchInDB(@Valid InvDO inv) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> bindUserDOS = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, inv.getSteamId()));
        if(Objects.isNull(bindUserDOS) || bindUserDOS.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        inv.setUserId(loginUser.getId());
        inv.setBindUserId(bindUserDOS.get(0).getId());
//        inv.setTransferStatus(0);
        // 全量不分表查询库存
        List<InvDO> invToMerge = ioInvUpdateService.getInvToMerge(inv);
        if(invToMerge.isEmpty()){
            throw new ServiceException(-1,"获取库存失败");
        }
        // 返回图片价格名称等
        List<AppInvPageReqVO> appInvPageReqVOS = ioInvUpdateService.searchInv(invToMerge);
        return appInvPageReqVOS;
    }


    /**
     * 入参：steamId(必传)
     *  查询可出售库存
     * @param reqVo
     */
    @GetMapping("/mergeToSell")
    @Operation(summary = "合并库存")
    public CommonResult<List<AppInvPageReqVO>> mergeToSell(@Valid AppMergeToSellReq reqVo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, reqVo.getSteamId()));
        if(Objects.isNull(collect) || collect.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        // 访问本地库存 按条件查询库存
        InvDO inv=new InvDO();
        inv.setSteamId(reqVo.getSteamId());
        inv.setUserId(loginUser.getId());
        inv.setBindUserId(collect.get(0).getId());
        inv.setTransferStatus(reqVo.getSearchType());
        if(reqVo.getSearchType() == null){
            List<InvDO> invToMerge = ioInvUpdateService.getInvToMerge1(inv);
            return success(steamInvService.mergeInvAll(invToMerge));
        }
        List<InvDO> invToMerge = ioInvUpdateService.getInvToMerge(inv);
        return success(steamInvService.mergeInv(invToMerge));
    }



    @GetMapping("/updateFromSteam")
    @Operation(summary = "更新库存 入参steamid")
    @ResponseBody
    public CommonResult<List<String>> updateFromSteam(@RequestParam String steamId ,Long id) throws JsonProcessingException {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
                .eq(BindUserDO::getUserId, loginUser.getId())
                .eq(BindUserDO::getUserType, loginUser.getUserType())
                .eq(BindUserDO::getSteamId, steamId));
        if(Objects.isNull(collect) || collect.isEmpty()){
            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
        }
        BindUserDO bindUserDO = new BindUserDO();
        bindUserDO.setSteamId(steamId);
        bindUserDO.setId(collect.get(0).getId());
        bindUserDO.setUserId(collect.get(0).getUserId());
        bindUserDO.setUserType(collect.get(0).getUserType());
        // 获取线上 steam 库存
        InventoryDto inventoryDto = ioInvUpdateService.gitInvFromSteam(bindUserDO);
        if(inventoryDto.getAssets() != null){
            // 删除原有库存中，getTransferStatus = 0 的库存
            BindUserDO user = new BindUserDO();
            user.setSteamId(bindUserDO.getSteamId());
            user.setUserId(bindUserDO.getUserId());
            user.setId(bindUserDO.getId());
            ioInvUpdateService.deleteInventory(user);
            // 插入库存 返回库存绑定的descId TODO 后期优化思路 copy插入库存方法在插入的时候比对Selling表中相同账户下的 AssetId ，有重复就不插入
            ioInvUpdateService.firstInsertInventory(inventoryDto, bindUserDO);

            // 查询上架出售中的物品
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>().eq(SellingDO::getSteamId, steamId));
            if (sellingDOS.isEmpty()){
                return success(new ArrayList<>());
            }
            // 删除重复的数据
            for(SellingDO invDO : sellingDOS){
                // 筛选重复的一条（新插入的重复数据 TransferStatus = 0）
                List<InvDO> invDOS1 = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                        .eq(InvDO::getAssetid, invDO.getAssetid())
                        .eq(InvDO::getTransferStatus, 0));

                invMapper.deleteById(invDOS1.get(0).getId());
                invDescMapper.deleteById(invDOS1.get(0).getInvDescId());
            }

        } else {
            throw new ServiceException(-1,"未获取到新的库存信息");
        }
        return success(new ArrayList<>());
    }
//
//
//
//    //================================================================
//                    // TODO 库存更新重写版
//    //================================================================
//
//    @GetMapping("/updateFromSteam")
//    @Operation(summary = "更新库存 入参steamid")
//    @ResponseBody
//    public CommonResult<List<String>> updateFromSteam2(@RequestParam String steamId) throws JsonProcessingException {
//        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//        List<BindUserDO> collect = bindUserMapper.selectList(new LambdaQueryWrapperX<BindUserDO>()
//                .eq(BindUserDO::getUserId, loginUser.getId())
//                .eq(BindUserDO::getUserType, loginUser.getUserType())
//                .eq(BindUserDO::getSteamId, steamId));
//        if(Objects.isNull(collect) || collect.isEmpty()){
//            throw new ServiceException(-1,"您没有权限获取该用户的库存信息");
//        }
//        BindUserDO bindUserDO = new BindUserDO();
//        bindUserDO.setSteamId(steamId);
//        // 获取线上 steam 库存
//        InventoryDto inventoryDto = ioInvUpdateService.gitInvFromSteam(bindUserDO);
//        if(inventoryDto.getAssets() != null){
//            // TODO 后期优化思路 copy插入库存方法在插入的时候比对Selling表中相同账户下的 AssetId ，有重复就不插入
//            ioInvUpdateService.updateInventory(inventoryDto, bindUserDO);
//            List<InvDO> invDOS = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
//                    .eq(InvDO::getSteamId, steamId)
//                    .eq(InvDO::getBindUserId, bindUserDO.getId())
//                    .eq(InvDO::getUserId, bindUserDO.getUserId())
//                    .eq(InvDO::getTransferStatus, 1));
//            if (invDOS.isEmpty()){
//                return success(new ArrayList<>());
//            }
//            // 删除重复的数据
//            for(InvDO invDO : invDOS){
//                invMapper.delete(new LambdaQueryWrapperX<InvDO>().eq(InvDO::getAssetid,invDO.getAssetid()).eq(InvDO::getTransferStatus,0));
//            }
//
//        } else {
//            throw new ServiceException(-1,"未获取到新的库存信息");
//        }
//        return success(new ArrayList<>());
//    }
}

