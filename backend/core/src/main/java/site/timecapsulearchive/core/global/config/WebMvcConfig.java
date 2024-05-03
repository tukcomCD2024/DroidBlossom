package site.timecapsulearchive.core.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.timecapsulearchive.core.domain.friend.interceptor.FriendControllerInterceptor;
import site.timecapsulearchive.core.global.api.limit.ApiLimitCheckInterceptor;
import site.timecapsulearchive.core.global.common.converter.ZonedDateTimeConverter;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiLimitCheckInterceptor apiLimitCheckInterceptor;
    private final FriendControllerInterceptor friendControllerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(apiLimitCheckInterceptor)
            .addPathPatterns("/auth/verification/send-message");

        registry.addInterceptor(friendControllerInterceptor)
            .addPathPatterns("/friends/*/request");
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new ZonedDateTimeConverter());
    }
}
