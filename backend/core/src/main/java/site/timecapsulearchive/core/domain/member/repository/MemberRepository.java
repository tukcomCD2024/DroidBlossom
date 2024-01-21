package site.timecapsulearchive.core.domain.member.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.entity.Member;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findByAuthIdAndSocialType(String authId, SocialType socialType);

    Member save(Member createMember);

    Optional<Member> findById(Long memberId);
}
