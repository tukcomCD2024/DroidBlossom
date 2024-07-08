package site.timecapsulearchive.core.domain.member.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public interface MemberRepository extends Repository<Member, Long>, MemberQueryRepository {

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

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.nickname = :nickname, m.tag = :tag WHERE m.id = :memberId")
    int updateMemberData(
        @Param("memberId") Long memberId,
        @Param("nickname") String nickname,
        @Param("tag") String tag
    );

    void delete(Member member);

    @Query("UPDATE Member m SET m.phoneHash = :phoneHash, m.phone = :encryptedPhone WHERE m.id = :memberId")
    @Modifying(clearAutomatically = true)
    int updateMemberPhoneHashAndPhone(
        @Param("memberId") Long memberId,
        @Param("phoneHash") byte[] phoneHash,
        @Param("encryptedPhone") byte[] encryptedPhone
    );

    @Query("UPDATE Member m SET m.tagSearchAvailable = :tagSearchAvailable WHERE m.id = :memberId")
    @Modifying(clearAutomatically = true)
    int updateMemberTagSearchAvailable(
        @Param("memberId") Long memberId,
        @Param("tagSearchAvailable") Boolean tagSearchAvailable
    );

    @Query("UPDATE Member m SET m.phoneSearchAvailable = :phoneSearchAvailable WHERE m.id = :memberId")
    @Modifying(clearAutomatically = true)
    int updateMemberPhoneSearchAvailable(
        @Param("memberId") Long memberId,
        @Param("phoneSearchAvailable") Boolean phoneSearchAvailable
    );
}
