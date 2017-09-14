package cn.dwxmp.core.support.weixin.company;

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
 * @since 2017年2月3日 下午5:18:15
 */
public class WeiXinCompanyUtils {

	private static final Logger logger = LogManager.getLogger();

	private static String token;
	private static Long tokenExpire;
	private static Long tokenTime;

	private static void initToken() {
		if (tokenTime == null || tokenExpire == null || System.currentTimeMillis() - tokenTime >= tokenExpire
				|| token == null) {
			String uriString = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
			StringBuilder sb = new StringBuilder();
			sb.append(uriString);
			sb.append("?corpid=").append(PropertiesUtil.getString("WX_QY_CORPID"));
			sb.append("&corpsecret=").append(PropertiesUtil.getString("WX_QY_CORPSECRET"));

			try {
				logger.debug(sb);
				URL url = new URL(sb.toString());
				HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
				InputStreamReader inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
				int responseInt = inputStreamReader.read();
				StringBuilder stringBuffer = new StringBuilder();
				while (responseInt != -1) {
					stringBuffer.append((char) responseInt);
					responseInt = inputStreamReader.read();
				}
				String tokenString = stringBuffer.toString();
				logger.debug(stringBuffer);
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
