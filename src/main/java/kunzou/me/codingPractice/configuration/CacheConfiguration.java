package kunzou.me.codingPractice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {

  @Value("${cache.maximum.size}")
  private int MAX_CACHE_SIZE;

  @Bean
  @Override
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager() {
      @Override
      protected Cache createConcurrentMapCache(final String name) {
        return new ConcurrentMapCache(name,
          CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).maximumSize(MAX_CACHE_SIZE).build().asMap(), false);
      }
    };
  }
}
