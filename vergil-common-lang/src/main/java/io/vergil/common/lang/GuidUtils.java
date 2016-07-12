package io.vergil.common.lang;

import java.util.UUID;

/**
 * @Description guid工具类
 * @ClassName GuidUtil
 * @author zhaowei
 * @date 2015年6月27日下午7:16:27
 */
public class GuidUtils {

	/**
	 * @Description 产生新的uuid
	 * @param
	 * @return String
	 */
	public static String newGuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
