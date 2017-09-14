package cn.dwxmp.core.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuXinsheng
 * @since 2017年3月19日 上午10:21:59
 */
public abstract class BaseFilter {
    private static final Logger logger = LogManager.getLogger(BaseFilter.class);
    /**
     * 读取白名单
     *
     * @param fileName
     * @return
     */
    protected List<String> readFile(String fileName) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        FileInputStream fis = null;
        try {
            File f = new File(fileName);
            if (f.isFile() && f.exists()) {
                fis = new FileInputStream(f);
                reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!"".equals(line)) {
                        list.add(line);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("readFile", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("InputStream关闭异常", e);
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                logger.error("FileInputStream关闭异常", e);
            }
        }
        return list;
    }
}
