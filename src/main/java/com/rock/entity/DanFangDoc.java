package com.rock.entity;

import com.rock.util.ArrayExtraUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 丹方 实体类
 */
@Getter
@Setter
@ToString
public class DanFangDoc {

    /**
     * 主药1
     */
    private YaoCaiShuLiangDoc mainHerb1;

    /**
     * 主药2
     */
    private YaoCaiShuLiangDoc mainHerb2;

    /**
     * 辅药1
     */
    private YaoCaiShuLiangDoc secondaryHerb1;

    /**
     * 辅药2
     */
    private YaoCaiShuLiangDoc secondaryHerb2;

    /**
     * 药引
     */
    private YaoCaiShuLiangDoc guideHerb;

    /**
     * 解析实体
     *
     * @param danFangStr   丹方字符串
     * @param yaoCaiDocMap 药材map
     * @return Formula对象
     */
    public static DanFangDoc parse(String danFangStr, Map<String, YaoCaiDoc> yaoCaiDocMap) {
        //删除头尾[]
        danFangStr = danFangStr.substring(1, danFangStr.length() - 1);
        //转为数组
        String[] partArr = danFangStr.split("_");
        //初始化
        DanFangDoc danFangDoc = new DanFangDoc();
        //解析并组装参数
        danFangDoc.setMainHerb1(YaoCaiShuLiangDoc.parse(ArrayExtraUtils.getString(partArr, 0), yaoCaiDocMap));
        danFangDoc.setMainHerb2(YaoCaiShuLiangDoc.parse(ArrayExtraUtils.getString(partArr, 1), yaoCaiDocMap));
        danFangDoc.setSecondaryHerb1(YaoCaiShuLiangDoc.parse(ArrayExtraUtils.getString(partArr, 2), yaoCaiDocMap));
        danFangDoc.setSecondaryHerb2(YaoCaiShuLiangDoc.parse(ArrayExtraUtils.getString(partArr, 3), yaoCaiDocMap));
        danFangDoc.setGuideHerb(YaoCaiShuLiangDoc.parse(ArrayExtraUtils.getString(partArr, 4), yaoCaiDocMap));
        //返回
        return danFangDoc;
    }

}