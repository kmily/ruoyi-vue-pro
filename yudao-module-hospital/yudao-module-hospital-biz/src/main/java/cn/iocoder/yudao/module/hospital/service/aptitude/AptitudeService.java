package cn.iocoder.yudao.module.hospital.service.aptitude;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.aptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.aptitude.AptitudeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 资质信息 Service 接口
 *
 * @author 芋道源码
 */
public interface AptitudeService extends IService<AptitudeDO>{

    /**
     * 创建资质信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAptitude(@Valid AptitudeCreateReqVO createReqVO);

    /**
     * 更新资质信息
     *
     * @param updateReqVO 更新信息
     */
    void updateAptitude(@Valid AptitudeUpdateReqVO updateReqVO);

    /**
     * 删除资质信息
     *
     * @param id 编号
     */
    void deleteAptitude(Long id);

    /**
     * 获得资质信息
     *
     * @param id 编号
     * @return 资质信息
     */
    AptitudeDO getAptitude(Long id);

    /**
     * 获得资质信息列表
     *
     * @param ids 编号
     * @return 资质信息列表
     */
    List<AptitudeDO> getAptitudeList(Collection<Long> ids);

    /**
     * 获得资质信息分页
     *
     * @param pageReqVO 分页查询
     * @return 资质信息分页
     */
    PageResult<AptitudeDO> getAptitudePage(AptitudePageReqVO pageReqVO);

    /**
     * 获得资质信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 资质信息列表
     */
    List<AptitudeDO> getAptitudeList(AptitudeExportReqVO exportReqVO);

    /**
     * 查询所有启用的资质信息
     * @return
     */
    List<AptitudeDO> listAllEnable();
}
