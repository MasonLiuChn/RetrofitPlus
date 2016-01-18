package net.masonliu.retrofit2plus.demo;

import net.masonliu.retrofit2plus.demo.model.GitResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.plus.HTTPS;

/**
 * Created by liumeng on 1/18/16.
 */
public class RestApi {

    private static GitApiService gitApiService;
    private static String baseUrl = "http://api.github.com";

    public static GitApiService getApiService() {
        if (gitApiService == null) {
            OkHttpClient okClient = new OkHttpClient();
            //okClient = OkHttpClientUtil.ssl(okClient, context, "ddd");
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiService = client.create(GitApiService.class);
        }
        return gitApiService;
    }

    public interface GitApiService {

        @HTTPS
        @Headers("User-Agent: Retrofit2.0Tutorial-App")
        @GET("/search/users")
        Call<GitResult> getUsersByName(@Query("q") String name);
    }

}
