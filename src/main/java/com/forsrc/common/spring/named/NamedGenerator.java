package com.forsrc.common.spring.named;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * 使用全类名(包名加类名)的模式命名，防止不同包下相同类名时 spring 加载冲突。
 * 如果启用请在启动类上加上：@ComponentScan(nameGenerator = NamedGenerator.class)。
 */
public class NamedGenerator implements BeanNameGenerator {

  @Override
  public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
    return definition.getBeanClassName();
  }

}