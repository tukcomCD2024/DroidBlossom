package site.timecapsulearchive.core.domain.member.repository;

import jakarta.validation.constraints.Email;
import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;
import site.timecapsulearchive.core.domain.member.entity.Member;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findBySocialTypeAndEmail(SocialType socialType, @Email String email);

    Member save(Member createMember);
}
