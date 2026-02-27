package com.rock.service;

import com.rock.entity.FormulaDoc;
import com.rock.entity.YaoCaiDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.enums.DanYaoGroupEnum;
import com.rock.util.ArrayExtraUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文件,转为实体
 */
public class InitService {

    //配置资源
    private static final String FILE_PATH_HERBAL_MEDICINE_FILE = "/觅长生-药材.txt";
    private static final String FILE_PATH_PILL_FILE = "/觅长生-丹药.txt";

    /**
     * 读取所有-药材数据
     *
     * @return 药材列表
     */
    public List<YaoCaiDoc> loadYaoCai() {
        //初始化药材列表
        List<YaoCaiDoc> medicineDocList = new ArrayList<>();
        //读取资源
        try (InputStream is = getClass().getResourceAsStream(FILE_PATH_HERBAL_MEDICINE_FILE);
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
                //如果满足条件
                if (partArr.length >= 5) {
                    //初始化实体
                    YaoCaiDoc medicine = new YaoCaiDoc();
                    //参数
                    medicine.setName(ArrayExtraUtils.getString(partArr, 0));
                    medicine.setGrade(ArrayExtraUtils.getString(partArr, 1));
                    medicine.setMainEffect(ArrayExtraUtils.getString(partArr, 2));
                    medicine.setSecondaryEffect(ArrayExtraUtils.getString(partArr, 3));
                    medicine.setProperty(ArrayExtraUtils.getString(partArr, 4));
                    //组装到列表
                    medicineDocList.add(medicine);
                }
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
     * @return 丹药列表
     */
    public List<DanYaoDoc> loadDanYao() {
        //初始化丹药列表
        List<DanYaoDoc> danYaoDocList = new ArrayList<>();
        //读取资源
        try (InputStream is = getClass().getResourceAsStream(FILE_PATH_PILL_FILE);
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
                danYaoDoc.setGrade(DanYaoGroupEnum.parseByCode(ArrayExtraUtils.getString(partArr, 1)));
                danYaoDoc.setType(ArrayExtraUtils.getString(partArr, 2));
                //解析丹方并组装
                danYaoDoc.setFormula(FormulaDoc.parse(ArrayExtraUtils.getString(partArr, 3)));
                //价格
                String valueStr = ArrayExtraUtils.getString(partArr, 4);
                //解析价格
                danYaoDoc.setValue(valueStr != null ? Integer.parseInt(valueStr) : 0);
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