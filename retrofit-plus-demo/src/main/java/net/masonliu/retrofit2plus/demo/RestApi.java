package net.masonliu.retrofit2plus.demo;

import android.content.Context;

import net.masonliu.retrofit2plus.demo.model.GitResult;
import net.masonliu.retrofit2plus.demo.model.HttpsResult;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.plus.HTTPS;

/**
 * Created by liumeng on 1/18/16.
 */
public class RestApi {
    private static String certificateString = "-----BEGIN CERTIFICATE-----\n" +
            "MIIGaTCCBVGgAwIBAgIQN23AqZqLxKVCT8qo88qpAzANBgkqhkiG9w0BAQUFADCB\n" +
            "vDELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQL\n" +
            "ExZWZXJpU2lnbiBUcnVzdCBOZXR3b3JrMTswOQYDVQQLEzJUZXJtcyBvZiB1c2Ug\n" +
            "YXQgaHR0cHM6Ly93d3cudmVyaXNpZ24uY29tL3JwYSAoYykxMDE2MDQGA1UEAxMt\n" +
            "VmVyaVNpZ24gQ2xhc3MgMyBJbnRlcm5hdGlvbmFsIFNlcnZlciBDQSAtIEczMB4X\n" +
            "DTE1MDQxNjAwMDAwMFoXDTE2MDQxNjIzNTk1OVowgaoxCzAJBgNVBAYTAkNOMRAw\n" +
            "DgYDVQQIEwdiZWlqaW5nMRAwDgYDVQQHFAdiZWlqaW5nMTkwNwYDVQQKFDBCZWlK\n" +
            "aW5nIEJhaWR1IE5ldGNvbSBTY2llbmNlIFRlY2hub2xvZ3kgQ28uLCBMdGQxJTAj\n" +
            "BgNVBAsUHHNlcnZpY2Ugb3BlcmF0aW9uIGRlcGFydG1lbnQxFTATBgNVBAMUDHd3\n" +
            "dy5iYWlkdS5jbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANWCP46U\n" +
            "yot79anMBfFoTg8n6Fj7Jsa2NpvJdZ+NT6D+ZZEp7BdWQkUtHup3Ohu/Nmuy0D9I\n" +
            "sxCMV23hPMOf8Ha+xYmfP0ege81IMENnI7d2NkE4PRWD7rVWsL/OjUGNUm1Pbvz3\n" +
            "1PY28M9SZZLfy9uAlGspZ/r5TQy7TtiMt/Ol4tBA3PjcOrlL5ik6KJf7iNKm0gDE\n" +
            "rF37JUvuVQjFxRYb9R2O1L9Lvxff/5JmOJTCdTTff1Pt6mb/j/vlVyCCARFz7leN\n" +
            "1GUCfcSAhr4Ow/j5PNVO6sdJoL4JbLqgLl/7oaS8cGMc581LI64qSfaTAgTd3x9r\n" +
            "UEwvuHrdbXpXUCsCAwEAAaOCAnUwggJxMIIBGgYDVR0RBIIBETCCAQ2CCGJhaWR1\n" +
            "LmNuggliYWlkdS5jb22CDGJhaWR1LmNvbS5jboILdy5iYWlkdS5jb22CDHd3LmJh\n" +
            "aWR1LmNvbYIMd3d3LmJhaWR1LmNughB3d3cuYmFpZHUuY29tLmNughB3d3cuYmFp\n" +
            "ZHUuY29tLmhrggx3d3cuYmFpZHUuaGuCEHd3dy5iYWlkdS5uZXQuYXWCEHd3dy5i\n" +
            "YWlkdS5uZXQubXmCEHd3dy5iYWlkdS5uZXQucGiCEHd3dy5iYWlkdS5uZXQucGuC\n" +
            "EHd3dy5iYWlkdS5uZXQudHeCEHd3dy5iYWlkdS5uZXQudm6CDnd3d3cuYmFpZHUu\n" +
            "Y29tghF3d3d3LmJhaWR1LmNvbS5jbjAJBgNVHRMEAjAAMA4GA1UdDwEB/wQEAwIF\n" +
            "oDAoBgNVHSUEITAfBggrBgEFBQcDAQYIKwYBBQUHAwIGCWCGSAGG+EIEATBlBgNV\n" +
            "HSAEXjBcMFoGCmCGSAGG+EUBBzYwTDAjBggrBgEFBQcCARYXaHR0cHM6Ly9kLnN5\n" +
            "bWNiLmNvbS9jcHMwJQYIKwYBBQUHAgIwGRoXaHR0cHM6Ly9kLnN5bWNiLmNvbS9y\n" +
            "cGEwHwYDVR0jBBgwFoAU15t82CKgFffdrV/OKZtYw7xGALUwKwYDVR0fBCQwIjAg\n" +
            "oB6gHIYaaHR0cDovL3NlLnN5bWNiLmNvbS9zZS5jcmwwVwYIKwYBBQUHAQEESzBJ\n" +
            "MB8GCCsGAQUFBzABhhNodHRwOi8vc2Uuc3ltY2QuY29tMCYGCCsGAQUFBzAChhpo\n" +
            "dHRwOi8vc2Uuc3ltY2IuY29tL3NlLmNydDANBgkqhkiG9w0BAQUFAAOCAQEAXm3C\n" +
            "4yOWxuG5h7KXjMjFzo7qzRN8GrkeENzm82GTaxwo3Op7WJv64DdreE9SSa3EHnXW\n" +
            "R72bvLU+f8Fq41Ptx24tBlv5tKtk1Z5/7hwIGJLRwh0i+OICKlW5tu71DhnJvZkD\n" +
            "/dEwfc6PygFTMEu4rLM8RlMSy9JM0FIGQeKoA2YfVd5rV/Z3dNb2y1YOMg1VE2EZ\n" +
            "23aKSXj1/dSd/KmSuoHI1dcOAnFXXRDJS37RejsOrq4sXiYhb7oOnErarVA9IpMs\n" +
            "cnM7TvlhUztnyzMpD1ABWjt/kqL2Wyx+CKfr4/Zd54i89fD+6jSMyS3iZIhtgXxd\n" +
            "CIhENC7RHoQqph7JJQ==\n" +
            "-----END CERTIFICATE-----";
    private static GitApiService gitApiService;
    private static String baseUrl = "http://api.github.com";
    public static GitApiService getApiService(Context context) {
        if (gitApiService == null) {
            OkHttpClient okClient = new OkHttpClient();
            //okClient = OkHttpClientUtil.getSSLClient(okClient, context, "Baidu.pem");
            //okClient = OkHttpClientUtil.getSSLClientByCertificateString(okClient,  certificateString);
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

        @HTTPS
        @Headers("User-Agent: Retrofit2.0Tutorial-App")
        @GET("/search/users")
        void getUsersByName2(@Query("q") String name, Callback<GitResult> callback);

        @HTTPS
        @GET("/settlement/bank/showBanks")
        void testHttps(Callback<HttpsResult> callback);
    }

}
