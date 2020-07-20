package com.forsrc.common.configure.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.forsrc.common.annotation.RequestSingleResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfigure implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new RequestSingleResolver());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")    // 允许跨域访问的路径
      .allowedOrigins("*")    // 允许跨域访问的源
      .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")    // 允许请求方法
      .maxAge(168000)    // 预检间隔时间
      .allowedHeaders("*")  // 允许头部设置
      .allowCredentials(true);    // 是否发送cookie
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    List<MediaType> supportedMediaTypes = new ArrayList<>();
    supportedMediaTypes.add(MediaType.APPLICATION_JSON);
    supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    supportedMediaTypes.add(MediaType.APPLICATION_PDF);
    supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
    supportedMediaTypes.add(MediaType.APPLICATION_XML);
    supportedMediaTypes.add(MediaType.IMAGE_GIF);
    supportedMediaTypes.add(MediaType.IMAGE_JPEG);
    supportedMediaTypes.add(MediaType.IMAGE_PNG);
    supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
    supportedMediaTypes.add(MediaType.TEXT_HTML);
    supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
    supportedMediaTypes.add(MediaType.TEXT_PLAIN);
    supportedMediaTypes.add(MediaType.TEXT_XML);
    converter.setSupportedMediaTypes(supportedMediaTypes);
    converter.setDefaultCharset(StandardCharsets.UTF_8);
    FastJsonConfig config = new FastJsonConfig();
    JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    config.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat);//格式化时间
    converter.setFastJsonConfig(config);
    converters.add(converter);
  }

}