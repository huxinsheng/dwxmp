package cn.dwxmp.core.support.weixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:11:07
 */
public class WeiXinUserUtils extends WeiXinBaseUtils {

    private static final Logger logger = LogManager.getLogger();

    public static String getUserList(String next_openid) {
        String token = WeiXinUtils.getToken();
        if (token != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token;
            if (next_openid != null) {
                urlString = urlString + "&next_openid=" + next_openid;
            }
            return getResponse(urlString);
        }
        return null;
    }

    public static String getUserInfo(String openId) {
        String token = WeiXinUtils.getToken();
        if (token != null) {
            String urlString = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid="
                    + openId;
            try {
                URL url = new URL(urlString);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setDoInput(true);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpsURLConnection.getInputStream()));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return null;
    }
}
