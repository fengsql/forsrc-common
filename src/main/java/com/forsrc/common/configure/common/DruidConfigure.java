package com.forsrc.common.configure.common;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.forsrc.common.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.druid.filter.stat.enabled", havingValue = "true")
public class DruidConfigure {

  @Value("${spring.datasource.druid.stat-view-servlet.url-pattern}")
  private String urlPattern;
  @Value("${spring.datasource.druid.stat-view-servlet.allow:}")
  private String allow;
  @Value("${spring.datasource.druid.stat-view-servlet.deny:}")
  private String deny;
  @Value("${spring.datasource.druid.stat-view-servlet.login-username:admin}")
  private String loginUsername;
  @Value("${spring.datasource.druid.stat-view-servlet.login-password:admin}")
  private String loginPassword;
  @Value("${spring.datasource.druid.stat-view-servlet.reset-enable:false}")
  private Boolean resetEnable;
  @Value("${spring.datasource.druid.web-stat-filter.exclusions:}")
  private String exclusions;

  @Bean
  public ServletRegistrationBean<Servlet> statViewServlet() {
    ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), urlPattern);
    //IP白名单，空或没有配置时，允许所有
    servletRegistrationBean.addInitParameter("allow", allow);
    //IP黑名单，优先白名单
    servletRegistrationBean.addInitParameter("deny", deny);
    //控制台用户
    servletRegistrationBean.addInitParameter("loginUsername", loginUsername);
    servletRegistrationBean.addInitParameter("loginPassword", loginPassword);
    //是否能够重置数据
    servletRegistrationBean.addInitParameter("resetEnable", Tool.toString(resetEnable));

    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean<Filter> statFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
    //添加过滤规则
    filterRegistrationBean.addInitParameter("exclusions", exclusions);
    return filterRegistrationBean;
  }

}
