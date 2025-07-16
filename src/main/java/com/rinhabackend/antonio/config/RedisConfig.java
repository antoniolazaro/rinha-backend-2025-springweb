package com.rinhabackend.antonio.config;

import com.rinhabackend.antonio.PaymentPaymentProcessorRequest;
import com.rinhabackend.antonio.ServiceHealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private Integer redisPort;


    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(5000))
                .shutdownTimeout(Duration.ofMillis(100))
                .build();

        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(redisHost, redisPort),
                clientConfig);
    }

    @Bean
    public RedisTemplate<String, ServiceHealthResponse> serviceHealthResponseRedisTemplate() {
        RedisTemplate<String, ServiceHealthResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Serializer para chaves (String)
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serializer para valores (JSON)
        var valueSerializer =
                new GenericJackson2JsonRedisSerializer();

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, PaymentPaymentProcessorRequest> logRedisTemplate() {
        RedisTemplate<String, PaymentPaymentProcessorRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // Serializer para chaves (String)
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serializer para valores (JSON)
        var valueSerializer =
                new GenericJackson2JsonRedisSerializer();

        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
