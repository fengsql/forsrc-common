package com.forsrc.common.configure.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "spring.redis.enable", havingValue = "true", matchIfMissing = true)
@EnableCaching
//@ConditionalOnExpression("#{!'${spring.redis.host}'.equals('')}")
@Slf4j
public class RedisConfigure {

  @Value("${spring.redis.host:127.0.0.1}")
  private String redisHost;

  @Value("${spring.redis.port:6379}")
  private int redisPort;

  @Value("${spring.redis.timeout:5000}")
  private int redisTimeout;

  @Value("${spring.redis.password:}")
  private String redisPassword;

  @Value("${spring.redis.database:0}")
  private int redisDatabase;

  @Value("${spring.redis.lettuce.pool.max-active:8}")
  private int maxActive;

  @Value("${spring.redis.lettuce.pool.max-wait:-1}")
  private int maxWait;

  @Value("${spring.redis.lettuce.pool.max-idle:8}")
  private int maxIdle;

  @Value("${spring.redis.lettuce.pool.min-idle:0}")
  private int minIdle;

  @Value("${redis.ttl:3600}")
  private long ttl;

  @Value("${redis.cache-prefix:}")
  private String cachePrefix;

  @Bean
  public RedisConnectionFactory localRedisConnectionFactory() {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(maxActive);
    poolConfig.setMaxIdle(maxIdle);
    poolConfig.setMaxWaitMillis(maxWait);
    poolConfig.setMinIdle(minIdle);
    poolConfig.setTestOnBorrow(false);
    poolConfig.setTestOnReturn(false);
    poolConfig.setTestWhileIdle(true);
    JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
      //
      .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(redisTimeout)).build();

    // 单点redis
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
    // 哨兵redis
    // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
    // 集群redis
    // RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
    redisConfig.setHostName(redisHost);
    redisConfig.setPassword(RedisPassword.of(redisPassword));
    redisConfig.setPort(redisPort);
    redisConfig.setDatabase(redisDatabase);

    return new JedisConnectionFactory(redisConfig, clientConfig);
  }

  @Bean
  public RedisSerializer fastJsonRedisSerializer() {
    return new FastJsonRedisSerializer<>(Object.class);
  }

  @Bean
  @ConditionalOnMissingBean(name = "redisTemplate")
  public RedisTemplate redisTemplate(RedisConnectionFactory localRedisConnectionFactory, RedisSerializer fastJsonRedisSerializer) {
    RedisTemplate redisTemplate = new RedisTemplate();
    redisTemplate.setConnectionFactory(localRedisConnectionFactory);
    //redis开启事务
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(fastJsonRedisSerializer);
    redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  public RedisCacheConfiguration redisCacheConfiguration(RedisSerializer fastJsonRedisSerializer) {
    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
    configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
    configuration.disableCachingNullValues();
    configuration.disableKeyPrefix();
    if (ttl > 0) {
      configuration.entryTtl(Duration.ofSeconds(ttl));
    }
    if (cachePrefix != null && cachePrefix.length() != 0) {
      configuration.computePrefixWith(cacheName -> cachePrefix + cacheName);
    }
    return configuration;
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate redisTemplate, RedisCacheConfiguration redisCacheConfiguration) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
    return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
  }

}