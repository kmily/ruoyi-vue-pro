package cn.iocoder.yudao.module.hospital.service.carebankcard;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;
import cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo.CareBankCardAppCreateReqVO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * 提现银行卡 Service 接口
 *
 * @author 管理人
 */
public interface CareBankCardService extends IService<CareBankCardDO>{

    /**
     * 创建提现银行卡
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCareBankCard(@Valid CareBankCardCreateReqVO createReqVO);

    /**
     * 更新提现银行卡
     *
     * @param updateReqVO 更新信息
     */
    void updateCareBankCard(@Valid CareBankCardUpdateReqVO updateReqVO);

    /**
     * 删除提现银行卡
     *
     * @param id 编号
     */
    void deleteCareBankCard(Long id);

    /**
     * 获得提现银行卡
     *
     * @param id 编号
     * @return 提现银行卡
     */
    CareBankCardDO getCareBankCard(Long id);

    /**
     * 获得提现银行卡列表
     *
     * @param ids 编号
     * @return 提现银行卡列表
     */
    List<CareBankCardDO> getCareBankCardList(Collection<Long> ids);

    /**
     * 获得提现银行卡分页
     *
     * @param pageReqVO 分页查询
     * @return 提现银行卡分页
     */
    PageResult<CareBankCardDO> getCareBankCardPage(CareBankCardPageReqVO pageReqVO);

    /**
     * 获得提现银行卡列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 提现银行卡列表
     */
    List<CareBankCardDO> getCareBankCardList(CareBankCardExportReqVO exportReqVO);


    /**
     * 创建提现银行卡--app端
     *
     * @param createAppReqVO 创建信息
     * @return 编号
     */
    Long createCareBankCardByApp(@Valid CareBankCardAppCreateReqVO createAppReqVO);

    List<CareBankCardDO> getCareBankCardByCareId(Long careId);

}
