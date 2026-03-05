package com.rock;

/**
 * 配置类
 */
public class Config {

    /**
     * 写入文件地址
     */

    //输出excel的路径
    public static final String OUT_EXCEL_FILE_PATH = "/Users/ayl/Downloads/out.xlsx";

    /**
     * 开关-一般
     */

    //药材是否包含妖丹
    public static final boolean NEED_YAO_DAN = true;

    /**
     * 开关-额外平衡寒热
     */

    //当单方没有主药2,是否将其用于平衡寒热(会大幅增加计算时间)
    public static final boolean SWITCH_MAIN_1_GUIDE = false;

    //当单方没有辅药1,是否将其用于平衡寒热(会大幅增加计算时间)
    public static final boolean SWITCH_SEC_1_GUIDE = false;

    //当单方没有辅药2,是否将妻用于平衡寒热(会大幅增加计算时间)
    public static final boolean SWITCH_SEC_2_GUIDE = false;

}