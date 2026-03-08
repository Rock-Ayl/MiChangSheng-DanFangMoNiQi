package com.rock;

/**
 * 配置类
 */
public class Config {

    /**
     * 输出文件地址
     */

    //输出excel的路径
    public static final String OUT_EXCEL_FILE_PATH = "/Users/ayl/Downloads/out.xlsx";

    /**
     * 资源地址
     */

    //药材配置-所有
    public static final String FILE_PATH_ALL_HERBAL_MEDICINE_FILE = "/觅长生-药材.txt";
    //药材配置-宗门库房-金虹
    public static final String FILE_PATH_ZONG_MEN_JIN_HONG_FILE = "/觅长生-药材-金虹剑派库房.txt";
    //药材配置-宗门库房-竹山
    public static final String FILE_PATH_ZONG_MEN_ZHU_SHAN_FILE = "/觅长生-药材-竹山宗库房.txt";
    //药材配置-宗门库房-离火
    public static final String FILE_PATH_ZONG_MEN_LI_HUO_FILE = "/觅长生-药材-离火宗库房.txt";

    //丹药配置-所有
    public static final String FILE_PATH_ALL_PILL_FILE = "/觅长生-丹药.txt";

    /**
     * 开关-一般
     */

    //开关-使用哪种-药材配置-作为药材组合单方
    public static final String SWITCH_YAO_CAI_FILE_PATH = FILE_PATH_ZONG_MEN_LI_HUO_FILE;

    //药材是否包含妖丹
    public static final boolean NEED_YAO_DAN = true;

    //todo 仅考虑 一品药材 and 对应辅药主药作为平衡寒热药材(如果是false,会大幅增加计算时间)

    /**
     * 开关-额外平衡寒热
     */

    //当单方没有主药2,是否将其用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_MAIN_1_GUIDE = true;

    //当单方没有辅药1,是否将其用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_SEC_1_GUIDE = true;

    //当单方没有辅药2,是否将妻用于平衡寒热(如果是true,会大幅增加计算时间)
    public static final boolean SWITCH_SEC_2_GUIDE = true;

}