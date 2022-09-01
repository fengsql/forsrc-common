package com.forsrc.common.configure.common;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.enable", havingValue = "true", matchIfMissing = true) //
@MapperScan(basePackages = "**.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DataSourceConfigure {

  @Value("${spring.datasource.mybatis.mapper-locations:classpath*:mybatis/**/*.xml}")
  private String mapper;

  /**
   * 生成数据源.
   */
  @Bean(name = "dataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  @Primary
  public DataSource getDataSource() {
    return DataSourceBuilder.create().build();
  }

  /**
   * 创建 SqlSessionFactory
   */
  @Bean(name = "sqlSessionFactory")
  @Primary
  public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapper));
    return bean.getObject();
  }

  /**
   * 配置事务管理
   */
  @Bean(name = "transactionManager")
  @Primary
  public DataSourceTransactionManager getTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean(name = "sqlSessionTemplate")
  @Primary
  public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

}