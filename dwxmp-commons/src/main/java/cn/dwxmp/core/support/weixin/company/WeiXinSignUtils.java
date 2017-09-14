package cn.dwxmp.core.support.weixin.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:19:09
 */
public class WeiXinSignUtils {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        String jsapiTicket = "jsapi_ticket";
        String url = "http://gd.hnxinshan.com";
        Map<String, String> ret = sign(jsapiTicket, url);
        for (Map.Entry<String, String> entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }

    /**
     * @param jsapiTicket
     * @param url          url具体到访问页面 http://xxxx.com/xxx/xxx.html
     * @return
     */
    public static Map<String, String> sign(String jsapiTicket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        string1 = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
