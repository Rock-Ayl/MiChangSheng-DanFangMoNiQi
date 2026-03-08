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
     * 资源地址
     */

    //所有药材配置
    public static final String FILE_PATH_ALL_HERBAL_MEDICINE_FILE = "/觅长生-药材.txt";

    //所有丹药配置
    public static final String FILE_PATH_ALL_PILL_FILE = "/觅长生-丹药.txt";

    /**
     * 开关-一般
     */

    //药材是否包含妖丹
    public static final boolean NEED_YAO_DAN = true;

    //todo 仅考虑 一品药材 and 对应辅药主药作为平衡寒热药材(如果是false,会大幅增加计算时间)

    /**
     * 开关-额外平衡寒热
     */

    //当单方没有主药2,是否将其用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_MAIN_1_GUIDE = false;

    //当单方没有辅药1,是否将其用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_SEC_1_GUIDE = false;

    //当单方没有辅药2,是否将妻用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_SEC_2_GUIDE = false;

}