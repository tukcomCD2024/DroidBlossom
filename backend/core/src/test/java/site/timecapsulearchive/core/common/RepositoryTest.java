package site.timecapsulearchive.core.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import site.timecapsulearchive.core.global.config.JpaAuditingConfig;

@Import(JpaAuditingConfig.class)
@DataJpaTest
@FlywayTestExtension
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class RepositoryTest extends DatabaseContainer {

    private static final Logger logger = LoggerFactory.getLogger("RepositoryTest");

    @FlywayTest
    @BeforeAll
    static void setupAll() {
        runScript("test/data/insert.sql");
    }

    private static void runScript(String path) {
        String url = mysqlContainer.getJdbcUrl();
        String username = mysqlContainer.getUsername();
        String password = mysqlContainer.getPassword();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource(path));
        } catch (SQLException e) {
            logger.error("sql init error");
            throw new RuntimeException(e);
        }

        logger.info("[SQL INIT] successfully init");
    }

    @AfterAll
    static void cleanupAll() {
        runScript("test/data/truncate.sql");
        logger.info("[SQL TRUNCATE] successfully truncate");
    }
}
