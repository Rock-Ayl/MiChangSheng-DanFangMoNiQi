package com.rock.entity;

import com.rock.enums.GroupEnum;
import com.rock.enums.YaoCaiMainEffectEnum;
import com.rock.enums.YaoCaiPropertyEnum;
import com.rock.enums.YaoCaiSecondaryEffectEnum;
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
     * -
     * {@link YaoCaiMainEffectEnum } 枚举
     */
    private YaoCaiMainEffectEnum mainEffect;

    /**
     * 辅药作用
     * -
     * {@link YaoCaiSecondaryEffectEnum } 枚举
     */
    private YaoCaiSecondaryEffectEnum secondaryEffect;

    /**
     * 药引性质
     * -
     * {@link YaoCaiPropertyEnum } 枚举
     */
    private YaoCaiPropertyEnum property;

}