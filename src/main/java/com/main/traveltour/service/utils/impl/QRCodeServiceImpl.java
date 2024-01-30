package com.main.traveltour.service.utils.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.main.traveltour.service.utils.QRCodeService;
import com.main.traveltour.utils.QRCodeUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public String generateQrCode(Object object) {
        String prettyData = QRCodeUtils.prettyObject(object);
        return QRCodeUtils.generateQrCode(prettyData, 300, 300);
    }

    @Override
    public BufferedImage generateQRCodeImage(String text) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", stream);

            byte[] imageBytes = stream.toByteArray();
            return ImageIO.read(new ByteArrayInputStream(imageBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
