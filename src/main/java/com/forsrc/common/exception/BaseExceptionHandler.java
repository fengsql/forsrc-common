package com.forsrc.common.exception;

import com.forsrc.common.reponse.ResponseBody;
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
public class BaseExceptionHandler { //extends ResponseEntityExceptionHandler

  @ExceptionHandler(Exception.class)
  @org.springframework.web.bind.annotation.ResponseBody
  protected ResponseEntity<?> handleException(Exception exception) throws Exception {
    log.error(ExceptionUtils.getStackTrace(exception));
    if (exception instanceof CommonException) {
      return handleCommon(exception);  //200
    }
    if (exception instanceof SQLException) {
      return handleSQLException(exception);  //500
    }
    if (exception instanceof ResponseStatusException) {
      return handleResponseStatusException(exception);  //raw
    }
    if (exception instanceof RawException) {
      return handleRawException(exception);   //raw
    }
    return handleUncaught(exception);   //raw or 500
  }

  private ResponseEntity<?> handleCommon(Exception exception) {
    CommonException commonException = (CommonException) exception;
    return createResponseBody(exception, commonException.getCode(), commonException.getMessage());
  }

  private ResponseEntity<?> handleSQLException(Exception exception) {
    return createResponseBody("SQL error!");
  }

  private ResponseEntity<?> handleResponseStatusException(Exception exception) {
    ResponseStatusException responseStatusException = (ResponseStatusException) exception;
    HttpStatus httpStatus = responseStatusException.getStatus();
    return createResponseBody(exception, httpStatus);
  }

  private ResponseEntity<?> handleRawException(Exception exception) {
    RawException rawException = (RawException) exception;
    HttpStatus httpStatus = rawException.getHttpStatus();
    return createResponseBody(exception, httpStatus);
  }

  private ResponseEntity<?> handleUncaught(Exception exception) {
    HttpStatus httpStatus = getHttpStatus(exception);
    if (httpStatus != null) {
      return createResponseBody(httpStatus, "Uncaught exception!");
    }
    return createResponseBody("Uncaught exception!");
  }

  protected ResponseEntity<?> createResponseBody(HttpStatus httpStatus, Integer code, String message) {
    ResponseBody responseBody = new ResponseBody();
    responseBody.setSuccess(false);
    responseBody.setCode(code);
    responseBody.setMessage(message);
    if (code == null) {
      responseBody.setCode(httpStatus.value());
    }
    //    String json = ToolJson.toJson(responseBody);
    return new ResponseEntity<>(responseBody, httpStatus);
  }

  protected ResponseEntity<?> createResponseBody(Exception exception, Integer code, String message) {
    message = getMessage(exception, message);
    return createResponseBody(HttpStatus.OK, code, message);
  }

  protected ResponseEntity<?> createResponseBody(Exception exception, HttpStatus httpStatus) {
    return createResponseBody(httpStatus, exception.getMessage());
  }

  protected ResponseEntity<?> createResponseBody(HttpStatus httpStatus, String message) {
    return createResponseBody(httpStatus, null, message);
  }

  protected ResponseEntity<?> createResponseBody(String message) {
    return createResponseBody(HttpStatus.INTERNAL_SERVER_ERROR, null, message);
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