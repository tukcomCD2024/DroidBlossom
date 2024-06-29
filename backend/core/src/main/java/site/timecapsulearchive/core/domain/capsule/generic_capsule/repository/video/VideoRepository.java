package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.capsule.entity.Video;

public interface VideoRepository extends Repository<Video, Long>, VideoQueryRepository {

    void save(Video video);

    @Query("UPDATE Video v SET v.deletedAt = :deletedAt WHERE v.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE Video v SET v.deletedAt = :deletedAt WHERE v.capsule.id in :groupCapsuleIds")
    @Modifying
    void deleteByCapsuleIds(
        @Param("groupCapsuleIds") List<Long> groupCapsuleIds,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
