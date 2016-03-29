package retrofit2.plus;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by liumeng on 1/18/16.
 */
public class OkHttpClientUtil {
    private OkHttpClientUtil() {
    }

    public static OkHttpClient getSSLClient(OkHttpClient client, Context context, String assetsSSLFileName) {
        InputStream inputStream = trustedCertificatesInputStream(context, assetsSSLFileName);
        if (inputStream != null) {
            SSLContext sslContext = sslContextForTrustedCertificates(inputStream);
            client = client.newBuilder().sslSocketFactory(sslContext.getSocketFactory()).build();
        }
        return client;
    }

    private static InputStream trustedCertificatesInputStream(Context context, String assetsFileName) {
        try {
            return context.getAssets().open(assetsFileName);
        } catch (IOException var3) {
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
        } catch (GeneralSecurityException var9) {
            throw new RuntimeException(var9);
        }
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore e = KeyStore.getInstance(KeyStore.getDefaultType());
            Object in = null;
            e.load((InputStream) in, password);
            return e;
        } catch (IOException var3) {
            throw new AssertionError(var3);
        }
    }
}
