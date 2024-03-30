package com.main.traveltour.configpayment.zalopay;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class ZaloPayUtil {

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public static Map<String, Object> createZaloPayOrder(int price, String bookingTourId) {
        Map<String, Object> order = new HashMap<>();
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

            order.put("appid", ZLConfig.config.get("appid"));
            order.put("apptransid", getCurrentTimeString("yyMMdd") + "_" + UUID.randomUUID());
            order.put("apptime", System.currentTimeMillis());
            order.put("appuser", "demo");
            order.put("amount", price);
            order.put("description", "Thanh Toan Don Hang #" + bookingTourId);
            order.put("bankcode", "");
            order.put("item", new JSONObject(item).toString());
            order.put("embeddata", new JSONObject(embeddata).toString());
            order.put("callback_url", "http://localhost:8080/api/v1/vnpay/success-payment");

            String data = order.get("appid") + "|" + order.get("apptransid") + "|" + order.get("appuser") + "|" + order.get("amount")
                    + "|" + order.get("apptime") + "|" + order.get("embeddata") + "|" + order.get("item");
            order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZLConfig.config.get("key1"), data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public static String submitPayment(Map<String, Object> order) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(ZLConfig.config.get("endpoint"));

            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> e : order.entrySet()) {
                params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
            }

            post.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());

            return result.get("orderurl").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
