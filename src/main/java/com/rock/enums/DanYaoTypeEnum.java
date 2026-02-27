package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 丹药种类 枚举
 */
@Getter
@ToString
public enum DanYaoTypeEnum {

    NONE("none"),

    HUA_SHEN("化神"),
    YI_RONG("易容"),
    JIE_YING("结婴"),
    SHEN_SHI("神识"),
    QI_TA("其他"),
    XIN_JING("心境"),
    SHOU_YUAN("寿元"),
    TI_ZHI("体质"),
    WU_XING("悟性"),
    GAN_WU("感悟"),
    ZHAN_DOU("战斗"),
    ZI_ZHI("资质"),
    XIU_LIAN("修炼"),
    DU_JIE("渡劫"),
    DAN_DU("丹毒"),
    ZHU_JI("筑基"),
    JIE_DAN("结丹"),
    HUI_FU("恢复"),
    QUAN_ZHONG("权重"),
    DUN_SU("遁速"),

    ;

    //编码
    private final String code;

    DanYaoTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static DanYaoTypeEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (DanYaoTypeEnum object : DanYaoTypeEnum.values()) {
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