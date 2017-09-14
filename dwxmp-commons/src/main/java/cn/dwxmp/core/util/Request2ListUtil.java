package cn.dwxmp.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ShenHueJie
 * @version 2016年5月20日 下午3:28:02
 */
public final class Request2ListUtil {

    static Logger logger = LogManager.getLogger(QrcodeUtil.class);

    private Request2ListUtil() {
    }

    private static final Integer paramSize(Set<Method> methodSet, Map<String, String[]> stringMap) {
        Integer size = 0;
        for (Method method : methodSet) {
            String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
            Integer tempSize = 0;
            if (stringMap.containsKey(key)) {
                tempSize = stringMap.get(key).length;
            }
            if (tempSize > size)
                size = tempSize;
        }
        return size;
    }

    public static final <K> List<K> covert(Class<K> T, HttpServletRequest request) {
        List<K> objectList = new LinkedList<>();
        try {
            // 获取类的方法集合
            Set<Method> methodSet = get_declared_methods(T);
            Map<String, String[]> stringMap = request.getParameterMap();
            Integer valueSize = paramSize(methodSet, stringMap);
            logger.debug(T.getName() + " Max Length:" + valueSize);
            for (int i = 0; i < valueSize; i++) {
                K object = T.newInstance();
                for (Method method : methodSet) {
                    String key = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    String[] value = stringMap.get(key);
                    if (value != null && i < value.length) {
                        Class<?>[] type = method.getParameterTypes();
                        Object[] paramValue = new Object[]{TypeParseUtil.convert(value[i], type[0], null)};
                        method.invoke(object, paramValue);
                    }
                }
                objectList.add(object);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return objectList;
    }

    /**
     * 取自定义Set方法
     *
     * @param T
     * @return
     */
    private static final <T> Set<Method> get_declared_methods(Class<T> T) {
        Method[] methods = T.getDeclaredMethods();
        Set<Method> methodSet = new HashSet<>();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                methodSet.add(method);
            }
        }
        return methodSet;
    }
}
