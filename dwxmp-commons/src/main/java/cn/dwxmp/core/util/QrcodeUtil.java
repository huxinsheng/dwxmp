package cn.dwxmp.core.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 二维码工具类
 *
 * @author HuXinsheng
 * @since 2017年2月21日 下午1:30:29
 */
public class QrcodeUtil {

    static Logger logger = LogManager.getLogger(QrcodeUtil.class);

    public static String createQrcode(String dir, String _text) {
        String qrcodeFilePath = "";
        try {
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;
            String qrcodeFormat = "png";
            Map<EncodeHintType, String> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(_text, BarcodeFormat.QR_CODE, qrcodeWidth,
                    qrcodeHeight, hints);

            BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
            File qrcodeFile = new File(dir + "/" + UUID.randomUUID().toString() + "." + qrcodeFormat);
            ImageIO.write(image, qrcodeFormat, qrcodeFile);
            MatrixToImageWriter.writeToPath(bitMatrix, qrcodeFormat, qrcodeFile.toPath());
            qrcodeFilePath = qrcodeFile.getAbsolutePath();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return qrcodeFilePath;
    }

    public static String decodeQr(String filePath) {
        String retStr = "";
        if ("".equalsIgnoreCase(filePath) || null == filePath) {
            return "图片路径为空!";
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hintTypeObjectHashMap = new EnumMap<>(DecodeHintType.class);
            hintTypeObjectHashMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(bitmap, hintTypeObjectHashMap);
            retStr = result.getText();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retStr;
    }
}
