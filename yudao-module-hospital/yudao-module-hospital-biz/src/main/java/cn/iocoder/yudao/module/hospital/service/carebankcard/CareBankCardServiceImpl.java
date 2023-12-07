package cn.iocoder.yudao.module.hospital.service.carebankcard;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.hospital.controller.app.carebankcard.vo.CareBankCardAppCreateReqVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.carebankcard.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.carebankcard.CareBankCardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.carebankcard.CareBankCardConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.carebankcard.CareBankCardMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 提现银行卡 Service 实现类
 *
 * @author 管理人
 */
@Service
@Validated
public class CareBankCardServiceImpl extends ServiceImpl<CareBankCardMapper, CareBankCardDO> implements CareBankCardService {

    @Resource
    private CareBankCardMapper careBankCardMapper;

    @Override
    public Long createCareBankCard(CareBankCardCreateReqVO createReqVO) {
        // 插入
        CareBankCardDO careBankCard = CareBankCardConvert.INSTANCE.convert(createReqVO);
        careBankCardMapper.insert(careBankCard);
        // 返回
        return careBankCard.getId();
    }

    @Override
    public void updateCareBankCard(CareBankCardUpdateReqVO updateReqVO) {
        // 校验存在
        validateCareBankCardExists(updateReqVO.getId());
        // 更新
        CareBankCardDO updateObj = CareBankCardConvert.INSTANCE.convert(updateReqVO);
        careBankCardMapper.updateById(updateObj);
    }

    @Override
    public void deleteCareBankCard(Long id) {
        // 校验存在
        validateCareBankCardExists(id);
        // 删除
        careBankCardMapper.deleteById(id);
    }

    private void validateCareBankCardExists(Long id) {
        if (careBankCardMapper.selectById(id) == null) {
            throw exception(CARE_BANK_CARD_NOT_EXISTS);
        }
    }

    @Override
    public CareBankCardDO getCareBankCard(Long id) {
        return careBankCardMapper.selectById(id);
    }

    @Override
    public List<CareBankCardDO> getCareBankCardList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return careBankCardMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CareBankCardDO> getCareBankCardPage(CareBankCardPageReqVO pageReqVO) {
        return careBankCardMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CareBankCardDO> getCareBankCardList(CareBankCardExportReqVO exportReqVO) {
        return careBankCardMapper.selectList(exportReqVO);
    }

    @Override
    public Long createCareBankCardByApp(CareBankCardAppCreateReqVO createAppReqVO) {
        //获取用户ID
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 插入
        CareBankCardDO careBankCard = CareBankCardConvert.INSTANCE.convert(createAppReqVO);
        //用户编号
        careBankCard.setCareId(loginUserId);
        careBankCardMapper.insert(careBankCard);
        // 返回
        return careBankCard.getId();
    }

    @Override
    public List<CareBankCardDO> getCareBankCardByCareId(Long careId) {
        return careBankCardMapper.selectListByCareIdAndDefaulted(careId, null);
    }

}
