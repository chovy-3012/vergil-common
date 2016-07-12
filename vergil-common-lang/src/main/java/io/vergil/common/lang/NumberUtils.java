package io.vergil.common.lang;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数字工具
 * 
 * @author zhaowei
 * @date 2015年10月30日下午4:23:19
 */
public class NumberUtils {

	/**
	 * 数字转换成万、亿单位
	 * 
	 * <pre>
	 * 例如：12345转换成1.23万
	 * </pre>
	 * 
	 * @author zhaowei
	 * @date 2015年10月30日下午5:09:35
	 * @param number
	 * @return
	 */
	public static String toUnitString(BigDecimal number) {
		// 是否大于一亿
		BigDecimal bd = new BigDecimal(100000000);
		if (number.compareTo(bd) >= 0) {
			DecimalFormat df = new DecimalFormat("####.##亿");
			return df.format(number.divide(bd));
		}
		// 是否大于一万
		bd = new BigDecimal(10000);
		if (number.compareTo(bd) >= 0) {
			DecimalFormat df = new DecimalFormat("####.##万");
			return df.format(number.divide(bd));
		}
		// 其它
		DecimalFormat df = new DecimalFormat("####.##");
		return df.format(number);
	}

	/**
	 * 给数字带上数字单位
	 * 
	 * <pre>
	 * 例如：1234567890转换成12亿3456万7890
	 * </pre>
	 * 
	 * @author zhaowei
	 * @date 2015年12月4日下午8:44:08
	 * @param number
	 * @return
	 */
	public static String toAccurateUnitString(BigDecimal number) {
		// 是否大于一亿
		String str = "";
		BigDecimal bd = new BigDecimal(100000000);
		if (number.compareTo(bd) >= 0) {
			int value = number.divide(bd).intValue();
			str += value + "亿";
			number = number.subtract(bd.multiply(new BigDecimal(value)));
		}
		// 是否大于一万
		bd = new BigDecimal(10000);
		if (number.compareTo(bd) >= 0) {
			int value = number.divide(bd).intValue();
			str += value + "万";
			number = number.subtract(bd.multiply(new BigDecimal(value)));
		}
		// 其它
		str += number.toString();
		return str;
	}

	public static void main(String[] args) {
		BigDecimal bigDecimal = new BigDecimal(1234567890);
		System.out.println(toAccurateUnitString(bigDecimal));
	}

	/**
	 * 格式化数字
	 * 
	 * @author zhaowei
	 * @date 2015年10月30日下午5:16:34
	 * @param number
	 * @return
	 */
	public static String format(BigDecimal number) {
		NumberFormat nf = NumberFormat.getInstance(Locale.CHINA);
		return nf.format(number);
	}
}
