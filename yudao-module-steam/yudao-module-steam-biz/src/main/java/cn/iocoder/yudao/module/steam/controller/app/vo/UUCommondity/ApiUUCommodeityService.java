package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;


import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoucommodity.UUCommodityMapper;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.UUTemplateMapper;
import cn.iocoder.yudao.module.steam.service.steam.InvTransferStatusEnum;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiUUTemplateVO;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ApiUUCommodeityService {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UUTemplateMapper uuTemplateMapper;

    @Resource
    private UUCommodityMapper uuCommodityMapper;




    /**
     *  下载UU商品模板
     */
    @Async
    @Transactional
    public void insertTemplateId(ApiResult<String> templateId) throws JsonProcessingException {
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
    @Transactional
    public void insertGoodsQuery(ApiResult<CommodityList> commodityList) throws JsonProcessingException {
        String commodityListJson = objectMapper.writeValueAsString(commodityList.getData());
        List<ApiUUCommodityDO> apiUUCommodityDOS = objectMapper.readValue(commodityListJson, new TypeReference<List<ApiUUCommodityDO>>() {});

        List<YouyouCommodityDO> youyouCommodityDOS = uuCommodityMapper.selectList(new QueryWrapper<YouyouCommodityDO>().select("id"));
        // 库存已有UU商品ID表
        ArrayList<Integer> localList = new ArrayList<>();
        // UU商品ID表
        ArrayList<Integer> UUList = new ArrayList<>();
        for (YouyouCommodityDO youyouCommodityDO : youyouCommodityDOS) {
            localList.add(youyouCommodityDO.getId());
        }
        List<YouyouCommodityDO> goodsList = new ArrayList<>();
        YouyouCommodityDO goods = new YouyouCommodityDO();
        for (ApiUUCommodityDO apiUUCommodityDO : apiUUCommodityDOS) {
            UUList.add(apiUUCommodityDO.getId());
            if(!localList.contains(apiUUCommodityDO.getId())){
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
        }
        if(!goodsList.isEmpty()){
            uuCommodityMapper.insertBatch(goodsList);
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (YouyouCommodityDO youyouCommodityDO : youyouCommodityDOS) {
            if(!UUList.contains(youyouCommodityDO.getTemplateId())){
                list.add(youyouCommodityDO.getTemplateId());
            }
        }
        if(!list.isEmpty()){
            uuCommodityMapper.deleteBatchIds(list);
        }
    }

}
