package com.rock;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.rock.entity.DanFangDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.DanLuEnum;
import com.rock.enums.GroupEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;
import com.rock.service.CombinationService;
import com.rock.service.InitService;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
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

        //读取 药材 配置
        List<YaoCaiDoc> yaoCaiDocList = dataService.loadYaoCai(Config.FILE_PATH_ALL_HERBAL_MEDICINE_FILE);

        /**
         * 读取 丹药(包含丹方) 配置
         */

        //基于药材,读取丹药数据
        List<DanYaoDoc> danYaoDocList = dataService.loadDanYao(yaoCaiDocList
                .stream()
                .collect(Collectors.toMap(YaoCaiDoc::getName, p -> p)));

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
         * 按照品级分组
         */

        //按照品级分组
        Map<GroupEnum, List<DanYaoDoc>> danYaoDocGroupMap = danYaoDocList
                .stream()
                .collect(Collectors.groupingBy(DanYaoDoc::getGrade));

        /**
         * 本次生成要使用的药材
         */

        //本次要使用的药材列表
        List<YaoCaiDoc> useYaoCaiDocList = dataService.loadYaoCai(Config.SWITCH_YAO_CAI_FILE_PATH);
        //如果不需要妖丹
        if (Config.NEED_YAO_DAN == false) {
            //过滤掉妖丹
            useYaoCaiDocList = useYaoCaiDocList.stream().filter(p -> p.getYaoDan() == false).collect(Collectors.toList());
        }
        //本次使用的药材名称集合
        Set<String> useYaoCaiNameSet = useYaoCaiDocList
                .stream()
                .map(YaoCaiDoc::getName)
                .collect(Collectors.toSet());
        //把空也放里面,这也是一种情况
        useYaoCaiDocList.add(null);

        /**
         * 组合排列生成所有丹方
         */

        //创建单个 BigExcelWriter（所有 sheet 都写到这个 writer 中）
        BigExcelWriter writer = ExcelUtil.getBigWriter(new File(Config.OUT_EXCEL_FILE_PATH));

        //按顺序循环分组
        for (GroupEnum groupEnum : GroupEnum.values()) {

            //判空
            if (groupEnum == GroupEnum.NONE) {
                //本轮过
                continue;
            }

            //当前品级丹药列表
            List<DanYaoDoc> thisGroupDanYaoDocList = danYaoDocGroupMap.get(groupEnum);
            //判空
            if (CollectionUtils.isEmpty(thisGroupDanYaoDocList)) {
                //本轮过
                continue;
            }

            //为当前品级汇总所有要写入的行（每一项对应一行 Map）
            List<Map<String, Object>> groupDataList = new ArrayList<>();
            //循环丹药
            for (DanYaoDoc danYaoDoc : thisGroupDanYaoDocList) {
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
                            useYaoCaiDocList,
                            useYaoCaiNameSet,
                            yaoCaiMainEffectMap,
                            yaoCaiSecondaryEffectMap,
                            danFangGroupMap
                    );
                    //结束时间
                    long endTime = System.currentTimeMillis();

                    /**
                     * 准备数据（先不写入 excel，先汇总到 groupDataList）
                     */

                    //循环
                    for (DanFangDoc danFangDoc : combinationList) {

                        //初始化行
                        Map<String, Object> rowMap = new HashMap<>();

                        //写入key、value
                        rowMap.put("danYaoName", danYaoDoc.getName());
                        rowMap.put("danLuName", danLuEnum.getCode());

                        //新增：将 名称 与 数量 单独拆分为两个字段，便于后续处理或筛选
                        if (danFangDoc.getMainHerb1() != null) {
                            rowMap.put("mainHerb1Name", danFangDoc.getMainHerb1().getYaoCai().getName());
                            rowMap.put("mainHerb1Quantity", danFangDoc.getMainHerb1().getQuantity());
                            rowMap.put("mainHerb1Grade", danFangDoc.getMainHerb1().getYaoCai().getGrade().getCode());
                            rowMap.put("mainHerb1YaoDan", danFangDoc.getMainHerb1().getYaoCai().getYaoDan() == true ? "是" : "否");
                        } else {
                            rowMap.put("mainHerb1Name", "无");
                            rowMap.put("mainHerb1Quantity", 0);
                            rowMap.put("mainHerb1Grade", "无");
                            rowMap.put("mainHerb1YaoDan", "无");
                        }
                        if (danFangDoc.getMainHerb2() != null) {
                            rowMap.put("mainHerb2Name", danFangDoc.getMainHerb2().getYaoCai().getName());
                            rowMap.put("mainHerb2Quantity", danFangDoc.getMainHerb2().getQuantity());
                            rowMap.put("mainHerb2Grade", danFangDoc.getMainHerb2().getYaoCai().getGrade().getCode());
                            rowMap.put("mainHerb2YaoDan", danFangDoc.getMainHerb2().getYaoCai().getYaoDan() == true ? "是" : "否");
                        } else {
                            rowMap.put("mainHerb2Name", "无");
                            rowMap.put("mainHerb2Quantity", 0);
                            rowMap.put("mainHerb2Grade", "无");
                            rowMap.put("mainHerb2YaoDan", "无");
                        }
                        if (danFangDoc.getSecondaryHerb1() != null) {
                            rowMap.put("secondaryHerb1Name", danFangDoc.getSecondaryHerb1().getYaoCai().getName());
                            rowMap.put("secondaryHerb1Quantity", danFangDoc.getSecondaryHerb1().getQuantity());
                            rowMap.put("secondaryHerb1Grade", danFangDoc.getSecondaryHerb1().getYaoCai().getGrade().getCode());
                            rowMap.put("secondaryHerb1YaoDan", danFangDoc.getSecondaryHerb1().getYaoCai().getYaoDan() == true ? "是" : "否");
                        } else {
                            rowMap.put("secondaryHerb1Name", "无");
                            rowMap.put("secondaryHerb1Quantity", 0);
                            rowMap.put("secondaryHerb1Grade", "无");
                            rowMap.put("secondaryHerb1YaoDan", "无");
                        }
                        if (danFangDoc.getSecondaryHerb2() != null) {
                            rowMap.put("secondaryHerb2Name", danFangDoc.getSecondaryHerb2().getYaoCai().getName());
                            rowMap.put("secondaryHerb2Quantity", danFangDoc.getSecondaryHerb2().getQuantity());
                            rowMap.put("secondaryHerb2Grade", danFangDoc.getSecondaryHerb2().getYaoCai().getGrade().getCode());
                            rowMap.put("secondaryHerb2YaoDan", danFangDoc.getSecondaryHerb2().getYaoCai().getYaoDan() == true ? "是" : "否");
                        } else {
                            rowMap.put("secondaryHerb2Name", "无");
                            rowMap.put("secondaryHerb2Quantity", 0);
                            rowMap.put("secondaryHerb2Grade", "无");
                            rowMap.put("secondaryHerb2YaoDan", "无");
                        }
                        if (danFangDoc.getGuideHerb() != null) {
                            rowMap.put("guideHerbName", danFangDoc.getGuideHerb().getYaoCai().getName());
                            rowMap.put("guideHerbQuantity", danFangDoc.getGuideHerb().getQuantity());
                            rowMap.put("guideHerbGrade", danFangDoc.getGuideHerb().getYaoCai().getGrade().getCode());
                            rowMap.put("guideHerbYaoDan", danFangDoc.getGuideHerb().getYaoCai().getYaoDan() == true ? "是" : "否");
                        } else {
                            rowMap.put("guideHerbName", "无");
                            rowMap.put("guideHerbQuantity", 0);
                            rowMap.put("guideHerbGrade", "无");
                            rowMap.put("guideHerbYaoDan", "无");
                        }
                        rowMap.put("yaoCaiCount", danFangDoc.getCurrentYaoCaiCount());
                        //组装到品级集合
                        groupDataList.add(rowMap);
                    }

                    /**
                     * 输出日志（生成信息）
                     */

                    //打印丹药+丹炉+丹方数量
                    System.out.println("成功生成[" + combinationList.size() + "]个[" + danYaoDoc.getName() +
                            "]的丹方,丹炉是[" + danLuEnum.getCode() +
                            "],耗时:" + (endTime - startTime) / 1000.0 + "秒");
                }
            }

            /**
             * 将当前品级的所有数据切为 N 份，写入不同 sheet
             *
             * 规则：
             *  - 每个 sheet 只放当前品级的一部分
             *  - 每写入一个 sheet 前都重新设置表头别名（和你之前逻辑保持一致）
             */

            //总数
            int total = groupDataList.size();
            //判空
            if (total == 0) {
                //本轮过
                continue;
            }

            //获取品级对应的分片数量
            Integer sheetsPerGroup = groupEnum.getSheetPart();
            //计算每份大小（向上取整）
            int partSize = (total + groupEnum.getSheetPart() - 1) / sheetsPerGroup;
            //循环
            for (int i = 0; i < sheetsPerGroup; i++) {
                //计算当前分段的起始索引
                int start = i * partSize;
                //如果结束
                if (start >= total) {
                    //超过总数,跳出
                    break;
                }
                //结束索引
                int end = Math.min(start + partSize, total);
                //切分
                List<Map<String, Object>> subList = groupDataList.subList(start, end);

                //每个Sheet都要重新设置表头（与你原代码保持一致）
                writer.addHeaderAlias("danYaoName", "丹药名称");
                writer.addHeaderAlias("danLuName", "丹炉名称");
                writer.addHeaderAlias("yaoCaiCount", "药材总数");

                //新增表头别名(拆分后的名称与数量字段)
                writer.addHeaderAlias("mainHerb1Name", "主药1名称");
                writer.addHeaderAlias("mainHerb1Quantity", "主药1数量");
                writer.addHeaderAlias("mainHerb1Grade", "主药1品级");
                writer.addHeaderAlias("mainHerb1YaoDan", "主药1是妖丹");

                writer.addHeaderAlias("mainHerb2Name", "主药2名称");
                writer.addHeaderAlias("mainHerb2Quantity", "主药2数量");
                writer.addHeaderAlias("mainHerb2Grade", "主药2品级");
                writer.addHeaderAlias("mainHerb2YaoDan", "主药2是妖丹");

                writer.addHeaderAlias("secondaryHerb1Name", "辅药1名称");
                writer.addHeaderAlias("secondaryHerb1Quantity", "辅药1数量");
                writer.addHeaderAlias("secondaryHerb1Grade", "辅药1品级");
                writer.addHeaderAlias("secondaryHerb1YaoDan", "辅药1是妖丹");

                writer.addHeaderAlias("secondaryHerb2Name", "辅药2名称");
                writer.addHeaderAlias("secondaryHerb2Quantity", "辅药2数量");
                writer.addHeaderAlias("secondaryHerb2Grade", "辅药2品级");
                writer.addHeaderAlias("secondaryHerb2YaoDan", "辅药2是妖丹");

                writer.addHeaderAlias("guideHerbName", "药引名称");
                writer.addHeaderAlias("guideHerbQuantity", "药引数量");
                writer.addHeaderAlias("guideHerbGrade", "药引品级");
                writer.addHeaderAlias("guideHerbYaoDan", "药引是妖丹");

                //设置sheet名称
                writer.setSheet(groupEnum.getCode() + "丹方_" + (i + 1));

                //写入数据（追加模式为 false，因为每次切分写入的都是该 sheet 的所有行）
                writer.write(subList, true);

            }

        }

        //统一关闭 writer
        writer.close();

    }

}