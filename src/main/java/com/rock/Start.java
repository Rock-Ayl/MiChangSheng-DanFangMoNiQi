package com.rock;

import com.rock.entity.DanFangDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.DanLuEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;
import com.rock.service.CombinationService;
import com.rock.service.InitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
         * 丹药的丹方按照药性分组(主药+辅药),为丹方覆盖做准备
         */

        //丹方分组map
        Map<String, List<DanYaoDoc>> danFangGroupMap = new HashMap<>();
        //循环
        for (DanYaoDoc danYaoDoc : danYaoDocList) {
            //判空
            if (danYaoDoc.getFormula() == null) {
                //本轮过
                continue;
            }
            //循环所有key
            for (String key : danYaoDoc.getFormula().getKey()) {
                //获取当前key的列表
                List<DanYaoDoc> keyGroupList = danFangGroupMap.getOrDefault(key, new ArrayList<>());
                //加入当前丹药
                keyGroupList.add(danYaoDoc);
                //加入map (覆盖)
                danFangGroupMap.put(key, keyGroupList);
            }
        }

        /**
         * 组合排列生成所有丹方
         */

        //所有药材列表(包含NULL)
        List<YaoCaiDoc> yaoCaiDocAndNullList = new ArrayList<>(yaoCaiDocList);
        //把空也放里面,这也是一种情况
        yaoCaiDocAndNullList.add(null);

        //循环所有丹药
        for (DanYaoDoc danYaoDoc : danYaoDocList) {
            //使用所有丹炉
            for (DanLuEnum danLuEnum : DanLuEnum.values()) {

                /**
                 * 生成本次单方
                 */

                //开始时间
                long startTime = System.currentTimeMillis();
                //生成本次组合
                List<DanFangDoc> combinationList = combinationService.combination(
                        danYaoDoc,
                        danLuEnum,
                        yaoCaiDocAndNullList,
                        yaoCaiMainEffectMap,
                        yaoCaiSecondaryEffectMap,
                        danFangGroupMap
                );
                //结束时间
                long endTime = System.currentTimeMillis();

                /**
                 * 输出
                 */

                //打印丹药+丹炉+丹方数量
                System.out.println(
                        "成功生成[" + combinationList.size() + "]个[" + danYaoDoc.getName() +
                                "]的丹方,丹炉是[" + danLuEnum.getCode() +
                                "],耗时:" + (endTime - startTime) / 1000.0 + "秒");
            }
        }
        System.out.println();

    }

}
