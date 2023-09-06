package cn.iocoder.yudao.module.member.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author whycode
 * @title: HomePageType
 * @projectName ruoyi-vue-pro
 * @description: TODO
 * @date 2023/8/717:46
 */
public enum HomePageType {

    SEEP((byte) 0, "睡眠"),
    TOILET((byte) 1, "如厕"),
    FALL((byte) 2, "跌倒"),
    LEAVE_BACK((byte) 3, "离/回家");

    public final byte type;
    public final String name;

    HomePageType(byte type, String name){
        this.type = type;
        this.name = name;
    }


    public static HomePageType valueOf(byte type){
        if(SEEP.type == type){
            return SEEP;
        }else if(TOILET.type == type){
            return TOILET;
        }else if(FALL.type == type){
            return FALL;
        }else{
            return LEAVE_BACK;
        }
    }
}
