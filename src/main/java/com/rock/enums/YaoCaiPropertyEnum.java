package com.rock.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 药材性质 枚举
 */
@Getter
@ToString
public enum YaoCaiPropertyEnum {

    NONE("none"),

    COLD("性寒"),
    HOT("性热"),
    NEUTRAL("性平"),

    ;

    //编码
    private final String code;

    YaoCaiPropertyEnum(String code) {
        this.code = code;
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