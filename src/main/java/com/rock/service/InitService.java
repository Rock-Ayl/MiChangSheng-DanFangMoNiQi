package com.rock.service;

import com.rock.entity.FormulaDoc;
import com.rock.entity.HerbalMedicineDoc;
import com.rock.entity.PillDoc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文件,转为实体
 */
public class InitService {

    //资源
    private static final String HERBAL_MEDICINE_FILE = "/觅长生-药材.txt";
    private static final String PILL_FILE = "/觅长生-丹药.txt";

    /**
     * 读取所有药材数据
     *
     * @return 药材列表
     */
    public List<HerbalMedicineDoc> loadHerbalMedicines() {
        List<HerbalMedicineDoc> medicines = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(HERBAL_MEDICINE_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

            String line;
            // 跳过第一行标题
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    HerbalMedicineDoc medicine = new HerbalMedicineDoc(
                            parts[0].trim(),  // 名称
                            parts[1].trim(),  // 品级
                            parts[2].trim(),  // 主药作用
                            parts[3].trim(),  // 辅药作用
                            parts[4].trim()   // 药引性质
                    );
                    medicines.add(medicine);
                }
            }

        } catch (Exception e) {
            System.err.println("读取药材数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * 读取所有丹药数据
     *
     * @return 丹药列表
     */
    public List<PillDoc> loadPills() {
        List<PillDoc> pills = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream(PILL_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

            String line;
            // 跳过第一行标题
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 按逗号分割，但要注意丹方部分包含逗号
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    // 丹药名称
                    String name = parts[0].trim();
                    // 品级
                    String grade = parts[1].trim();
                    // 种类
                    String type = parts[2].trim();
                    // 价值
                    int value = Integer.parseInt(parts[parts.length - 1].trim());

                    // 丹方部分需要重新组合(从parts[3]到parts[length-2])
                    StringBuilder formulaBuilder = new StringBuilder();
                    for (int i = 3; i < parts.length - 1; i++) {
                        if (i > 3) formulaBuilder.append(",");
                        formulaBuilder.append(parts[i]);
                    }
                    String formulaStr = formulaBuilder.toString();

                    FormulaDoc formula = FormulaDoc.parse(formulaStr);

                    PillDoc pill = new PillDoc(name, grade, type, formula, value);
                    pills.add(pill);
                }
            }

        } catch (Exception e) {
            System.err.println("读取丹药数据文件失败: " + e.getMessage());
            e.printStackTrace();
        }

        return pills;
    }

}