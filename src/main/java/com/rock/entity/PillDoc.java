package com.rock.entity;

import com.rock.enums.PillGroupEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 丹药 实体类
 */
@Getter
@Setter
public class PillDoc {

    /**
     * 丹药名称
     */
    private String name;

    /**
     * 丹药品级 (一品、二品、三品...)
     */
    private PillGroupEnum grade;

    /**
     * 丹药种类 (心境、战斗、恢复、修炼等)
     */
    private String type;

    /**
     * 丹方配方
     */
    private FormulaDoc formula;

    /**
     * 丹药价值
     */
    private int value;

}