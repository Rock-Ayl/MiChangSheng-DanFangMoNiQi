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
import java.util.Optional;

/**
 * 组合排列服务
 */
public class CombinationService {

    /**
     * 组合排列
     *
     * @param danYaoDoc                丹药
     * @param danLuEnum                丹炉
     * @param yaoCaiDocAndNullList     所有药材列表(包含NULL)
     * @param yaoCaiMainEffectMap      药材主药分组map
     * @param yaoCaiSecondaryEffectMap 药材副药分组map
     * @param danFangGroupMap          丹方分组map
     * @return
     */
    public List<DanFangDoc> combination(
            DanYaoDoc danYaoDoc,
            DanLuEnum danLuEnum,
            List<YaoCaiDoc> yaoCaiDocAndNullList,
            Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap,
            Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap,
            Map<String, List<DanYaoDoc>> danFangGroupMap) {

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

        //构建主药2
        result = buildMain2(result, baseFormula, maxCount, yaoCaiDocAndNullList, yaoCaiMainEffectMap);

        //构建辅药1
        result = buildSecondary1(result, baseFormula, maxCount, 2, yaoCaiDocAndNullList, yaoCaiSecondaryEffectMap);

        //构建辅药2
        result = buildSecondary2(result, baseFormula, maxCount, 1, yaoCaiDocAndNullList, yaoCaiSecondaryEffectMap, danFangGroupMap);

        //构建药引
        result = buildGuideHerb(result, baseFormula, maxCount, 0, yaoCaiDocAndNullList);

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
     * 构建主药2
     *
     * @param danFangDocList       当前丹方列表
     * @param baseFormula          基础丹方
     * @param maxCount             丹炉最大药材数量
     * @param yaoCaiDocAndNullList 所有药材列表(包含NULL)
     * @param yaoCaiMainEffectMap  药材主药分组map
     * @return
     */
    private List<DanFangDoc> buildMain2(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            List<YaoCaiDoc> yaoCaiDocAndNullList,
            Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap) {

        /**
         * 获取主药2
         */

        //目标主药22列表
        List<YaoCaiDoc> main2YaoCaiList = new ArrayList<>();
        //获取基础丹方-主药2
        DanFangItemDoc mainHerb2 = baseFormula.getMainHerb2();
        //获取主药2-所需总药力,默认null
        Integer requiredPower = null;
        //如果不需要主药2
        if (mainHerb2 == null) {
            //尝试用每种药材填充(平衡寒热)
            main2YaoCaiList = yaoCaiDocAndNullList;
        } else {
            //获取主药2-所需总药力
            requiredPower = mainHerb2.getTotalPower();
            //获取主药2-主药作用
            YaoCaiMainEffectEnum requiredMainEffect = mainHerb2.getYaoCai().getMainEffect();
            //获取主药2-对应药材列表
            main2YaoCaiList = yaoCaiMainEffectMap.get(requiredMainEffect);
        }

        /**
         * 组合排列
         */

        //组合排列后的丹方列表
        List<DanFangDoc> newResultList = new ArrayList<>();
        //循环
        for (YaoCaiDoc main2YaoCai : main2YaoCaiList) {
            //计算需要的最小数量,如果不需要辅药2,则默认1个填充平衡
            Integer minCount = requiredPower == null ? 1 : calculateMinCount(requiredPower, main2YaoCai.getGrade().getPower());
            //为单方新增新的组合
            for (DanFangDoc danFang : danFangDocList) {

                /**
                 * 特殊填充逻辑
                 * -
                 * 如果主药1 存在
                 * 如果主药1 与主药2是同一种药材
                 * 如果主药1 有多个
                 * -
                 * 那么就不仅仅是平衡药性的药引了,还可以平衡主药1的数量
                 */

                //主药1-药材名称
                String main1Name = Optional.ofNullable(danFang)
                        .map(DanFangDoc::getMainHerb1)
                        .map(DanFangItemDoc::getYaoCai)
                        .map(YaoCaiDoc::getName)
                        .orElse(null);
                //主药1-药材数量
                Integer main1Quantity = Optional.ofNullable(danFang)
                        .map(DanFangDoc::getMainHerb1)
                        .map(DanFangItemDoc::getQuantity)
                        .orElse(1);
                //克隆实体
                DanFangDoc newDanFang = FastJsonExtraUtils.deepClone(danFang, DanFangDoc.class);
                //如果有主药2
                if (main2YaoCai != null) {
                    //如果满足特殊填充
                    if (main2YaoCai.getName().equals(main1Name) && main1Quantity > 1) {
                        //主药1数量-1(因为主药2使得药性多了一个)
                        newDanFang.getMainHerb1().setQuantity(newDanFang.getMainHerb1().getQuantity() - 1);
                    }
                    //设置主药2
                    newDanFang.setMainHerb2(new DanFangItemDoc(main2YaoCai, minCount));
                }
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
     * @param maxHotAndCold            最大寒热数值
     * @param yaoCaiDocAndNullList     所有药材列表(包含NULL)
     * @param yaoCaiSecondaryEffectMap 药材辅药分组map
     * @returno
     */
    private List<DanFangDoc> buildSecondary1(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            Integer maxHotAndCold,
            List<YaoCaiDoc> yaoCaiDocAndNullList,
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
            secondary1YaocaiList = yaoCaiDocAndNullList;
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
                //如果有辅药1
                if (secondary1YaoCai != null) {
                    //设置辅药1
                    newDanFang.setSecondaryHerb1(new DanFangItemDoc(secondary1YaoCai, minCount));
                }
                //如果当前单方的药材总数 大于 丹炉最大药材数量
                if (newDanFang.getCurrentYaoCaiCount() > maxCount) {
                    //炸炉,过
                    continue;
                }
                //判断寒热
                if (Math.abs(newDanFang.getCurrentYaoCaiHeatAndColdValue()) > maxHotAndCold) {
                    //寒热肯定不平,过
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
     * 构建辅药2
     *
     * @param danFangDocList           当前丹方列表
     * @param baseFormula              基础丹方
     * @param maxCount                 丹炉最大药材数量
     * @param maxHotAndCold            最大寒热数值
     * @param yaoCaiDocAndNullList     所有药材列表(包含NULL)
     * @param yaoCaiSecondaryEffectMap 药材辅药分组map
     * @param danFangGroupMap          丹方分组map
     * @return
     */
    private List<DanFangDoc> buildSecondary2(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            Integer maxHotAndCold,
            List<YaoCaiDoc> yaoCaiDocAndNullList,
            Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap,
            Map<String, List<DanYaoDoc>> danFangGroupMap) {

        /**
         * 获取辅药2
         */

        //目标辅药2列表
        List<YaoCaiDoc> secondary2YaocaiList = new ArrayList<>();
        //获取基础丹方-辅药2
        DanFangItemDoc baseSecondaryHerb2 = baseFormula.getSecondaryHerb2();
        //获取辅药2-所需总药力,默认null
        Integer requiredPower = null;
        //如果不需要辅药2
        if (baseSecondaryHerb2 == null) {
            //尝试用每种药材填充(平衡寒热)
            secondary2YaocaiList = yaoCaiDocAndNullList;
        } else {
            //获取辅药2-所需总药力
            requiredPower = baseSecondaryHerb2.getTotalPower();
            //获取辅药2-辅药作用
            YaoCaiSecondaryEffectEnum requiredSecondaryEffect = baseSecondaryHerb2.getYaoCai().getSecondaryEffect();
            //获取辅药2-对应药材列表
            secondary2YaocaiList = yaoCaiSecondaryEffectMap.get(requiredSecondaryEffect);
        }

        /**
         * 组合排列
         */

        //组合排列后的丹方列表
        List<DanFangDoc> newResultList = new ArrayList<>();
        //循环
        for (YaoCaiDoc secondary2YaoCai : secondary2YaocaiList) {
            //计算需要的最小数量,如果不需要辅药2,则默认1个填充平衡
            Integer minCount = requiredPower == null ? 1 : calculateMinCount(requiredPower, secondary2YaoCai.getGrade().getPower());
            //跳出标记
            out:
            //为单方新增新的组合
            for (DanFangDoc danFang : danFangDocList) {

                /**
                 * 特殊填充逻辑
                 * -
                 * 如果辅药1 存在
                 * 如果辅药1 与辅药2是同一种药材
                 * 如果辅药1 有多个
                 * -
                 * 那么就不仅仅是平衡药性的药引了,还可以平衡辅药1的数量
                 */

                //辅药1-药材名称
                String sec1Name = Optional.ofNullable(danFang)
                        .map(DanFangDoc::getSecondaryHerb1)
                        .map(DanFangItemDoc::getYaoCai)
                        .map(YaoCaiDoc::getName)
                        .orElse(null);
                //辅药1-药材数量
                Integer sec1Quantity = Optional.ofNullable(danFang)
                        .map(DanFangDoc::getSecondaryHerb1)
                        .map(DanFangItemDoc::getQuantity)
                        .orElse(1);
                //克隆实体
                DanFangDoc newDanFang = FastJsonExtraUtils.deepClone(danFang, DanFangDoc.class);
                //如果有辅药2
                if (secondary2YaoCai != null) {
                    //如果满足特殊填充
                    if (secondary2YaoCai.getName().equals(sec1Name) && sec1Quantity > 1) {
                        //辅药1数量-1(因为辅药2使得药性多了一个)
                        newDanFang.getSecondaryHerb1().setQuantity(newDanFang.getSecondaryHerb1().getQuantity() - 1);
                    }
                    //设置辅药2
                    newDanFang.setSecondaryHerb2(new DanFangItemDoc(secondary2YaoCai, minCount));
                }
                //如果当前单方的药材总数 大于 丹炉最大药材数量
                if (newDanFang.getCurrentYaoCaiCount() > maxCount) {
                    //炸炉,过
                    continue;
                }
                //判断寒热
                if (Math.abs(newDanFang.getCurrentYaoCaiHeatAndColdValue()) > maxHotAndCold) {
                    //寒热肯定不平,过
                    continue;
                }

                /**
                 * 检查 主药1+主药2+辅药1+辅药2 满足其他单方,则略过
                 * -
                 * 主药辅药满足更高一级的丹方,要不丹药升级为别的丹药,要不寒热不平(药引不足)
                 * 主药辅药满足其他单方,则药性相冲(药性相冲)
                 */

                //所有相关key列表
                List<String> keyList = newDanFang.getKey();
                //循环
                for (String key : keyList) {
                    //获取相似的丹方,循环
                    for (DanYaoDoc danYaoDoc : danFangGroupMap.get(key)) {
                        //获取对应单方单方
                        DanFangDoc formula = danYaoDoc.getFormula();
                        //如果覆盖其他单方药性
                        if (isCoverOtherFormula(newDanFang, formula)) {
                            //冲突 or 升级,跳过
                            continue out;
                        }
                    }
                }

                /**
                 * 加入结果
                 */

                //添加到结果列表
                newResultList.add(newDanFang);
            }
        }
        //返回新的结果
        return newResultList;
    }

    /**
     * 构建药引
     *
     * @param danFangDocList       当前丹方列表
     * @param baseFormula          基础丹方
     * @param maxCount             丹炉最大药材数量
     * @param maxHotAndCold        最大寒热数值
     * @param yaoCaiDocAndNullList 所有药材列表(包含NULL)
     * @return
     */
    private List<DanFangDoc> buildGuideHerb(
            List<DanFangDoc> danFangDocList,
            DanFangDoc baseFormula,
            Integer maxCount,
            Integer maxHotAndCold,
            List<YaoCaiDoc> yaoCaiDocAndNullList) {

        /**
         * 获取药引
         */

        //获取基础丹方-药引
        DanFangItemDoc baseGuideHerb = baseFormula.getGuideHerb();
        //目标药引列表
        List<YaoCaiDoc> guideYaoCaiList = new ArrayList<>(yaoCaiDocAndNullList);
        //不能有空的药引
        guideYaoCaiList.remove(null);
        //获取药引-所需总药力
        Integer requiredPower = baseGuideHerb.getTotalPower();

        /**
         * 组合排列
         */

        //组合排列后的丹方列表
        List<DanFangDoc> newResultList = new ArrayList<>();
        //循环
        for (YaoCaiDoc guideYaoCai : guideYaoCaiList) {
            //计算需要的最小数量,如果不需要药引,则默认1个填充平衡
            Integer minCount = requiredPower == null ? 1 : calculateMinCount(requiredPower, guideYaoCai.getGrade().getPower());
            //为单方新增新的组合
            for (DanFangDoc danFang : danFangDocList) {
                //克隆实体
                DanFangDoc newDanFang = FastJsonExtraUtils.deepClone(danFang, DanFangDoc.class);
                //设置药引
                newDanFang.setGuideHerb(new DanFangItemDoc(guideYaoCai, minCount));
                //如果当前单方的药材总数 大于 丹炉最大药材数量
                if (newDanFang.getCurrentYaoCaiCount() > maxCount) {
                    //炸炉,过
                    continue;
                }
                //判断寒热
                if (Math.abs(newDanFang.getCurrentYaoCaiHeatAndColdValue()) > maxHotAndCold) {
                    //寒热不平,过
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

    /**
     * 检查新丹方是否覆盖其他单方药性
     * -
     * 判断标准：新丹方的主药和辅药是否完全包含另一个丹方的主药和辅药
     *
     * @param newFormula 新丹方
     * @param oldFormula 旧丹方
     * @return true-覆盖，false-不覆盖
     */
    private boolean isCoverOtherFormula(DanFangDoc newFormula, DanFangDoc oldFormula) {

        //获取新丹方的主药药性列表
        List<String> newMainEffects = new ArrayList<>();
        if (newFormula.getMainHerb1() != null) {
            newMainEffects.add(newFormula.getMainHerb1().getYaoCai().getMainEffect().getCode());
        }
        if (newFormula.getMainHerb2() != null) {
            newMainEffects.add(newFormula.getMainHerb2().getYaoCai().getMainEffect().getCode());
        }

        //获取旧丹方的主药药性列表
        List<String> oldMainEffects = new ArrayList<>();
        if (oldFormula.getMainHerb1() != null) {
            oldMainEffects.add(oldFormula.getMainHerb1().getYaoCai().getMainEffect().getCode());
        }
        if (oldFormula.getMainHerb2() != null) {
            oldMainEffects.add(oldFormula.getMainHerb2().getYaoCai().getMainEffect().getCode());
        }

        //检查新丹方是否包含旧丹方的所有主药药性
        for (String oldMainEffect : oldMainEffects) {
            if (!newMainEffects.contains(oldMainEffect)) {
                return false;
            }
        }

        //获取新丹方的辅药药性列表
        List<String> newSecondaryEffects = new ArrayList<>();
        if (newFormula.getSecondaryHerb1() != null) {
            newSecondaryEffects.add(newFormula.getSecondaryHerb1().getYaoCai().getSecondaryEffect().getCode());
        }
        if (newFormula.getSecondaryHerb2() != null) {
            newSecondaryEffects.add(newFormula.getSecondaryHerb2().getYaoCai().getSecondaryEffect().getCode());
        }

        //获取旧丹方的辅药药性列表
        List<String> oldSecondaryEffects = new ArrayList<>();
        if (oldFormula.getSecondaryHerb1() != null) {
            oldSecondaryEffects.add(oldFormula.getSecondaryHerb1().getYaoCai().getSecondaryEffect().getCode());
        }
        if (oldFormula.getSecondaryHerb2() != null) {
            oldSecondaryEffects.add(oldFormula.getSecondaryHerb2().getYaoCai().getSecondaryEffect().getCode());
        }

        //检查新丹方是否包含旧丹方的所有辅药药性
        for (String oldSecondaryEffect : oldSecondaryEffects) {
            if (!newSecondaryEffects.contains(oldSecondaryEffect)) {
                return false;
            }
        }

        //如果新丹方包含旧丹方的所有主药和辅药药性，则认为覆盖
        return true;
    }

}