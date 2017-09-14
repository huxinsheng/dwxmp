package cn.dwxmp.core.support.weixin.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:15:32
 */
public class WeiXinCompanyOAuth {
    private static final Logger logger = LogManager.getLogger();

    public static String getUserInfo(String code, int agentid) {
        String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + WeiXinCompanyUtils.getToken() +
//				"&code="+code+"&agentid="+CommonUtils.WX_QY_AGENT_TEST;
                "&code=" + code + "&agentid=" + agentid;
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setDoInput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            JSONObject jsonObject = JSON.parseObject(sb.toString());
            if (jsonObject.get("UserId") != null) {
                return jsonObject.get("UserId").toString();
            }
            reader.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
