package com.self.study.flashsale.flashsale.config;

import org.mockito.Mockito;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableCaching
@Profile("test")
public class TestConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return Mockito.mock(KafkaTemplate.class);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("events", "events_all", "events_paged", "orders", "orders_all", "orders_paged");
    }
}
