package cn.dwxmp.core.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 加载配置
 *
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:19:19
 */
public final class Resources {
    protected static final Logger logger = LogManager.getLogger(Resources.class);
    /**
     * 第三方登录配置
     */
    @Resource
    public static ResourceBundle THIRDPARTY;
    //= ResourceBundle.getBundle(System.getProperty("spring.profiles.active") + "/thirdParty");
    /**
     * 国际化信息
     */
    private static final Map<String, ResourceBundle> MESSAGES = new HashMap<>();

    /**
     * 国际化信息
     */
    public static String getMessage(String key, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle message = MESSAGES.get(locale.getLanguage());
        if (message == null) {
            synchronized (MESSAGES) {
                message = MESSAGES.get(locale.getLanguage());
                if (message == null) {
                    message = ResourceBundle.getBundle("i18n/messages", locale);
                    MESSAGES.put(locale.getLanguage(), message);
                }
            }
        }
        if (params != null && params.length > 0) {
            return String.format(message.getString(key), params);
        }
        return message.getString(key);
    }

    /**
     * 清除国际化信息
     */
    public static void flushMessage() {
        MESSAGES.clear();
    }
}
