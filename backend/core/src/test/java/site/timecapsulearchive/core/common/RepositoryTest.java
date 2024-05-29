package site.timecapsulearchive.core.common;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import site.timecapsulearchive.core.global.config.repository.JpaAuditingConfig;
import site.timecapsulearchive.core.global.config.repository.QueryDSLConfig;

@Import(value = {JpaAuditingConfig.class, QueryDSLConfig.class})
@DataJpaTest
@FlywayTestExtension
@FlywayTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class RepositoryTest extends DatabaseContainer {

}
