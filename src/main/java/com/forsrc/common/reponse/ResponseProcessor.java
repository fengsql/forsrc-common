package com.forsrc.common.reponse;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ResponseProcessor extends RequestResponseBodyMethodProcessor implements InitializingBean {

  private final RequestMappingHandlerAdapter adapter;
  private final IResponseHandler<?> responseHandler;

  public ResponseProcessor(List<HttpMessageConverter<?>> converters, RequestMappingHandlerAdapter adapter, IResponseHandler<?> responseHandler) {
    super(converters);
    this.adapter = adapter;
    this.responseHandler = responseHandler;
  }

  @Override
  public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
    //    String reqUri = null;
    //    if (webRequest instanceof ServletWebRequest) {
    //      reqUri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    //    }
    //    if (reqUri.contains("actuator/prometheus")) {
    //      super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    //      return;
    //    }
    //    log.info("handleReturnValue returnValue: {}", returnValue);
    Object responseBody = getResposeBody(returnValue);
    //    ResponseBody responseBody = ToolResponse.getResponse(returnValue);
    super.handleReturnValue(responseBody, returnType, mavContainer, webRequest);
  }

  private Object getResposeBody(Object returnValue) {
    return responseHandler.createResponse(true, HttpStatus.OK.value(), null, returnValue);
  }

  @Override
  public void afterPropertiesSet() {
    List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(Objects.requireNonNull(this.adapter.getReturnValueHandlers()));
    for (HandlerMethodReturnValueHandler handler : handlers) {
      if (handler instanceof RequestResponseBodyMethodProcessor) {
        int index = handlers.indexOf(handler);
        handlers.set(index, this);
        break;
      }
    }
    this.adapter.setReturnValueHandlers(handlers);
  }

}