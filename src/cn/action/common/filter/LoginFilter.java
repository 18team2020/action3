package cn.action.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.action.common.utils.UserUtils;
import cn.action.common.web.SverResponse;
import cn.action.modules.sys.entity.User;

public class LoginFilter implements Filter {

	private String permitUrls[] = null;
	private String gotoUrl = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String permitUrls = filterConfig.getInitParameter("permitUrls");
		String gotoUrl = filterConfig.getInitParameter("gotoUrl");
		this.gotoUrl = gotoUrl;
		if (permitUrls != null && permitUrls.length() > 0) {
			this.permitUrls = permitUrls.split(",");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest res = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (!isPermitUrl(request)) {
			if (filterCurrUrl(request)) {
				System.out.println("--->请登录");
				resp.sendRedirect(res.getContextPath() + gotoUrl);
				return;
			}
		}
		System.out.println("--->允许访问");
		chain.doFilter(request, response);
	}

	public String test(HttpServletResponse response, String accept) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();// 构建jackson mapper对象
		String json = objectMapper.writeValueAsString(SverResponse.createRespBySuccessMessage("请登录"));// json转为字符串
		response.setCharacterEncoding("UTF-8");// 设置响应头
		response.setContentType("application/json; charset=utf-8");// 设置响应类型
		PrintWriter out = response.getWriter();// 获取输出流
		out.append(json);// 输出json
		return "";
	}

	@Override
	public void destroy() {
		permitUrls = null;
		gotoUrl = null;
	}

	public boolean filterCurrUrl(ServletRequest request) {
		boolean filter = false;
		HttpServletRequest res = (HttpServletRequest) request;
		User user = (User) res.getSession().getAttribute(UserUtils.CURRENT_USER);
		if (null == user)
			filter = true;
		return filter;
	}

	public boolean isPermitUrl(ServletRequest request) {
		boolean isPermit = false;
		String currentUrl = currentUrl(request);
		if (permitUrls != null && permitUrls.length > 0) {
			for (int i = 0; i < permitUrls.length; i++) {
				if (currentUrl.startsWith(permitUrls[i])) {
					isPermit = true;
					break;
				}
			}
		}
		return isPermit;
	}

	// 请求地址
	public String currentUrl(ServletRequest request) {
		HttpServletRequest res = (HttpServletRequest) request;
		String task = request.getParameter("task");
		String path = res.getContextPath();
		String uri = res.getRequestURI();
		if (task != null) {// uri格式 xx/ser
			uri = uri.substring(path.length(), uri.length()) + "?" + "task=" + task;
		} else {
			uri = uri.substring(path.length(), uri.length());
		}
		System.out.println("当前请求地址:" + uri);
		return uri;
	}
}
