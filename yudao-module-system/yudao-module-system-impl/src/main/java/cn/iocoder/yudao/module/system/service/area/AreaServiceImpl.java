package cn.iocoder.yudao.module.system.service.area;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaRespVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaUpdateReqVO;
import cn.iocoder.yudao.module.system.convert.area.AreaConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.area.AreaDO;
import cn.iocoder.yudao.module.system.dal.mysql.area.AreaMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.AREA_NOT_EXISTS;

/**
 * @author lyt
 * @version 1.0.0
 * @ClassName AreaServiceImpl.java
 * @Description TODO
 * @createTime 2022年04月07日 17:43:00
 */

@Service
@Validated
@Slf4j
public class AreaServiceImpl implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    public static void main(String[] args) {
        initArea1();
    }


    /**
     * 初始化国家统计局数据 拉取耗时比较长
     * <p>
     * http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/index.html
     */
    public static boolean initArea1() {

        String baseUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/";

        String version = "2021";

        String indexUrl = baseUrl + "index.html";

        List<AreaDO> areaList = new ArrayList<>();

        try {
            Document provinceDoc = Jsoup.connect(indexUrl).get();
            log.info(provinceDoc.title());
            Elements provinceElements = provinceDoc.select(".provincetr a ");
            for (Element provinceElement : provinceElements) {
                log.info("省：{},href；{}", provinceElement.text(), provinceElement.attr("href"));
                String provinceHref = provinceElement.attr("href");
                String provinceCode = provinceHref.replaceAll("\\.html", "");
                String provinceName = provinceElement.text();

                AreaDO provinceArea = new AreaDO();
                provinceArea.setDataVersion(version);
                provinceArea.setUseFlag(1);
                provinceArea.setProvince(provinceName);
                provinceArea.setProvinceCode(provinceCode);

                areaList.add(provinceArea);

                String cityUrl = baseUrl + provinceHref;

                Thread.sleep(1000);
                Document cityDoc = Jsoup.connect(cityUrl).get();
                Elements cityElements = cityDoc.select(".citytr");

                for (Element cityElement : cityElements) {
                    Elements nodes = cityElement.select("td");
                    Element cityNode0 = nodes.get(0);
                    Element cityNode1 = nodes.get(1);

                    String countryHref = cityNode0.select("a").attr("href");
                    String cityCode = countryHref.split("/")[0];
                    String cityCode0 = cityNode0.text();
                    String cityName = cityNode1.text();
                    log.info("市：{}-{}-{}", cityNode0.text(), cityNode1.text(), countryHref);

                    AreaDO cityArea = new AreaDO();
                    cityArea.setDataVersion(version);
                    cityArea.setUseFlag(1);
                    cityArea.setProvince(provinceName);
                    cityArea.setProvinceCode(provinceCode);
                    cityArea.setCity(cityName);
                    cityArea.setCityCode(cityCode0);

                    areaList.add(cityArea);

                    String countryUrl = baseUrl + countryHref;

                    Thread.sleep(1000);
                    Document countryDoc = Jsoup.connect(countryUrl).get();
                    Elements countyElements = countryDoc.select(".countytr");
                    for (Element countyElement : countyElements) {
                        Elements countyNodes = countyElement.select("td");

                        Element countyNode0 = countyNodes.get(0);
                        Element countyNode1 = countyNodes.get(1);

                        String townHref = countyNode0.select("a").attr("href");
                        String townCode = townHref.split("/")[0];
                        log.info("区：{}-{}-{}", countyNode0.text(), countyNode1.text(), townHref);

                        AreaDO countyArea = new AreaDO();
                        countyArea.setDataVersion(version);
                        countyArea.setUseFlag(1);
                        countyArea.setProvince(provinceName);
                        countyArea.setProvinceCode(provinceCode);
                        countyArea.setCity(cityName);
                        countyArea.setCityCode(cityCode0);
                        countyArea.setCounty(countyNode1.text());
                        countyArea.setCountyCode(countyNode0.text());

                        areaList.add(countyArea);


                        String townUrl = baseUrl + cityCode + "/" + townHref;
                        //乡镇
                        Thread.sleep(1000);
                        Document townDoc = Jsoup.connect(townUrl).get();
                        Elements townElements = townDoc.select(".towntr");
                        for (Element townElement : townElements) {
                            Elements townNodes = townElement.select("td");

                            Element townNode0 = townNodes.get(0);
                            Element townNode1 = townNodes.get(1);

                            String villageHref = townNode0.select("a").attr("href");
                            log.info("街道：{}-{}-{}", townNode0.text(), townNode1.text(), villageHref);


                            AreaDO townArea = new AreaDO();
                            townArea.setDataVersion(version);
                            townArea.setUseFlag(1);
                            townArea.setProvince(provinceName);
                            townArea.setProvinceCode(provinceCode);
                            townArea.setCity(cityName);
                            townArea.setCityCode(cityCode0);
                            townArea.setCounty(countyNode1.text());
                            townArea.setCountyCode(countyNode0.text());
                            townArea.setTown(townNode1.text());
                            townArea.setTownCode(townNode0.text());
                            areaList.add(townArea);


                            String villageUrl = baseUrl + cityCode + "/" + townCode + "/" + villageHref;
                            //
                            Thread.sleep(1000);
                            Document villageDoc = Jsoup.connect(villageUrl).get();
                            Elements villageElements = villageDoc.select(".villagetr");
                            for (Element villageElement : villageElements) {
                                Elements villageNodes = villageElement.select("td");

                                Element villageNode0 = villageNodes.get(0);
                                Element villageNode1 = villageNodes.get(1);
                                Element villageNode2 = villageNodes.get(2);

                                log.info("村社区：{}-{}-{}", villageNode0.text(), villageNode1.text(), villageNode2.text());

                                AreaDO villageArea = new AreaDO();
                                villageArea.setDataVersion(version);
                                villageArea.setUseFlag(1);
                                villageArea.setProvince(provinceName);
                                villageArea.setProvinceCode(provinceCode);
                                villageArea.setCity(cityName);
                                villageArea.setCityCode(cityCode0);
                                villageArea.setCounty(countyNode1.text());
                                villageArea.setCountyCode(countyNode0.text());
                                villageArea.setTown(townNode1.text());
                                villageArea.setTownCode(townNode0.text());
                                villageArea.setVillage(villageNode2.text());
                                villageArea.setVillageCode(villageNode0.text());

                                areaList.add(villageArea);

                            }
                        }
                    }
                }
            }

            System.out.println(areaList.size());
            return true;
        } catch (Exception e) {
            log.error("jsoup error :{}", e);
        }

        return false;

    }


    @Override
    public boolean initArea() {
        return initArea1();
    }

    private void validateAreaExists(Long id) {
        if (areaMapper.selectById(id) == null) {
            throw exception(AREA_NOT_EXISTS);
        }
    }

    @Override
    public void updateArea(AreaUpdateReqVO updateReqVO) {
        // 校验存在
        this.validateAreaExists(updateReqVO.getId());
        // 更新
        AreaDO updateObj = AreaConvert.INSTANCE.convert(updateReqVO);
        areaMapper.updateById(updateObj);
    }

    @Override
    public PageResult<AreaDO> getAreaPage(AreaPageReqVO pageReqVO) {
        return areaMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AreaDO> provinces(AreaRespVO areaRespVO) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getDataVersion, areaRespVO.getDataVersion());
        queryWrapper.eq(AreaDO::getUseFlag, 1);
        queryWrapper.eq(AreaDO::getCityCode, 0);
        queryWrapper.eq(AreaDO::getCountyCode, 0);
        queryWrapper.eq(AreaDO::getTownCode, 0);
        queryWrapper.eq(AreaDO::getVillageCode, 0);
        return areaMapper.selectList(queryWrapper);
    }

    @Override
    public List<AreaDO> cities(AreaRespVO areaRespVO) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getDataVersion, areaRespVO.getDataVersion());
        queryWrapper.eq(AreaDO::getUseFlag, 1);
        queryWrapper.eq(AreaDO::getProvinceCode, areaRespVO.getProvinceCode());
        queryWrapper.ne(AreaDO::getCityCode, 0);
        queryWrapper.eq(AreaDO::getCountyCode, 0);
        queryWrapper.eq(AreaDO::getTownCode, 0);
        queryWrapper.eq(AreaDO::getVillageCode, 0);
        return areaMapper.selectList(queryWrapper);
    }

    @Override
    public List<AreaDO> counties(AreaRespVO areaRespVO) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getDataVersion, areaRespVO.getDataVersion());
        queryWrapper.eq(AreaDO::getUseFlag, 1);
        queryWrapper.eq(AreaDO::getProvinceCode, areaRespVO.getProvinceCode());
        queryWrapper.eq(AreaDO::getCityCode, areaRespVO.getCityCode());
        queryWrapper.ne(AreaDO::getCountyCode, 0);
        queryWrapper.eq(AreaDO::getTownCode, 0);
        queryWrapper.eq(AreaDO::getVillageCode, 0);
        return areaMapper.selectList(queryWrapper);
    }

    @Override
    public List<AreaDO> towns(AreaRespVO areaRespVO) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getDataVersion, areaRespVO.getDataVersion());
        queryWrapper.eq(AreaDO::getUseFlag, 1);
        queryWrapper.eq(AreaDO::getProvinceCode, areaRespVO.getProvinceCode());
        queryWrapper.eq(AreaDO::getCityCode, areaRespVO.getCityCode());
        queryWrapper.eq(AreaDO::getCountyCode, areaRespVO.getCountyCode());
        queryWrapper.ne(AreaDO::getTownCode, 0);
        queryWrapper.eq(AreaDO::getVillageCode, 0);
        return areaMapper.selectList(queryWrapper);
    }


    @Override
    public List<AreaDO> villages(AreaRespVO areaRespVO) {
        LambdaQueryWrapper<AreaDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AreaDO::getDataVersion, areaRespVO.getDataVersion());
        queryWrapper.eq(AreaDO::getUseFlag, 1);
        queryWrapper.eq(AreaDO::getProvinceCode, areaRespVO.getProvinceCode());
        queryWrapper.eq(AreaDO::getCityCode, areaRespVO.getCityCode());
        queryWrapper.eq(AreaDO::getCountyCode, areaRespVO.getCountyCode());
        queryWrapper.eq(AreaDO::getTownCode, areaRespVO.getTownCode());
        queryWrapper.ne(AreaDO::getVillageCode, 0);
        return areaMapper.selectList(queryWrapper);
    }



}
