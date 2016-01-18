package retrofit2.plus;

import okhttp3.HttpUrl;
import retrofit2.BaseUrl;

/**
 * Created by liumeng on 1/18/16.
 */
public class RetrofitUtil {
    public static BaseUrl convertBaseUrl(BaseUrl baseUrl, boolean isHttps) {
        if (isHttps && !baseUrl.url().isHttps()) {
            final HttpUrl newHttpUrl = baseUrl.url().newBuilder().scheme("https").port(443).build();
            return new BaseUrl() {
                @Override
                public HttpUrl url() {
                    return newHttpUrl;
                }
            };
        }
        return baseUrl;
    }
}
