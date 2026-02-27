package com.rock.enums;

import lombok.Getter;

/**
 * 丹药-品级 枚举
 */
@Getter
public enum PillGroupEnum {

    NONE("none", "未知"),

    ONE("一品", "一品"),
    TWO("二品", "二品"),
    THREE("三品", "三品"),
    FOUR("四品", "四品"),
    FIVE("五品", "五品"),
    SIX("六品", "六品"),

    ;

    //编码
    private final String code;

    //描述
    private final String desc;

    PillGroupEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static PillGroupEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (PillGroupEnum object : PillGroupEnum.values()) {
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