package com.tiendaonline.clientes.config;

import com.statsig.Statsig;
import com.statsig.StatsigOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "statsig.enabled", havingValue = "true", matchIfMissing = true)
public class StatsigConfig {

    @Bean(destroyMethod = "shutdown")
    public Statsig statsig(@Value("${statsig.server-secret}") String secret) throws Exception {
        StatsigOptions options = new StatsigOptions.Builder().build();
        Statsig statsig = new Statsig(secret, options);
        statsig.initialize().get();
        return statsig;
    }
}