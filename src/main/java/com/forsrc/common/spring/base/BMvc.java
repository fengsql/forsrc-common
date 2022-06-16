package com.forsrc.common.spring.base;

import com.forsrc.common.constant.Code;
import com.forsrc.common.reponse.ResponseBody;
import com.forsrc.common.tool.ToolJson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Data
@Slf4j
public class BMvc {
  
  private static final String contentType_default = "application/json;charset=UTF-8";
  private static final String charset_default = "UTF-8";

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  protected ResponseBody createResponseBody() {
    return newResponseBody();
  }

  protected ResponseBody createResponseBody(Object data) {
    ResponseBody responseBody = new ResponseBody();
    if (data == null) {
      responseBody.setSuccess(false);
      responseBody.setCode(Code.FAIL.getCode());
      return responseBody;
    }
    responseBody.setCode(Code.SUCCESS.getCode());
    if (data instanceof Boolean) {
      responseBody.setSuccess((Boolean) data);
      return responseBody;
    }
    responseBody.setSuccess(true);
    responseBody.setData(data);
    return responseBody;
  }

  protected ResponseBody createResponseBody(boolean success) {
    ResponseBody responseBody = newResponseBody();
    responseBody.setSuccess(success);
    if (!success) {
      responseBody.setCode(Code.SUCCESS.getCode());
    } else {
      responseBody.setCode(Code.FAIL.getCode());
    }
    return responseBody;
  }

  protected ResponseBody createResponseBody(boolean success, String message) {
    ResponseBody responseBody = newResponseBody();
    responseBody.setSuccess(success);
    if (!success) {
      responseBody.setCode(Code.SUCCESS.getCode());
    } else {
      responseBody.setCode(Code.FAIL.getCode());
    }
    responseBody.setMessage(message);
    return responseBody;
  }

  protected ResponseBody createResponseBody(int code, boolean success) {
    ResponseBody responseBody = newResponseBody();
    responseBody.setCode(code);
    responseBody.setSuccess(success);
    return responseBody;
  }

  protected ResponseBody createResponseBody(int code, boolean success, String message) {
    ResponseBody responseBody = newResponseBody();
    responseBody.setCode(code);
    responseBody.setSuccess(success);
    responseBody.setMessage(message);
    return responseBody;
  }

  protected final void response(HttpServletResponse response, ResponseBody responseBody) {
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
    } catch (IOException e) {
      log.error("writeResponse error!", e);
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }

  protected final void responseFail(HttpServletResponse response, int code) {
    ResponseBody responseBody = createResponseBody(code, false);
    response(response, responseBody);
  }

  protected final void responseFail(HttpServletResponse response) {
    ResponseBody responseBody = createResponseBody(false);
    response(response, responseBody);
  }

  // >>>----------------------- normal -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  private ResponseBody newResponseBody() {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setCode(Code.SUCCESS.getCode());
    responseBody.setSuccess(true);
    return responseBody;
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

}