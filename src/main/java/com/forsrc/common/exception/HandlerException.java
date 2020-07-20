package com.forsrc.common.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.forsrc.common.constant.Code;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class HandlerException {

  @ExceptionHandler(Exception.class)
  private ModelAndView handleException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      return getView(exception, ((CommonException) exception).getCode(), ((CommonException) exception).getMessage());
    }
    if (exception instanceof MissingServletRequestParameterException) {
      return getView(exception, "MissingServletRequestParameterException");
    }
    if (exception instanceof IllegalArgumentException) {
      return getView(exception, "IllegalArgumentException");
    }
    if (exception instanceof HttpMessageNotReadableException) {
      return getView(exception, "HttpMessageNotReadableException");
    }
    return getView(exception);
  }

  private ModelAndView getView(Exception exception, Integer code, String message) {
    message = getMessage(exception, message);
    FastJsonJsonView jsonView = new FastJsonJsonView();
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("code", code);
    attributes.put("message", message);
    attributes.put("success", false);
    jsonView.setAttributesMap(attributes);
    return new ModelAndView(jsonView);
  }

  private ModelAndView getView(Exception exception, String message) {
    return getView(exception, Code.FAIL.getCode(), message);
  }

  private ModelAndView getView(Exception exception) {
    return getView(exception, Code.FAIL.getCode(), null);
  }

  private String getMessage(Exception exception, String message) {
    if (message == null) {
      return exception.getMessage();
    }
//    message += ": " + exception.getMessage();
    return message;
  }

}