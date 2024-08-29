package site.timecapsulearchive.core.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.ErrorResponse;
import site.timecapsulearchive.core.global.security.property.DefaultKeyProperties;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAuthenticationFilter extends OncePerRequestFilter {

    private final DefaultKeyProperties defaultKeyProperties;

    @Override
    @Order(1)
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String requestKey = request.getHeader("Default-Key");

        if (requestKey == null || !requestKey.equals(defaultKeyProperties.defaultKey())) {
            log.warn("Invalid default key provided: {}", requestKey);

            final ErrorResponse errorResponse = ErrorResponse.fromErrorCode(
                ErrorCode.REQUEST_DEFAULT_KEY_ERROR
            );

            response.setStatus(ErrorCode.REQUEST_DEFAULT_KEY_ERROR.getStatus());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                    errorResponse
                )
            );

            return;
        }

        filterChain.doFilter(request, response);
    }
}
