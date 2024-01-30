package com.main.traveltour.service.utils;

import java.awt.image.BufferedImage;

public interface QRCodeService {

    String generateQrCode(Object object);

    BufferedImage generateQRCodeImage(String text);
}
