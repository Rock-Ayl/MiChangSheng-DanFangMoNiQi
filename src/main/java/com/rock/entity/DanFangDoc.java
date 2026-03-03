package com.rock.entity;

import com.rock.enums.YaoCaiPropertyEnum;
import com.rock.util.ArrayExtraUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * 返回当前单方的药材总数
     *
     * @return
     */
    public Integer getCurrentYaoCaiCount() {
        //初始化
        Integer count = 0;
        //主药1
        if (this.mainHerb1 != null) {
            //叠加
            count += this.mainHerb1.getQuantity();
        }
        //主药2
        if (this.mainHerb2 != null) {
            //叠加
            count += this.mainHerb2.getQuantity();
        }
        //辅药1
        if (this.secondaryHerb1 != null) {
            //叠加
            count += this.secondaryHerb1.getQuantity();
        }
        //辅药2
        if (this.secondaryHerb2 != null) {
            //叠加
            count += this.secondaryHerb2.getQuantity();
        }
        //药引
        if (this.guideHerb != null) {
            //叠加
            count += this.guideHerb.getQuantity();
        }
        //返回
        return count;
    }

    /**
     * 返回该单方 寒热数值总和
     * 热+1，寒-1，主要辅药药引均算，不考虑数量
     *
     * @return
     */
    public int getCurrentYaoCaiHeatAndColdValue() {
        //所有可能的寒热平衡列表
        List<DanFangItemDoc> danFangItemDocList = new ArrayList<>();
        //按顺序添加
        danFangItemDocList.add(this.mainHerb1);
        danFangItemDocList.add(this.mainHerb2);
        danFangItemDocList.add(this.secondaryHerb1);
        danFangItemDocList.add(this.secondaryHerb2);
        danFangItemDocList.add(this.guideHerb);
        //返回寒热总和
        return danFangItemDocList
                .stream()
                //过滤空的
                .filter(Objects::nonNull)
                //收集寒热属性
                .map(DanFangItemDoc::getYaoCai)
                .map(YaoCaiDoc::getProperty)
                .map(YaoCaiPropertyEnum::getValue)
                //求和
                .reduce(0, Integer::sum);
    }

    /**
     * 返回该单方,丹方所需药性唯一key(完全相同的key具有覆盖作用)
     *
     * @return
     */
    public String getKey() {

        /**
         * todo 主药药性
         */

        /**
         * todo 辅药药性
         */

        /**
         * todo 组合并返回
         */

        return null;
    }

}