package site.timecapsulearchive.core.global.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import site.timecapsulearchive.core.domain.auth.service.MemberInfo;

@Configuration
@EnableTransactionManagement
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, MemberInfo> memberInfoRedisTemplate(
        RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, MemberInfo> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(MemberInfo.class));

        template.setConnectionFactory(connectionFactory);
        template.setEnableTransactionSupport(true);

        return template;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
        EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
