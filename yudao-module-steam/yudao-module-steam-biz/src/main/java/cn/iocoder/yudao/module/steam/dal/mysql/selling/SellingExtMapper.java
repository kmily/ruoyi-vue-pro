package cn.iocoder.yudao.module.steam.dal.mysql.selling;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.steam.controller.admin.selling.vo.SellingPageReqVO;
import cn.iocoder.yudao.module.steam.dal.dataobject.selling.SellingDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 在售饰品 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface SellingExtMapper extends BaseMapperX<SellingDO> {
    @Select("SELECT  market_hash_name,count(1) sell_num,min(price) min_sell_price,icon_url,ss.sel_exterior,sel_rarity,sel_quality\n" +
            "from steam_selling ss  \n" +
            "where ss.market_hash_name in ('Recoil Case','SCAR-20 | Contractor (Field-Tested)') and ss.deleted=0\n" +
            "group by ss.market_hash_name,icon_url,ss.sel_exterior,sel_rarity,sel_quality")
    List<SellingHashSummary> SelectByHashName(List<String> hashName);

}