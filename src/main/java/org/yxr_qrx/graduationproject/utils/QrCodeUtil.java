package org.yxr_qrx.graduationproject.utils;

import cn.hutool.core.codec.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @ClassName:QrCodeUtil
 * @Author:41713
 * @Date 2021/12/15  11:42
 * @Version 1.0
 **/
public class QrCodeUtil {

    //二维码宽度,单位：像素pixels
    private static final int QRCODE_WIDTH = 300;
    //二维码高度,单位：像素pixels
    private static final int QRCODE_HEIGHT = 300;
    //二维码logo路径
    private static final String QRCODE_LOGO="src/main/resources/repair.png";

    public static String createBase64QrCode(String content){
        try {
            HashMap<EncodeHintType,Comparable> hints = new HashMap<EncodeHintType,Comparable>();
            hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN,2);

            QRCodeWriter writer=new QRCodeWriter();
            //生成二维码图片
            BitMatrix bitMatrix=writer.encode(content, BarcodeFormat.QR_CODE,QRCODE_WIDTH,QRCODE_HEIGHT,hints);
            BufferedImage bufferedImage= MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",os);

            return "data:image/png;base64," + Base64.encode(os.toByteArray());

        } catch (IOException e) {
            return null;
        } catch (WriterException e) {
            return null;
        }
    }
}
