package site.timecapsulearchive.core.common;

import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import site.timecapsulearchive.core.global.config.JpaAuditingConfig;

@Import(JpaAuditingConfig.class)
@DataJpaTest
@FlywayTestExtension
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class RepositoryTest extends DatabaseContainer {

}
