package com.rock.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 药材 & 数量 实体类
 */
@Getter
@Setter
@ToString
public class YaoCaiShuLiangDoc {

    /**
     * 药材实体
     */
    private YaoCaiDoc yaoCai;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 解析实体
     *
     * @param yaoCaiShuLiangStr 药材 & 数量 字符串
     */
    public static YaoCaiShuLiangDoc parse(String yaoCaiShuLiangStr) {
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
        //初始化实体
        YaoCaiShuLiangDoc yaoCaiShuLiangDoc = new YaoCaiShuLiangDoc();
        //todo 组装
        yaoCaiShuLiangDoc.setYaoCai(null);
        yaoCaiShuLiangDoc.setQuantity(Integer.parseInt(parts[1]));
        //返回
        return yaoCaiShuLiangDoc;
    }

}