package cn.dwxmp.core.support.weixin;

import cn.dwxmp.core.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:11:18
 */
public class WeiXinUtils {

    private static final Logger logger = LogManager.getLogger();

    private static String token;
    private static Long tokenExpire;
    private static Long tokenTime;
    private static String grantType = "client_credential";

    private static void initToken() {
        if (tokenTime == null || tokenExpire == null || System.currentTimeMillis() - tokenTime >= tokenExpire) {
            String uriString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grantType + "&appid="
                    + PropertiesUtil.getString("WX_PUBLIC_APPID") + "&secret="
                    + PropertiesUtil.getString("WX_PUBLIC_SECRET");
            try {
                URL url = new URL(uriString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
                int responseInt = inputStreamReader.read();
                StringBuilder stringBuffer = new StringBuilder();
                while (responseInt != -1) {
                    stringBuffer.append((char) responseInt);
                    responseInt = inputStreamReader.read();
                }
                String tokenString = stringBuffer.toString();
                JSONObject jsonObject = JSON.parseObject(tokenString);
                if (jsonObject.containsKey("access_token")) {
                    tokenTime = System.currentTimeMillis();
                    token = jsonObject.getString("access_token");
                    tokenExpire = jsonObject.getLong("expires_in");
                } else {
                    // TODO 验证错误
                    logger.error(jsonObject.get("errcode"));
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public static String getToken() {
        initToken();
        return token;
    }
}
