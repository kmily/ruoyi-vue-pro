package cn.iocoder.yudao.module.steam.service.invpreview;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.invpreview.vo.InvPreviewPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.invpreview.InvPreviewDO;
import cn.iocoder.yudao.module.steam.dal.mysql.invpreview.InvPreviewMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 饰品在售预览 Service 实现类
 *
 * @author LeeAm
 */
@Service
public class InvPreviewExtService {

    @Resource
    private InvPreviewMapper invPreviewMapper;



    public PageResult<InvPreviewDO> getInvPreviewPage(InvPreviewPageReqVO pageReqVO) {
        return invPreviewMapper.selectPage(pageReqVO);
    }

    /**
     * 增加库存标识
     * @param marketHashName 标签名称
     */
    public void markInv(String marketHashName) {
        List<InvPreviewDO> invPreviewDOS = invPreviewMapper.selectList(new LambdaQueryWrapperX<InvPreviewDO>()
                .eqIfPresent(InvPreviewDO::getMarketHashName, marketHashName));
        if(Objects.nonNull(invPreviewDOS)){
            invPreviewDOS.forEach(item->
                    invPreviewMapper.updateById(new InvPreviewDO().setId(item.getId()).setExistInv(true)));
        }
    }
}