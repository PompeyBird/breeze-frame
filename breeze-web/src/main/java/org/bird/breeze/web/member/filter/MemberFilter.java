package org.bird.breeze.web.member.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bird.breeze.web.base.util.HttpUtil;
import org.bird.breeze.web.member.bean.MenuBean;
import org.bird.breeze.web.member.constant.MemberConstant;
import org.bird.breeze.web.member.util.MenuUtil;

public class MemberFilter implements Filter {

    /** 需要排除（不拦截）的URL的正则表达式 */
    private Pattern exceptUrlPattern;
    
    /** 检查不通过时，转发的URL */
    private String forwardUrl;
    
//    private static final Logger log = Logger.getLogger(MemberFilter.class) ;

    public void init(FilterConfig cfg) throws ServletException {
        String excepUrlRegex = cfg.getInitParameter("excepUrlRegex");
        if (!StringUtils.isBlank(excepUrlRegex)) {
//        	exceptUrlPattern = WildcardPatternBuilder.build(excepUrlRegex);
        }
        forwardUrl = cfg.getInitParameter("forwardUrl");
//        errorUrl = cfg.getInitParameter("errorUrl");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String servletPath = request.getServletPath();
        // 如果请求的路径与forwardUrl相同，或请求的路径是排除的URL时，则直接放行
        if (exceptUrlPattern.matcher(servletPath).matches()) {
            chain.doFilter(req, res);
            return;
        }
        
        //检查session是否还在，跳转登录页面
        HttpSession session = request.getSession(false);
        if(null == session || null == session.getAttribute(MemberConstant.MEM_INFO)){
        	dispatchToForwardPage(request, response, null);
        	return;
        }
        
        List<MenuBean> menuList = MenuUtil.getMenus();
		request.setAttribute("menuList", menuList);
        String fullSrc = request.getServletPath();
		String queryStr = request.getQueryString();
		if(null != queryStr && "" != queryStr){
			fullSrc =  fullSrc + "?" +  queryStr;
		}
		MenuBean bean = MenuUtil.getMenuBeanBySrc(fullSrc);
		List<String> nvgList = null;
		if(null != bean){
			nvgList = bean.getNavigation();
		}
		request.setAttribute("nvgList", nvgList);
        
        chain.doFilter(req, res);
		return;
        
    }
    
    private void dispatchToForwardPage(HttpServletRequest request, HttpServletResponse response,String message) 
    		throws ServletException, IOException{
    	
//		request.getRequestDispatcher(forwardUrl).forward(request, response);
    	String contextPath = request.getContextPath();
        StringBuffer reqUrl = request.getRequestURL();
        String currentUrl = reqUrl.toString();
        String redirectUrl = currentUrl;
        Integer position = currentUrl.indexOf(";");
        if(position != -1){
        	redirectUrl = currentUrl.substring(0, position);
        }
        String redirect = redirectUrl + "?" + StringUtils.defaultString(request.getQueryString());
		String url = StringUtils.defaultIfEmpty(request.getContextPath() + forwardUrl, contextPath+"/")
				+ "?redirect=" + URLEncoder.encode(redirect, "UTF-8");
		
		if(!HttpUtil.isAjaxRequest(request)){
        	request.setAttribute("url",url);
    		request.getRequestDispatcher("/common.jsp").forward(request, response);
        }
		else{
			ajaxRedirect(response, request.getContextPath() + forwardUrl, message);
		}
    }

	public void destroy() {
    }
	
	private void ajaxRedirect(HttpServletResponse response, String url, String msg){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("filterFlag",true);
		returnMap.put("filterUrl", url);
		HttpUtil.returnJson(response, returnMap);
	}
}
