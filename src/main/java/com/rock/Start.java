package com.rock;

import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.service.InitService;

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

        /**
         * 读取文件、转为所需实体
         * -
         * 1. 药材
         * 2. 丹药(包含丹方)
         */

        //初始化服务
        InitService dataService = new InitService();

        //读取药材数据
        List<YaoCaiDoc> yaoCaiDocList = dataService.loadYaoCai();

        //药材转为map
        Map<String, YaoCaiDoc> yaoCaiDocMap = yaoCaiDocList
                .stream()
                .collect(Collectors.toMap(YaoCaiDoc::getName, p -> p));

        //基于药材,读取丹药数据
        List<DanYaoDoc> danYaoDocList = dataService.loadDanYao(yaoCaiDocMap);

        System.out.println();

        /**
         * todo
         */

    }

}
