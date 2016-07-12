package io.vergil.common.lang;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json工具类
 * 
 * @author zhaowei
 * @date 2015年12月14日下午1:34:08
 */
public class JsonUtils {

	/**
	 * 对象转换为json字符串，值为null也显示该字段
	 * 
	 * @author zhaowei
	 * @date 2015年12月14日下午1:36:25
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		String str = JSONObject.toJSONString(object, SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty);
		return str;
	}
}
