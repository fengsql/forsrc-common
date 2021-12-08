package com.forsrc.common.exception;

import com.forsrc.common.reponse.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@ControllerAdvice
public class HandlerException {

  //  @ExceptionHandler(Exception.class)
  //  private ModelAndView handleException1(Exception exception) throws Exception {
  //    log.error(ExceptionUtils.getStackTrace(exception));
  //    if (exception instanceof CommonException) {
  //      return getView(exception, ((CommonException) exception).getCode(), ((CommonException) exception).getMessage());
  //    }
  //    if (exception instanceof MissingServletRequestParameterException) {
  //      return getView(exception, "MissingServletRequestParameterException");
  //    }
  //    if (exception instanceof IllegalArgumentException) {
  //      return getView(exception, "IllegalArgumentException");
  //    }
  //    if (exception instanceof HttpMessageNotReadableException) {
  //      return getView(exception, "HttpMessageNotReadableException");
  //    }
  //    if (exception instanceof ResponseStatusException) {
  //      throw exception;
  //    }
  //    return getView(exception);
  //  }

  @ExceptionHandler(Exception.class)
  private ResponseBody handleException(Exception exception) throws Exception {
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      return handleCommon(exception);
    }
    if (exception instanceof HttpException) {
      return handleHttp(exception);
    }
    if (exception instanceof BusinessException) {
      return handleBusiness(exception);
    }
    if (exception instanceof ResponseStatusException) {
      throw exception;
    }
    throw exception;
  }

  private ResponseBody handleCommon(Exception exception) {
    CommonException commonException = (CommonException) exception;
    return getResponseBody(exception, commonException.getCode(), commonException.getMessage(), null);
  }

  private ResponseBody handleHttp(Exception exception) {
    HttpException httpException = (HttpException) exception;
    throw new ResponseStatusException(httpException.getHttpStatus(), httpException.getMessage(), httpException.getCause());
  }

  private ResponseBody handleBusiness(Exception exception) {
    BusinessException businessException = (BusinessException) exception;
    return getResponseBody(exception, businessException.getCode(), businessException.getMessage(), businessException.getData());
  }

  private ResponseBody getResponseBody(Exception exception, Integer code, String message, Object data) {
    message = getMessage(exception, message);
    ResponseBody responseBody = new ResponseBody();
    responseBody.setSuccess(false);
    responseBody.setCode(code);
    responseBody.setMessage(message);
    responseBody.setData(data);
    return responseBody;
  }

  //  private ModelAndView getView(Exception exception, Integer code, String message) {
  //    message = getMessage(exception, message);
  //    FastJsonJsonView jsonView = new FastJsonJsonView();
  //    Map<String, Object> attributes = new HashMap<>();
  //    attributes.put("code", code);
  //    attributes.put("message", message);
  //    attributes.put("success", false);
  //    jsonView.setAttributesMap(attributes);
  //    return new ModelAndView(jsonView);
  //  }
  //
  //  private ModelAndView getView(Exception exception, String message) {
  //    return getView(exception, Code.FAIL.getCode(), message);
  //  }
  //
  //  private ModelAndView getView(Exception exception) {
  //    return getView(exception, Code.FAIL.getCode(), null);
  //  }

  private String getMessage(Exception exception, String message) {
    if (message == null) {
      return exception.getMessage();
    }
    return message;
  }

}