package site.timecapsulearchive.core.domain.member.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;

public interface MemberTemporaryRepository extends Repository<MemberTemporary, Long> {

    MemberTemporary save(MemberTemporary memberTemporary);

    Optional<MemberTemporary> findById(Long id);
}
