package cn.iocoder.yudao.module.hospital.service.medicalcare;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppMedicalCarePageReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppMedicalCarePerfectVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.AppRealNameReqVO;
import cn.iocoder.yudao.module.hospital.controller.app.medicalcare.vo.CareFavoritePageReqVO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.careaptitude.CareAptitudeDO;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcarechecklog.MedicalCareCheckLogDO;
import cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareStatusEnum;
import cn.iocoder.yudao.module.hospital.service.medicalcarechecklog.MedicalCareCheckLogService;
import cn.iocoder.yudao.module.member.api.user.MemberUserApi;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserReqDTO;
import cn.iocoder.yudao.module.system.api.organization.OrganizationApi;
import cn.iocoder.yudao.module.system.api.organization.dto.OrganizationDTO;
import cn.iocoder.yudao.module.system.api.tenant.TenantApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.hospital.controller.admin.medicalcare.vo.*;
import cn.iocoder.yudao.module.hospital.dal.dataobject.medicalcare.MedicalCareDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.hospital.convert.medicalcare.MedicalCareConvert;
import cn.iocoder.yudao.module.hospital.dal.mysql.medicalcare.MedicalCareMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.hospital.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareSourceEnum.BACK_ADD;
import static cn.iocoder.yudao.module.hospital.enums.medicalcare.MedicalCareSourceEnum.SELF;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 医护管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MedicalCareServiceImpl extends ServiceImpl<MedicalCareMapper, MedicalCareDO> implements MedicalCareService {

    private static final String DEFAULT_PWD = "123456789";

    @Resource
    private MedicalCareMapper medicalCareMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

//    @Resource
//    private OrganizationApi organizationApi;

    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private MedicalCareCheckLogService medicalCareCheckLogService;

    @Override
    public boolean save(MedicalCareDO entity) {
        if(StrUtil.isNotBlank(entity.getIdCard())){
            String idCard = entity.getIdCard();
            DateTime birth = IdcardUtil.getBirthDate(idCard);
            int gender = IdcardUtil.getGenderByIdCard(idCard);
            entity.setBirthday(LocalDate.of(birth.year(), birth.month(), birth.dayOfMonth()));
            entity.setSex(gender == 1? (byte)1: (byte)2);
        }
        return super.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createMedicalCare(MedicalCareCreateReqVO createReqVO) {
        // 插入
        MedicalCareDO medicalCare = MedicalCareConvert.INSTANCE.convert(createReqVO);
        String password = createReqVO.getPassword();
        if(StrUtil.isBlank(password)){
            password = DEFAULT_PWD;
        }
        Long memberId = memberUserApi.createMember(new MemberUserReqDTO()
                .setMobile(medicalCare.getMobile())
                .setName(medicalCare.getName())
                .setNickname(medicalCare.getName())
                .setPassword(encodePassword(password))
                .setStatus(CommonStatusEnum.ENABLE.getStatus()));

        if(memberId == null){
           throw exception(MEDICAL_CARE_CREATE_FAIL);
        }

//        MedicalCareDO medicalCareDO = this.selectOneByMobile(medicalCare.getMobile());
//        if(Objects.nonNull(medicalCareDO)){
//            throw exception(MEDICAL_CARE_HAS_EXISTS);
//        }

        medicalCare.setId(memberId);
        if(Objects.equals(BACK_ADD.value(), medicalCare.getSource())){
            // 后台管理端添加默认审核通过
            medicalCare.setStatus(MedicalCareStatusEnum.OPEN.value());

            // 去掉机构概念 统一使用租户概念
            //medicalCare.setOrgId(SecurityFrameworkUtils.getLoginOrgId());
            //OrganizationDTO organization = organizationApi.getOrganization(SecurityFrameworkUtils.getLoginOrgId());
        }else{
            // 否则为自册需要审核
            medicalCare.setSource(SELF.value())
                    .setStatus(MedicalCareStatusEnum.APPLYING.value());
//            OrganizationDTO organization = organizationApi.getSelfOrganization();
//            medicalCare.setOrgId(organization.getId());
        }
        boolean save = this.save(medicalCare);

//        if(save){
//            medicalCareMapper.updateById(new MedicalCareDO().setMemberId(memberId).setId(medicalCare.getId()));
//        }else {
//            throw exception(MEDICAL_CARE_CREATE_FAIL);
//        }

        // 返回
        return medicalCare.getId();
    }

    @Override
    public void updateMedicalCare(MedicalCareUpdateReqVO updateReqVO) {
        // 校验存在
        validateMedicalCareExists(updateReqVO.getId());
        // 更新
        MedicalCareDO updateObj = MedicalCareConvert.INSTANCE.convert(updateReqVO);
        medicalCareMapper.updateById(updateObj);
    }

    @Override
    public void deleteMedicalCare(Long id) {
        // 校验存在
        validateMedicalCareExists(id);
        // 删除
        medicalCareMapper.deleteById(id);
    }

    private MedicalCareDO validateMedicalCareExists(Long id) {
        MedicalCareDO medicalCareDO = medicalCareMapper.selectById(id);
        if (medicalCareDO == null) {
            throw exception(MEDICAL_CARE_NOT_EXISTS);
        }
        return medicalCareDO;
    }

    @Override
    public MedicalCareDO getMedicalCare(Long id) {
        return medicalCareMapper.selectById(id);
    }

    @Override
    public List<MedicalCareDO> getMedicalCareList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return medicalCareMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MedicalCareDO> getMedicalCarePage(MedicalCarePageReqVO pageReqVO) {
        return medicalCareMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MedicalCareDO> getMedicalCareList(MedicalCareExportReqVO exportReqVO) {
        return medicalCareMapper.selectList(exportReqVO);
    }

    @Override
    public MedicalCareDO selectOneByMobile(String mobile) {
        return this.getOne(new LambdaQueryWrapper<MedicalCareDO>().eq(MedicalCareDO::getMobile, mobile));
    }

    @Override
    public MedicalCareDO getByMemberId(Long memberId) {
        MedicalCareDO one = this.getById(memberId);//this.getOne(new LambdaQueryWrapper<MedicalCareDO>().eq(MedicalCareDO::getMemberId, memberId));
        if(one == null){
            throw exception(MEDICAL_CARE_NOT_EXISTS);
        }
        return one;
    }

    @Override
    public long createMedicalCare(Long memberId, String mobile) {
        MedicalCareDO medicalCareDO = this.getMedicalCare(memberId);
        if(medicalCareDO != null){
            return medicalCareDO.getId();
        }
        //OrganizationDTO organization = organizationApi.getSelfOrganization();
        //medicalCareDO = this.selectOneByMobile(mobile);
//         if(medicalCareDO != null && Objects.equals(memberId, medicalCareDO.getMemberId())) {
//             return medicalCareDO.getId();
//         }else if(medicalCareDO != null){
//             this.updateById(new MedicalCareDO()
//                     .setMemberId(memberId)
//                     .setOrgId(organization.getId())
//                     .setId(medicalCareDO.getId()));
//             return medicalCareDO.getId();
//         }else {
             medicalCareDO = new MedicalCareDO().setId(memberId).setMemberId(memberId).setMobile(mobile)
                     .setStatus(MedicalCareStatusEnum.APPLYING.value())
                     .setSource(SELF.value());
            this.save(medicalCareDO);
            return medicalCareDO.getId();
        //}
    }

    @Override
    public void closeOrOpenMedicalCare(Long id, MedicalCareStatusEnum status) {
        validateMedicalCareExists(id);
        medicalCareMapper.updateById(new MedicalCareDO().setStatus(status.value()).setId(id));
    }

    @Override
    public void auditMedicalCare(MedicalCareAuditVO auditVO) {
        MedicalCareDO medicalCareDO = validateMedicalCareExists(auditVO.getId());
        AdminUserRespDTO user = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        medicalCareMapper.updateById(new MedicalCareDO().setStatus(auditVO.getStatus()).setId(auditVO.getId())
                .setOpinion(auditVO.getOpinion())
                .setCheckTime(LocalDateTime.now())
                .setCheckName(user.getNickname()));

        medicalCareCheckLogService.save(new MedicalCareCheckLogDO()
                .setCareId(medicalCareDO.getId())
                .setCheckStatus(auditVO.getStatus())
                .setOpinion(auditVO.getOpinion())
                .setCheckTime(LocalDateTime.now())
                .setCheckName(user.getNickname()));


    }

    @Override
    public void realNameMedicalCare(AppRealNameReqVO realNameReqVO) {
        validateMedicalCareExists(realNameReqVO.getId());
        medicalCareMapper.updateById(new MedicalCareDO().setRealname(CommonStatusEnum.YES.name())
              .setCardPath(realNameReqVO.picPaths())
              .setId(realNameReqVO.getId()));
    }

    @Override
    public void updateMedicalCareAptitude(Long careId) {
        medicalCareMapper.updateById(new MedicalCareDO().setAptitude(CommonStatusEnum.YES.name()).setId(careId));
    }

    @Override
    public PageResult<MedicalCareDO> getCareAptitudePage(AppMedicalCarePageReqVO pageVO) {
        if(CollUtil.isNotEmpty(pageVO.getAptitudes())){
            // 如果资质不为空
            return medicalCareMapper.selectPageByAptitude(pageVO);
        }
        return medicalCareMapper.selectPage(pageVO);
    }

    @Override
    public void updateCareComment(Long id, Integer commentScore) {
        if(commentScore == null || commentScore <= 0){
            return;
        }
        medicalCareMapper.update(new MedicalCareDO(), new LambdaUpdateWrapper<MedicalCareDO>()
                .setSql("`comment_score` = `comment_score` + " + commentScore + ", `comment_count` = `comment_count` + 1")
                .eq(MedicalCareDO::getId, id));
    }

    @Override
    public PageResult<MedicalCareDO> getCareFavoritePage(CareFavoritePageReqVO pageVO) {
        return medicalCareMapper.selectCareFavoritePage(pageVO);
    }



    @Override
    public void perfectMedicalCare(AppMedicalCarePerfectVO perfectVO) {
        // 校验存在
        validateMedicalCareExists(perfectVO.getId());
        // 更新
        MedicalCareDO perfectObj = MedicalCareConvert.INSTANCE.convert(perfectVO);
        perfectObj.setPerfect(CommonStatusEnum.YES.name());
        medicalCareMapper.updateById(perfectObj);
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}