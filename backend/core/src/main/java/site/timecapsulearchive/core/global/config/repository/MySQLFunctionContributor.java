package site.timecapsulearchive.core.global.config.repository;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class MySQLFunctionContributor implements FunctionContributor {

    private static final String FUNCTION_NAME = "match_against";
    private static final String FUNCTION_PATTERN = "match (?1) against (?2 in boolean mode)";

    @Override
    public void contributeFunctions(final FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry()
            .registerPattern(FUNCTION_NAME, FUNCTION_PATTERN,
                functionContributions.getTypeConfiguration().getBasicTypeRegistry()
                    .resolve(StandardBasicTypes.DOUBLE));
    }
}
