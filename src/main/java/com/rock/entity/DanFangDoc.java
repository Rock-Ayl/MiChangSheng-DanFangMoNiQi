package com.rock.entity;

import com.rock.enums.YaoCaiPropertyEnum;
import com.rock.util.ArrayExtraUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

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
     * 解析丹方实体
     * -
     * 如果单方压根不存在,跳过
     * 如果传入的药材不足以支持完整单方,跳过
     *
     * @param danFangStr   丹方字符串
     * @param yaoCaiDocMap 药材map
     * @return Formula对象
     */
    public static DanFangDoc parse(String danFangStr, Map<String, YaoCaiDoc> yaoCaiDocMap) {

        /**
         * 解析参数、简单验证
         */

        //删除头尾[]
        danFangStr = danFangStr.substring(1, danFangStr.length() - 1);
        //转为数组
        String[] partArr = danFangStr.split("_");
        //如果不是固定格式
        if (partArr.length != 5) {
            //过
            return null;
        }
        //如果没有主药,视为没有丹方
        if ("/".equals(partArr[0])) {
            //过
            return null;
        }

        /**
         * 解析、复杂验证
         */

        //初始化
        DanFangDoc danFangDoc = new DanFangDoc();
        //解析并组装参数
        danFangDoc.setMainHerb1(DanFangItemDoc.parse(ArrayExtraUtils.getString(partArr, 0), yaoCaiDocMap));
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
     * 返回该单方,寒热是否平衡
     *
     * @return
     */
    public boolean getCurrentYaoCaiHeatAndColdValue() {
        //所有可能的寒热平衡列表
        List<DanFangItemDoc> danFangItemDocList = new ArrayList<>();
        //按顺序添加
        danFangItemDocList.add(this.mainHerb1);
        danFangItemDocList.add(this.mainHerb2);
        danFangItemDocList.add(this.secondaryHerb1);
        danFangItemDocList.add(this.secondaryHerb2);
        //求出主药辅药寒热和
        int sum = danFangItemDocList
                .stream()
                //过滤空的
                .filter(Objects::nonNull)
                //收集寒热属性
                .map(DanFangItemDoc::getYaoCai)
                .map(YaoCaiDoc::getProperty)
                .map(YaoCaiPropertyEnum::getValue)
                //求和
                .reduce(0, Integer::sum);
        //获取药引
        YaoCaiPropertyEnum property = this.getGuideHerb().getYaoCai().getProperty();
        //判空
        if (property == null) {
            //不平
            return false;
        }
        //如果主药辅药热
        if (sum > 0) {
            //药引必须是寒
            return property == YaoCaiPropertyEnum.COLD;
        }
        //如果主药辅药寒
        else if (sum < 0) {
            //药引必须是热
            return property == YaoCaiPropertyEnum.HOT;
        }
        //如果主药辅药平
        else {
            //药引必须是平
            return property == YaoCaiPropertyEnum.NEUTRAL;
        }
    }

    /**
     * 返回该单方,丹方所需药性唯一key(完全相同的key具有覆盖作用)
     *
     * @return
     */
    public List<String> getKey() {

        //key列表
        Set<String> keySet = new HashSet<>();

        /**
         * 主药药性
         */

        //主药1和主药2的key
        List<String> mainKeyList = new ArrayList<>();
        //判空
        if (this.mainHerb1 != null) {
            //加入
            mainKeyList.add(this.mainHerb1.getYaoCai().getMainEffect().getCode());
        }
        //判空
        if (this.mainHerb2 != null) {
            //加入
            mainKeyList.add(this.mainHerb2.getYaoCai().getMainEffect().getCode());
        }

        /**
         * 辅药药性
         *
         */

        //辅药1和辅药2的key
        List<String> secKeyList = new ArrayList<>();
        //判空
        if (this.secondaryHerb1 != null) {
            //加入
            secKeyList.add(this.secondaryHerb1.getYaoCai().getSecondaryEffect().getCode());
        }
        //判空
        if (this.secondaryHerb2 != null) {
            //加入
            secKeyList.add(this.secondaryHerb2.getYaoCai().getSecondaryEffect().getCode());
        }

        /**
         * 组合
         */

        //循环1
        for (String mainKey : mainKeyList) {
            //循环2
            for (String secKey : secKeyList) {
                //加入key列表
                keySet.add(mainKey + "_" + secKey);
            }
        }
        //如果没有辅药
        if (keySet.isEmpty()) {
            //循环
            for (String mainKey : mainKeyList) {
                //加入key列表
                keySet.add(mainKey + "_" + "无");
            }
        }

        /**
         * 返回
         */

        //返回
        return new ArrayList<>(keySet);
    }

}