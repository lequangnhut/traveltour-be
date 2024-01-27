package com.main.traveltour.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QRCodeUtils {

    public static String prettyObject(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String generateQrCode(String data, int width, int height) {
        StringBuilder builder = new StringBuilder();

        if (!data.isEmpty()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", bos);

                builder.append("data:image/png;base64,");
                builder.append(new String(Base64.getEncoder().encode(bos.toByteArray())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
