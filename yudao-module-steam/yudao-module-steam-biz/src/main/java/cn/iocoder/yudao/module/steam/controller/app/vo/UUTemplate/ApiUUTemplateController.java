package cn.iocoder.yudao.module.steam.controller.app.vo.UUTemplate;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplatedownloadRespVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.UUTemplateMapper;
import cn.iocoder.yudao.module.steam.service.uu.UUService;
import cn.iocoder.yudao.module.steam.service.uu.vo.ApiUUTemplateVO;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;




@Slf4j
@Tag(name = "管理后台 - 悠悠商品模板")
@RestController
@RequestMapping("/steam/youyou_inv")
@Validated
public class ApiUUTemplateController {

    @Resource
    private UUService uuService;
    @Resource
    private UUTemplateMapper uuTemplateMapper;

    @Resource
    private ObjectMapper objectMapper;


    @PostMapping("/template_insert")
    @Operation(summary = "插入UU商品平台")
    public void youyouTemplate() throws IOException {
        ApiResult<YouyouTemplatedownloadRespVO> templateId = uuService.getTemplateId();
    //    String url="https://youpin-commodity.oss-cn-shenzhen.aliyuncs.com/youpin/commodity_template/20240306204216_ac4c6a1.txt?Expires=1709733036&OSSAccessKeyId=LTAI5tEdUtG2NHxwgB6Kpnao&Signature=2FkonptvMGmqWC%2BkPLMs3JEsk6U%3D";
        HttpUtil.HttpRequest.HttpRequestBuilder builder = HttpUtil.HttpRequest.builder();
        builder.method(HttpUtil.Method.GET).url(templateId.getData().getData());
    //    builder.method(HttpUtil.Method.GET).url(url);
        HttpUtil.HttpResponse sent = HttpUtil.sent(builder.build(),HttpUtil.getClient());
        ArrayList json = sent.json(ArrayList.class);
        List<ApiUUTemplateVO> templateVOS = new ArrayList<>();
        for (Object item:json){
            ApiUUTemplateVO apiUUTemplateVO = objectMapper.readValue(objectMapper.writeValueAsString(item), ApiUUTemplateVO.class);
            templateVOS.add(apiUUTemplateVO);
        }
        for(ApiUUTemplateVO item:templateVOS){
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
}