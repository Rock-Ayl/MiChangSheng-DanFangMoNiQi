package com.rock.entity;

import com.rock.enums.GroupEnum;
import com.rock.enums.DanYaoTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 丹药 实体类
 */
@Getter
@Setter
@ToString
public class DanYaoDoc {

    /**
     * 丹药名称
     */
    private String name;

    /**
     * 品级
     * -
     * {@link GroupEnum } 枚举
     */
    private GroupEnum grade;

    /**
     * 丹药种类
     * -
     * {@link com.rock.enums.DanYaoTypeEnum } 枚举
     */
    private DanYaoTypeEnum type;

    /**
     * 丹方配方
     */
    private DanFangDoc formula;

    /**
     * 丹药价格
     */
    private Integer amount;

}