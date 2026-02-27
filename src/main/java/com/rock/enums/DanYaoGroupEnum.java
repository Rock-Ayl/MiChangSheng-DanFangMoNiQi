package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 丹药品级 枚举
 */
@Getter
@ToString
public enum DanYaoGroupEnum {

    NONE("none"),

    ONE("一品"),
    TWO("二品"),
    THREE("三品"),
    FOUR("四品"),
    FIVE("五品"),
    SIX("六品"),

    ;

    //编码
    private final String code;

    DanYaoGroupEnum(String code) {
        this.code = code;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static DanYaoGroupEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (DanYaoGroupEnum object : DanYaoGroupEnum.values()) {
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