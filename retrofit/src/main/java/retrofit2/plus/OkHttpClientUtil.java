package retrofit2.plus;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
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

    public static OkHttpClient getSSLClientIgnoreExpire(OkHttpClient client, Context context, String assetsSSLFileName) {
        InputStream inputStream = getStream(context, assetsSSLFileName);
        try {
            //Certificate
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = null;
            final String pubSub;
            final String pubIssuer;
            certificate = certificateFactory.generateCertificate(inputStream);
            Principal pubSubjectDN = ((X509Certificate) certificate).getSubjectDN();
            Principal pubIssuerDN = ((X509Certificate) certificate).getIssuerDN();
            pubSub = pubSubjectDN.getName();
            pubIssuer = pubIssuerDN.getName();

            Log.e("sssss", "--"+pubSubjectDN.getName());
            Log.e("sssss", "--"+pubIssuerDN.getName());

            // Create an SSLContext that uses our TrustManager
            final TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            /**
                             for (X509Certificate cert : chain) {
                             // Make sure that it hasn't expired.
                             cert.checkValidity();
                             // Verify the certificate's public key chain.
                             try {
                             cert.verify(((X509Certificate) ca).getPublicKey());
                             } catch (Exception e) {
                             e.printStackTrace();
                             }
                             }
                             */
                            //1、判断证书是否是本地信任列表里颁发的证书
                            try {
                                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                                tmf.init((KeyStore) null);
                                for (TrustManager trustManager : tmf.getTrustManagers()) {
                                    ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                                }
                            } catch (Exception e) {
                                throw new CertificateException(e);
                            }
                            //2、判断服务器证书 发布方的标识名  和 本地证书 发布方的标识名 是否一致
                            //3、判断服务器证书 主体的标识名  和 本地证书 主体的标识名 是否一致
                            //getIssuerDN()  获取证书的 issuer（发布方的标识名）值。
                            //getSubjectDN()  获取证书的 subject（主体的标识名）值。
                            Log.e("sssss", "server--"+chain[0].getSubjectDN().getName());
                            Log.e("sssss", "server--"+chain[0].getIssuerDN().getName());
                            if (!chain[0].getSubjectDN().getName().equals(pubSub)) {
                                throw new CertificateException("server's SubjectDN is not equals to client's SubjectDN");
                            }
                            if (!chain[0].getIssuerDN().getName().equals(pubIssuer)) {
                                throw new CertificateException("server's IssuerDN is not equals to client's IssuerDN");
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            //SSLContext  and SSLSocketFactory
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            //okhttpclient
            OkHttpClient.Builder builder = client.newBuilder();
            builder.sslSocketFactory(sslSocketFactory);
            return builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    public static OkHttpClient getTrustAllSSLClient(OkHttpClient client) {
        try {
            //Certificate

            //keystore

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustManagers = new TrustManager[]{
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

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = client.newBuilder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("verify", hostname);
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            return client;
        }
    }

    public static OkHttpClient getSSLClient(OkHttpClient client, Context context, String assetsSSLFileName) {
        InputStream inputStream = getStream(context, assetsSSLFileName);
        return getSSLClientByInputStream(client, inputStream);
    }

    public static OkHttpClient getSSLClientByCertificateString(OkHttpClient client, String certificate) {
        InputStream inputStream = getStream(certificate);
        return getSSLClientByInputStream(client, inputStream);
    }

    private static InputStream getStream(Context context, String assetsFileName) {
        try {
            return context.getAssets().open(assetsFileName);
        } catch (Exception var3) {
            return null;
        }
    }

    private static InputStream getStream(String certificate) {
        try {
            return new ByteArrayInputStream(certificate.getBytes("UTF-8"));
        } catch (Exception var3) {
            return null;
        }
    }

    private static OkHttpClient getSSLClientByInputStream(OkHttpClient client, InputStream inputStream) {
        if (inputStream != null) {
            SSLSocketFactory sslSocketFactory = setCertificates(inputStream);
            if (sslSocketFactory != null) {
                client = client.newBuilder().sslSocketFactory(sslSocketFactory).build();
            }
        }
        return client;
    }

    private static SSLSocketFactory setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
