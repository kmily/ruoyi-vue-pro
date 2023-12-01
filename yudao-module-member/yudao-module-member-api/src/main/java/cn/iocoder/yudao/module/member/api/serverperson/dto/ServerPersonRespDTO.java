package cn.iocoder.yudao.module.member.api.serverperson.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author whycode
 * @title: ServerPersonDTO
 * @projectName home-care
 * @description: 被护人
 * @date 2023/11/2816:52
 */

@Data
public class ServerPersonRespDTO {


    /**
     * 被护人编号
     */
    private Long id;

    /**
     * 会员编号
     */
    private Long memberId;

    /**
     * 被护人姓名
     */
    private String name;

    /**
     * 被护人联系方式
     */
    private String mobile;

    /**
     * 被护人紧急联系人
     */
    private String critical;

    /**
     * 被护人出生日期
     */
    private LocalDate birthday;

    /**
     * 被护人性别
     */
    private Byte sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     *病历资料路径
     */
    private List<String> medicalRecord;

    /**
     *特殊情况路径
     */
    private  List<String> special;

    /**
     * 状态
     */
    private String status;
}
