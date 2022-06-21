package com.forsrc.common.tool;

import com.forsrc.common.constant.Code;
import com.forsrc.common.reponse.IResponseHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
@Slf4j
public class ToolResponse {

  private static final String contentType_default = "application/json;charset=UTF-8";
  private static final String charset_default = "UTF-8";

  private static ToolResponse toolResponse;
  @Resource
  private IResponseHandler<?> responseHandler;

  //  public static HttpServletRequest getHttpServletRequest() {
  //    return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  //  }

  @PostConstruct
  private void init() {
    toolResponse = this;
    toolResponse.responseHandler = this.responseHandler;
  }

  public static void writeData(HttpServletResponse response, Object data) {
    Object responseBody = getResponseSuccess(data);
    writeBody(response, responseBody);
  }

  @SneakyThrows
  public static void writeBody(HttpServletResponse response, Object responseBody) {
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
    Object responseBody = getResponseError();
    writeBody(response, responseBody);
  }

  public static void error(HttpServletResponse response, Exception exception) {
    Object responseBody = getResponseError(Code.FAIL.getCode(), exception.getMessage());
    writeBody(response, responseBody);
  }

  public static void error(HttpServletResponse response, int code, String message) {
    Object responseBody = getResponseError(code, message);
    writeBody(response, responseBody);
  }

  public static void error(HttpServletResponse response, Code code) {
    Object responseBody = getResponse(false, code.getCode(), code.getMsg(), null);
    writeBody(response, responseBody);
  }

  public static Object getResponse(Object data) {
    return getResponseSuccess(data);
  }

  public static Object getResponse(boolean success, int code, String message, Object data) {
    return toolResponse.responseHandler.createResponse(success, code, message, data);
  }

  public static Object getResponse(boolean success, int code, String message) {
    return getResponse(success, code, message, null);
  }

  public static Object getResponseSuccess(int code, String message) {
    return getResponse(true, code, message, null);
  }

  private static Object getResponseSuccess(Object data) {
    return getResponse(true, HttpStatus.OK.value(), null, data);
  }

  private static Object getResponseError() {
    return getResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
  }

  private static Object getResponseError(int code, String message) {
    return getResponse(false, code, message, null);
  }

}
