package cn.iocoder.yudao.module.statistics.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductStatisticsComparisonRespVO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductSpuStatisticsDO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductStatisticsDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 商品统计 Service 接口
 *
 * @Author：麦子
 */
@Service
@Validated
public class ProductStatisticsServiceImpl implements ProductStatisticsService{

    /**
     * 根据日期或者商品统计的环比数据
     * @return
     */
    @Override
    public ProductStatisticsComparisonRespVO getProductStatisticsComparison() {
        return new ProductStatisticsComparisonRespVO()
                .setNewProductNum(0).setNewProductNumRatio(10)
                .setPageView(2).setPageViewRatio(1)
                .setCollectNum(3).setCollectNumRatio(-10)
                .setAddCartNum(4).setAddCartNumRatio(-11)
                .setOrderProductNum(5).setOrderProductNumRatio(-20)
                .setOrderSuccessProductNum(6).setOrderSuccessProductNumRatio(-30)
                ;
    }

    /**
     * 根据日期获取商品统计的趋势
     * @return
     */
    @Override
    public List<ProductStatisticsDO> getProductStatisticsList(){
        return null;
    }

    /**
     * 根据日期获取商品spu的排行
     * @return
     */
    @Override
    public PageResult<ProductSpuStatisticsDO> getProductSpuStatisticsPage(){
        return null;
    }
}
