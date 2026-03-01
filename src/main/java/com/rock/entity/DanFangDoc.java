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
    private DanFangItemDoc mainHerb1;

    /**
     * 主药2
     */
    private DanFangItemDoc mainHerb2;

    /**
     * 辅药1
     */
    private DanFangItemDoc secondaryHerb1;

    /**
     * 辅药2
     */
    private DanFangItemDoc secondaryHerb2;

    /**
     * 药引
     */
    private DanFangItemDoc guideHerb;

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
        danFangDoc.setMainHerb1(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 0), yaoCaiDocMap));
        //如果没有主药,视为没有丹方
        if (danFangDoc.getMainHerb1() == null) {
            //过
            return null;
        }
        danFangDoc.setMainHerb2(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 1), yaoCaiDocMap));
        danFangDoc.setSecondaryHerb1(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 2), yaoCaiDocMap));
        danFangDoc.setSecondaryHerb2(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 3), yaoCaiDocMap));
        danFangDoc.setGuideHerb(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 4), yaoCaiDocMap));
        //返回
        return danFangDoc;
    }

}