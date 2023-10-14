package cn.iocoder.yudao.module.statistics.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.statistics.controller.admin.product.vo.ProductStatisticsComparisonRespVO;
import cn.iocoder.yudao.module.statistics.controller.admin.trade.vo.TradeSummaryRespVO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductSpuStatisticsDO;
import cn.iocoder.yudao.module.statistics.dal.mysql.product.ProductStatisticsDO;

import java.util.List;

/**
 * 商品统计 Service 接口
 *
 * @Author：麦子
 */
public interface ProductStatisticsService {

    /**
     * 根据日期或者商品统计的环比数据
     * @return
     */
    ProductStatisticsComparisonRespVO getProductStatisticsComparison();

    /**
     * 根据日期获取商品统计的趋势
     * @return
     */
    List<ProductStatisticsDO> getProductStatisticsList();

    /**
     * 根据日期获取商品spu的排行
     * @return
     */
    PageResult<ProductSpuStatisticsDO> getProductSpuStatisticsPage();
}
