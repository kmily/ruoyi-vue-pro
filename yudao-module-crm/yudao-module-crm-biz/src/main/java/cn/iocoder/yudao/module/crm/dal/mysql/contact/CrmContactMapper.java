package cn.iocoder.yudao.module.crm.dal.mysql.contact;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import cn.iocoder.yudao.module.crm.controller.admin.contact.vo.CrmContactPageReqVO;
import cn.iocoder.yudao.module.crm.dal.dataobject.contact.CrmContactDO;
import cn.iocoder.yudao.module.crm.enums.common.CrmBizTypeEnum;
import cn.iocoder.yudao.module.crm.util.CrmQueryPageUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * CRM 联系人 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CrmContactMapper extends BaseMapperX<CrmContactDO> {

    default PageResult<CrmContactDO> selectPage(CrmContactPageReqVO pageReqVO, Long userId, Collection<Long> subordinateIds, Boolean isAdmin) {
        IPage<CrmContactDO> mpPage = MyBatisUtils.buildPage(pageReqVO);
        MPJLambdaWrapperX<CrmContactDO> mpjLambdaWrapperX = new MPJLambdaWrapperX<>();
        // 构建数据权限连表条件
        CrmQueryPageUtils.builderQuery(mpjLambdaWrapperX, pageReqVO, userId,
                CrmBizTypeEnum.CRM_CONTACT.getType(), CrmContactDO::getId, subordinateIds, isAdmin);
        mpjLambdaWrapperX
                .selectAll(CrmContactDO.class)
                .eq(CrmContactDO::getCustomerId, pageReqVO.getCustomerId()) // 必须传递
                .likeIfPresent(CrmContactDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmContactDO::getMobile, pageReqVO.getMobile())
                .eqIfPresent(CrmContactDO::getTelephone, pageReqVO.getTelephone())
                .eqIfPresent(CrmContactDO::getEmail, pageReqVO.getEmail())
                .eqIfPresent(CrmContactDO::getQq, pageReqVO.getQq())
                .eqIfPresent(CrmContactDO::getWechat, pageReqVO.getWechat())
                .orderByDesc(CrmContactDO::getId);
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(pageReqVO.getPageNo())) {
            List<CrmContactDO> list = selectJoinList(CrmContactDO.class, mpjLambdaWrapperX);
            return new PageResult<>(list, (long) list.size());
        }
        mpPage = selectJoinPage(mpPage, CrmContactDO.class, mpjLambdaWrapperX);
        return new PageResult<>(mpPage.getRecords(), mpPage.getTotal());
    }

}
