package org.telegram.drughubbot.command;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Map;

public class Boltalka extends Command {

    private static final String LOGTAG = "BOLTALKA";
    private static final int COUNT_JOKES = 100;
    private static final int CACHE_TIME = 36000;
    private static long START_CACHE = 0;
    private static JSONArray JOKS = null;

    @Override
    public String answer() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        String message;

        try {

            long unixTime = System.currentTimeMillis() / 1000L;
            Boolean needNewJoks = ((unixTime - START_CACHE) > CACHE_TIME);

            if (JOKS == null || needNewJoks) {
                String completeURL = "http://www.umori.li/api/get?site=bash.im&name=bash&num=" + COUNT_JOKES;
                CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
                HttpGet request = new HttpGet(completeURL);

                CloseableHttpResponse response = client.execute(request);
                HttpEntity ht = response.getEntity();

                BufferedHttpEntity buf = new BufferedHttpEntity(ht);
                String responseString = EntityUtils.toString(buf, "UTF-8");

                JOKS = new JSONArray(responseString);
                START_CACHE = unixTime;
            }

            int min = 1;
            int max = COUNT_JOKES;
            max -= min;
            int rnd = (int) (Math.random() * ++max) + min;

            message = JOKS.getJSONObject(rnd).getString("elementPureHtml");
            message = Jsoup.parse(message).text();

        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            message = "Мне нечего тебе сказать";
        }

        return message;
    }

    @Override
    public Map<String, String> getButtons() {
        return super.getButtons();
    }

    @Override
    public ReplyKeyboard getKeyboard() {
        return super.getKeyboard();
    }
}
