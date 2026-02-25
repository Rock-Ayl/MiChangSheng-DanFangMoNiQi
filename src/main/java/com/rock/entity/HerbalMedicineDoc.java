package com.rock.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 药材 实体类
 */
@Getter
@Setter
public class HerbalMedicineDoc {

    /**
     * 药材名称
     */
    private String name;

    /**
     * 药材品级 (一品、二品、三品...)
     */
    private String grade;

    /**
     * 主药作用
     */
    private String mainEffect;

    /**
     * 辅药作用
     */
    private String secondaryEffect;

    /**
     * 药引性质 (性平、性寒、性热)
     */
    private String property;

    /**
     * 默认初始化
     */
    public HerbalMedicineDoc() {

    }

    /**
     * 初始化
     */
    public HerbalMedicineDoc(String name, String grade, String mainEffect, String secondaryEffect, String property) {
        this.name = name;
        this.grade = grade;
        this.mainEffect = mainEffect;
        this.secondaryEffect = secondaryEffect;
        this.property = property;
    }

}