package cn.dwxmp.core.support.weixin;

import cn.dwxmp.core.support.weixin.company.WeiXinCompanyOAuth;
import cn.dwxmp.core.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Promission {

    protected Logger logger = Logger.getLogger(getClass());

    /**
     * 检查手机访问权限
     */
    public static boolean mobileCheck(HttpServletRequest request, ModelMap modelMap)
            throws IOException {
        Object user = request.getSession().getAttribute("employee");
        String code = request.getParameter("code");// code
        String userId = "";
        if (user == null) { // 手机用 start
            Object o = request.getSession().getAttribute("userid");
            if (o == null) {
                if (code == null) {
                    userId = request.getParameter("userid");
                } else {
                    userId = WeiXinCompanyOAuth.getUserInfo(code, PropertiesUtil.getInt("AGENTID_GONGGAO"));
                }
            } else {
                userId = o.toString();
            }
            if (null == userId) {// 获取userid失败;
                return false;
            }
            // 手机用 end
        }
        modelMap.put("userid", userId);
        modelMap.put("code", code);
        return true;
    }
}
