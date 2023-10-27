package com.forsrc.common.configure.common;

//import com.alibaba.fastjson2.support.config.FastJsonConfig;
//import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;

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
      //      .allowedOrigins("*")    // 允许跨域访问的源, 2.2
      .allowedOriginPatterns("*")    // 允许跨域访问的源，2.4
      .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")    // 允许请求方法
      .maxAge(168000)    // 预检间隔时间
      .allowedHeaders("*")  // 允许头部设置
      .allowCredentials(true);    // 是否发送cookie
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    List<MediaType> mediaTypes = new ArrayList<>();
    mediaTypes.add(MediaType.APPLICATION_JSON);
    mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
    mediaTypes.add(MediaType.APPLICATION_ATOM_XML);
    mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    mediaTypes.add(MediaType.APPLICATION_PDF);
    mediaTypes.add(MediaType.APPLICATION_RSS_XML);
    mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
    mediaTypes.add(MediaType.APPLICATION_XML);
    mediaTypes.add(MediaType.IMAGE_GIF);
    mediaTypes.add(MediaType.IMAGE_JPEG);
    mediaTypes.add(MediaType.IMAGE_PNG);
    mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
    mediaTypes.add(MediaType.TEXT_HTML);
    mediaTypes.add(MediaType.TEXT_MARKDOWN);
    mediaTypes.add(MediaType.TEXT_PLAIN);
    mediaTypes.add(MediaType.TEXT_XML);
    setFastJson(converters, mediaTypes);
  }

  private void setFastJson(List<HttpMessageConverter<?>> converters, List<MediaType> mediaTypes) {
    FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    converter.setSupportedMediaTypes(mediaTypes);
    converter.setDefaultCharset(StandardCharsets.UTF_8);
    FastJsonConfig config = new FastJsonConfig();
    config.setDateFormat("yyyy-MM-dd HH:mm:ss");
    //    JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //    config.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat  //格式化时间
    //      SerializerFeature.WriteNullBooleanAsFalse,  //
    //      SerializerFeature.WriteNullNumberAsZero,  //
    //      SerializerFeature.WriteNullStringAsEmpty,  //
    //      SerializerFeature.WriteMapNullValue,  //
    //      SerializerFeature.WriteNullListAsEmpty,  //
    //      SerializerFeature.WriteBigDecimalAsPlain   //
    //    );
    converter.setFastJsonConfig(config);
    converters.add(converter);
  }

}