package cn.dwxmp.core.filter;

import cn.dwxmp.core.util.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CsrfFilter extends BaseFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(CsrfFilter.class);

    // 白名单
    private List<String> whiteUrls;

    private int _size = 0;

    public void init(FilterConfig filterConfig) {
        // 读取文件
        String path = CsrfFilter.class.getResource("/").getFile();
        whiteUrls = readFile(path + "csrfWhite.txt");
        _size = null == whiteUrls ? 0 : whiteUrls.size();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            // 获取请求url地址
            String url = req.getRequestURL().toString();
            String referurl = req.getHeader("Referer");
            if (isWhiteReq(referurl)) {
                chain.doFilter(request, response);
            } else {
                req.getRequestDispatcher("/").forward(req, res);

                // 记录跨站请求日志
                String log = "";
                String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String clientIp = WebUtil.getHost(req);

                log = "跨站请求---->>>" + clientIp + "||" + date + "||" + referurl + "||" + url;
                logger.warn(log);
            }

        } catch (Exception e) {
            logger.error("doFilter", e);
        }

    }

    /*
     * 判断是否是白名单
     */
    private boolean isWhiteReq(String referUrl) {
        String url = referUrl;
        if (url == null || "".equals(url) || _size == 0) {
            return true;
        } else {
            String refHost = "";
            url = url.toLowerCase();
            if (url.startsWith("http://")) {
                refHost = url.substring(7);
            } else if (url.startsWith("https://")) {
                refHost = url.substring(8);
            }

            for (String urlTemp : whiteUrls) {
                if (refHost.contains(urlTemp.toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void destroy() {
    }
}
