package cn.iocoder.yudao.module.steam.job.uu;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.steam.controller.app.vo.ApiResult;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodeityService;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityDO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.ApiUUCommodityReqVO;
import cn.iocoder.yudao.module.steam.controller.app.vo.UUCommondity.CommodityList;
import cn.iocoder.yudao.module.steam.dal.dataobject.youyoutemplate.YouyouTemplateDO;
import cn.iocoder.yudao.module.steam.dal.mysql.youyoutemplate.YouyouTemplateMapper;
import cn.iocoder.yudao.module.steam.service.uu.UUService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class UUGetCommodityJob implements JobHandler {

    @Resource
    private ApiUUCommodeityService apiUUCommodeityService;
    @Resource
    private YouyouTemplateMapper youyouTemplateMapper;

    @Resource
    private UUService uuService;

    @Autowired
    public void setApiUUCommodityService(ApiUUCommodeityService apiUUCommodeityService) {
        this.apiUUCommodeityService = apiUUCommodeityService;
    }

    @Override
    public String execute(String param) {
        Integer execute = TenantUtils.execute(1L, () -> {
            // 查询所有模板 ID
            List<Integer> allTemplateIds = getTemplateIds();

            // 分批次查询商品信息
            int totalCount = 0;
            int batchSize = 250; // 每批查询 250 个 ID
            int threadCount = 10; // 使用 10 个线程

            // 定义一个List，用来存放Future类型的ApiResult<CommodityList>
            List<Future<ApiResult<CommodityList>>> futures = new ArrayList<>();
            // 创建一个固定线程数的线程池
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            // 循环遍历allTemplateIds，分批处理
            for (int i = 0; i < allTemplateIds.size(); i += batchSize * threadCount) {
                // 循环threadCount次，每次处理batchSize个数据
                for (int j = 0; j < threadCount; j++) {
                    // 计算当前批次的起始位置
                    int start = i + j * batchSize;
                    // 计算当前批次的结束位置，并取最小值，防止数组越界
                    int end = Math.min(start + batchSize, allTemplateIds.size());
                    // 获取当前批次的数据
                    List<Integer> batchIds = allTemplateIds.subList(start, end);

                    // 创建ApiUUCommodityReqVO对象，并设置单个templateId
                    for (Integer batchId : batchIds) {
                        ApiUUCommodityReqVO reqVO = new ApiUUCommodityReqVO();
                        reqVO.setTemplateId(String.valueOf(batchId));

                        // 使用线程池执行任务
                        Future<ApiResult<CommodityList>> future = executorService.submit(() -> {
                            // 调用uuService的getCommodityList方法获取商品列表
                            ApiResult<CommodityList> apiResult = uuService.getCommodityList(reqVO);

                            if (apiResult.isSuccess() && apiResult.getData() != null) {
                                CommodityList commodityList = apiResult.getData();
                                commodityList.setData(commodityList.getData().subList(0, Math.min(50, commodityList.getData().size())));
                                return ApiResult.success(commodityList);
                            } else {
                                return apiResult;
                            }
                        });
                        futures.add(future);
                    }
                }

                for (Future<ApiResult<CommodityList>> future : futures) {
                    try {
                        ApiResult<CommodityList> apiResult = future.get(30, TimeUnit.SECONDS); // 设置30秒超时时间
                        if (apiResult.isSuccess() && apiResult.getData() != null) {
                            apiUUCommodeityService.insertGoodsQuery(apiResult);
                            totalCount += apiResult.getData().getData().size();
                        } else {
                            log.error("调用 uuService.getCommodityList 方法返回错误: {}", apiResult.getMsg());
                        }
                    } catch (TimeoutException e) {
                        log.error("调用 uuService.getCommodityList 方法超时: {}", e.getMessage(), e);
                    } catch (Exception e) {
                        log.error("等待线程任务出错: {}", e.getMessage(), e);
                    }
                }
                futures.clear();
            }

            executorService.shutdown();
            return totalCount;
        });

        return String.format("执行关闭成功 %s 个", execute);
    }

    //获取模板ids
    private List<Integer> getTemplateIds() {
        //创建一个Integer类型的列表
        List<Integer> templateIds = new ArrayList<>();
        //查询出所有的YouyouTemplateDO对象，只选择template_id字段
        List<YouyouTemplateDO> youyouTemplateDOS = youyouTemplateMapper.selectList(new QueryWrapper<YouyouTemplateDO>().select("template_id"));
        //遍历所有的YouyouTemplateDO对象
        for (YouyouTemplateDO youyouTemplateDO : youyouTemplateDOS) {
            //将所有的template_id添加到templateIds列表中
            templateIds.add(youyouTemplateDO.getTemplateId());
        }
        //返回templateIds列表
        return templateIds;
    }
}