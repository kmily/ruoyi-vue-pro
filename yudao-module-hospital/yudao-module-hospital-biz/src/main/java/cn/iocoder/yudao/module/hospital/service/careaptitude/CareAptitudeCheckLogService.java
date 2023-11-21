package cn.iocoder.yudao.module.hospital.service.careaptitude;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.careaptitude.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeCheckLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 医护资质审核记录 Service 接口
 *
 * @author 芋道源码
 */
public interface CareAptitudeCheckLogService extends IService<CareAptitudeCheckLogDO>{


    /**
     * 获得医护资质审核记录
     *
     * @param id 编号
     * @return 医护资质审核记录
     */
    CareAptitudeCheckLogDO getCareAptitudeCheckLog(Long id);

    /**
     * 获得医护资质审核记录列表
     *
     * @param ids 编号
     * @return 医护资质审核记录列表
     */
    List<CareAptitudeCheckLogDO> getCareAptitudeCheckLogList(Collection<Long> ids);

    /**
     * 获得医护资质审核记录分页
     *
     * @param pageReqVO 分页查询
     * @return 医护资质审核记录分页
     */
    PageResult<CareAptitudeCheckLogDO> getCareAptitudeCheckLogPage(CareAptitudeCheckLogPageReqVO pageReqVO);

    /**
     * 获得医护资质审核记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 医护资质审核记录列表
     */
    List<CareAptitudeCheckLogDO> getCareAptitudeCheckLogList(CareAptitudeCheckLogExportReqVO exportReqVO);

}
