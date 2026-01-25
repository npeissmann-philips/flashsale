package com.self.study.flashsale.flashsale.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.self.study.flashsale.flashsale.drivers.db.repository")
@EnableTransactionManagement
public class BeanConfiguration {
}
