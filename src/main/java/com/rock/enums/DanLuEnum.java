package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 丹炉 枚举
 */
@Getter
@ToString
public enum DanLuEnum {

    NONE("none", GroupEnum.NONE, 0),

    ONE("一品丹炉", GroupEnum.ONE, 9),
    TWO("二品丹炉", GroupEnum.TWO, 10),
    THREE("三品丹炉", GroupEnum.THREE, 11),
    FOUR("四品丹炉", GroupEnum.FOUR, 12),
    FIVE("五品丹炉", GroupEnum.FIVE, 13),
    SIX("六品丹炉", GroupEnum.SIX, 14),

    ;

    //编码
    private final String code;

    //丹炉品级
    private final GroupEnum groupEnum;

    //最大草药数
    private final Integer maxCount;

    //初始化
    DanLuEnum(String code, GroupEnum groupEnum, Integer maxCount) {
        this.code = code;
        this.groupEnum = groupEnum;
        this.maxCount = maxCount;
    }

}