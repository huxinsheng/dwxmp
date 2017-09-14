package cn.dwxmp.core.support.weixin.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

import static java.lang.String.*;


/**
 * @author HuXinsheng
 * @since 2017年2月3日 下午5:16:15
 */
public class WeiXinCompanyUpload {

    private static final Logger logger = LogManager.getLogger();

    public static String upload(File file) throws IOException {
        String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + WeiXinCompanyUtils.getToken()
                + "&type=file";
        // 定义数据分隔符
        String boundary = "------------7da2e536604c8";
        URL uploadUrl = new URL(urlStr);
        HttpsURLConnection uploadConn = (HttpsURLConnection) uploadUrl.openConnection();
        uploadConn.setDoOutput(true);
        uploadConn.setDoInput(true);
        uploadConn.setRequestMethod("POST");
        // 设置请求头Content-Type
        uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        // 获取媒体文件上传的输出流（往微信服务器写数据）
        OutputStream outputStream = uploadConn.getOutputStream();
        // 从请求头中获取内容类型
        String contentType = "text";
        // 根据内容类型判断文件扩展名
        String ext = file.getName().substring(file.getName().lastIndexOf("."));
        String name = file.getName();

        // 请求体开始
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        String aaa =
                format("Content-Disposition: form-data; name=\"media\"; filename=\"" + name + "." + "%s\"\r\n", ext);
        outputStream.write(aaa.getBytes());
        String bbb = format("Content-Type: %s\r\n\r\n", contentType);
        outputStream.write(bbb.getBytes());

        // 获取媒体文件的输入流（读取文件）
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[8096];
        int size;
        while ((size = bis.read(buf)) != -1) {
            // 将媒体文件写到输出流（往微信服务器写数据）
            outputStream.write(buf, 0, size);
        }
        // 请求体结束
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        outputStream.close();
        bis.close();


        // 获取媒体文件上传的输入流（从微信服务器读数据）
        InputStream inputStream = uploadConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder buffer = new StringBuilder();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        uploadConn.disconnect();

        logger.debug(buffer.toString());
        return buffer.toString();
    }

    public static String upload(String fileName, File file) throws IOException {
        String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=" + WeiXinCompanyUtils.getToken()
                + "&type=file";
        // 定义数据分隔符
        String boundary = "------------7da2e536604c8";
        URL uploadUrl = new URL(urlStr);
        HttpsURLConnection uploadConn = (HttpsURLConnection) uploadUrl.openConnection();
        uploadConn.setDoOutput(true);
        uploadConn.setDoInput(true);
        uploadConn.setRequestMethod("POST");
        // 设置请求头Content-Type
        uploadConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        // 获取媒体文件上传的输出流（往微信服务器写数据）
        OutputStream outputStream = uploadConn.getOutputStream();

        // 从请求头中获取内容类型
        String contentType = "text";
        // 根据内容类型判断文件扩展名
        @SuppressWarnings("unused")
        String[] f = fileName.split("\\.");
        // 请求体开始
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        // String aaa = String.format("Content-Disposition: form-data;
        // name=\"media\"; filename=\""+f[0]+"."+"%s\"\r\n", f[1]);
        String aaa = "Content-Disposition: form-data; name=\"media\"; filename=\"" + fileName + "\"\r\n";
        outputStream.write(aaa.getBytes());
        String bbb = format("Content-Type: %s\r\n\r\n", contentType);
        outputStream.write(bbb.getBytes());

        // 获取媒体文件的输入流（读取文件）
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[8096];
        int size;
        while ((size = bis.read(buf)) != -1) {
            // 将媒体文件写到输出流（往微信服务器写数据）
            outputStream.write(buf, 0, size);
        }
        // 请求体结束
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        outputStream.close();
        bis.close();

        // 获取媒体文件上传的输入流（从微信服务器读数据）
        InputStream inputStream = uploadConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder buffer = new StringBuilder();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        uploadConn.disconnect();

        logger.debug(buffer.toString());
        return buffer.toString();
    }
}
