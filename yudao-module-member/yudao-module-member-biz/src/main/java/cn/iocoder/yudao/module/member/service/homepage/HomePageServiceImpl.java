package cn.iocoder.yudao.module.member.service.homepage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.enums.HomePageType;
import cn.iocoder.yudao.module.radar.api.device.DeviceApi;
import cn.iocoder.yudao.module.radar.api.device.dto.DeviceDTO;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.iocoder.yudao.module.member.controller.app.homepage.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.homepage.HomePageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.homepage.HomePageConvert;
import cn.iocoder.yudao.module.member.dal.mysql.homepage.HomePageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 首页配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class HomePageServiceImpl implements HomePageService {

    @Resource
    private HomePageMapper homePageMapper;

    @Resource
    private DeviceApi deviceApi;

    @Override
    public Long createHomePage(AppHomePageCreateReqVO createReqVO) {
        // 插入
        HomePageDO homePage = HomePageConvert.INSTANCE.convert(createReqVO);
        homePageMapper.insert(homePage);
        // 返回
        return homePage.getId();
    }

    @Override
    public void updateHomePage(AppHomePageUpdateReqVO updateReqVO) {
        // 校验存在
        validateHomePageExists(updateReqVO.getId());
        // 更新
        HomePageDO updateObj = HomePageConvert.INSTANCE.convert(updateReqVO);
        homePageMapper.updateById(updateObj);
    }

    @Override
    public void deleteHomePage(Long id) {
        // 校验存在
        HomePageDO pageDO = homePageMapper.selectById(id);
        if(pageDO == null){
            throw exception(HOME_PAGE_NOT_EXISTS);
        }
        if(Objects.equals(pageDO.getMold(), (byte)0)){
            throw exception(HOME_PAGE_SYSTEM_CANOT_DELETED);
        }
        // 删除
        homePageMapper.deleteById(id);
    }

    private void validateHomePageExists(Long id) {
        if (homePageMapper.selectById(id) == null) {
            throw exception(HOME_PAGE_NOT_EXISTS);
        }
    }

    @Override
    public HomePageDO getHomePage(Long id) {
        return homePageMapper.selectById(id);
    }

    @Override
    public List<HomePageDO> getHomePageList(Collection<Long> ids) {
        return homePageMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<HomePageDO> getHomePagePage(AppHomePagePageReqVO pageReqVO) {
        return homePageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<HomePageDO> getHomePageList(AppHomePageExportReqVO exportReqVO) {
        return homePageMapper.selectList(exportReqVO);
    }

    @Override
    public List<HomePageDO> initialization(Long familyId) {
        HomePageType[] values = HomePageType.values();
        List<HomePageDO> homePageDOList = Stream.of(values)
                .map(type -> new HomePageDO()
                        .setFamilyId(familyId)
                        .setMold((byte) 0)
                        .setSort(type.type)
                        .setName(type.name)
                        .setUserId(SecurityFrameworkUtils.getLoginUserId())
                        .setType(type.type)).collect(Collectors.toList());
        homePageMapper.insertBatch(homePageDOList);
        return homePageDOList;
    }

    @Override
    public List<HomePageDO> getHomePageList(Long familyId) {
        List<HomePageDO> homePageDOList = homePageMapper.selectList(HomePageDO::getFamilyId, familyId);
        // 如果为空进行初始化
        if(homePageDOList.isEmpty()){
            homePageDOList =  initialization(familyId);
        }
        return homePageDOList;
    }

    @Override
    public void bindDevice(Long id, Set<Long> devices) {
        if(devices.isEmpty()){
            throw exception(HOME_PAGE_DEVICE_EMPTY);
        }
        validateHomePageExists(id);
        //List<DeviceDTO> deviceDTOS = deviceApi.getByIds(devices);
        String join = CollUtil.join(devices, ",");
        HomePageDO pageDO = new HomePageDO();
        pageDO.setDevices(join);
        pageDO.setId(id);
        homePageMapper.updateById(pageDO);
    }

    @Override
    public void saveOrUpdate(List<AppHomePageUpdateReqVO> updateReqVOS) {
        List<AppHomePageUpdateReqVO> saveList = updateReqVOS.stream().filter(item -> item.getId() == 0).collect(Collectors.toList());
        List<AppHomePageUpdateReqVO> updateList = updateReqVOS.stream().filter(item -> item.getId() != 0).collect(Collectors.toList());

        if(CollUtil.isNotEmpty(saveList)){
            List<HomePageDO> saveHpList = saveList.stream().map(item -> HomePageConvert.INSTANCE.convert(item).setId(null)).collect(Collectors.toList());
            homePageMapper.insertBatch(saveHpList);
        }

        if(CollUtil.isNotEmpty(updateList)){
            updateList.forEach(item -> {
                homePageMapper.updateById(HomePageConvert.INSTANCE.convert(item));
            });
        }

    }

}
