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

    NOTHING(0,"未受教育"),
    PRIMARY_SCHOOL_DIPLOMA(1, "小学"),
    MIDDLE_SCHOOL_DIPLOMA(2, "初中"),
    HIGH_SCHOOL_DIPLOMA(3, "高中"),
    COLLEGE_DIPLOMA(4, "大专"),
    BACHELOR_DEGREE(5, "本科"),
    MASTER_DEGREE(6, "硕士"),
    DOCTORAL_DEGREE(7, "博士"),
    ;

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
