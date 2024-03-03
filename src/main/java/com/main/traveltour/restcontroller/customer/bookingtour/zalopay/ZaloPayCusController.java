package com.main.traveltour.restcontroller.customer.bookingtour.zalopay;

import com.main.traveltour.configpayment.zalopay.HMACUtil;
import com.main.traveltour.entity.ResponseObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class ZaloPayCusController {

    private static final Map<String, String> config = new HashMap<String, String>() {{
        put("appid", "2554");
        put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
        put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
        put("endpoint", "https://sandbox.zalopay.com.vn/v001/tpe/createorder");
    }};

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @PostMapping("zalopay/submit-payment")
    public ResponseObject createOrder(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey("amount") || !requestData.containsKey("description")) {
            new ResponseObject("404", "Dữ liệu không hợp lệ !", null);
        }

        int price = (int) requestData.get("amount");
        String bookingTourId = String.valueOf(requestData.get("bookingTourId"));

        try {
            final Map embeddata = new HashMap() {{
                put("preferred_payment_method", "[\"domestic_card\",  \"account\"]");
            }};

            final Map[] item = {
                    new HashMap() {{
                        put("itemid", "knb");
                        put("itemname", "kim nguyen bao");
                        put("itemprice", 198400);
                        put("itemquantity", 1);
                    }}
            };

            Map<String, Object> order = new HashMap<String, Object>() {{
                put("appid", config.get("appid"));
                put("apptransid", getCurrentTimeString("yyMMdd") + "_" + UUID.randomUUID()); // mã giao dich có định dạng yyMMdd_xxxx
                put("apptime", System.currentTimeMillis()); // miliseconds
                put("appuser", "demo");
                put("amount", price);
                put("description", "Thanh Toan Don Hang #" + bookingTourId);
                put("bankcode", "");
                put("item", new JSONObject(item).toString());
                put("embeddata", new JSONObject(embeddata).toString());
                put("callbackurl", "/api/v1/vnpay/success-payment");
            }};

            // appid +”|”+ apptransid +”|”+ appuser +”|”+ amount +"|" + apptime +”|”+ embeddata +"|" +item
            String data = order.get("appid") + "|" + order.get("apptransid") + "|" + order.get("appuser") + "|" + order.get("amount")
                    + "|" + order.get("apptime") + "|" + order.get("embeddata") + "|" + order.get("item");
            order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(config.get("endpoint"));

            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> e : order.entrySet()) {
                params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
            }

            // Content-Type: application/x-www-form-urlencoded
            post.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            return new ResponseObject("200", "Thành công", result.get("orderurl"));
        } catch (Exception e) {
            return new ResponseObject("404", "Dữ liệu không hợp lệ !", null);
        }
    }
}
