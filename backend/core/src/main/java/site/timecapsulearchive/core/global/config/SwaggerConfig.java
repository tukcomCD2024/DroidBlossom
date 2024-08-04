package site.timecapsulearchive.core.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .servers(getServers())
            .info(getInfo())
            .components(new Components()
                .addSecuritySchemes("temporary_user_token", temporaryTokenSecuritySchema())
                .addSecuritySchemes("user_token", tokenSecuritySchema())
            );
    }

    private List<Server> getServers() {
        return List.of(
            new Server()
                .url("/api")
                .description("백엔드 api 서버")
        );
    }

    private Info getInfo() {
        return new Info().title("Archive API")
            .description("AR을 이용해 추억을 저장하고 공유하는 서비스")
            .version("v1");
    }

    public SecurityScheme temporaryTokenSecuritySchema() {
        return new SecurityScheme()
            .name("Authorization")
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .type(SecurityScheme.Type.HTTP);
    }

    public SecurityScheme tokenSecuritySchema() {
        return new SecurityScheme()
            .name("Authorization")
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .type(SecurityScheme.Type.HTTP);
    }
}

