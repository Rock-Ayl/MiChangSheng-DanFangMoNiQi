package com.rock.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 丹方 实体类
 */
@Getter
@Setter
public class FormulaDoc {

    /**
     * 主药1 (格式: 药材名*数量)
     */
    private String mainHerb1;

    /**
     * 主药2 (格式: 药材名*数量)
     */
    private String mainHerb2;

    /**
     * 辅药1 (格式: 药材名*数量)
     */
    private String secondaryHerb1;

    /**
     * 辅药2 (格式: 药材名*数量)
     */
    private String secondaryHerb2;

    /**
     * 药引 (格式: 药材名*数量)
     */
    private String guideHerb;

    /**
     * 默认初始化
     */
    public FormulaDoc() {

    }

    /**
     * 初始化
     */
    public FormulaDoc(String mainHerb1, String mainHerb2, String secondaryHerb1, String secondaryHerb2, String guideHerb) {
        this.mainHerb1 = mainHerb1;
        this.mainHerb2 = mainHerb2;
        this.secondaryHerb1 = secondaryHerb1;
        this.secondaryHerb2 = secondaryHerb2;
        this.guideHerb = guideHerb;
    }

    /**
     * 根据String,解析实体
     *
     * @param formulaStr 配方字符串
     * @return Formula对象
     */
    public static FormulaDoc parse(String formulaStr) {
        //去除方括号
        String content = formulaStr.replaceAll("[\\[\\]]", "");
        //转为数组
        String[] partArr = content.split(",");
        //解析参数
        String mainHerb1 = partArr.length > 0 ? partArr[0].trim() : "/";
        String mainHerb2 = partArr.length > 1 ? partArr[1].trim() : "/";
        String secondaryHerb1 = partArr.length > 2 ? partArr[2].trim() : "/";
        String secondaryHerb2 = partArr.length > 3 ? partArr[3].trim() : "/";
        String guideHerb = partArr.length > 4 ? partArr[4].trim() : "/";
        //返回
        return new FormulaDoc(mainHerb1, mainHerb2, secondaryHerb1, secondaryHerb2, guideHerb);
    }

}