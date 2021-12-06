package com.forsrc.common.configure.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class RestTemplateConfigure {

  @Resource
  RestTemplate restTemplate;

  //  @Bean
  //  public RestTemplate restTemplate(RestTemplateBuilder builder) {
  //    return builder.build();
  //  }

  public <T> T get(String url, Class<T> returnType, Object... uriVariables) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity(headers);
    ResponseEntity<T> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, returnType, uriVariables);

    return responseEntity.getBody();
  }

  public <T> T get(String url, MediaType mediaType, Class<T> returnType, Object... uriVariables) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(mediaType);
    HttpEntity<String> requestEntity = new HttpEntity(headers);
    ResponseEntity<T> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, returnType, uriVariables);
    return responseEntity.getBody();
  }

  public <T> T post(String url, String params, Class<T> returnType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity(params, headers);
    ResponseEntity<T> response = this.restTemplate.postForEntity(url, requestEntity, returnType, new Object[0]);
    return response.getBody();
  }

  //  public <T> ResponseEntity<T> post(String url, String params, LinkedHashMap<String, String> headMap, Class<T> returnType) {
  //    HttpHeaders headers = new HttpHeaders();
  //    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
  //    headMap.forEach(headers::add);
  //    HttpEntity<String> requestEntity = new HttpEntity(params, headers);
  //    ResponseEntity<T> response = this.restTemplate.postForEntity(url, requestEntity, returnType, new Object[0]);
  //    return response;
  //  }

  //  public <T> ResponseEntity<T> uploadFile(String url, File file, LinkedHashMap<String, String> headMap, Class<T> returnType) throws IOException {
  //    HttpHeaders headers = new HttpHeaders();
  //    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
  //    headMap.forEach(headers::add);
  //    MultiValueMap<String, Object> parts = new LinkedMultiValueMap();
  //    FileSystemResource resource = new FileSystemResource(file);
  //    parts.add("file", resource);
  //    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(parts, headers);
  //    ResponseEntity<T> response = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, returnType, new Object[0]);
  //    return response;
  //  }

}