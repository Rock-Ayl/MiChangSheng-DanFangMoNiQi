package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 药材-药引性质 枚举
 */
@Getter
@ToString
public enum YaoCaiPropertyEnum {

    NONE("none", 0),

    COLD("性寒", -1),
    HOT("性热", 1),
    NEUTRAL("性平", 0),

    ;

    //编码
    private final String code;

    //寒热平衡值
    private final Integer value;

    YaoCaiPropertyEnum(String code, Integer value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 根据 code 解析出对应枚举
     *
     * @param code
     * @return
     */
    public static YaoCaiPropertyEnum parseByCode(String code) {
        //判空
        if (code != null) {
            //循环
            for (YaoCaiPropertyEnum object : YaoCaiPropertyEnum.values()) {
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