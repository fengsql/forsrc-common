package com.forsrc.common.annotation;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@Slf4j
public class RequestSingleResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(RequestSingle.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    RequestSingle requestSingle = parameter.getParameterAnnotation(RequestSingle.class);
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    BufferedReader reader = request.getReader();
    StringBuilder sb = new StringBuilder();
    char[] buf = new char[1024];
    int rd;
    while ((rd = reader.read(buf)) != -1) {
      sb.append(buf, 0, rd);
    }
    String source = sb.toString();
    JSONObject jsonObject = JSONObject.parseObject(source);
    String value = requestSingle.value();
    return jsonObject.get(value);
  }

}