package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

public interface ImageRepository extends Repository<Image, Long>, ImageQueryRepository {

    void save(Image newImage);

    @Query("UPDATE Image i SET i.deletedAt = :deletedAt WHERE i.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE Image i SET i.deletedAt = :deletedAt WHERE i.capsule.id in :groupCapsuleIds")
    @Modifying
    void deleteByCapsuleIds(
        @Param("groupCapsuleIds") List<Long> groupCapsuleIds,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
