package com.rock.service;

import com.rock.Config;
import com.rock.entity.DanFangDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.enums.*;
import com.rock.util.ArrayExtraUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取文件,转为实体
 */
public class InitService {


    /**
     * 读取所有-药材数据
     *
     * @param filePath 药材文件路径
     * @return 药材列表
     */
    public List<YaoCaiDoc> loadYaoCai(String filePath) {
        //初始化药材列表
        List<YaoCaiDoc> medicineDocList = new ArrayList<>();
        //读取资源
        try (InputStream is = getClass().getResourceAsStream(filePath);
             //读取流
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            //当前行
            String line;
            //跳过第一行标题
            reader.readLine();
            //循环
            while ((line = reader.readLine()) != null) {
                //判空
                if (line.trim().isEmpty()) {
                    //本轮过
                    continue;
                }
                //读取本行
                String[] partArr = line.split(",");
                //初始化实体
                YaoCaiDoc yaoCaiDoc = new YaoCaiDoc();
                //参数
                yaoCaiDoc.setName(ArrayExtraUtils.getString(partArr, 0));
                //解析枚举
                yaoCaiDoc.setGrade(GroupEnum.parseByCode(ArrayExtraUtils.getString(partArr, 1)));
                //解析参数
                yaoCaiDoc.setMainEffect(YaoCaiMainEffectEnum.parseByCode(ArrayExtraUtils.getString(partArr, 2)));
                //解析参数
                yaoCaiDoc.setSecondaryEffect(YaoCaiSecondaryEffectEnum.parseByCode(ArrayExtraUtils.getString(partArr, 3)));
                //解析枚举
                yaoCaiDoc.setProperty(YaoCaiPropertyEnum.parseByCode(ArrayExtraUtils.getString(partArr, 4)));
                //是否为妖丹
                yaoCaiDoc.setYaoDan("是".equals(ArrayExtraUtils.getString(partArr, 5)));
                //组装到列表
                medicineDocList.add(yaoCaiDoc);
            }
        } catch (Exception e) {
            System.err.println("读取药材数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }
        //返回结果
        return medicineDocList;
    }

    /**
     * 读取所有-丹药数据
     *
     * @param yaoCaiDocMap 药材map
     * @return 丹药列表
     */
    public List<DanYaoDoc> loadDanYao(Map<String, YaoCaiDoc> yaoCaiDocMap) {
        //初始化丹药列表
        List<DanYaoDoc> danYaoDocList = new ArrayList<>();
        //读取资源
        try (InputStream is = getClass().getResourceAsStream(Config.FILE_PATH_ALL_PILL_FILE);
             //读取流
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            //当前行
            String line;
            //跳过第一行标题
            reader.readLine();
            //循环
            while ((line = reader.readLine()) != null) {
                //判空
                if (line.trim().isEmpty()) {
                    //本轮过
                    continue;
                }
                //读取本行
                String[] partArr = line.split(",");
                //初始化实体
                DanYaoDoc danYaoDoc = new DanYaoDoc();
                //参数
                danYaoDoc.setName(ArrayExtraUtils.getString(partArr, 0));
                //解析枚举
                danYaoDoc.setGrade(GroupEnum.parseByCode(ArrayExtraUtils.getString(partArr, 1)));
                //解析枚举
                danYaoDoc.setType(DanYaoTypeEnum.parseByCode(ArrayExtraUtils.getString(partArr, 2)));
                //解析单方
                DanFangDoc danFangDoc = DanFangDoc.parse(ArrayExtraUtils.getString(partArr, 3), yaoCaiDocMap);
                //如果根据药材,无法生成单方
                if (danFangDoc == null) {
                    //本轮过
                    continue;
                }
                //解析丹方并组装
                danYaoDoc.setFormula(danFangDoc);
                //解析价格
                danYaoDoc.setAmount(Integer.parseInt(ArrayExtraUtils.getString(partArr, 4)));
                //组装到列表
                danYaoDocList.add(danYaoDoc);
            }
        } catch (Exception e) {
            System.err.println("读取丹药数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }
        //返回结果
        return danYaoDocList;
    }

}