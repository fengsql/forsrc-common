package com.forsrc.common.configure.common;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true", matchIfMissing = true) //方式二
public class SwaggerConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("doc.html") //
      .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**") //
      .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  @Bean
  public Docket createRestApi() {
    // 添加请求参数，我们这里把token作为请求头部参数传入后端
    ParameterBuilder parameterBuilder = new ParameterBuilder();
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameterBuilder.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
    parameters.add(parameterBuilder.build());
    return new Docket(DocumentationType.SWAGGER_2) //
      .genericModelSubstitutes(DeferredResult.class) //
      .useDefaultResponseMessages(false) //
      .forCodeGeneration(false) //
      .apiInfo(apiInfo()) //
      .groupName("api") //
      .select() //
      //            .apis(RequestHandlerSelectors.any()) //
      .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //
      .paths(PathSelectors.any()) //
      .paths(Predicates.not(PathSelectors.regex("/error.*"))) //
      .build()                    //
      .globalOperationParameters(parameters);  //
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder() //
      .title("SpringBoot 接口文档 (create by forsrc)") //
      .contact(new Contact("www.forsrc.com", "https://www.forsrc.com", ""))  //
      //      .termsOfServiceUrl("https://www.forsrc.com")                 //
      .description("This is a restful api document of Spring Boot created by forsrc.").version("1.0").build();
  }

}