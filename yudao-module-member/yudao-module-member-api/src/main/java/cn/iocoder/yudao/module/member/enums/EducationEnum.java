package cn.iocoder.yudao.module.member.enums;

import cn.hutool.core.util.EnumUtil;
import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 会员学历程度
 *
 * @author owen
 */
@Getter
@AllArgsConstructor
public enum EducationEnum implements IntArrayValuable {

    PRIMARY_SCHOOL_DIPLOMA(0, "小学"),
    MIDDLE_SCHOOL_DIPLOMA(1, "初中"),
    HIGH_SCHOOL_DIPLOMA(2, "高中"),
    COLLEGE_DIPLOMA(3, "大专"),
    BACHELOR_DEGREE(4, "本科"),
    MASTER_DEGREE(5, "硕士"),
    DOCTORAL_DEGREE(6, "博士"),
    NOTHING(7,"未受教育");

    /**
     * 业务类型
     */
    private final int type;
    /**
     * 标题
     */
    private final String title;


    public static EducationEnum getByType(Integer type) {
        return EnumUtil.getBy(EducationEnum.class,
                e -> Objects.equals(type, e.getType()));
    }
    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(EducationEnum::getType).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
