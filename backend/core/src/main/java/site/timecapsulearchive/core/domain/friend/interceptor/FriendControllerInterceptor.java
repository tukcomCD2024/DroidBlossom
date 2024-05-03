package site.timecapsulearchive.core.domain.friend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.exception.FriendTwoWayInviteException;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;

@Component
@RequiredArgsConstructor
public class FriendControllerInterceptor implements HandlerInterceptor {

    private final FriendInviteRepository friendInviteRepository;

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler
    ) {
        final Long memberId = (Long) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        final Long friendId = Long.parseLong(request.getRequestURI().split("/")[3]);

        final Optional<FriendInvite> foundFriendInvite = friendInviteRepository.findFriendInviteByOwnerIdAndFriendId(
            friendId, memberId);

        if (foundFriendInvite.isPresent()) {
            throw new FriendTwoWayInviteException();
        }

        return true;
    }
}
