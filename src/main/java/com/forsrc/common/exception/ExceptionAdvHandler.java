package com.forsrc.common.exception;

import com.forsrc.common.constant.Code;
import com.forsrc.common.reponse.ResponseBody;
import com.forsrc.common.tool.ToolJson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvHandler { //extends ResponseEntityExceptionHandler

  @ExceptionHandler(Exception.class)
  @org.springframework.web.bind.annotation.ResponseBody
  protected ResponseEntity<?> handleException(Exception exception) throws Exception {
    log.info("handleException");
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      log.info("handleException handleCommon");
      return handleCommon(exception);
    }
    if (exception instanceof HttpException) {
      log.info("handleException handleHttp");
      return handleHttp(exception);
    }
    if (exception instanceof BusinessException) {
      log.info("handleException handleBusiness");
      return handleBusiness(exception);
    }
    if (exception instanceof ResponseStatusException) {
      log.info("handleException ResponseStatusException");
      throw exception;
    }
    if (exception instanceof SQLException) {
      log.info("handleException handleSQLException");
      return handleSQLException(exception);
    }
    if (exception instanceof SystemException) {
      log.info("handleException SystemException");
      throw exception;
    }
    log.info("handleException handleOther");
    return handleOther(exception);
  }

  private ResponseEntity<?> handleOther(Exception exception) {
    HttpStatus httpStatus = getHttpStatus(exception);
    if (httpStatus != null) {
      return getResponseBody(exception, httpStatus.value());
    }
    return handUncaught(exception);
  }

  private ResponseEntity<?> handUncaught(Exception exception) {
    return createResponseBody("Uncaught exception!");
  }

  private ResponseEntity<?> handleCommon(Exception exception) {
    CommonException commonException = (CommonException) exception;
    return getResponseBody(exception, commonException.getCode(), commonException.getMessage(), null);
  }

  private ResponseEntity<?> handleHttp(Exception exception) {
    HttpException httpException = (HttpException) exception;
    throw new ResponseStatusException(httpException.getHttpStatus(), httpException.getMessage(), httpException.getCause());
  }

  private ResponseEntity<?> handleBusiness(Exception exception) {
    BusinessException businessException = (BusinessException) exception;
    return getResponseBody(exception, businessException.getCode(), businessException.getMessage(), businessException.getData());
  }

  private ResponseEntity<?> handleSQLException(Exception exception) {
    return createResponseBody(Code.FAIL.getCode(), "SQL error!");
  }

  private ResponseEntity<?> getResponseBody(Exception exception, Integer code, String message, Object data) {
    message = getMessage(exception, message);
    return createResponseBody(code, message, data);
  }

  private ResponseEntity<?> getResponseBody(Exception exception, Integer code) {
    return createResponseBody(code, exception.getMessage());
  }

  private ResponseEntity<?> createResponseBody(HttpStatus httpStatus, Integer code, String message, Object data) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setSuccess(false);
    responseBody.setCode(code);
    responseBody.setMessage(message);
    responseBody.setData(data);
    if (code == null) {
      responseBody.setCode(httpStatus.value());
    }
    String json = ToolJson.toJson(responseBody);
    ResponseEntity<?> responseEntity = new ResponseEntity<>(json, httpStatus);
    return responseEntity;
  }

  private ResponseEntity<?> createResponseBody(Integer code, String message, Object data) {
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, code, message, data);
  }

  private ResponseEntity<?> createResponseBody(Integer code, String message) {
    return createResponseBody(code, message, null);
  }

  private ResponseEntity<?> createResponseBody(HttpStatus httpStatus, String message) {
    return createResponseBody(httpStatus, null, message, null);
  }

  private ResponseEntity<?> createResponseBody(String message) {
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, null, message, null);
  }

  private String getMessage(Exception exception, String message) {
    if (message == null) {
      return exception.getMessage();
    }
    return message;
  }

  private final HttpStatus getHttpStatus(Exception ex) {
    HttpStatus status;
    if (ex instanceof HttpRequestMethodNotSupportedException) {
      status = HttpStatus.METHOD_NOT_ALLOWED;
    } else if (ex instanceof HttpMediaTypeNotSupportedException) {
      status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
      status = HttpStatus.NOT_ACCEPTABLE;
    } else if (ex instanceof MissingPathVariableException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } else if (ex instanceof MissingServletRequestParameterException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof ServletRequestBindingException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof ConversionNotSupportedException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } else if (ex instanceof TypeMismatchException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof HttpMessageNotReadableException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof HttpMessageNotWritableException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } else if (ex instanceof MethodArgumentNotValidException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof MissingServletRequestPartException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof BindException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof NoHandlerFoundException) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex instanceof AsyncRequestTimeoutException) {
      status = HttpStatus.SERVICE_UNAVAILABLE;
    } else if (ex instanceof RuntimeException) {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    } else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return status;
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

}