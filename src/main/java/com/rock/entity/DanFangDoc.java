package com.rock.entity;

import com.rock.util.ArrayExtraUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 丹方 实体类
 */
@Getter
@Setter
public class DanFangDoc {

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
     * 解析实体
     *
     * @param danFangStr 丹方字符串
     * @return Formula对象
     */
    public static DanFangDoc parse(String danFangStr) {
        //删除头尾[]
        danFangStr = danFangStr.substring(1, danFangStr.length() - 1);
        //转为数组
        String[] partArr = danFangStr.split("_");
        //初始化
        DanFangDoc danFangDoc = new DanFangDoc();
        //组装参数
        danFangDoc.setMainHerb1(ArrayExtraUtils.getString(partArr, 0));
        danFangDoc.setMainHerb2(ArrayExtraUtils.getString(partArr, 1));
        danFangDoc.setSecondaryHerb1(ArrayExtraUtils.getString(partArr, 2));
        danFangDoc.setSecondaryHerb2(ArrayExtraUtils.getString(partArr, 3));
        danFangDoc.setGuideHerb(ArrayExtraUtils.getString(partArr, 4));
        //返回
        return danFangDoc;
    }

}