package com.rock;

import com.rock.entity.HerbalMedicineDoc;
import com.rock.entity.DanYaoDoc;
import com.rock.service.InitService;

import java.util.List;

/**
 * 启动器
 */
public class Running {

    /**
     * 一切工作流程,从这里开始
     *
     * @param args
     */
    public static void main(String[] args) {

        /**
         * 读取文件、转为所需实体
         * -
         * 药材
         * 丹药(丹方)
         */

        //初始化服务
        InitService dataService = new InitService();

        //读取药材数据
        List<HerbalMedicineDoc> medicineDocList = dataService.loadHerbalMedicines();

        //读取丹药数据
        List<DanYaoDoc> danYaoDocList = dataService.loadPills();

        System.out.println();

        /**
         * todo
         */

    }

}
