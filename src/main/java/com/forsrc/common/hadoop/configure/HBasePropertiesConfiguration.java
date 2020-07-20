package com.forsrc.common.hadoop.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hbase")
@Data
public class HBasePropertiesConfiguration {

    /**
     * hbase zk address
     */
    private String zkQuorum;

    /**
     * hbase zk port
     */
    private String zkPort;

    private String zkPath;

    /**
     * hbase master
     */
    private String hbaseMaster;

    /**
     * hbase连接池数量
     */
    private int connPoolMaxSize;

    private int connPoolCoreSize;


}
