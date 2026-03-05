package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 品级 枚举
 */
@Getter
@ToString
public enum GroupEnum {

    NONE("none", -1, 0),

    ONE("一品", 1, 1),
    TWO("二品", 3, 2),
    THREE("三品", 9, 3),
    FOUR("四品", 36, 4),
    FIVE("五品", 180, 5),
    SIX("六品", 1080, 6),

    ;

    //编码
    private final String code;

    //单个药材,对应品级,对应的药力
    private final Integer power;

    //等级
    private final Integer level;

    GroupEnum(String code, Integer power, Integer level) {
        this.code = code;
        this.power = power;
        this.level = level;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static GroupEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (GroupEnum object : GroupEnum.values()) {
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