package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;


import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUBatchGetOnSaleCommodity.BatchGetCommodity;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUBatchGetOnSaleCommodity.UUSaleTemplateRespVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;

import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.YouyouTemplateMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiUUTemplateVO;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiUUCommodeityService {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private YouyouTemplateMapper uuTemplateMapper;

    @Resource
    private UUCommodityMapper uuCommodityMapper;




    /**
     *  下载UU商品模板
     */
    @Async
    public void insertTemplateId(@RequestBody ApiResult<String> templateId) throws JsonProcessingException {
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.method(HttpUtil.Method.GET).url(templateId.getData());
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),HttpUtil.getClient());
        ArrayList json = sent.json(ArrayList.class);

        // 提取下载的UU模板id
        List<Integer> idList = new ArrayList<>();
        // 本地的UU模板id
        List<Integer> localList = new ArrayList<>();
        List<ApiUUTemplateVO> templateVOS = new ArrayList<>();
        for (Object item:json){
            ApiUUTemplateVO apiUUTemplateVO = objectMapper.readValue(objectMapper.writeValueAsString(item), ApiUUTemplateVO.class);
            templateVOS.add(apiUUTemplateVO);
            idList.add(apiUUTemplateVO.getId());
        }
        // TODO 对象直接提取成列表
        List<YouyouTemplateDO> templateIds = uuTemplateMapper.selectList(new QueryWrapper<YouyouTemplateDO>().select("template_id"));
        for(YouyouTemplateDO youyouTemplateDO: templateIds){
            localList.add(youyouTemplateDO.getTemplateId());
        }

        // UU库存到本地
        for(ApiUUTemplateVO item:templateVOS){
            if(!localList.contains(item.getId())){
                YouyouTemplateDO templateDO = new YouyouTemplateDO();
                templateDO.setTemplateId(item.getId());
                templateDO.setName(item.getName());
                templateDO.setHashName(item.getHashName());
                templateDO.setTypeId(item.getTypeId());
                templateDO.setTypeHashName(item.getTypeHashName());
                templateDO.setWeaponId(item.getWeaponId());
                templateDO.setWeaponName(item.getWeaponName());
                uuTemplateMapper.insert(templateDO);
            }
        }
        ArrayList<Integer> list = new ArrayList<>();
        // 本地对比UU
        for(YouyouTemplateDO item: templateIds){
            if(!idList.contains(item.getTemplateId())){
                list.add(item.getTemplateId());
            }
        }
        if(!list.isEmpty()){
            uuTemplateMapper.deleteBatchIds(list);
        }

    }



    /**
     *  下载UU商品列表
     */
    @Async
    public void insertGoodsQuery(@RequestBody ApiResult<CommodityList> commodityList, Integer templateId) throws JsonProcessingException {
        String commodityListJson = objectMapper.writeValueAsString(commodityList.getData());
        List<ApiUUCommodityDO> apiUUCommodityDOS = objectMapper.readValue(commodityListJson, new TypeReference<List<ApiUUCommodityDO>>() {});

        uuCommodityMapper.delete(new LambdaQueryWrapperX<YouyouCommodityDO>().eq(YouyouCommodityDO::getTemplateId,templateId));


        List<YouyouCommodityDO> youyouCommodityDOS = uuCommodityMapper.selectList(new QueryWrapper<YouyouCommodityDO>().select("id"));
        // 库存已有UU商品ID表
        ArrayList<Integer> localList = new ArrayList<>();
        // UU商品ID表
        ArrayList<Integer> UUList = new ArrayList<>();
        for (YouyouCommodityDO youyouCommodityDO : youyouCommodityDOS) {
            localList.add(youyouCommodityDO.getId());
        }
        List<YouyouCommodityDO> goodsList = new ArrayList<>();
        for (ApiUUCommodityDO apiUUCommodityDO : apiUUCommodityDOS) {
            YouyouCommodityDO goods = new YouyouCommodityDO();
            UUList.add(apiUUCommodityDO.getId());
                goods.setId(apiUUCommodityDO.getId());
                goods.setTemplateId(apiUUCommodityDO.getTemplateId());
                goods.setShippingMode(apiUUCommodityDO.getShippingMode());
                goods.setCommodityName(apiUUCommodityDO.getCommodityName());
                goods.setCommodityPrice(apiUUCommodityDO.getCommodityPrice());
                goods.setCommodityAbrade(apiUUCommodityDO.getCommodityAbrade());
                goods.setCommodityPaintSeed(apiUUCommodityDO.getCommodityPaintSeed());
                goods.setCommodityPaintIndex(apiUUCommodityDO.getCommodityPaintIndex());
                goods.setCommodityHaveNameTag(apiUUCommodityDO.getCommodityHaveNameTag());
                goods.setCommodityHaveBuzhang(apiUUCommodityDO.getCommodityHaveBuzhang());
                goods.setCommodityHaveSticker(apiUUCommodityDO.getCommodityHaveSticker());
                goods.setTemplateisFade(apiUUCommodityDO.getTemplateisFade());
                goods.setTemplateisHardened(apiUUCommodityDO.getTemplateisHardened());
                goods.setTemplateisDoppler(apiUUCommodityDO.getTemplateisDoppler());
                goods.setCommodityStickers(apiUUCommodityDO.getCommodityStickers().toString());
                goods.setTransferStatus(InvTransferStatusEnum.SELL.getStatus());
                goods.setStatus(0);
                goodsList.add(goods);

        }
        if(!goodsList.isEmpty()){
            uuCommodityMapper.insertBatch(goodsList);
        }
/*        ArrayList<Integer> list = new ArrayList<>();
        for (YouyouCommodityDO youyouCommodityDO : youyouCommodityDOS) {
            if(!UUList.contains(youyouCommodityDO.getTemplateId())){
                list.add(youyouCommodityDO.getTemplateId());
            }
        }*/
/*        if(!list.isEmpty()){
            uuCommodityMapper.deleteBatchIds(list);
        }*/
    }


    /**
     *  批量查询在售商品价格插入数据库
     */
    @Async
    public void insertOnSaleCommodityInfo(@RequestBody ApiResult<BatchGetCommodity> batchGetCommodityApiResult) throws JsonProcessingException {

        String onSaleCommodityInfoListJson = objectMapper.writeValueAsString(batchGetCommodityApiResult.getData());
        List<UUSaleTemplateRespVO> uuSaleTemplateRespVOS = objectMapper.readValue(onSaleCommodityInfoListJson, new TypeReference<List<UUSaleTemplateRespVO>>() {});

        List<YouyouTemplateDO> list = new ArrayList<>();
        for (UUSaleTemplateRespVO uuSaleTemplateRespVO : uuSaleTemplateRespVOS) {
            YouyouTemplateDO templateDO = new YouyouTemplateDO();
            templateDO.setTemplateId(Integer.valueOf(uuSaleTemplateRespVO.getSaleTemplateResponse().getTemplateId()));
            templateDO.setTypeHashName(uuSaleTemplateRespVO.getSaleTemplateResponse().getTemplateHashName());
            templateDO.setIconUrl(uuSaleTemplateRespVO.getSaleTemplateResponse().getIconUrl());
            templateDO.setExteriorName(uuSaleTemplateRespVO.getSaleTemplateResponse().getExteriorName());
            templateDO.setRarityName(uuSaleTemplateRespVO.getSaleTemplateResponse().getRarityName());
            templateDO.setQualityName(uuSaleTemplateRespVO.getSaleTemplateResponse().getQualityName());
            templateDO.setMinSellPrice(uuSaleTemplateRespVO.getSaleCommodityResponse().getMinSellPrice());
            templateDO.setFastShippingMinSellPrice(uuSaleTemplateRespVO.getSaleCommodityResponse().getFastShippingMinSellPrice());
            templateDO.setReferencePrice(uuSaleTemplateRespVO.getSaleCommodityResponse().getReferencePrice());
            templateDO.setSellNum(uuSaleTemplateRespVO.getSaleCommodityResponse().getSellNum());
            list.add(templateDO);
        }
        uuTemplateMapper.updateBatch(list);
    }

    public Integer getOnSealGoodsId(ApiUUBuyGoodsByIdReqVO apiUUBuyGoodsByIdReqVO) {
        List<YouyouCommodityDO> youyouCommodityDOS = null;
        if (apiUUBuyGoodsByIdReqVO.getTemplateId() != null) {
            youyouCommodityDOS = uuCommodityMapper.selectList(new LambdaQueryWrapperX<YouyouCommodityDO>()
                    .eq(YouyouCommodityDO::getTemplateId, apiUUBuyGoodsByIdReqVO.getTemplateId())
                    .orderByAsc(YouyouCommodityDO::getCommodityPrice));
            // uu价格
            int num1 = Integer.parseInt(youyouCommodityDOS.get(0).getCommodityPrice());
            // 买家最大出价
            int num2 = Integer.parseInt(apiUUBuyGoodsByIdReqVO.getMaxPrice());
            if(num1 < num2){
                return youyouCommodityDOS.get(0).getId();

            }
            return null;
        }
        YouyouTemplateDO youyouTemplateDO = uuTemplateMapper.selectOne(new LambdaQueryWrapperX<YouyouTemplateDO>().eq(YouyouTemplateDO::getTemplateId, apiUUBuyGoodsByIdReqVO.getTemplateId()));
        List<YouyouCommodityDO> youyouCommodityDOS1 = uuCommodityMapper.selectList(new LambdaQueryWrapperX<YouyouCommodityDO>()
                .eq(YouyouCommodityDO::getTemplateId, youyouTemplateDO.getTemplateId())
                .orderByAsc(YouyouCommodityDO::getCommodityPrice));
        // uu价格
        int num1 = Integer.parseInt(youyouCommodityDOS1.get(0).getCommodityPrice());
        // 买家最大出价
        int num2 = Integer.parseInt(apiUUBuyGoodsByIdReqVO.getMaxPrice());
        if(num1 < num2){
            return youyouCommodityDOS1.get(0).getId();
        }
        return null;
    }
}
