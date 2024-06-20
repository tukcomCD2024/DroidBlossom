package site.timecapsulearchive.core.global.config.redis;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import site.timecapsulearchive.core.domain.auth.data.dto.MemberInfo;

@Configuration
@EnableTransactionManagement
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MemberInfo> memberInfoRedisTemplate(
        final RedisConnectionFactory connectionFactory
    ) {
        final RedisTemplate<String, MemberInfo> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(MemberInfo.class));

        template.setConnectionFactory(connectionFactory);
        template.setEnableTransactionSupport(true);

        return template;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
        final EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {

        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));

        return template;
    }
}
