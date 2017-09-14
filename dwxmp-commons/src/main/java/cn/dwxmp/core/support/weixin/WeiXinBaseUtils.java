package cn.dwxmp.core.support.weixin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.net.URL;

/**
 * Created by HuXinsheng on 15-5-18.
 */
public abstract class WeiXinBaseUtils {

    private static final Logger logger = LogManager.getLogger();

    public static String getResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            DataInputStream dataInputStream = new DataInputStream(httpsURLConnection.getInputStream());
            StringBuilder stringBuffer = new StringBuilder();
            int inputByte = dataInputStream.read();
            while (inputByte != -1) {
                stringBuffer.append((char) inputByte);
                inputByte = dataInputStream.read();
            }
            return stringBuffer.toString();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
}
