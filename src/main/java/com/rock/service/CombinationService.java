package com.rock.service;

import com.rock.entity.DanFangDoc;
import com.rock.entity.DanFangItemDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.DanLuEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;

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
     * @param yaoCaiMainEffectMap      药材主药分组map
     * @param yaoCaiSecondaryEffectMap 药材副药分组map
     * @return
     */
    public List<DanFangDoc> combination(
            DanYaoDoc danYaoDoc,
            DanLuEnum danLuEnum,
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

        /**
         * 每种药的单独可能
         */

        //生成主药组合
        List<List<DanFangItemDoc>> mainHerbCombinations = generateMainHerbCombinations(
                baseFormula, yaoCaiMainEffectMap, hasMain2);

        //生成辅药组合
        List<List<DanFangItemDoc>> secondaryHerbCombinations = generateSecondaryHerbCombinations(
                baseFormula, yaoCaiSecondaryEffectMap, hasSecondary2);

        //生成药引组合
        List<DanFangItemDoc> guideHerbCombinations = generateGuideHerbCombinations(baseFormula);

        /**
         * 组合排列
         */

        System.out.println();

        //组合所有可能的结果
        for (List<DanFangItemDoc> mainHerbs : mainHerbCombinations) {
            for (List<DanFangItemDoc> secondaryHerbs : secondaryHerbCombinations) {
                for (DanFangItemDoc guideHerb : guideHerbCombinations) {
                    //创建新丹方
                    DanFangDoc newFormula = new DanFangDoc();

                    //设置主药
                    if (mainHerbs.size() > 0) {
                        newFormula.setMainHerb1(mainHerbs.get(0));
                    }
                    if (mainHerbs.size() > 1) {
                        newFormula.setMainHerb2(mainHerbs.get(1));
                    }

                    //设置辅药
                    if (secondaryHerbs.size() > 0) {
                        newFormula.setSecondaryHerb1(secondaryHerbs.get(0));
                    }
                    if (secondaryHerbs.size() > 1) {
                        newFormula.setSecondaryHerb2(secondaryHerbs.get(1));
                    }

                    //设置药引
                    newFormula.setGuideHerb(guideHerb);

                    //计算总药力
                    int totalPower = calculateTotalPower(newFormula);

                    //检查总药力是否超过丹炉限制
                    if (totalPower <= maxCount) {
                        result.add(newFormula);
                    }
                }
            }
        }
        //返回
        return result;
    }

    /**
     * 生成主药组合
     *
     * @param baseFormula         基础丹方
     * @param yaoCaiMainEffectMap 药材主药分组map
     * @param hasMain2            是否有主药2槽位
     * @return 主药组合列表
     */
    private List<List<DanFangItemDoc>> generateMainHerbCombinations(
            DanFangDoc baseFormula,
            Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap,
            boolean hasMain2) {

        List<List<DanFangItemDoc>> result = new ArrayList<>();

        //获取基础丹方的主药效果
        YaoCaiMainEffectEnum mainEffect1 = baseFormula.getMainHerb1() != null ?
                baseFormula.getMainHerb1().getYaoCai().getMainEffect() : YaoCaiMainEffectEnum.NONE;
        YaoCaiMainEffectEnum mainEffect2 = baseFormula.getMainHerb2() != null ?
                baseFormula.getMainHerb2().getYaoCai().getMainEffect() : YaoCaiMainEffectEnum.NONE;

        //获取对应效果的药材列表
        List<YaoCaiDoc> mainEffect1List = yaoCaiMainEffectMap.getOrDefault(mainEffect1, new ArrayList<>());
        List<YaoCaiDoc> mainEffect2List = yaoCaiMainEffectMap.getOrDefault(mainEffect2, new ArrayList<>());

        //生成主药1组合
        for (YaoCaiDoc main1 : mainEffect1List) {
            DanFangItemDoc mainHerb1 = new DanFangItemDoc();
            mainHerb1.setYaoCai(main1);
            mainHerb1.setQuantity(baseFormula.getMainHerb1().getQuantity());
            mainHerb1.setTotalPower(main1.getGrade().getPower() * mainHerb1.getQuantity());

            //主药2组合（如果有）
            if (hasMain2 && mainEffect2 != YaoCaiMainEffectEnum.NONE) {
                for (YaoCaiDoc main2 : mainEffect2List) {
                    DanFangItemDoc mainHerb2 = new DanFangItemDoc();
                    mainHerb2.setYaoCai(main2);
                    mainHerb2.setQuantity(baseFormula.getMainHerb2().getQuantity());
                    mainHerb2.setTotalPower(main2.getGrade().getPower() * mainHerb2.getQuantity());

                    List<DanFangItemDoc> combination = new ArrayList<>();
                    combination.add(mainHerb1);
                    combination.add(mainHerb2);
                    result.add(combination);
                }
            } else {
                //没有主药2的情况
                List<DanFangItemDoc> combination = new ArrayList<>();
                combination.add(mainHerb1);
                result.add(combination);
            }
        }

        return result;
    }

    /**
     * 生成辅药组合
     *
     * @param baseFormula              基础丹方
     * @param yaoCaiSecondaryEffectMap 药材副药分组map
     * @param hasSecondary2            是否有辅药2槽位
     * @return 辅药组合列表
     */
    private List<List<DanFangItemDoc>> generateSecondaryHerbCombinations(
            DanFangDoc baseFormula,
            Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap,
            boolean hasSecondary2) {

        List<List<DanFangItemDoc>> result = new ArrayList<>();

        //获取基础丹方的辅药效果
        YaoCaiSecondaryEffectEnum secondaryEffect1 = baseFormula.getSecondaryHerb1() != null ?
                baseFormula.getSecondaryHerb1().getYaoCai().getSecondaryEffect() : YaoCaiSecondaryEffectEnum.NONE;
        YaoCaiSecondaryEffectEnum secondaryEffect2 = baseFormula.getSecondaryHerb2() != null ?
                baseFormula.getSecondaryHerb2().getYaoCai().getSecondaryEffect() : YaoCaiSecondaryEffectEnum.NONE;

        //获取对应效果的药材列表
        List<YaoCaiDoc> secondaryEffect1List = yaoCaiSecondaryEffectMap.getOrDefault(secondaryEffect1, new ArrayList<>());
        List<YaoCaiDoc> secondaryEffect2List = yaoCaiSecondaryEffectMap.getOrDefault(secondaryEffect2, new ArrayList<>());

        //辅药1组合
        if (secondaryEffect1 != YaoCaiSecondaryEffectEnum.NONE) {
            for (YaoCaiDoc sec1 : secondaryEffect1List) {
                DanFangItemDoc secondaryHerb1 = new DanFangItemDoc();
                secondaryHerb1.setYaoCai(sec1);
                secondaryHerb1.setQuantity(baseFormula.getSecondaryHerb1().getQuantity());
                secondaryHerb1.setTotalPower(sec1.getGrade().getPower() * secondaryHerb1.getQuantity());

                //辅药2组合（如果有）
                if (hasSecondary2 && secondaryEffect2 != YaoCaiSecondaryEffectEnum.NONE) {
                    for (YaoCaiDoc sec2 : secondaryEffect2List) {
                        DanFangItemDoc secondaryHerb2 = new DanFangItemDoc();
                        secondaryHerb2.setYaoCai(sec2);
                        secondaryHerb2.setQuantity(baseFormula.getSecondaryHerb2().getQuantity());
                        secondaryHerb2.setTotalPower(sec2.getGrade().getPower() * secondaryHerb2.getQuantity());

                        List<DanFangItemDoc> combination = new ArrayList<>();
                        combination.add(secondaryHerb1);
                        combination.add(secondaryHerb2);
                        result.add(combination);
                    }
                } else {
                    //没有辅药2的情况
                    List<DanFangItemDoc> combination = new ArrayList<>();
                    combination.add(secondaryHerb1);
                    result.add(combination);
                }
            }
        } else {
            //没有辅药1的情况
            if (hasSecondary2 && secondaryEffect2 != YaoCaiSecondaryEffectEnum.NONE) {
                for (YaoCaiDoc sec2 : secondaryEffect2List) {
                    DanFangItemDoc secondaryHerb2 = new DanFangItemDoc();
                    secondaryHerb2.setYaoCai(sec2);
                    secondaryHerb2.setQuantity(baseFormula.getSecondaryHerb2().getQuantity());
                    secondaryHerb2.setTotalPower(sec2.getGrade().getPower() * secondaryHerb2.getQuantity());

                    List<DanFangItemDoc> combination = new ArrayList<>();
                    combination.add(secondaryHerb2);
                    result.add(combination);
                }
            } else {
                //没有辅药1和辅药2的情况
                result.add(new ArrayList<>());
            }
        }

        return result;
    }

    /**
     * 生成药引组合
     *
     * @param baseFormula 基础丹方
     * @return 药引组合列表
     */
    private List<DanFangItemDoc> generateGuideHerbCombinations(DanFangDoc baseFormula) {
        List<DanFangItemDoc> result = new ArrayList<>();

        //药引保持不变
        if (baseFormula.getGuideHerb() != null) {
            result.add(baseFormula.getGuideHerb());
        } else {
            result.add(null);
        }

        return result;
    }

    /**
     * 计算丹方总药力
     *
     * @param formula 丹方
     * @return 总药力
     */
    private int calculateTotalPower(DanFangDoc formula) {
        int totalPower = 0;

        if (formula.getMainHerb1() != null) {
            totalPower += formula.getMainHerb1().getTotalPower();
        }
        if (formula.getMainHerb2() != null) {
            totalPower += formula.getMainHerb2().getTotalPower();
        }
        if (formula.getSecondaryHerb1() != null) {
            totalPower += formula.getSecondaryHerb1().getTotalPower();
        }
        if (formula.getSecondaryHerb2() != null) {
            totalPower += formula.getSecondaryHerb2().getTotalPower();
        }
        if (formula.getGuideHerb() != null) {
            totalPower += formula.getGuideHerb().getTotalPower();
        }

        return totalPower;
    }

}