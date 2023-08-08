package cn.iocoder.yudao.module.member.service.family;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.api.sms.SmsCodeApi;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.member.controller.app.family.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.family.FamilyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.member.convert.family.FamilyConvert;
import cn.iocoder.yudao.module.member.dal.mysql.family.FamilyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.FAMILY_NOT_EXISTS;

/**
 * 用户家庭 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FamilyServiceImpl implements FamilyService {

    @Resource
    private FamilyMapper familyMapper;

    @Resource
    private SmsCodeApi smsCodeApi;

    @Resource
    @Lazy
    private MemberUserService memberUserService;

    @Override
    public Long createFamily(FamilyCreateReqVO createReqVO) {
        MemberUserDO user = memberUserService.getUser(createReqVO.getUserId());
        // 插入
        FamilyDO family = FamilyConvert.INSTANCE.convert(createReqVO);
        family.setPhones(ListUtil.toList(user.getMobile()));
        familyMapper.insert(family);
        // 返回
        return family.getId();
    }
    @Override
    public Long createFamily(Long userId, String nickName, String mobile) {
        String name = StrUtil.isBlank(nickName)? "我的家": nickName + "的家";
        FamilyDO familyDO = FamilyDO.builder()
                .userId(userId)
                .name(name)
                .phones(Collections.singletonList(mobile))
                .build();
        familyMapper.insert(familyDO);
        return familyDO.getId();
    }

    @Override
    public void updateFamily(FamilyUpdateReqVO updateReqVO) {
        // 校验存在
        validateFamilyExists(updateReqVO.getId());
        // 更新
        FamilyDO updateObj = FamilyConvert.INSTANCE.convert(updateReqVO);
        familyMapper.updateById(updateObj);
    }

    @Override
    public void deleteFamily(Long id) {
        // 校验存在
        validateFamilyExists(id);
        // 删除
        familyMapper.deleteById(id);
    }

    private void validateFamilyExists(Long id) {
        if (familyMapper.selectById(id) == null) {
            throw exception(FAMILY_NOT_EXISTS);
        }
    }

    @Override
    public FamilyDO getFamily(Long id) {
        return familyMapper.selectById(id);
    }

    @Override
    public List<FamilyDO> getFamilyList(Collection<Long> ids) {
        return familyMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FamilyDO> getFamilyPage(FamilyPageReqVO pageReqVO) {
        return familyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FamilyDO> getFamilyList(FamilyExportReqVO exportReqVO) {
        return familyMapper.selectList(exportReqVO);
    }

    @Override
    public Collection<String> addMobile(FamilyAddMobileVO mobileVO) {

        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(mobileVO.getMobile()).setCode(mobileVO.getCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

        FamilyDO familyDO = familyMapper.selectById(mobileVO.getId());
        if(null == familyDO){
            throw exception(FAMILY_NOT_EXISTS);
        }
        String mobile = mobileVO.getMobile();
        List<String> mobiles =  familyDO.getPhones();
        mobiles.add(mobile);
        familyMapper.updateById(FamilyDO.builder().phones(mobiles).id(mobileVO.getId()).build());
        return mobiles;
    }

    @Override
    public Collection<String> updateMobile(FamilyUpdateMobileVO mobileVO) {

        // 校验旧手机和旧验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(mobileVO.getOldMobile()).setCode(mobileVO.getOldCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));
        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(mobileVO.getMobile()).setCode(mobileVO.getCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

        Long id = mobileVO.getId();
        FamilyDO familyDO = familyMapper.selectById(id);
        if(null == familyDO){
            throw exception(FAMILY_NOT_EXISTS);
        }
        String oldMobile = mobileVO.getOldMobile();
        String newMobile = mobileVO.getMobile();
        List<String> mobiles =  familyDO.getPhones();
        mobiles.remove(oldMobile);
        mobiles.add(newMobile);
        familyMapper.updateById(FamilyDO.builder().phones(mobiles).id(id).build());
        return mobiles;
    }

    @Override
    public Collection<String> deleteMobile(FamilyAddMobileVO mobileVO) {
        // 使用新验证码
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setMobile(mobileVO.getMobile()).setCode(mobileVO.getCode())
                .setScene(SmsSceneEnum.MEMBER_UPDATE_MOBILE.getScene()).setUsedIp(getClientIP()));

        FamilyDO familyDO = familyMapper.selectById(mobileVO.getId());
        if(null == familyDO){
            throw exception(FAMILY_NOT_EXISTS);
        }
        String mobile = mobileVO.getMobile();
        List<String> mobiles =  familyDO.getPhones();
        mobiles.remove(mobile);
        familyMapper.updateById(FamilyDO.builder().phones(mobiles).id(mobileVO.getId()).build());
        return mobiles;
    }




}
