package retrofit2.plus;

import android.content.Context;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by liumeng on 1/18/16.
 */
public class OkHttpClientUtil {
    private OkHttpClientUtil() {
    }

    public static OkHttpClient getTrustAllSSLClient(OkHttpClient client) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = client.newBuilder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            return client;
        }
    }

    public static OkHttpClient getSSLClient(OkHttpClient client, Context context, String assetsSSLFileName) {
        InputStream inputStream = trustedCertificatesInputStream(context, assetsSSLFileName);
        return getSSLClientByInputStream(client, inputStream);
    }

    public static OkHttpClient getSSLClientByCertificateString(OkHttpClient client, String certificate) {
        InputStream inputStream = trustedCertificatesInputStreamByCertificateString(certificate);
        return getSSLClientByInputStream(client, inputStream);
    }

    private static OkHttpClient getSSLClientByInputStream(OkHttpClient client, InputStream inputStream) {
        if (inputStream != null) {
            SSLContext sslContext = sslContextForTrustedCertificates(inputStream);
            if (sslContext != null) {
                client = client.newBuilder().sslSocketFactory(sslContext.getSocketFactory()).build();
            }
        }
        return client;
    }

    private static InputStream trustedCertificatesInputStream(Context context, String assetsFileName) {
        try {
            return context.getAssets().open(assetsFileName);
        } catch (Exception var3) {
            return null;
        }
    }

    private static InputStream trustedCertificatesInputStreamByCertificateString(String certificate) {
        try {
            return new ByteArrayInputStream(certificate.getBytes("UTF-8"));
        } catch (Exception var3) {
            return null;
        }
    }

    private static SSLContext sslContextForTrustedCertificates(InputStream in) {
        try {
            CertificateFactory e = CertificateFactory.getInstance("X.509");
            Collection certificates = e.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            } else {
                char[] password = "password".toCharArray();
                KeyStore keyStore = newEmptyKeyStore(password);
                int index = 0;
                Iterator keyManagerFactory = certificates.iterator();
                while (keyManagerFactory.hasNext()) {
                    Certificate trustManagerFactory = (Certificate) keyManagerFactory.next();
                    String sslContext = Integer.toString(index++);
                    keyStore.setCertificateEntry(sslContext, trustManagerFactory);
                }

                KeyManagerFactory var10 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                var10.init(keyStore, password);
                TrustManagerFactory var11 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                var11.init(keyStore);
                SSLContext var12 = SSLContext.getInstance("TLS");
                var12.init(var10.getKeyManagers(), var11.getTrustManagers(), new SecureRandom());
                return var12;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        return null;
    }

    private static KeyStore newEmptyKeyStore(char[] password) {
        try {
            KeyStore e = KeyStore.getInstance(KeyStore.getDefaultType());
            Object in = null;
            e.load((InputStream) in, password);
            return e;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return null;
    }

    /**
     * 由于okhttp header 中的 value 不支持 null, \n 和 中文这样的特殊字符,所以encode字符串
     *
     * @param value
     * @return
     */
    public static String getHeaderValueEncoded(String value) {
        if (TextUtils.isEmpty(value)) return " ";
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if ((c <= '\u001f' && c != '\t') || c >= '\u007f') {//根据源码okhttp允许[0020-007E]+\t的字符
                try {
                    return URLEncoder.encode(value, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return " ";
                }
            }
        }
        return value;
    }

    /**
     * 由于okhttp header 中的 name 不支持 null,空格、\t、 \n 和 中文这样的特殊字符,所以encode字符串
     */
    public static String getHeaderNameEncoded(String name) {
        if (TextUtils.isEmpty(name)) return "null";
        for (int i = 0, length = name.length(); i < length; i++) {
            char c = name.charAt(i);
            if (c <= '\u0020' || c >= '\u007f') {//根据源码okhttp允许[0021-007E]的字符
                try {
                    return URLEncoder.encode(name, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                    return " ";
                }
            }
        }
        return name;
    }
}
