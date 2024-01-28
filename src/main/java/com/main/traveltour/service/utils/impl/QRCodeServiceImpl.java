package com.main.traveltour.service.utils.impl;

import com.main.traveltour.service.utils.QRCodeService;
import com.main.traveltour.utils.QRCodeUtils;
import org.springframework.stereotype.Service;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public String generateQrCode(Object object) {
        String prettyData = QRCodeUtils.prettyObject(object);
        return QRCodeUtils.generateQrCode(prettyData, 300, 300);
    }
}
