package com.forsrc.common.configure.okhttp;

import com.forsrc.common.reponse.IResponseHandler;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolJson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class BeanOkHttp {
  private static final int buffer_size = 1024;

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
  private static final MediaType BIN = MediaType.parse("application/octet-stream");

  @Resource
  private OkHttpClient okHttpClient;
  @Resource
  private IResponseHandler responseHandler;

  // <<----------------------- public -----------------------

  // <<<----------------------- post -----------------------

  public String post(String url, Map<String, String> params, Map<String, String> headers) {
    log.info("post param url: {}.", url);
    Request request = getRequest(url, params, headers);
    return requestBody(request);
  }

  public String post(String url, Map<String, String> params) {
    return post(url, params, null);
  }

  /**
   * post 请求, 请求数据为自定义 MediaType 类型。
   * @param url   请求url地址
   * @param param 请求数据, param 字符串
   * @return string
   */
  public String post(MediaType mediaType, String url, String param, Map<String, String> headers) {
    log.info("post url: {}.", url);
    Request request = getRequest(mediaType, url, param, headers);
    return requestBody(request);
  }

  /**
   * post 请求, 请求数据为 json 类型。
   * @param url  请求url地址
   * @param json 请求数据, json 字符串
   * @return string
   */
  public String postJson(String url, String json, Map<String, String> headers) {
    log.info("post json url: {}.", url);
    Request request = getRequest(JSON, url, json, headers);
    return requestBody(request);
  }

  /**
   * post 请求, 请求数据为 json 类型。
   * @param url  请求url地址
   * @param json 请求数据, json 字符串
   * @return string
   */
  public String postJson(String url, String json) {
    log.info("post json url: {}.", url);
    Request request = getRequest(JSON, url, json, null);
    return requestBody(request);
  }

  /**
   * post 请求, 请求数据为 xml 类型。
   * @param url   请求url地址
   * @param param 请求数据, param 字符串
   * @return string
   */
  public String postXml(String url, String param, Map<String, String> headers) {
    log.info("post xml url: {}.", url);
    Request request = getRequest(XML, url, param, headers);
    return requestBody(request);
  }

  /**
   * post 请求, 请求数据为 bin 类型。
   * @param url   请求url地址
   * @param param 请求数据
   * @return string
   */
  public String postBin(String url, byte[] param, Map<String, String> headers) {
    log.info("post bin url: {}.", url);
    Request request = getRequest(BIN, url, param, headers);
    return requestBody(request);
  }

  // >>>----------------------- post -----------------------

  // <<<----------------------- get -----------------------

  public String get(String url, Map<String, String> params, Map<String, String> headers) {
    log.info("get url: {}.", url);
    String param = toUrlParam(params);
    if (param != null) {
      url += param;
    }
    Request request = getRequest(url, headers);
    return requestBody(request);
  }

  public String get(String url) {
    log.info("get url: {}.", url);
    return get(url, null, null);
  }

  public String get(String url, Map<String, String> params) {
    log.info("get url: {}.", url);
    return get(url, params, null);
  }

  // >>>----------------------- get -----------------------

  // <<<----------------------- down -----------------------

  public byte[] down(String url, String json, Map<String, String> headers) {
    log.info("down url: {}.", url);
    Request request = getRequest(JSON, url, json, headers);
    return requestBytes(request);
  }

  public byte[] down(String url, String json) {
    return down(url, json, null);
  }

  // >>>----------------------- down -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- down -----------------------

  // >>----------------------- down -----------------------

  // <<----------------------- execute -----------------------

  private String requestBody(Request request) {
    Response response = null;
    try {
      response = execute(request);
      return getResponseBody(response);
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  private byte[] requestBytes(Request request) {
    Response response = null;
    try {
      response = execute(request);
      return getResponseBytes(response);
    } finally {
      if (response != null) {
        response.close();
      }
    }
  }

  private Response execute(Request request) {
    try {
      return okHttpClient.newCall(request).execute();
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  // >>----------------------- execute -----------------------

  // <<----------------------- getRequest -----------------------

  private Request getRequest(MediaType contentType, String url, String param, Map<String, String> headers) {
    RequestBody requestBody = RequestBody.create(contentType, param);
    return getRequest(url, requestBody, headers);
  }

  private Request getRequest(MediaType contentType, String url, byte[] param, Map<String, String> headers) {
    RequestBody requestBody = RequestBody.create(contentType, param);
    return getRequest(url, requestBody, headers);
  }

  private Request getRequest(String url, RequestBody requestBody, Map<String, String> headers) {
    Request.Builder builder = new Request.Builder();
    addHeader(builder, headers);
    return builder.url(url).post(requestBody).build();
  }

  private Request getRequest(String url, Map<String, String> params, Map<String, String> headers) {
    FormBody.Builder builder = new FormBody.Builder();
    addParam(builder, params);
    return getRequest(url, builder.build(), headers);
  }

  private Request getRequest(String url, Map<String, String> headers) {
    Request.Builder builder = new Request.Builder();
    addHeader(builder, headers);
    return builder.url(url).build();
  }

  // >>----------------------- getRequest -----------------------

  // <<----------------------- getResponse -----------------------

  @SneakyThrows
  private String getResponseBody(Response response) {
    if (response == null) {
      return null;
    }
    if (response.isSuccessful()) {
      return Tool.toString(response.body().string());
    }
    return getResponseFail(response);
  }

  @SneakyThrows
  private String getResponseFail(Response response) {
    if (response == null) {
      return null;
    }
    int code = response.code();
    String msg = Tool.toString(response.body().string());
    if (Tool.isNull(msg)) {
      msg = response.message();
    }
    Object object = responseHandler.createResponse(false, code, msg, null);
    return ToolJson.toJson(object);
  }

  @SneakyThrows
  private byte[] getResponseBytes(Response response) {
    if (response == null) {
      log.warn("down fail!");
      return null;
    }
    if (!response.isSuccessful()) {
      log.warn("down fail! {}", getResponseFail(response));
      return null;
    }
    InputStream inputStream = response.body().byteStream();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[buffer_size];
      int len;
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
      return outputStream.toByteArray();
    } finally {
      try {
        inputStream.close();
      } catch (Exception e) {
      }
      try {
        outputStream.close();
      } catch (Exception e) {
      }
    }
  }

  // >>----------------------- getResponse -----------------------

  // <<----------------------- tool -----------------------

  private void addHeader(Request.Builder builder, Map<String, String> headers) {
    if (headers == null || headers.keySet().size() == 0) {
      return;
    }
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();
      builder.addHeader(key, val);
    }
  }

  private String toUrlParam(Map<String, String> params) {
    if (params == null || params.keySet().size() == 0) {
      return null;
    }
    StringBuilder stringBuilder = new StringBuilder();
    boolean firstFlag = true;
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();
      if (firstFlag) {
        stringBuilder.append("?").append(key).append("=").append(val);
        firstFlag = false;
      } else {
        stringBuilder.append("&").append(key).append("=").append(val);
      }
    }
    return stringBuilder.toString();
  }

  private void addParam(FormBody.Builder builder, Map<String, String> params) {
    if (params == null || params.keySet().size() == 0) {
      return;
    }
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();
      builder.add(key, val);
    }
  }

  // >>----------------------- tool -----------------------

}