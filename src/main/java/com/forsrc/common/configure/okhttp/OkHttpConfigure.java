package com.forsrc.common.configure.okhttp;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfigure {

  @Value("${okhttp.connect-timeout:30}")
  private Integer connectTimeout;

  @Value("${okhttp.read-timeout:30}")
  private Integer readTimeout;

  @Value("${okhttp.write-timeout:30}")
  private Integer writeTimeout;

  @Value("${okhttp.max-idle-connections:200}")
  private Integer maxIdleConnections;

  @Value("${okhttp.keep-alive-duration:300}")
  private Long keepAliveDuration;

  @Value("${okhttp.proxy.enable:false}")
  private Boolean proxyEnable;

  @Value("${okhttp.proxy.host:}")
  private String host;

  @Value("${okhttp.proxy.port:}")
  private Integer port;

  @Bean
  public OkHttpClient okHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.sslSocketFactory(sslSocketFactory(), x509TrustManager())
      // 是否开启缓存
      .retryOnConnectionFailure(false) //
      .connectionPool(pool()) //
      .connectTimeout(connectTimeout, TimeUnit.SECONDS) //
      .readTimeout(readTimeout, TimeUnit.SECONDS) //
      .writeTimeout(writeTimeout, TimeUnit.SECONDS) //
      .hostnameVerifier(new CustomHostnameVerifier());

    if (proxyEnable) {
      Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
      builder.proxy(proxy);
    }
    return builder.build();
  }

  public class CustomHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
      return true;
    }
  }

  @Bean
  public X509TrustManager x509TrustManager() {
    return new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };
  }

  @Bean
  public SSLSocketFactory sslSocketFactory() {
    try {
      // 信任任何链接
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
      return sslContext.getSocketFactory();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Bean
  public ConnectionPool pool() {
    return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
  }
}