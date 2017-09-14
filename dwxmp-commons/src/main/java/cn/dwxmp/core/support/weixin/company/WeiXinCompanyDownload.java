package cn.dwxmp.core.support.weixin.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 根据微信media_id下载工单图片
 *
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:13:06
 */
public class WeiXinCompanyDownload implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private String[] mediaIds;

    public WeiXinCompanyDownload(String[] mediaIds) {
        this.mediaIds = mediaIds;
    }

    @Override
    public void run() {
        int len = mediaIds.length;
        for (int i = 0; i < len; i++) {
            @SuppressWarnings("unused")
            String fileName = download(mediaIds[i]);
        }
    }

    public static String download(String mediaId) {
        FileOutputStream fos = null;
        InputStream input = null;
        try {
            String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token="
                    + WeiXinCompanyUtils.getToken() + "&media_id=" + mediaId;

            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoInput(true);

            String disposition = conn.getHeaderField("Content-disposition");
            if (disposition == null) {
                return null;
            }

            String[] s = disposition.split(";");

            String ss[] = s[1].trim().split("\\=");
            String fileName = ss[1].trim().replaceAll("\"", "");
            // fileName.getBytes("iso-8859-1");
            fileName = new String(fileName.getBytes("iso-8859-1"), "utf-8");

            String path = "/www/gd_image/" + fileName;
            File file = new File(path);
            boolean createFile = false;
            if (!file.exists()) {
                createFile = file.createNewFile();
            }
            if(!createFile)
                return null;
            try {
                fos = new FileOutputStream(file);
            } finally {
                if (null != fos)
                    fos.close();
            }
            input = conn.getInputStream();
            byte[] bytes = new byte[2048];
            int size;
            while ((size = input.read(bytes)) != -1) {
                fos.write(bytes, 0, size);
            }
            return fileName;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (null != input)
                    input.close();
            } catch (IOException | NullPointerException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }
}
