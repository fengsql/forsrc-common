package com.forsrc.common.hadoop.configure;

import com.forsrc.common.tool.Tool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * hbase 链接初始化
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class HBaseConnectConfigure implements InitializingBean {

  @Resource
  private HBasePropertiesConfiguration hBasePropertiesConfiguration;

  @Getter
  @Setter
  private HBaseAdmin admin;

  private Configuration configuration;

  private ThreadPoolExecutor executor;

  private static Connection conn = null;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (Tool.isNull(hBasePropertiesConfiguration.getZkQuorum())) {
      return;
    }
    log.info("connect hbase start. zkQuorum: {}. port: {}", hBasePropertiesConfiguration.getZkQuorum(), hBasePropertiesConfiguration.getZkPort());
    configuration = HBaseConfiguration.create();
    //zk address,多个,分隔
    configuration.set("hbase.zookeeper.quorum", hBasePropertiesConfiguration.getZkQuorum());
    //zk port
    configuration.set("hbase.zookeeper.property.clientPort", hBasePropertiesConfiguration.getZkPort());
    //HMaster
//    configuration.set("hbase.master", hBasePropertiesConfiguration.getHbaseMaster());
    configuration.set("zookeeper.znode.parent", hBasePropertiesConfiguration.getZkPath());

    executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(hBasePropertiesConfiguration.getConnPoolCoreSize());
    executor.setMaximumPoolSize(hBasePropertiesConfiguration.getConnPoolMaxSize());

    //创建hbase的连接，这是一个分布式连接
    Connection connection = getConnection();

    //这个admin是管理table时使用的，比如说创建表
    admin = (HBaseAdmin) connection.getAdmin();
    log.info("connect hbase ok. zkQuorum: {}. port: {}", hBasePropertiesConfiguration.getZkQuorum(), hBasePropertiesConfiguration.getZkPort());
  }

  public Connection getConnection() throws IOException {
    return conn == null ? conn = ConnectionFactory.createConnection(configuration, executor) : conn;
  }
}

