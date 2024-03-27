package cn.iocoder.yudao.module.steam.controller.app.vo.UUTemplate;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.steam.controller.admin.youyoutemplate.vo.YouyouTemplatePageReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUSellingList.QueryUUSellingListReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.service.youyoutemplate.UUTemplateService;
import cn.smallbun.screw.core.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@Slf4j
public class QueryUUTemplateService {

    @Resource
    private UUTemplateService uuTemplateService;

    public ApiResult<QueryUUSellingListReqVO> queryTemplateSaleByCategory(@RequestBody YouyouTemplatePageReqVO reqVO) {
        // 获取分页请求参数
        int currentPage = reqVO.getPageNo();
        int pageSize = reqVO.getPageSize();

        // 查询模板分页数据
        PageResult<YouyouTemplateDO> pageResult = uuTemplateService.getYouyouTemplatePage(reqVO);
        List<YouyouTemplateDO> youyouTemplateDOS = pageResult.getList();
        QueryUUSellingListReqVO queryUUSellingListReqVO = new QueryUUSellingListReqVO();
        List<QueryUUSellingListReqVO.SaleTemplateByCategoryResponseList> saleTemplateByCategoryResponseList = new ArrayList<>();

        // 如果模板列表为空,直接返回成功
        if (CollectionUtils.isEmpty(youyouTemplateDOS)) {
            queryUUSellingListReqVO.setSaleTemplateByCategoryResponseList(saleTemplateByCategoryResponseList);
            return ApiResult.success(queryUUSellingListReqVO, "成功");
        }

        // 遍历每个模板,计算相关指标并构建响应对象
        for (YouyouTemplateDO youyouTemplateDO : youyouTemplateDOS) {
            // 构建响应对象
            QueryUUSellingListReqVO.SaleTemplateByCategoryResponseList item = new QueryUUSellingListReqVO.SaleTemplateByCategoryResponseList();
            item.setTemplateId(youyouTemplateDO.getTemplateId());
            item.setTemplateHashName(youyouTemplateDO.getHashName());
            item.setTemplateName(youyouTemplateDO.getName());
            item.setIconUrl(youyouTemplateDO.getIconUrl());
            item.setExteriorName(youyouTemplateDO.getExteriorName());
            item.setRarityName(youyouTemplateDO.getRarityName());
            item.setTypeId(youyouTemplateDO.getTypeId());
            item.setTypeHashName(youyouTemplateDO.getTypeHashName());
            item.setWeaponId(youyouTemplateDO.getWeaponId());
            item.setWeaponHashName(youyouTemplateDO.getWeaponHashName());
            item.setMinSellPrice(youyouTemplateDO.getMinSellPrice());
            item.setFastShippingMinSellPrice(youyouTemplateDO.getFastShippingMinSellPrice());
            item.setSellNum(youyouTemplateDO.getSellNum());

            saleTemplateByCategoryResponseList.add(item);
        }

        // 按照 templateId 升序排列
        saleTemplateByCategoryResponseList.sort(Comparator.comparingInt(QueryUUSellingListReqVO.SaleTemplateByCategoryResponseList::getTemplateId));

        // 计算总页数
        int totalCount = Math.toIntExact(pageResult.getTotal());
        int totalPage = (totalCount + pageSize - 1) / pageSize;

        // 设置响应对象属性
        queryUUSellingListReqVO.setSaleTemplateByCategoryResponseList(saleTemplateByCategoryResponseList);
        queryUUSellingListReqVO.setCurrentPage(currentPage);
        queryUUSellingListReqVO.setTotalPage(totalPage);
        queryUUSellingListReqVO.setNewPageIsHaveContent(saleTemplateByCategoryResponseList.size() > 1);

        return ApiResult.success(queryUUSellingListReqVO, "成功");
    }
}
