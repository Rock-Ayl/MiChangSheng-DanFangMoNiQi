package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 药材-主药作用 枚举
 */
@Getter
@ToString
public enum YaoCaiMainEffectEnum {

    NONE("none"),

    WU("无"),
    LIAN_MO("炼魔"),
    JU_YUAN("聚元"),
    SHENG_XI("生息"),
    QU_YAO("驱妖"),
    HUO_XUE("活血"),
    JING_XUE("净血"),
    YU_QI("御气"),
    ZHEN_QI("振气"),
    YOU_YAO("诱妖"),

    ;

    //编码
    private final String code;

    YaoCaiMainEffectEnum(String code) {
        this.code = code;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static YaoCaiMainEffectEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (YaoCaiMainEffectEnum object : YaoCaiMainEffectEnum.values()) {
                //对比
                if (object.getCode().equals(code)) {
                    //返回
                    return object;
                }
            }
        }
        //默认
        return NONE;
    }

}