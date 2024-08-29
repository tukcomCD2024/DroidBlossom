package site.timecapsulearchive.core.global.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class GlobalHeaderOperationCustomizer implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        Parameter customHeaderVersion = new Parameter().in(ParameterIn.HEADER.toString())
            .name("Default-Key")
            .description("api key").schema(new StringSchema())
            .required(false);

        operation.addParametersItem(customHeaderVersion);
        return operation;
    }
}