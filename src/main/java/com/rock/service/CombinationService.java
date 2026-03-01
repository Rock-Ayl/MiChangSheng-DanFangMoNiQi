package com.rock.service;

import com.rock.entity.DanFangDoc;
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
         * todo 组合排列
         */

        //获取主药1可能的草药
        List<YaoCaiDoc> main1YaoCaiList = yaoCaiMainEffectMap.get(baseFormula.getMainHerb1().getYaoCai().getMainEffect());
        //循环
        for (YaoCaiDoc main1YaoCai : main1YaoCaiList) {

            /**
             * 计算主药1是否条件
             */

            //主药1所需的药力
            Integer totalPower = baseFormula.getMainHerb1().getTotalPower();
            //药材1所需数量
            Integer main1YaoCaiCount = calculateMinCount(totalPower, main1YaoCai.getGrade().getPower());
            //如果超过丹炉最大,则跳过
            if (main1YaoCaiCount > maxCount) {
                //跳过
                continue;
            }

            System.out.println();

        }

        /**
         * 返回结果
         */

        //返回
        return result;
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