package retrofit2.plus;

import okhttp3.HttpUrl;

/**
 * Created by liumeng on 1/18/16.
 */
public class RetrofitUtil {
    public static HttpUrl convertHttpsUrl(HttpUrl httpUrl) {
        if (!httpUrl.isHttps()) {
            return httpUrl.newBuilder().scheme("https").port(443).build();
        }
        return httpUrl;
    }
}
