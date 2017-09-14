package cn.dwxmp.core.filter;

import cn.dwxmp.core.support.HttpCode;
import cn.dwxmp.core.support.Token;
import cn.dwxmp.core.util.DataUtil;
import cn.dwxmp.core.util.InstanceUtil;
import cn.dwxmp.core.util.TokenUtil;
import cn.dwxmp.core.util.WebUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HuXinsheng
 * @since 2017年3月19日 上午10:21:59
 */
public class TokenFilter extends BaseFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(TokenFilter.class);

	// 白名单
	private List<String> whiteUrls;

	private int _size = 0;

	public void init(FilterConfig config) throws ServletException {
		// 读取文件
		String path = CsrfFilter.class.getResource("/").getFile();
		whiteUrls = readFile(path + "tokenWhite.txt");
		_size = null == whiteUrls ? 0 : whiteUrls.size();
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		if (isWhiteReq(request.getRequestURI())) {
			chain.doFilter(request, response);
		} else {
			String token = request.getHeader("UUID");
			if (StringUtils.isNotBlank(token)) {
				try {
					Token tokenInfo = TokenUtil.getTokenInfo(token);
					if (tokenInfo != null) {
						Long now = System.currentTimeMillis();
						if (now - tokenInfo.getTime() < 1000 * 60 * 30) {
							String value = tokenInfo.getValue();
							TokenUtil.setTokenInfo(token, value);
							WebUtil.saveCurrentUser(request, value);
						}
					}
				} catch (Exception e) {
					logger.error("token检查发生异常:", e);
				}
			}
			// 响应
			if (DataUtil.isEmpty(WebUtil.getCurrentUser(request))) {
				response.setContentType("text/html; charset=UTF-8");
				Map<String, Object> modelMap = InstanceUtil.newLinkedHashMap();
				modelMap.put("httpCode", HttpCode.UNAUTHORIZED.value());
				modelMap.put("msg", HttpCode.UNAUTHORIZED.msg());
				modelMap.put("timestamp", System.currentTimeMillis());
				PrintWriter out = response.getWriter();
				out.println(JSON.toJSONString(modelMap));
				out.flush();
				out.close();
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	public void destroy() {
	}

	/*
	 * 判断是否是白名单
	 */
	private boolean isWhiteReq(String requestUrl) {
		if (_size == 0) {
			return true;
		} else {
			for (String urlTemp : whiteUrls) {
				if (requestUrl.contains(urlTemp.toLowerCase())) {
					return true;
				}
			}
		}

		return false;
	}
}
