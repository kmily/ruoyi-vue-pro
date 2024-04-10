package cn.iocoder.yudao.module.steam.service.selling;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.steam.controller.admin.otherselling.vo.OtherSellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.droplist.vo.SellListItemResp;
import cn.iocoder.yudao.module.steam.controller.app.selling.vo.*;
import cn.iocoder.yudao.module.steam.dal.dataobject.inv.InvDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invdesc.InvDescDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.otherselling.OtherSellingDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import cn.iocoder.yudao.module.steam.dal.mysql.inv.InvMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invdesc.InvDescMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.otherselling.OtherSellingMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.selling.SellingMapper;
import cn.iocoder.yudao.module.steam.enums.OpenApiCode;
import cn.iocoder.yudao.module.steam.service.fin.PaySteamOrderService;
import cn.iocoder.yudao.module.steam.service.invpreview.InvPreviewExtService;
import cn.iocoder.yudao.module.steam.service.otherselling.OtherSellingService;
import cn.iocoder.yudao.module.steam.service.otherselling.vo.OtherSellingListDo;
import cn.iocoder.yudao.module.steam.service.steam.C5ItemInfo;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 在售饰品 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
@Slf4j
public class SellingExtService {

    @Resource
    private SellingMapper sellingMapper;
    @Resource
    private InvMapper invMapper;
    @Resource
    private InvDescMapper invDescMapper;
    @Resource
    private InvPreviewExtService invPreviewExtService;
    @Autowired
    private PaySteamOrderService paySteamOrderService;
    @Resource
    private InvPreviewMapper invPreviewMapper;

    @Resource
    private OtherSellingService otherSellingService;

    @Resource
    private OtherSellingMapper otherSellingMapper;

    private MemberUserApi memberUserApi;

