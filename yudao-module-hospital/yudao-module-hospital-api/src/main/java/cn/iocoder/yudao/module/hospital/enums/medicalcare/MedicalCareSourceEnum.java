package cn.iocoder.yudao.module.hospital.enums.medicalcare;

/**
 * @author whycode
 * @title: MedicalCareSourceEnum
 * @projectName home-care
 * @description: TODO
 * @date 2023/11/1615:38
 */
public enum MedicalCareSourceEnum {

    /**
     * 自册
     */
    SELF("自册"),

    /**
     * 申请中
     */
    BACK_ADD("后管添加");

    private final String description;

    MedicalCareSourceEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }

}
