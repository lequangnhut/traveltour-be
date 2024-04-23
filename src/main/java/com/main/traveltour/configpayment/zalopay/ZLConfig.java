package com.main.traveltour.configpayment.zalopay;

import java.util.HashMap;
import java.util.Map;

public class ZLConfig {

    public static final Map<String, String> config = new HashMap<String, String>() {{
        put("appid", "2554");
        put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
        put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
        put("endpoint", "https://sandbox.zalopay.com.vn/v001/tpe/createorder");
//        put("app_id", "2553");
//        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
//        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
//        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};
}