    @Transactional(rollbackFor = ServiceException.class)
    public void batchSale(BatchSellReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchSellReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }
        List<InvDO> invDOList = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .inIfPresent(InvDO::getId, invIds)
                .eq(InvDO::getUserId, loginUser.getId())
                .eq(InvDO::getUserType, loginUser.getUserType())
        );
        if (invIds.size() != invDOList.size()) {
            throw new ServiceException(-1, "检测到你上线的库存不是所有库存均为可上线状态，请检查后再提交");
        }
        List<Long> invDescId = invDOList.stream().map(InvDO::getInvDescId).collect(Collectors.toList());
        List<InvDescDO> invDescDO2 = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                .in(InvDescDO::getId, invDescId));
        long count = invDescDO2.stream().filter(item -> item.getTradable() == 0).count();
        if (count > 0) {
            throw new ServiceException(-1, "饰品不能上架,请检查是否冷却中或不能交易");
        }
        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds)
                .eq(SellingDO::getStatus, CommonStatusEnum.ENABLE.getStatus()));
        if (!sellingDOInSelling.isEmpty()) {
            throw new ServiceException(-1, "部分商品已上架构，请检查后再操作上架构");
        }
        for (InvDO item : invDOList) {
            Optional<BatchSellReqVo.Item> first = reqVo.getItems().stream().filter(i -> i.getId().equals(item.getId())).findFirst();
            item.setPrice(invDOList.get(0).getPrice());
            if (!first.isPresent()) {
                log.error("价格信息未查找到{},{}", item, reqVo.getItems());
                throw new ServiceException(OpenApiCode.JACKSON_EXCEPTION);
            }

            BatchSellReqVo.Item itemPriceInfo = first.get();
            item.setPrice(itemPriceInfo.getPrice());
            item.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            invMapper.updateById(item);
            Optional<InvDescDO> invDescDO;
            String tempSteamId = "11111111111111111";
            if (!item.getSteamId().equals(tempSteamId)) {
                invDescDO = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                        .eq(InvDescDO::getClassid, item.getClassid())
                        .eq(InvDescDO::getSteamId, item.getSteamId())
                        .eq(InvDescDO::getInstanceid, item.getInstanceid())).stream().findFirst();
            } else {
                invDescDO = invDescMapper.selectList(new LambdaQueryWrapperX<InvDescDO>()
                        .eq(InvDescDO::getId, item.getInvDescId())).stream().findFirst();
            }

            if (!invDescDO.isPresent()) {
                throw new ServiceException(-1, "物品描述信息未查找到");
            }
            Long l = sellingMapper.selectCount(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getInvId, item.getId())
            );
            if (l > 0) {
                throw new ServiceException(-1, "该物品已上架");
            }
            SellingDO sellingDO = new SellingDO();
            sellingDO.setAppid(item.getAppid());
            sellingDO.setAssetid(item.getAssetid());
            sellingDO.setClassid(item.getClassid());
            sellingDO.setInstanceid(item.getInstanceid());
            sellingDO.setAmount(item.getAmount());
            sellingDO.setSteamId(item.getSteamId());
            sellingDO.setUserId(item.getUserId());
            sellingDO.setUserType(item.getUserType());
            sellingDO.setBindUserId(item.getBindUserId());
            sellingDO.setContextid(item.getContextid());
            sellingDO.setInvDescId(invDescDO.get().getId());
            sellingDO.setSelExterior(invDescDO.get().getSelExterior());
            sellingDO.setSelItemset(invDescDO.get().getSelItemset());
            sellingDO.setSelQuality(invDescDO.get().getSelQuality());
            sellingDO.setSelRarity(invDescDO.get().getSelRarity());
            sellingDO.setSelType(invDescDO.get().getSelType());
            sellingDO.setSelWeapon(invDescDO.get().getSelWeapon());
            sellingDO.setMarketName(invDescDO.get().getMarketName());
            sellingDO.setIconUrl(invDescDO.get().getIconUrl());
            sellingDO.setMarketHashName(invDescDO.get().getMarketHashName());
            sellingDO.setInvId(item.getId());
            sellingDO.setDisplayWeight(2);
            sellingDO.setShortName(invDescDO.get().getName());
            if (itemPriceInfo.getPrice() == null || itemPriceInfo.getPrice() <= 0) {
                throw new ServiceException(-1, "未设置价格");
            }
            sellingDO.setPrice(itemPriceInfo.getPrice());
            sellingDO.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
            sellingMapper.insert(sellingDO);
            invPreviewExtService.markInvEnable(sellingDO.getMarketHashName());
        }
    }


    @Transactional(rollbackFor = ServiceException.class)
    public void batchOffSale(BatchOffSellReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchOffSellReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }
        List<InvDO> invDOList = invMapper.selectList(new LambdaQueryWrapperX<InvDO>()
                .inIfPresent(InvDO::getId, invIds)
                .eq(InvDO::getUserId, loginUser.getId())
                .eq(InvDO::getUserType, loginUser.getUserType())
        );
        if (invIds.size() != invDOList.size()) {
            throw new ServiceException(-1, "检测到你下架的库存部分不存在，请检查后再提交");
        }
        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds));

        long count1 = sellingDOInSelling.stream().filter(item -> Arrays.asList(
                InvTransferStatusEnum.INORDER.getStatus(),
                InvTransferStatusEnum.TransferFINISH.getStatus(),
                InvTransferStatusEnum.OFF_SALE.getStatus(),
                InvTransferStatusEnum.TransferERROR.getStatus()).contains(item.getTransferStatus())).count();

        if (count1 > 0) {
            throw new ServiceException(-1, "部分商品已不存在，请检查后再操作下架");
        }
        for (InvDO item : invDOList) {
            item.setPrice(null);
            item.setTransferStatus(InvTransferStatusEnum.INIT.getStatus());
            invMapper.updateById(item);
        }
        for (SellingDO item : sellingDOInSelling) {
            if (paySteamOrderService.getExpOrder(item.getId()).size() > 0) {
                throw new ServiceException(-1, "商品交易中，不允许下架");
            }
            sellingMapper.deleteById(item.getId());
            invPreviewExtService.markInvEnable(item.getMarketHashName());
        }
    }

    /**
     * 批量修改价格
     *
     * @param reqVo
     */

    @Transactional(rollbackFor = ServiceException.class)
    public void batchChangePrice(BatchChangePriceReqVo reqVo, LoginUser loginUser) {
        if (Objects.isNull(loginUser)) {
            throw new ServiceException(OpenApiCode.ID_ERROR);
        }
        List<Long> invIds = reqVo.getItems().stream().map(BatchChangePriceReqVo.Item::getId).distinct().collect(Collectors.toList());
        if (invIds.size() != reqVo.getItems().size()) {
            throw new ServiceException(-1, "传入的id不允许有重复");
        }

        List<SellingDO> sellingDOInSelling = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getUserId, loginUser.getId())
                .eq(SellingDO::getUserType, loginUser.getUserType())
                .in(SellingDO::getInvId, invIds)
                .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));

        if (Objects.isNull(sellingDOInSelling)) {
            throw new ServiceException(-1, "商品不存在或已下架");
        }

        if (sellingDOInSelling.size() != invIds.size()) {
            throw new ServiceException(-1, "部分商品已不存在，请检查后再操作改价");
        }

        for (SellingDO item : sellingDOInSelling) {
            Optional<BatchChangePriceReqVo.Item> first = reqVo.getItems().stream().filter(i -> i.getId().equals(item.getInvId())).findFirst();
            if (first.isPresent()) {
                BatchChangePriceReqVo.Item item1 = first.get();
                item.setPrice(item1.getPrice());
                sellingMapper.updateById(item);
                invPreviewExtService.markInvEnable(item.getMarketHashName());
            } else {
                throw new ServiceException(-1, "信息不正确，请检查");
            }
        }
    }


    public PageResult<SellingMergeListReqVo> sellingMerge(SellingPageReqVO sellingPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (sellingPageReqVO.getSteamId() != null && !sellingPageReqVO.getSteamId().equals("")) {
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getUserType, loginUser.getUserType())
                    .eq(SellingDO::getUserId, loginUser.getId())
                    .eq(SellingDO::getSteamId, sellingPageReqVO.getSteamId())
                    .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));


            if (Objects.isNull(sellingDOS) || sellingDOS.size() <= 0) {
                return new PageResult<>(Collections.emptyList(), 0L);
            }

            List<String> sellingHashNameList = sellingDOS.stream().map(SellingDO::getMarketHashName).distinct().collect(Collectors.toList());
            List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .in(InvPreviewDO::getMarketHashName, sellingHashNameList));
            Map<String, InvPreviewDO> mapInvPreview = new HashMap<>();
            if (Objects.nonNull(invPreviewDOS)) {
                mapInvPreview = invPreviewDOS.stream().collect(Collectors.toMap(InvPreviewDO::getMarketHashName, i -> i, (v1, v2) -> v1));
            }


            Map<String, SellingMergeListReqVo> invPage = new HashMap<>();
            for (SellingDO element : sellingDOS) {
                if (Objects.nonNull(invPage.get(element.getMarketName()))) {
                    SellingMergeListReqVo sellingMergeListReqVo = invPage.get(element.getMarketName());

                    List<Integer> list1 = new ArrayList<>(sellingMergeListReqVo.getPrice());
                    list1.add(Integer.valueOf(element.getPrice()));
                    sellingMergeListReqVo.setPrice(list1);

                    ArrayList<String> list = new ArrayList<>(sellingMergeListReqVo.getInvId());
                    list.add(String.valueOf(element.getInvId()));
                    sellingMergeListReqVo.setInvId(list);
                    sellingMergeListReqVo.setNumber(list.size());

                } else {
                    SellingMergeListReqVo sellingPageReqVO1 = new SellingMergeListReqVo();

                    InvPreviewDO invPreviewDO = mapInvPreview.get(element.getMarketHashName());

                    if (Objects.nonNull(invPreviewDO)) {
                        sellingPageReqVO1.setItemInfo(invPreviewDO.getItemInfo());
                        sellingPageReqVO1.setMinPrice(invPreviewDO.getMinPrice());
                    } else {
                        sellingPageReqVO1.setItemInfo(new C5ItemInfo());
                        sellingPageReqVO1.setMinPrice(0);
                    }
                    sellingPageReqVO1.setMarketHashName(element.getMarketHashName());
                    sellingPageReqVO1.setIconUrl(element.getIconUrl());
                    sellingPageReqVO1.setSelType(element.getSelType());
                    sellingPageReqVO1.setSelExterior(element.getSelExterior());
                    sellingPageReqVO1.setSelItemset(element.getSelItemset());
                    sellingPageReqVO1.setSelQuality(element.getSelQuality());
                    sellingPageReqVO1.setSelRarity(element.getSelRarity());
                    sellingPageReqVO1.setSelWeapon(element.getSelWeapon());
                    sellingPageReqVO1.setAssetId(element.getAssetid());
                    sellingPageReqVO1.setMarketName(element.getMarketName());
                    sellingPageReqVO1.setStatus(element.getStatus());
                    sellingPageReqVO1.setTransferStatus(element.getTransferStatus());
                    sellingPageReqVO1.setInvId(Arrays.asList(String.valueOf(element.getInvId())));
                    sellingPageReqVO1.setPrice(Arrays.asList(element.getPrice()));
                    invPage.put(element.getMarketName(), sellingPageReqVO1);
                }
            }

            List<SellingMergeListReqVo> sellingMergePage = new ArrayList<>();
            for (Map.Entry<String, SellingMergeListReqVo> key : invPage.entrySet()) {
                sellingMergePage.add(key.getValue());
            }

            return new PageResult<>(sellingMergePage, (long) sellingDOS.size());
        } else {
            List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                    .eq(SellingDO::getUserType, loginUser.getUserType())
                    .eq(SellingDO::getUserId, loginUser.getId())
                    .eq(SellingDO::getTransferStatus, InvTransferStatusEnum.SELL.getStatus()));

            if (Objects.isNull(sellingDOS) || sellingDOS.size() <= 0) {
                return new PageResult<>(Collections.emptyList(), 0L);
            }

            List<String> sellingHashNameList = sellingDOS.stream().map(SellingDO::getMarketHashName).distinct().collect(Collectors.toList());
            List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                    .in(InvPreviewDO::getMarketHashName, sellingHashNameList));
            Map<String, InvPreviewDO> mapInvPreview = new HashMap<>();
            if (Objects.nonNull(invPreviewDOS)) {
                mapInvPreview = invPreviewDOS.stream().collect(Collectors.toMap(InvPreviewDO::getMarketHashName, i -> i, (v1, v2) -> v1));
            }


            Map<String, SellingMergeListReqVo> invPage = new HashMap<>();
            for (SellingDO element : sellingDOS) {
                if (Objects.nonNull(invPage.get(element.getMarketName()))) {
                    SellingMergeListReqVo sellingMergeListReqVo = invPage.get(element.getMarketName());

                    List<Integer> list1 = new ArrayList<>(sellingMergeListReqVo.getPrice());
                    list1.add(Integer.valueOf(element.getPrice()));
                    sellingMergeListReqVo.setPrice(list1);

                    ArrayList<String> list = new ArrayList<>(sellingMergeListReqVo.getInvId());
                    list.add(String.valueOf(element.getInvId()));
                    sellingMergeListReqVo.setInvId(list);
                    sellingMergeListReqVo.setNumber(list.size());

                } else {
                    SellingMergeListReqVo sellingPageReqVO1 = new SellingMergeListReqVo();

                    InvPreviewDO invPreviewDO = mapInvPreview.get(element.getMarketHashName());

                    if (Objects.nonNull(invPreviewDO)) {
                        sellingPageReqVO1.setItemInfo(invPreviewDO.getItemInfo());
                        sellingPageReqVO1.setMinPrice(invPreviewDO.getMinPrice());
                    } else {
                        sellingPageReqVO1.setItemInfo(new C5ItemInfo());
                        sellingPageReqVO1.setMinPrice(0);
                    }
                    sellingPageReqVO1.setMarketHashName(element.getMarketHashName());
                    sellingPageReqVO1.setIconUrl(element.getIconUrl());
                    sellingPageReqVO1.setSelType(element.getSelType());
                    sellingPageReqVO1.setSelExterior(element.getSelExterior());
                    sellingPageReqVO1.setSelItemset(element.getSelItemset());
                    sellingPageReqVO1.setSelQuality(element.getSelQuality());
                    sellingPageReqVO1.setSelRarity(element.getSelRarity());
                    sellingPageReqVO1.setSelWeapon(element.getSelWeapon());
                    sellingPageReqVO1.setAssetId(element.getAssetid());
                    sellingPageReqVO1.setMarketName(element.getMarketName());
                    sellingPageReqVO1.setStatus(element.getStatus());
                    sellingPageReqVO1.setTransferStatus(element.getTransferStatus());
                    sellingPageReqVO1.setInvId(Arrays.asList(String.valueOf(element.getInvId())));
                    sellingPageReqVO1.setPrice(Arrays.asList(element.getPrice()));
                    invPage.put(element.getMarketName(), sellingPageReqVO1);
                }
            }

            List<SellingMergeListReqVo> sellingMergePage = new ArrayList<>();
            for (Map.Entry<String, SellingMergeListReqVo> key : invPage.entrySet()) {
                sellingMergePage.add(key.getValue());
            }

            return new PageResult<>(sellingMergePage, (long) sellingDOS.size());
        }

    }


    /**
     * WearCategory0  崭新出厂
     * WearCategory1  略有磨损
     * WearCategory2  久经沙场
     * WearCategory3  破损不堪
     * WearCategory4  战痕累累
     *
     * @param sellingPageReqVO
     */
    public Map<String, Integer> showGoodsWithMarketName(GoodsWithMarketHashNameReqVO sellingPageReqVO) {
        List<SellingDO> sellingDOS = sellingMapper.selectList(new LambdaQueryWrapperX<SellingDO>()
                .eq(SellingDO::getShortName, sellingPageReqVO.getShortName()));

//        List<SellingDO> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (SellingDO sellingDO : sellingDOS) {
            if (!map.containsKey(sellingDO.getSelExterior())) {
                map.put(sellingDO.getSelExterior(), sellingDO.getPrice());
            }
            for (Map.Entry<String, Integer> element : map.entrySet()) {
                if (element.getKey().equals(sellingDO.getSelExterior()) && element.getValue() > sellingDO.getPrice()) {
                    map.put(sellingDO.getSelExterior(), sellingDO.getPrice());
                }
            }
        }
        return map;
    }

    public PageResult<OtherSellingPageReqVO> otherSale(SellingDO sellingDO) {
        List<OtherSellingDO> otherSellingDO = otherSellingMapper.selectList(new LambdaQueryWrapperX<OtherSellingDO>()
                .eq(OtherSellingDO::getMarketHashName, sellingDO.getMarketHashName()));

        List<OtherSellingListDo> otherSellingPageReqVOS = new ArrayList<>();

        for (OtherSellingDO element : otherSellingDO) {
            String text = "园有静观动观之分这一点我们在造园之先首要考虑何谓静观就是园中予游者多驻足的观赏点动观就是要有较长的游览线二者说来小园应以静观为主动观为辅庭院专主静观大园则以动观为主静观为辅前者如苏州网师园后者则苏州拙政园差可似之人们进入网师园宜坐宜留之建筑多绕池一周有槛前细数游鱼有亭中待月迎风而轩外花影移墙峰峦当窗宛然如画静中生趣至于拙政园径缘池转廊引人随与日午画船桥下过衣香人影太匆匆的瘦西湖相仿佛妙在移步换影这是动观立意在先文循意出动静之分有关园林性质与园林面积大小象上海正在建造的盆景园则宜以静观为主即为一例中国园林是由建筑山水花木等组合而成的一个综合艺术品富有诗情画意叠山理水要造成虽由人作宛自天开的境界山与水的关系究竟如何呢简言之模山范水用局部之景而非缩小网师园水池仿虎丘白莲池极妙处理原则悉符画本山贵有脉水贵有源脉源贯通全园生动我曾经用水随山转山因水活与溪水因山成曲折山蹊随地作低平来说明山水之间的关系也就是从真山真水中所得到的启示明末清初叠山家张南垣主张用平冈小陂陵阜陂阪也就是要使园林山水接近自然如果我们能初步理解这个道理就不至于离自然太远多少能呈现水石交融的美妙境界";

            Random random = new Random();
            Set<Character> selectedChars = new HashSet<>();

            while (selectedChars.size() < 4) {
                int index = random.nextInt(text.length());
                char ch = text.charAt(index);

                // 确保字符不是空格，并且之前没有被选过
                if (ch != ' ' && !selectedChars.contains(ch)) {
                    selectedChars.add(ch);
                }
            }

            // 将选中的字符转换为字符串
            StringBuilder randomFourChars = new StringBuilder();
            for (Character ch : selectedChars) {
                randomFourChars.append(ch);
            }
            String nickName = "IO661用户" + randomFourChars;

            OtherSellingListDo otherSellingListDo = new OtherSellingListDo();
            otherSellingListDo.setMarketHashName(element.getMarketHashName());
            otherSellingListDo.setPrice(element.getPrice());
            otherSellingListDo.setIconUrl(element.getIconUrl());
            otherSellingListDo.setSelType(element.getSelType());
            otherSellingListDo.setSelExterior(element.getSelExterior());
            otherSellingListDo.setSelQuality(element.getSelQuality());
            otherSellingListDo.setSelRarity(element.getSelRarity());
            otherSellingListDo.setAppid(element.getAppid());
            otherSellingListDo.setMarketName(element.getMarketName());
            otherSellingListDo.setPlatformIdentity(element.getPlatformIdentity());
            MemberUserRespDTO memberUserRespDTO = new MemberUserRespDTO();
            memberUserRespDTO.setNickname(nickName);
            memberUserRespDTO.setAvatar("https://s3.io661.com/1b4d8182f422382339114e2a80bfe4fc2482722f7cc787da88e45a9d720ee058.jpg");
            memberUserRespDTO.setMobile("");
            memberUserRespDTO.setStatus(999);
            memberUserRespDTO.setPoint(999);
            memberUserRespDTO.setCreateTime(LocalDateTime.now());
            memberUserRespDTO.setLevelId(999l);
            otherSellingListDo.setMemberUserRespDTO(memberUserRespDTO);
            otherSellingPageReqVOS.add(otherSellingListDo);
        }
        return new PageResult(otherSellingPageReqVOS, (long) otherSellingDO.size());
    }
}

