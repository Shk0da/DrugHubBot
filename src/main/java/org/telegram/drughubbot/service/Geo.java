package org.telegram.drughubbot.service;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

public class Geo {

    private static final String LOGTAG = "GEO";

    public static String getCityNameByCoordinate(double lat, double lng) {
        String city = "";

        try {
            String completeURL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true&language=ru";
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(completeURL);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            JSONObject jsonObject = new JSONObject(responseString);

            if (jsonObject.getString("status").equals("OK")) {
                city = jsonObject
                        .getJSONArray("results")
                        .getJSONObject(1)
                        .getJSONArray("address_components")
                        .getJSONObject(2)
                        .getString("short_name");
            }

        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            city = "<не определен>";
        }

        return city;
    }

}
