package com.forsrc.common.exception;

import com.forsrc.common.reponse.IResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
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

import javax.annotation.Resource;
import javax.validation.ValidationException;
import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class BaseExceptionHandler { //extends ResponseEntityExceptionHandler
  private static final int msg_length = 400;  //如果异常信息在 400 以内，直接显示为异常消息
  private static final String msg_error = "error";  // 当异常信息长度超过 msg_length 时，显示这个信息
  private static final String head_status = "status";  // head 中 message 名称，由于 body 中不能正常发送 message，所以暂时使用把 message 放入 head 中

  @Resource
  private IResponseHandler<?> responseHandler;

  @ExceptionHandler(Exception.class)
  @org.springframework.web.bind.annotation.ResponseBody
  protected ResponseEntity<?> handleException(Exception exception) throws Exception {
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      return handleCommon(exception);  //200
    }
    if (exception instanceof ErrorException) {
      return handleError(exception);  //500
    }
    if (exception instanceof RawException) {
      return handleRaw(exception);   //raw
    }
    if (exception instanceof SQLException) {
      return handleSQL(exception);  //500
    }
    if (exception instanceof IllegalArgumentException || exception instanceof BindException || exception instanceof ValidationException) {
      return handleIllegalArgument(exception);  //500
    }
    if (exception instanceof ResponseStatusException) {
      return handleResponseStatus(exception);  //raw
    }
    return handleUncaught(exception);   //raw or 500
  }

  private ResponseEntity<?> handleCommon(Exception exception) {
    log.info("handleCommon.");
    CommonException commonException = (CommonException) exception;
    String message = getMessage(exception, commonException.getMessage());
    return createResponseBody(HttpStatus.OK, commonException.getCode(), message);
  }

  private ResponseEntity<?> handleError(Exception exception) {
    log.info("handleError.");
    ErrorException errorException = (ErrorException) exception;
    String message = getMessage(exception, errorException.getMessage());
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, errorException.getCode(), message);
  }

  private ResponseEntity<?> handleRaw(Exception exception) {
    log.info("handleRaw.");
    RawException rawException = (RawException) exception;
    HttpStatus httpStatus = rawException.getHttpStatus();
    return createResponseBody(exception, httpStatus);
  }

  private ResponseEntity<?> handleSQL(Exception exception) {
    log.info("handleSQL.");
    return createResponseBody("SQL error!");
  }

  private ResponseEntity<?> handleIllegalArgument(Exception exception) {
    log.info("handleIllegalArgument.");
    String message = getMessage(exception);
    return createResponseBody(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), message);
  }

  private ResponseEntity<?> handleResponseStatus(Exception exception) {
    log.info("handleResponseStatus.");
    ResponseStatusException responseStatusException = (ResponseStatusException) exception;
    HttpStatus httpStatus = responseStatusException.getStatus();
    return createResponseBody(exception, httpStatus);
  }

  private ResponseEntity<?> handleUncaught(Exception exception) {
    log.info("handleUncaught.");
    String msg = getMessage(exception);
    HttpStatus httpStatus = getHttpStatus(exception);
    if (httpStatus != null) {
      return createResponseBody(httpStatus, msg);
    }
    return createResponseBody(msg);
  }

  protected ResponseEntity<?> createResponseBody(HttpStatus httpStatus, Integer code, String message) {
    Object responseBody = getResposeBody(httpStatus, code, message);
    HttpHeaders responseHeaders = getHttpHeaders(code);
    //    log.info("createResponseBody. msg: {}. body: {}", message, responseBody);
    return new ResponseEntity<>(responseBody, responseHeaders, httpStatus);
  }

  private HttpHeaders getHttpHeaders(Integer code) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add(head_status, String.valueOf(code));
    return responseHeaders;
  }

  protected ResponseEntity<?> createResponseBody(Exception exception, HttpStatus httpStatus) {
    return createResponseBody(httpStatus, getMessage(exception));
  }

  protected ResponseEntity<?> createResponseBody(HttpStatus httpStatus, String message) {
    return createResponseBody(httpStatus, null, message);
  }

  protected ResponseEntity<?> createResponseBody(String message) {
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, null, message);
  }

  private Object getResposeBody(HttpStatus httpStatus, Integer code, String message) {
    if (code == null) {
      code = httpStatus.value();
    }
    return responseHandler.createResponse(false, code, message, null);
  }

  private String getMessage(Exception exception, String message) {
    if (message != null) {
      return message;
    }
    return getMessage(exception);
  }

  private String getMessage(Exception exception) {
    String msg = exception.getMessage();
    if (msg != null && msg.length() <= msg_length) {
      return msg;
    }
    return msg_error;
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