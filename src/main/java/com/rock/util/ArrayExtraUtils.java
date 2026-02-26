package com.rock.util;

/**
 * 数组 扩展扩展包
 *
 * @Author ayl
 * @Date 2023-03-03
 */
public class ArrayExtraUtils {

    /**
     * 获取数组内String
     *
     * @param partArr 数组
     * @param index   索引
     * @return
     */
    public static String getString(String[] partArr, int index) {
        //判空
        if (partArr == null || partArr.length <= index) {
            //过
            return null;
        }
        //判空
        if (partArr[index] == null) {
            //过
            return null;
        }
        //返回
        return partArr[index].trim();
    }

}
