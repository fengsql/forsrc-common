package com.forsrc.common.reponse;

import com.forsrc.common.tool.ToolResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ResponseProcessor extends RequestResponseBodyMethodProcessor implements InitializingBean {

  private RequestMappingHandlerAdapter adapter;

  public ResponseProcessor(List<HttpMessageConverter<?>> converters, RequestMappingHandlerAdapter adapter) {
    super(converters);
    this.adapter = adapter;
  }

  @Override
  public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
    String reqUri = null;
    if (webRequest instanceof ServletWebRequest) {
      reqUri = ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }
    if (reqUri.contains("actuator/prometheus")) {
      super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
      return;
    }

//    HashMap<String, Object> jsonMap = new HashMap<>();
//    jsonMap.put("code", Code.SUCCESS.getCode());
//    jsonMap.put("success", true);
//    jsonMap.put("message", Code.SUCCESS.getMsg());
//    jsonMap.put("data", returnValue);

    ResponseBody responseBody = ToolResponse.getResponse(returnValue);
    super.handleReturnValue(responseBody, returnType, mavContainer, webRequest);
  }

  public void afterPropertiesSet() {
    List<HandlerMethodReturnValueHandler> handlers = Lists.newArrayList(this.adapter.getReturnValueHandlers());
    Iterator var2 = handlers.iterator();

    while (var2.hasNext()) {
      HandlerMethodReturnValueHandler handler = (HandlerMethodReturnValueHandler) var2.next();
      if (handler instanceof RequestResponseBodyMethodProcessor) {
        int index = handlers.indexOf(handler);
        handlers.set(index, this);
        break;
      }
    }

    this.adapter.setReturnValueHandlers(handlers);
  }

}