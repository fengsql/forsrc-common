package com.forsrc.common.tool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@Slf4j
public class ToolBean implements ApplicationContextAware {

  private static ConfigurableApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    applicationContext = (ConfigurableApplicationContext) context;
    log.info("init applicationContext ok.");
  }

  /**
   * 通过name获取 Bean。
   * @param name 名称。
   * @return 返回Bean。
   */
  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  /**
   * 通过class获取Bean。
   * @param clazz 类。
   * @return 返回Bean。
   */
  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean。
   * @param name  名称。
   * @param clazz 类。
   * @return 返回Bean。
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return applicationContext.getBean(name, clazz);
  }

  /**
   * 获取HttpServletRequest。
   * @return 返回HttpServletRequest。
   */
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest();
  }

  /**
   * 获取HttpSession。
   * @return 返回HttpSession。
   */
  public static HttpSession getSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest().getSession();
  }

  /**
   * 获取完整的请求URL。
   * @return 返回url。
   */
  public static String getRequestUrl() {
    return getRequestUrl(getRequest());
  }

  /**
   * 获取完整的请求URL。
   * @param request 请求。
   * @return 返回url。
   */
  public static String getRequestUrl(HttpServletRequest request) {
    //当前请求路径
    String currentUrl = request.getRequestURL().toString();
    //请求参数
    String queryString = request.getQueryString();
    if (!StringUtils.isEmpty(queryString)) {
      currentUrl = currentUrl + "?" + queryString;
    }

    String result = "";
    try {
      result = URLEncoder.encode(currentUrl, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      //ignore
    }

    return result;
  }

  public static String getRequestSsoUrl(HttpServletRequest request) {
    //当前请求路径
    if (!request.getRequestURL().toString().contains("/sso/token")) {
      return getRequestUrl(request);
    }

    String currentUrl = request.getContextPath().toString() + "/sso/token";

    //请求参数
    String queryString = request.getQueryString();
    if (!StringUtils.isEmpty(queryString)) {
      currentUrl = currentUrl + "?" + queryString;
    }

    String result = "";
    try {
      result = URLEncoder.encode(currentUrl, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      //ignore
    }

    return result;
  }

  /**
   * 获取请求的客户端IP。
   * @param request 请求。
   * @return 返回客户端IP。
   */
  public static String getRequestIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (StringUtils.isNoneBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      //多次反向代理后会有多个ip值，第一个ip才是真实ip
      int index = ip.indexOf(",");
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    ip = request.getHeader("X-Real-IP");
    if (StringUtils.isNoneBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddr();
  }

  /**
   * 获取 ApplicationContext 对象。
   * @return 返回 ApplicationContext 对象
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 停止应用程序
   */
  public static void close() {
    if (applicationContext != null) {
      applicationContext.close();
    }
  }

}