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
     * 总药力
     */
    private Integer totalPower;

    /**
     * 初始化
     */
    public DanFangItemDoc() {

    }

    /**
     * 初始化2
     */
    public DanFangItemDoc(YaoCaiDoc yaoCai, Integer quantity) {
        this.yaoCai = yaoCai;
        this.quantity = quantity;
        //计算总药效
        this.totalPower = yaoCai.getGrade().getPower() * quantity;
    }

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
        //初始化实体,返回
        return new DanFangItemDoc(yaoCaiDoc, Integer.parseInt(parts[1]));
    }

    //方便调试
    @Override
    public String toString() {
        return this.yaoCai.getName() + "*" + this.quantity + "(" + this.totalPower + ")";
    }

}