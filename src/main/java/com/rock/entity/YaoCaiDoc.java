package com.rock.entity;

import com.rock.enums.GroupEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 药材 实体类
 */
@Getter
@Setter
@ToString
public class YaoCaiDoc {

    /**
     * 药材名称
     */
    private String name;

    /**
     * 品级
     * -
     * {@link GroupEnum } 枚举
     */
    private GroupEnum grade;

    /**
     * 主药作用
     */
    private String mainEffect;

    /**
     * 辅药作用
     */
    private String secondaryEffect;

    /**
     * 药引性质 (性平、性寒、性热)
     */
    private String property;

}