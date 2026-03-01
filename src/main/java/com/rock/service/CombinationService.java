package com.rock.service;

import com.rock.entity.DanFangDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.enums.DanLuEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合排列服务
 */
public class CombinationService {

    /**
     * 组合排列
     *
     * @param danYaoDoc 丹药
     * @param danLuEnum 丹炉
     * @return
     */
    public List<DanFangDoc> combination(DanYaoDoc danYaoDoc, DanLuEnum danLuEnum) {

        /**
         * todo 校验
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
         * todo 计算
         */

        return null;
    }

}
