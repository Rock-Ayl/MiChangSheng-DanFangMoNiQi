package com.rock.service;

import com.rock.entity.FormulaDoc;
import com.rock.entity.HerbalMedicineDoc;
import com.rock.entity.PillDoc;
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
     * 读取所有药材数据
     *
     * @return 药材列表
     */
    public List<HerbalMedicineDoc> loadHerbalMedicines() {
        //初始化药材列表
        List<HerbalMedicineDoc> medicineDocList = new ArrayList<>();
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
                    HerbalMedicineDoc medicine = new HerbalMedicineDoc();
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
     * 读取所有丹药数据
     *
     * @return 丹药列表
     */
    public List<PillDoc> loadPills() {
        //初始化丹药列表
        List<PillDoc> pillDocList = new ArrayList<>();
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
                //按逗号分割，但要注意丹方部分包含逗号
                String[] parts = line.split(",");
                //如果满足
                if (parts.length >= 6) {
                    //丹药名称
                    String name = parts[0].trim();
                    //品级
                    String grade = parts[1].trim();
                    //种类
                    String type = parts[2].trim();
                    //价值
                    int value = Integer.parseInt(parts[parts.length - 1].trim());
                    //丹方部分需要重新组合(从parts[3]到parts[length-2])
                    StringBuilder formulaBuilder = new StringBuilder();
                    //循环
                    for (int i = 3; i < parts.length - 1; i++) {
                        //
                        if (i > 3) {
                            formulaBuilder.append(",");
                            formulaBuilder.append(parts[i]);
                        }
                    }
                    //转为对应丹方实体
                    FormulaDoc formula = FormulaDoc.parse(formulaBuilder.toString());
                    //初始化丹药实体
                    PillDoc pill = new PillDoc(name, grade, type, formula, value);
                    //组装
                    pillDocList.add(pill);
                }
            }
        } catch (Exception e) {
            System.err.println("读取丹药数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }
        //返回列表
        return pillDocList;
    }

}