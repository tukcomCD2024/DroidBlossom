package site.timecapsulearchive.core.common;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.util.function.Consumer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class DatabaseContainer {

    protected static final MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.35"))
            .withCreateContainerCmdModifier(bingPort())
            .withDatabaseName("test-database")
            .withUsername("test-user")
            .withPassword("test-password");

        mysqlContainer.start();
    }

    private static Consumer<CreateContainerCmd> bingPort() {
        return cmd -> cmd.withHostConfig(new HostConfig().withPortBindings(
            new PortBinding(Ports.Binding.bindPort(36915), new ExposedPort(3306))
        ));
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
}
