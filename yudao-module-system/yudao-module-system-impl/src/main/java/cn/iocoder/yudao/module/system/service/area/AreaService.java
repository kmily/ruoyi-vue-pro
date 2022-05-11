package cn.iocoder.yudao.module.system.service.area;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaExportReqVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaRespVO;
import cn.iocoder.yudao.module.system.controller.admin.area.vo.AreaUpdateReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.area.AreaDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * @author lyt
 * @version 1.0.0
 * @ClassName AreaService.java
 * @Description TODO
 * @createTime 2022年04月07日 17:42:00
 */
public interface AreaService {


    /**
     * 初始化行政区树
     */
    boolean initArea();


    /**
     * 更新行政区域
     *
     * @param updateReqVO 更新信息
     */
    void updateArea(@Valid AreaUpdateReqVO updateReqVO);

    /**
     * 获得行政区域分页
     *
     * @param pageReqVO 分页查询
     * @return 行政区域分页
     */
    PageResult<AreaDO> getAreaPage(AreaPageReqVO pageReqVO);

    /**
     * 获得全国省份列表
     */
    List<AreaDO> provinces(AreaRespVO areaRespVO);


    /**
     * 获得省份下的城市列表
     */
    List<AreaDO> cities(AreaRespVO provinceCode);

    /**
     * 获得城市下的区县列表
     */
    List<AreaDO> counties(AreaRespVO cityCode);

    /**
     * 获得区县下的街道列表
     */
    List<AreaDO> towns(AreaRespVO countyCode);

    /**
     * 获得街道下的社区或村列表
     */
    List<AreaDO> villages(AreaRespVO townCode);

}
