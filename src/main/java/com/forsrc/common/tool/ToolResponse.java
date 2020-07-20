package com.forsrc.common.tool;

import com.forsrc.common.constant.Code;
import com.forsrc.common.reponse.ResponseBody;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class ToolResponse {

  private static final String contentType_default = "application/json;charset=UTF-8";
  private static final String charset_default = "UTF-8";

  public static HttpServletRequest getHttpServletRequest() {
    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  }

  public static void write(HttpServletResponse response, Object data) {
    ResponseBody responseBody = getResponseSuccess(data);
    write(response, responseBody);
  }

  @SneakyThrows
  public static void write(HttpServletResponse response, ResponseBody responseBody) {
    String content = ToolJson.toJson(responseBody);
    if (response == null) {
      log.warn("response is null!");
      return;
    }
    //    log.info("response content: " + content);
    PrintWriter printWriter = null;
    try {
      response.setHeader("Cache-Control", "no-cache");
      response.setContentType(contentType_default);
      response.setCharacterEncoding(charset_default);
      printWriter = response.getWriter();
      //			content = java.net.URLEncoder.encode(content, "UTF-8");
      printWriter.write(content);
      printWriter.flush();
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  public static void error(HttpServletResponse response) {
    ResponseBody responseBody = getResponseError();
    write(response, responseBody);
  }

  public static void error(HttpServletResponse response, Exception exception) {
    ResponseBody responseBody = getResponseError(Code.FAIL.getCode(), exception.getMessage());
    write(response, responseBody);
  }

  public static void error(HttpServletResponse response, int code, String message) {
    ResponseBody responseBody = getResponseError(code, message);
    write(response, responseBody);
  }

  public static void error(HttpServletResponse response, Code code) {
    ResponseBody responseBody = getResponseByCode(code);
    write(response, responseBody);
  }

  public static ResponseBody getResponse(Object data) {
    return getResponseSuccess(data);
  }

  private static ResponseBody getResponseSuccess(Object data) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setCode(Code.SUCCESS.getCode());
    responseBody.setSuccess(true);
    responseBody.setMessage(Code.SUCCESS.getMsg());
    responseBody.setData(data);
    return responseBody;
  }

  private static ResponseBody getResponseError() {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setCode(Code.ERROR.getCode());
    responseBody.setSuccess(false);
    responseBody.setMessage(Code.ERROR.getMsg());
    return responseBody;
  }

  private static ResponseBody getResponseError(int code, String message) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setCode(code);
    responseBody.setSuccess(false);
    responseBody.setMessage(message);
    return responseBody;
  }

  private static ResponseBody getResponseByCode(Code code) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setCode(code.getCode());
    responseBody.setSuccess(false);
    responseBody.setMessage(code.getMsg());
    return responseBody;
  }

}
