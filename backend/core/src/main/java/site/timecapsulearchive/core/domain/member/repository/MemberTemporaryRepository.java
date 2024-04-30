package site.timecapsulearchive.core.domain.member.repository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;

public interface MemberTemporaryRepository extends Repository<MemberTemporary, Long> {

    MemberTemporary save(MemberTemporary memberTemporary);
}
