package site.timecapsulearchive.core.domain.member.repository;

import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.member.dto.VerifiedCheckDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory query;

    public Boolean findIsVerifiedByAuthIdAndSocialType(
        String authId,
        SocialType socialType
    ) {
        return query.select(member.isVerified)
            .from(member)
            .where(member.authId.eq(authId).and(member.socialType.eq(socialType)))
            .fetchOne();
    }

    public Optional<VerifiedCheckDto> findVerifiedCheckDtoByAuthIdAndSocialType(
        String authId,
        SocialType socialType
    ) {
        return Optional.ofNullable(query.select(
                Projections.constructor(VerifiedCheckDto.class, member.id, member.isVerified))
            .from(member)
            .where(member.authId.eq(authId).and(member.socialType.eq(socialType)))
            .fetchOne()
        );
    }
}
