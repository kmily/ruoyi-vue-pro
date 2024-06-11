package cn.iocoder.boot.module.therapy.enums;



public enum PsycoTroubleEnum {
    UNKNOWN(0, "未知", "unknown"),
    PSYCO_ASPECT(1, "心理层面", "psyco_aspect"),
    PHYSI_ASPECT(2, "生理层面", "physi_aspect"),
    FAMILY_ASPECT(3, "家庭层面", "family_aspect"),
    SCHOOL_ASPECT(4, "学校环境的问题", "school_aspect"),
    SOCIAL_ASPECT(5, "社交和人际关系的问题", "social_aspect"),
    ADDICTION_ASPECT(6, "成瘾问题", "addiction_aspect"),
    SEXUAL_ASPECT(7, "性相关问题", "sexual_aspect"),
    ;

    private int intVal;
    private String category;
    private String code;

    PsycoTroubleEnum(int intVal, String category, String code) {
        this.intVal = intVal;
        this.category = category;
        this.code = code;
    }

    public String getCategory() {
        return category;
    }
}
