package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 丹炉 枚举
 */
@Getter
@ToString
public enum DanLuEnum {

    NONE("none", GroupEnum.NONE),

    ONE("一品丹炉", GroupEnum.ONE),
    TWO("二品丹炉", GroupEnum.TWO),
    THREE("三品丹炉", GroupEnum.THREE),
    FOUR("四品丹炉", GroupEnum.FOUR),
    FIVE("五品丹炉", GroupEnum.FIVE),
    SIX("六品丹炉", GroupEnum.SIX),

    ;

    //编码
    private final String code;

    //丹炉品级
    private final GroupEnum groupEnum;

    //初始化
    DanLuEnum(String code, GroupEnum groupEnum) {
        this.code = code;
        this.groupEnum = groupEnum;
    }

}