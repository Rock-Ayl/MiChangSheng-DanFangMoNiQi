package com.rock;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
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

        //创建output目录
        java.io.File outputDir = new java.io.File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

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
                 * 写入excel
                 */

                //创建BigExcelWriter
                BigExcelWriter writer = ExcelUtil.getBigWriter(Config.OUT_EXCEL_FILE_PATH);

                //设置表头
                writer.addHeaderAlias("danYaoName", "丹药名称");
                writer.addHeaderAlias("danLuName", "丹炉名称");
                writer.addHeaderAlias("mainHerb1", "主药1");
                writer.addHeaderAlias("mainHerb2", "主药2");
                writer.addHeaderAlias("secondaryHerb1", "辅药1");
                writer.addHeaderAlias("secondaryHerb2", "辅药2");
                writer.addHeaderAlias("guideHerb", "药引");
                writer.addHeaderAlias("yaoCaiCount", "药材总数");
                writer.addHeaderAlias("heatAndColdValue", "寒热数值");

                //准备数据
                List<Map<String, Object>> dataList = new java.util.ArrayList<>();
                //循环
                for (DanFangDoc danFangDoc : combinationList) {
                    //初始化行
                    Map<String, Object> row = new java.util.HashMap<>();
                    //写入key、value
                    row.put("danYaoName", danYaoDoc.getName());
                    row.put("danLuName", danLuEnum.getCode());
                    row.put("mainHerb1", danFangDoc.getMainHerb1() != null ? danFangDoc.getMainHerb1().getYaoCai().getName() + "(" + danFangDoc.getMainHerb1().getQuantity() + ")" : "无");
                    row.put("mainHerb2", danFangDoc.getMainHerb2() != null ? danFangDoc.getMainHerb2().getYaoCai().getName() + "(" + danFangDoc.getMainHerb2().getQuantity() + ")" : "无");
                    row.put("secondaryHerb1", danFangDoc.getSecondaryHerb1() != null ? danFangDoc.getSecondaryHerb1().getYaoCai().getName() + "(" + danFangDoc.getSecondaryHerb1().getQuantity() + ")" : "无");
                    row.put("secondaryHerb2", danFangDoc.getSecondaryHerb2() != null ? danFangDoc.getSecondaryHerb2().getYaoCai().getName() + "(" + danFangDoc.getSecondaryHerb2().getQuantity() + ")" : "无");
                    row.put("guideHerb", danFangDoc.getGuideHerb() != null ? danFangDoc.getGuideHerb().getYaoCai().getName() + "(" + danFangDoc.getGuideHerb().getQuantity() + ")" : "无");
                    row.put("yaoCaiCount", danFangDoc.getCurrentYaoCaiCount());
                    row.put("heatAndColdValue", danFangDoc.getCurrentYaoCaiHeatAndColdValue());
                    //组装
                    dataList.add(row);
                }
                //写入数据
                writer.write(dataList, true);
                //关闭writer，释放资源
                writer.close();

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