package site.timecapsulearchive.core.domain.member.repository;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailDto;
import site.timecapsulearchive.core.domain.member.data.dto.MemberStatusDto;
import site.timecapsulearchive.core.domain.member.data.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

public interface MemberQueryRepository {


    MemberStatusDto findIsVerifiedByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    );

    Optional<VerifiedCheckDto> findVerifiedCheckDtoByAuthIdAndSocialType(
        final String authId,
        final SocialType socialType
    );

    Optional<MemberDetailDto> findMemberDetailResponseDtoById(final Long memberId);

    Optional<Boolean> findIsAlarmByMemberId(final Long memberId);

    List<Long> findMemberIdsByIds(final List<Long> ids);

    boolean checkTagDuplication(final String tag);

    Optional<ByteArrayWrapper> findMemberPhoneHash(final Long memberId);

}
