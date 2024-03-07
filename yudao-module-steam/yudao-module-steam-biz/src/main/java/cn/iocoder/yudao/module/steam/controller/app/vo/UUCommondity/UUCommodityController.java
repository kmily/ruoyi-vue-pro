package cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity;


import cn.iocoder.yudao.module.steam.controller.admin.youyoucommodity.vo.UUCommodityRespVO;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplateRespVO;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplatedownloadRespVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoucommodity.YouyouCommodityDO;
import cn.iocoder.yudao.module.steam.service.uu.UUService;
import cn.iocoder.yudao.module.steam.utils.HttpUtil;
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
import java.util.ArrayList;

@Slf4j
@Tag(name = "管理后台 - 悠悠商品列表")
@RestController
@RequestMapping("/steam/youyou_commodity")
@Validated
public class UUCommodityController {

    @Resource
    private UUService uuService;

    @Resource
    private ObjectMapper objectMapper;

    @PostMapping("/insert")
    @Operation(summary = "插入UU商品列表")
    public void youyouTemplate(YouyouTemplateRespVO youyouTemplateRespVO) throws IOException {

        ApiResult<YouyouTemplatedownloadRespVO> templateId = uuService.getCommodityList(youyouTemplateRespVO);

        YouyouTemplatedownloadRespVO data = templateId.getData();

        Integer code = templateId.getCode();


    }
}
