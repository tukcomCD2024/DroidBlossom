package site.timecapsulearchive.core.global.api.limit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.timecapsulearchive.core.domain.auth.exception.TooManyRequestException;

@Component
@RequiredArgsConstructor
public class ApiLimitCheckInterceptor implements HandlerInterceptor {

    private static final int NO_USAGE = 0;

    private final ApiLimitProperties apiLimitProperties;
    private final ApiUsageCacheRepository apiUsageCacheRepository;

    /**
     * 엔드포인트에 Api 요청 횟수를 검사하는 인터셉터이다. WebMvcConfig에 path로 등록된 경로는 여기를 거치게 된다. 아직 문자 인증에 대한 요청만
     * 걸려있다.
     *
     * @param request  요청
     * @param response 응답
     * @param handler  해당 요청을 처리할 메서드
     * @return 클라이언트가 보낸 요청이 Api 횟수 제한에 걸리는지 여부 {@code True, False}
     * @throws TooManyRequestException 횟수 제한이 발생하면 예외 발생
     */
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws TooManyRequestException {
        Long memberId = (Long) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        Integer apiUsageCount = apiUsageCacheRepository.getSmsApiUsage(memberId)
            .orElse(NO_USAGE);

        if (apiUsageCount > apiLimitProperties.smsLimit()) {
            throw new TooManyRequestException();
        }

        if (isFirstRequest(apiUsageCount)) {
            apiUsageCacheRepository.saveAsFirstRequest(memberId);
            return true;
        }

        apiUsageCacheRepository.increaseSmsApiUsage(memberId);

        return true;
    }

    private boolean isFirstRequest(Integer apiUsageCount) {
        return apiUsageCount.equals(NO_USAGE);
    }
}
