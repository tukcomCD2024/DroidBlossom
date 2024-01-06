package site.timecapsulearchive.core.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// swagger 접속 url -> http://localhost:8080/api/swagger-ui/index.html#/

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("Archive v1")
            .pathsToMatch("/api/**")
            .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Archive API")
                .description("AR을 이용해 추억을 저장하고 공유하는 서비스")
                .version("v1"));
    }
}

