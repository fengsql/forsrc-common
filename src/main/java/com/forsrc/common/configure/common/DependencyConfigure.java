package com.forsrc.common.configure.common;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.forsrc.common.reponse.ResponseProcessor;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class DependencyConfigure {

  @Resource
  private RequestMappingHandlerAdapter adapter;

  @Bean
  public ResponseProcessor resourceResponseBodyProcessor() {
    List<HttpMessageConverter<?>> messageConverters = adapter.getMessageConverters();
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    //fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    fastConverter.setFastJsonConfig(fastJsonConfig);

    for (HttpMessageConverter<?> messageConverter : messageConverters) {
      if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
        int index = messageConverters.indexOf(messageConverter);
        messageConverters.set(index, fastConverter);
        break;
      }
    }
    return new ResponseProcessor(adapter.getMessageConverters(), adapter);
  }

//  @Bean
//  public ExceptionAdvHandler restHandlerExceptionResolver() {
//    return new ExceptionAdvHandler();
//  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(httpClientFactory());
  }

  @Bean
  public ClientHttpRequestFactory httpClientFactory() {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setConnectTimeout(5000);
    requestFactory.setReadTimeout(5000);
    return requestFactory;
  }

  @Bean
  MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name:}") String applicationName) {
    return (registry) -> registry.config().commonTags("application", applicationName);
  }

}
