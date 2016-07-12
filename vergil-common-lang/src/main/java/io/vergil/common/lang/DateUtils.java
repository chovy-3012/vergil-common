package io.vergil.common.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author zhaowei
 * @date 2015年9月7日下午5:36:37
 */
public class DateUtils {
	public static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd",
			"yyyy-MM", "yyyy-MM-dd HH:mm:ss.S", "yyyy年MM月dd日", "yyyy年MM月dd日 HH:mm", "yyyyMMdd", "yyyy年MM月dd日 HH:mm:ss",
			"HH:mm:ss", "yyMMdd" };

	/**
	 * 字符串转换成Date对象
	 * 
	 * @author zhaowei
	 * @date 2015年9月7日下午5:40:51
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date parseToDate(String str) throws ParseException {
		return org.apache.commons.lang3.time.DateUtils.parseDate(str, DATE_FORMATS);
	}

	/**
	 * 格式化日期
	 * 
	 * @author zhaowei
	 * @date 2015年10月2日上午10:12:51
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMMddHHmm(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATS[0]);
		return sdf.format(date);
	}
}
