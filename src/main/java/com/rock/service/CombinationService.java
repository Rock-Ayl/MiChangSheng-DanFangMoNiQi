package com.rock.service;

import com.rock.entity.DanFangDoc;
import com.rock.entity.DanFangItemDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.DanLuEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;
import com.rock.util.FastJsonExtraUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 组合排列服务
 */
public class CombinationService {

    /**
     * 组合排列
     *
     * @param danYaoDoc                丹药
     * @param danLuEnum                丹炉
     * @param yaoCaiDocList            药材列表
     * @param yaoCaiMainEffectMap      药材主药分组map
     * @param yaoCaiSecondaryEffectMap 药材副药分组map
     * @return
     */
    public List<DanFangDoc> combination(
            DanYaoDoc danYaoDoc,
            DanLuEnum danLuEnum,
            List<YaoCaiDoc> yaoCaiDocList,
            Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap,
            Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap) {

        /**
         * 校验参数
         */

        //获取基础丹方
        DanFangDoc baseFormula = danYaoDoc.getFormula();
        //如果没有基础丹方
        if (baseFormula == null) {
            //过
            return new ArrayList<>();
        }
        //判空丹炉
        if (danLuEnum == null || danLuEnum == DanLuEnum.NONE) {
            //过
            return new ArrayList<>();
        }

        /**
         * 所需其他参数
         */

        //初始化结果列表
        List<DanFangDoc> result = new ArrayList<>();

        //获取丹炉最大药材数量
        Integer maxCount = danLuEnum.getMaxCount();
        //是否有主药2槽位
        boolean hasMain2 = danLuEnum.isMain2();
        //是否有辅药2槽位
        boolean hasSecondary2 = danLuEnum.isSecondary2();
        //如果没有主药2槽位 and 需要主药2
        if (hasMain2 == false && baseFormula.getMainHerb2() != null) {
            //过
            return new ArrayList<>();
        }
        //如果没有辅药2槽位 and 需要辅药2
        if (hasSecondary2 == false && baseFormula.getSecondaryHerb2() != null) {
            //过
            return new ArrayList<>();
        }

        /**
         * 组合排列
         */

        //创建空丹方,加入列表
        result.add(new DanFangDoc());

        //构建主药1
        result = buildMain1(result, baseFormula, maxCount, yaoCaiMainEffectMap);

        //todo 构建主药2

        //构建辅药1
        result = buildSecondary1(result, baseFormula, maxCount, yaoCaiDocList, yaoCaiSecondaryEffectMap);

        //todo 构建辅药2

        //todo 构建药引

        //todo 检查寒热平衡

        //todo 检查是否有覆盖的更高级配方(完全相同单方分组,取品级最高的)

        /**
         * 返回结果
         */

        //返回
        return result;
    }

    /**
     * 构建主药1
     *
     * @param danFangDocList      当前丹方列表
     * @param baseFormula         基础丹方
     * @param maxCount            丹炉最大药材数量
     * @param yaoCaiMainEffectMap 药材主药分组map
     * @return
     */
    private List<DanFangDoc> buildMain1(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap) {

        /**
         * 获取主药1
         */

        //获取基础丹方-主药1
        DanFangItemDoc baseMainHerb1 = baseFormula.getMainHerb1();
        //获取主药1-所需总药力
        Integer requiredPower = baseMainHerb1.getTotalPower();
        //获取主药1-主药作用
        YaoCaiMainEffectEnum requiredMainEffect = baseMainHerb1.getYaoCai().getMainEffect();
        //获取主药1-对应药材列表
        List<YaoCaiDoc> Main1YaocaiList = yaoCaiMainEffectMap.get(requiredMainEffect);

        /**
         * 组合排列
         */

        //组合排列后的丹方列表
        List<DanFangDoc> newResultList = new ArrayList<>();
        //循环
        for (YaoCaiDoc main1YaoCai : Main1YaocaiList) {
            //计算需要的最小数量
            Integer minCount = calculateMinCount(requiredPower, main1YaoCai.getGrade().getPower());
            //为单方新增新的组合
            for (DanFangDoc danFang : danFangDocList) {
                //克隆实体
                DanFangDoc newDanFang = FastJsonExtraUtils.deepClone(danFang, DanFangDoc.class);
                //设置主药1
                newDanFang.setMainHerb1(new DanFangItemDoc(main1YaoCai, minCount));
                //如果当前单方的药材总数 大于 丹炉最大药材数量
                if (newDanFang.getCurrentYaoCaiCount() > maxCount) {
                    //炸炉,过
                    continue;
                }
                //添加到结果列表
                newResultList.add(newDanFang);
            }
        }
        //返回新的结果
        return newResultList;
    }

    /**
     * 构建辅药1
     *
     * @param danFangDocList           当前丹方列表
     * @param baseFormula              基础丹方
     * @param maxCount                 丹炉最大药材数量
     * @param yaoCaiDocList            所有药材列表
     * @param yaoCaiSecondaryEffectMap 药材辅药分组map
     * @returno
     */
    private List<DanFangDoc> buildSecondary1(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            List<YaoCaiDoc> yaoCaiDocList,
            Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap) {

        /**
         * 获取辅药1
         */

        //目标辅药1列表
        List<YaoCaiDoc> secondary1YaocaiList = new ArrayList<>();
        //获取基础丹方-辅药1
        DanFangItemDoc baseSecondaryHerb1 = baseFormula.getSecondaryHerb1();
        //获取辅药1-所需总药力,默认null
        Integer requiredPower = null;
        //如果不需要辅药1
        if (baseSecondaryHerb1 == null) {
            //尝试用每种药材填充(平衡寒热)
            secondary1YaocaiList = yaoCaiDocList;
        } else {
            //获取辅药1-所需总药力
            requiredPower = baseSecondaryHerb1.getTotalPower();
            //获取辅药1-辅药作用
            YaoCaiSecondaryEffectEnum requiredSecondaryEffect = baseSecondaryHerb1.getYaoCai().getSecondaryEffect();
            //获取辅药1-对应药材列表
            secondary1YaocaiList = yaoCaiSecondaryEffectMap.get(requiredSecondaryEffect);
        }

        /**
         * 组合排列
         */

        //组合排列后的丹方列表
        List<DanFangDoc> newResultList = new ArrayList<>();
        //循环
        for (YaoCaiDoc secondary1YaoCai : secondary1YaocaiList) {
            //计算需要的最小数量,如果不需要辅药1,则默认1个填充平衡
            Integer minCount = requiredPower == null ? 1 : calculateMinCount(requiredPower, secondary1YaoCai.getGrade().getPower());
            //为单方新增新的组合
            for (DanFangDoc danFang : danFangDocList) {
                //克隆实体
                DanFangDoc newDanFang = FastJsonExtraUtils.deepClone(danFang, DanFangDoc.class);
                //设置辅药1
                newDanFang.setSecondaryHerb1(new DanFangItemDoc(secondary1YaoCai, minCount));
                //如果当前单方的药材总数 大于 丹炉最大药材数量
                if (newDanFang.getCurrentYaoCaiCount() > maxCount) {
                    //炸炉,过
                    continue;
                }
                //添加到结果列表
                newResultList.add(newDanFang);
            }
        }
        //返回新的结果
        return newResultList;
    }

    /**
     * 根据所需药力、药材药力,计算最小需要几个
     *
     * @param totalPower 所需总药力
     * @param power      单个药材药力
     * @return
     */
    private Integer calculateMinCount(Integer totalPower, Integer power) {
        return (totalPower / power) + (totalPower % power != 0 ? 1 : 0);
    }

}