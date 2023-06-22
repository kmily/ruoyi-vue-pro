package com.cw.module.trade.convert.account;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.cw.module.trade.controller.admin.account.vo.*;
import com.cw.module.trade.dal.dataobject.account.AccountDO;

/**
 * 交易账号 Convert
 *
 * @author chengjiale
 */
@Mapper
public interface AccountConvert {

    AccountConvert INSTANCE = Mappers.getMapper(AccountConvert.class);

    AccountDO convert(AccountCreateReqVO bean);

    AccountDO convert(AccountUpdateReqVO bean);

    AccountRespVO convert(AccountDO bean);

    List<AccountRespVO> convertList(List<AccountDO> list);

    PageResult<AccountRespVO> convertPage(PageResult<AccountDO> page);

    List<AccountExcelVO> convertList02(List<AccountDO> list);

}
