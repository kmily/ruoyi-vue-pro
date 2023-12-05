package cn.iocoder.yudao.module.crm.dal.mysql.customer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerExportReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerLimitConfigReqVO;
import cn.iocoder.yudao.module.crm.controller.admin.customer.vo.CrmCustomerPageReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.iocoder.yudao.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * 客户限制配置 Mapper
 *
 * @author Joey
 */
@Mapper
public interface CrmCustomerLimitConfigMapper extends BaseMapperX<CrmCustomerLimitConfigDO> {




    default CrmCustomerLimitConfigDO selectByLimitConfig(CrmCustomerLimitConfigReqVO reqVO){
        LambdaQueryWrapperX<CrmCustomerLimitConfigDO> queryWrapper = new LambdaQueryWrapperX<>();
            queryWrapper.apply("FIND_IN_SET({0}, user_ids) > 0", reqVO.getUserIds());
            queryWrapper.eq(CrmCustomerLimitConfigDO::getType, reqVO.getType());
            queryWrapper.apply("FIND_IN_SET({0}, dept_ids) > 0", reqVO.getDeptIds());
        return selectOne(queryWrapper);

    }

}
