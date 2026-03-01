package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 丹炉 枚举
 */
@Getter
@ToString
public enum DanLuEnum {

    NONE("none", GroupEnum.NONE, 0, false, false),

    ONE("一品丹炉", GroupEnum.ONE, 9, false, false),
    TWO("二品丹炉", GroupEnum.TWO, 10, false, true),
    THREE("三品丹炉", GroupEnum.THREE, 11, false, true),
    FOUR("四品丹炉", GroupEnum.FOUR, 12, false, true),
    FIVE("五品丹炉", GroupEnum.FIVE, 13, false, true),
    SIX("六品丹炉", GroupEnum.SIX, 14, true, true),

    ;

    //编码
    private final String code;

    //丹炉品级
    private final GroupEnum groupEnum;

    //最大草药数
    private final Integer maxCount;

    //是否有主药2槽位
    private final boolean main2;

    //是否有辅药2槽位
    private final boolean secondary2;

    //初始化
    DanLuEnum(String code, GroupEnum groupEnum, Integer maxCount, boolean main2, boolean secondary2) {
        this.code = code;
        this.groupEnum = groupEnum;
        this.maxCount = maxCount;
        this.main2 = main2;
        this.secondary2 = secondary2;
    }

}