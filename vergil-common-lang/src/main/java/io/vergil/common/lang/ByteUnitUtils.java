package io.vergil.common.lang;

import java.text.DecimalFormat;

/**
 * 字节单位转换
 *
 * @author zhaowei
 * @date 2015年12月8日下午7:03:57
 */
public class ByteUnitUtils {

    public static String format(long size) {
        if (size <= 0) {
            return "0B";
        }
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.###").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
