package site.timecapsulearchive.core.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// swagger 접속 url -> http://localhost:8080/api/swagger-ui/index.html#/

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .servers(getServers())
            .info(getInfo());
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
}

