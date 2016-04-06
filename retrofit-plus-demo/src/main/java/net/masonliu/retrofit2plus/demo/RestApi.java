package net.masonliu.retrofit2plus.demo;

import android.content.Context;

import net.masonliu.retrofit2plus.demo.model.GitResult;

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
            "MIIFSzCCBDOgAwIBAgIQDXBCOWMCxia8htXPzVA77zANBgkqhkiG9w0BAQsFADBw\n" +
            "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
            "d3cuZGlnaWNlcnQuY29tMS8wLQYDVQQDEyZEaWdpQ2VydCBTSEEyIEhpZ2ggQXNz\n" +
            "dXJhbmNlIFNlcnZlciBDQTAeFw0xNDA0MDgwMDAwMDBaFw0xNzA0MTIxMjAwMDBa\n" +
            "MGgxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\n" +
            "YW4gRnJhbmNpc2NvMRUwEwYDVQQKEwxHaXRIdWIsIEluYy4xFTATBgNVBAMMDCou\n" +
            "Z2l0aHViLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAN7p2kz/\n" +
            "Al3iu4vkThCP+Cv5//FkkMMisICBjIu0FctWdnQZ4TG7m6WMLdf/Ty+mLV/hEkV5\n" +
            "mwYmI8vc/RZrr5L4sYddmgpXJ/GcFmneMeO7oOpB5FTrmDNwOdYA2vJUi4T4T0k+\n" +
            "9a7HCu/2dq5jZDSsfffs1WCbX2SR2kyafq3WpLiaoDnruMgfg6wg2PMoOXNGa1aA\n" +
            "SARrD1KjV424cV0frGAwbKCd7EbIKYmM9mdf1bxmUwPlVvS5f3jzmpUURvVuJWET\n" +
            "86R0JLUeQRjiYZ9MY+MEF4w24PzD6H6aJaLaDFOqQ8LCz7U7vffOo/7WgrcBgIro\n" +
            "wotvmsEveE8rtkkCAwEAAaOCAecwggHjMB8GA1UdIwQYMBaAFFFo/5CvAgd1PMzZ\n" +
            "ZWRiohK4WXI7MB0GA1UdDgQWBBQeW60hetRWA5FotQYl9ZfjSXoMKjAjBgNVHREE\n" +
            "HDAaggwqLmdpdGh1Yi5jb22CCmdpdGh1Yi5jb20wDgYDVR0PAQH/BAQDAgWgMB0G\n" +
            "A1UdJQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjB1BgNVHR8EbjBsMDSgMqAwhi5o\n" +
            "dHRwOi8vY3JsMy5kaWdpY2VydC5jb20vc2hhMi1oYS1zZXJ2ZXItZzEuY3JsMDSg\n" +
            "MqAwhi5odHRwOi8vY3JsNC5kaWdpY2VydC5jb20vc2hhMi1oYS1zZXJ2ZXItZzEu\n" +
            "Y3JsMEIGA1UdIAQ7MDkwNwYJYIZIAYb9bAEBMCowKAYIKwYBBQUHAgEWHGh0dHBz\n" +
            "Oi8vd3d3LmRpZ2ljZXJ0LmNvbS9DUFMwgYMGCCsGAQUFBwEBBHcwdTAkBggrBgEF\n" +
            "BQcwAYYYaHR0cDovL29jc3AuZGlnaWNlcnQuY29tME0GCCsGAQUFBzAChkFodHRw\n" +
            "Oi8vY2FjZXJ0cy5kaWdpY2VydC5jb20vRGlnaUNlcnRTSEEySGlnaEFzc3VyYW5j\n" +
            "ZVNlcnZlckNBLmNydDAMBgNVHRMBAf8EAjAAMA0GCSqGSIb3DQEBCwUAA4IBAQAX\n" +
            "2PZ9vGZArhpwyg15ZCSCyLc7iqkW/lHj3q3jr+oQ6ZlUu6QL/gUsB5jfXUvUoLuk\n" +
            "XB/HCCdd41rbM7s+gLWFKaaPpcdL0dZLmcT2R8vjz3VdsDSsIoFce/9Abt1Ozjnh\n" +
            "oSqulEAyCbQcgyWdjIcbrC7++8xHUTztNEMQVsxL9clgXiZ7HCyhqKHVsrhlWoCX\n" +
            "gJQfCqcEPdeq7svwiWXO76V5u52s15vM9ziivHxYEAhB9MWD2vI9ThKxRhiVqHtK\n" +
            "A+DZBEFl12A0kKgRc3oCNHf/fwp/ShRoIwvZAmoZ+WYb3xEm3Di81MB4OH2Bh1JC\n" +
            "Xj4yL0JvP2bD1pGDF/Pk\n" +
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
        @GET("/search/users")
        void testHttps(@Query("q") String name,Callback<GitResult> callback);
    }

}
