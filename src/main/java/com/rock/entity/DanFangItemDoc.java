package com.rock.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 丹方-明细 实体类
 */
@Getter
@Setter
public class DanFangItemDoc {

    /**
     * 药材实体
     */
    private YaoCaiDoc yaoCai;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 所需总药力
     */
    private Integer totalPower;

    /**
     * 解析实体
     *
     * @param yaoCaiShuLiangStr 药材 & 数量 字符串
     * @param yaoCaiDocMap      药材map
     **/
    public static DanFangItemDoc parse(String yaoCaiShuLiangStr, Map<String, YaoCaiDoc> yaoCaiDocMap) {
        //判空
        if (yaoCaiShuLiangStr == null || yaoCaiShuLiangStr.isEmpty()) {
            //过
            return null;
        }
        //拆分
        String[] parts = yaoCaiShuLiangStr.split("\\*");
        //如果不满足数量
        if (parts.length != 2) {
            //过
            return null;
        }
        //获取药材名称
        String yaoCaiName = parts[0].trim();
        //获取药材实体
        YaoCaiDoc yaoCaiDoc = yaoCaiDocMap.get(yaoCaiName);
        //如果不存在
        if (yaoCaiDoc == null) {
            //过
            return null;
        }
        //初始化实体
        DanFangItemDoc danFangItemDoc = new DanFangItemDoc();
        //组装
        danFangItemDoc.setYaoCai(yaoCaiDoc);
        danFangItemDoc.setQuantity(Integer.parseInt(parts[1]));
        //计算所需总药力
        danFangItemDoc.setTotalPower(yaoCaiDoc.getGrade().getPower() * danFangItemDoc.getQuantity());
        //返回
        return danFangItemDoc;
    }

    //方便调试
    @Override
    public String toString() {
        return this.yaoCai.getName() + "*" + this.quantity + "(" + this.totalPower + ")";
    }

}