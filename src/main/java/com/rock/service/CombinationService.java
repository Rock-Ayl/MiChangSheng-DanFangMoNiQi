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
        DanFangDoc formula = danYaoDoc.getFormula();
        //如果没有基础丹方
        if (formula == null) {
            //过
            return new ArrayList<>();
        }
        //判空丹炉
        if (danLuEnum == null || danLuEnum == DanLuEnum.NONE) {
            //过
            return new ArrayList<>();
        }

        /**
         * todo 计算所有可能结果
         */

        //初始化结果列表
        List<DanFangDoc> result = new ArrayList<>();
        //返回
        return result;
    }

}
