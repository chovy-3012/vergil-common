package io.vergil.common.web.exception;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import io.vergil.common.lang.exception.BusinessException;
import io.vergil.common.lang.message.Result;

/**
 * springmvc默认异常处理类
 * 
 * @author zhaowei
 * @date 2015年9月21日下午3:44:38
 */
public class SpringMVCDefaultException extends SimpleMappingExceptionResolver {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		String result = null;
		if (ex instanceof BusinessException) {
			result = Result.error(ex.getMessage(), null);
			log.error(ex.getMessage());
		} else if (ex instanceof ValidatorException) {
			result = Result.error(ex.getMessage());
		} else if (ex instanceof MissingServletRequestParameterException) {
			result = Result.lackOfParam();
			logger.error(ex.getMessage());
		} else if (ex.getClass().getName().equals("org.apache.catalina.connector.ClientAbortException")) {
			// 客户端断开，http返回数据无法返回
		} else {
			result = Result.error("系统错误", null);
			log.error("系统错误", ex);
		}

		boolean isAjaxResponse = true;
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			isAjaxResponse = method.isAnnotationPresent(ResponseBody.class);
		}

		if (isAjaxResponse) {
			try {
				response.setContentType("application/json;charset=utf-8");
				PrintWriter pw = response.getWriter();
				pw.print(result);
				pw.flush();
			} catch (Exception e) {
			}
			return new ModelAndView();
		} else {
			return super.doResolveException(request, response, handler, ex);
		}
	}
}
