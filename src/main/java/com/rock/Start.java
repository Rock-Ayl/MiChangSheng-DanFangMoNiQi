package com.rock;

import com.rock.entity.DanFangDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.DanLuEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;
import com.rock.service.CombinationService;
import com.rock.service.InitService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 启动器
 */
public class Start {

    /**
     * 一切工作流程,从这里开始
     *
     * @param args
     */
    public static void main(String[] args) {

        //初始化服务
        InitService dataService = new InitService();
        CombinationService combinationService = new CombinationService();

        /**
         * 读取 药材 配置
         */

        //读取药材数据
        List<YaoCaiDoc> yaoCaiDocList = dataService.loadYaoCai();

        /**
         * 读取 丹药(包含丹方) 配置
         */

        //转为 药材名称map
        Map<String, YaoCaiDoc> yaoCaiDocMap = yaoCaiDocList
                .stream()
                .collect(Collectors.toMap(YaoCaiDoc::getName, p -> p));
        //基于药材,读取丹药数据
        List<DanYaoDoc> danYaoDocList = dataService.loadDanYao(yaoCaiDocMap);

        /**
         * 转为 主药药性map(同药性药材一组)
         */

        //药材主药分组map
        Map<YaoCaiMainEffectEnum, List<YaoCaiDoc>> yaoCaiMainEffectMap = yaoCaiDocList
                .stream()
                .collect(Collectors.groupingBy(YaoCaiDoc::getMainEffect));

        /**
         * 转为 辅药药性map(同药性药材一组)
         */

        //药材辅药分组map
        Map<YaoCaiSecondaryEffectEnum, List<YaoCaiDoc>> yaoCaiSecondaryEffectMap = yaoCaiDocList
                .stream()
                .collect(Collectors.groupingBy(YaoCaiDoc::getSecondaryEffect));

        /**
         * todo 丹药的丹方按照药性分组(主药+辅药)
         */

        List<Map.Entry<String, List<DanYaoDoc>>> test = danYaoDocList
                .stream()
                .filter(p -> p.getFormula() != null)
                .collect(Collectors.groupingBy(p -> p.getFormula().getKey()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
        System.out.println();

        /**
         * 组合排列生成所有丹方
         */

        //所有药材列表(包含NULL)
        List<YaoCaiDoc> yaoCaiDocAndNullList = new ArrayList<>(yaoCaiDocList);
        //把空也放里面,这也是一种情况
        yaoCaiDocAndNullList.add(null);

        //丹方map
        Map<DanYaoDoc, List<DanFangDoc>> danFangDocMap = new HashMap<>();
        //循环所有丹药
        for (DanYaoDoc danYaoDoc : danYaoDocList) {
            //使用所有丹炉
            for (DanLuEnum danLuEnum : DanLuEnum.values()) {
                //生成所有组合,并加入map
                danFangDocMap.put(danYaoDoc, combinationService.combination(
                        danYaoDoc,
                        danLuEnum,
                        yaoCaiDocAndNullList,
                        yaoCaiMainEffectMap,
                        yaoCaiSecondaryEffectMap
                ));
            }
        }
        System.out.println();

    }

}
