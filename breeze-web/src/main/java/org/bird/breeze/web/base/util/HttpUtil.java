package org.bird.breeze.web.base.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

public class HttpUtil {

	private static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";
	private static final String AJAX_REQUEST_HEADER_NAME = "X-Requested-With";

	/**
	 * 检测客户端请求是否是ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(final HttpServletRequest request) {
		return AJAX_REQUEST_HEADER_VALUE.equals(request.getHeader(AJAX_REQUEST_HEADER_NAME)) ||
				AJAX_REQUEST_HEADER_VALUE.equals(request.getParameter(AJAX_REQUEST_HEADER_NAME)) ||
						Boolean.TRUE.equals(request.getAttribute(AJAX_REQUEST_HEADER_NAME));
	}
	
	public static String returnJson(HttpServletResponse response, Object data) {
		return returnText(response, JSONObject.toJSONString(data),"text/html;charset=UTF-8");
	}
	
	public static String returnText(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, "text/plain;charset=UTF-8");
	}
	
	public static String returnXml(HttpServletResponse response, Object data) {
		XStream xStream = new XStream();
		return returnText(response, xStream.toXML(data), "text/xml;charset=UTF-8");
	}
	
	public static String returnHtml(HttpServletResponse response, CharSequence text) {
		return returnText(response, text, "text/html;charset=UTF-8");
	}
	
	private static String returnText(HttpServletResponse response, CharSequence text,
			final String contenttype) {
		response.setContentType(contenttype);
		if (text != null) {
			try {
				response.getWriter().write(text.toString());
			} catch (IOException e) {
				// TODO
//				throw new AppException(e);
			}
		}
		return null;
	}
	
}
