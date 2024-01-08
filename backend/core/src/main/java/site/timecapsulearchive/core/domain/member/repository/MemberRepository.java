package site.timecapsulearchive.core.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
