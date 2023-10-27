package com.forsrc.common.exception;

import com.forsrc.common.reponse.IResponseHandler;
import com.forsrc.common.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
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

  @Resource
  private IResponseHandler<?> responseHandler;

  @ExceptionHandler(Exception.class)
  @org.springframework.web.bind.annotation.ResponseBody
  protected Object handleException(Exception exception) throws Exception {
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      return handleCommon(exception);  //500
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

  private Object handleCommon(Exception exception) {
    //    log.info("handleCommon.");
    CommonException commonException = (CommonException) exception;
    String message = getMessage(exception, commonException.getMessage());
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, commonException.getCode(), message);
  }

  private Object handleRaw(Exception exception) {
    //    log.info("handleRaw.");
    RawException rawException = (RawException) exception;
    HttpStatus httpStatus = rawException.getHttpStatus();
    return createResponseBody(exception, httpStatus);
  }

  private Object handleSQL(Exception exception) {
    //    log.info("handleSQL.");
    return createResponseBody("SQL error!");
  }

  private Object handleIllegalArgument(Exception exception) {
    //    log.info("handleIllegalArgument.");
    String message = getMessage(exception);
    return createResponseBody(HttpStatus.BAD_REQUEST, null, message);
  }

  private Object handleResponseStatus(Exception exception) {
    //    log.info("handleResponseStatus.");
    ResponseStatusException responseStatusException = (ResponseStatusException) exception;
    HttpStatus httpStatus = responseStatusException.getStatus();
    return createResponseBody(exception, httpStatus);
  }

  private Object handleUncaught(Exception exception) {
    //    log.info("handleUncaught.");
    String msg = getMessage(exception);
    HttpStatus httpStatus = getHttpStatus(exception);
    if (httpStatus != null) {
      return createResponseBody(httpStatus, msg);
    }
    return createResponseBody(msg);
  }

  protected Object createResponseBody(Exception exception, HttpStatus httpStatus) {
    return createResponseBody(httpStatus, getMessage(exception));
  }

  protected Object createResponseBody(HttpStatus httpStatus, String message) {
    return createResponseBody(httpStatus, null, message);
  }

  protected Object createResponseBody(String message) {
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, null, message);
  }

  protected Object createResponseBody(HttpStatus httpStatus, Integer code, String message) {
    Object responseBody = getResposeBody(httpStatus, code, message);
    //    log.info("createResponseBody. msg: {}. body: {}", message, responseBody);
    //    return new ResponseEntity<>(responseBody, httpStatus);
    return responseBody;
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
    return getStackTrace(exception);
  }

  private String getMessage(Exception exception) {
    //    String msg = exception.getMessage();
    //    if (msg != null && msg.length() <= msg_length) {
    //      return msg;
    //    }
    //    return msg_error;
    return ExceptionUtils.getStackTrace(exception);
  }

  private HttpStatus getHttpStatus(Exception ex) {
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

  private static String getStackTrace(Throwable throwable) {
    StringBuilder stringBuilder = new StringBuilder("\n").append(throwable);
    for (StackTraceElement traceElement : throwable.getStackTrace()) {
      String msg = traceElement.toString();
      msg = Tool.getSimpleText(msg);
      stringBuilder.append("\n\tat ").append(msg);
    }
    return stringBuilder.toString();
  }

}