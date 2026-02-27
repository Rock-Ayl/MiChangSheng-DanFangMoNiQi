package com.rock.entity;

import com.rock.util.ArrayExtraUtils;
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
     * 根据String,解析实体
     *
     * @param formulaStr 配方字符串
     * @return Formula对象
     */
    public static FormulaDoc parse(String formulaStr) {
        //删除头尾[]
        formulaStr = formulaStr.substring(1, formulaStr.length() - 1);
        //转为数组
        String[] partArr = formulaStr.split("_");
        //初始化
        FormulaDoc formulaDoc = new FormulaDoc();
        //组装参数
        formulaDoc.setMainHerb1(ArrayExtraUtils.getString(partArr, 0));
        formulaDoc.setMainHerb2(ArrayExtraUtils.getString(partArr, 1));
        formulaDoc.setSecondaryHerb1(ArrayExtraUtils.getString(partArr, 2));
        formulaDoc.setSecondaryHerb2(ArrayExtraUtils.getString(partArr, 3));
        formulaDoc.setGuideHerb(ArrayExtraUtils.getString(partArr, 4));
        //返回
        return formulaDoc;
    }

}