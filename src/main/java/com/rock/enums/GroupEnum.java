package com.rock.enums;

import com.rock.Config;
import lombok.Getter;
import lombok.ToString;

/**
 * 品级 枚举
 */
@Getter
@ToString
public enum GroupEnum {

    NONE("none", -1, 0, 0),

    ONE("一品", 1, 1, Config.SHEET_PART_PIN_ONE),
    TWO("二品", 3, 2, Config.SHEET_PART_PIN_TWO),
    THREE("三品", 9, 3, Config.SHEET_PART_PIN_THREE),
    FOUR("四品", 36, 4, Config.SHEET_PART_PIN_FOUR),
    FIVE("五品", 180, 5, Config.SHEET_PART_PIN_FIVE),
    SIX("六品", 1080, 6, Config.SHEET_PART_PIN_SIX),

    ;

    //编码
    private final String code;

    //单个药材,对应品级,对应的药力
    private final Integer power;

    //等级
    private final Integer level;

    //对应品级的丹方的sheet分片(单个丹方超过100万就需要分片)
    private final Integer sheetPart;

    GroupEnum(String code, Integer power, Integer level, Integer sheetPart) {
        this.code = code;
        this.power = power;
        this.level = level;
        this.sheetPart = sheetPart;
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