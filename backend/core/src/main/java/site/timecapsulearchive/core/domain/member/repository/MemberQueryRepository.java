package site.timecapsulearchive.core.domain.member.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.member.data.dto.EmailVerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberNotificationDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public interface MemberQueryRepository {


    Boolean findIsVerifiedByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    );

    Optional<VerifiedCheckDto> findVerifiedCheckDtoByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    );

    Optional<MemberDetailDto> findMemberDetailResponseDtoById(final Long memberId);

    Slice<MemberNotificationDto> findNotificationSliceByMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    List<MemberNotificationDto> findMemberNotificationDtos(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    Optional<EmailVerifiedCheckDto> findEmailVerifiedCheckDtoByEmail(final String email);

    Boolean checkEmailDuplication(final String email);

    Optional<Boolean> findIsAlarmByMemberId(final Long memberId);

    List<Long> findMemberIdsByIds(List<Long> ids);

    boolean checkTagDuplication(String tag);
}
