package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 药材-辅药作用 枚举
 */
@Getter
@ToString
public enum YaoCaiSecondaryEffectEnum {

    NONE("none"),

    WU("无"),
    YANG_QI("养气"),
    QING_XIN("清心"),
    NING_SHEN("凝神"),
    GU_YUAN("固元"),
    HUN_YUAN("混元"),
    PEI_YUAN("培元"),
    QIANG_JIN("强金"),
    QIANG_MU("强木"),
    QIANG_SHUI("强水"),
    QIANG_HUO("强火"),
    QIANG_TU("强土"),
    JIAN_YI("剑意"),
    TIAO_HE("调和"),
    SHEN_XING("神行"),
    LIAN_QI("炼气"),
    JIE_DU("解毒"),
    YANG_HUN("养魂"),
    DING_SHA("定煞"),
    YI_SHOU("益寿"),
    DUAN_TI("锻体"),
    NING_YE("凝液"),
    KAI_QIAO("开窍"),
    XI_SUI("洗髓"),
    JU_LING("聚灵"),
    NING_YING("凝婴"),
    DAO_YUN("道蕴"),
    KAI_WU("开悟"),
    HUA_SHEN("化神"),
    BI_JIE("避劫"),
    YI_RONG("易容"),

    ;

    //编码
    private final String code;

    YaoCaiSecondaryEffectEnum(String code) {
        this.code = code;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static YaoCaiSecondaryEffectEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (YaoCaiSecondaryEffectEnum object : YaoCaiSecondaryEffectEnum.values()) {
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