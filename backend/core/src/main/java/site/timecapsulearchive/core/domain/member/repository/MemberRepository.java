package site.timecapsulearchive.core.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findMemberByAuthIdAndSocialType(String authId, SocialType socialType);

    Member save(Member createMember);

    Optional<Member> findMemberById(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.fcmToken = :fcmToken WHERE m.id = :memberId")
    int updateMemberFCMToken(@Param("memberId") Long memberId, @Param("fcmToken") String fcmToken);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.notificationEnabled = :notificationEnabled WHERE m.id = :memberId")
    int updateMemberNotificationEnabled(
        @Param("memberId") Long memberId,
        @Param("notificationEnabled") Boolean notificationEnabled
    );
}
