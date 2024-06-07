package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule;

import java.util.List;
import org.locationtech.jts.geom.Polygon;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;

public interface CapsuleQueryRepository {
    List<NearbyARCapsuleSummaryDto> findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    );

    List<NearbyCapsuleSummaryDto> findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    );

    List<NearbyCapsuleSummaryDto> findFriendsCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final List<Long> friendIds,
        final Polygon mbr
    );

    List<NearbyARCapsuleSummaryDto> findFriendsARCapsulesByCurrentLocation(
        final List<Long> friendIds,
        final Polygon mbr
    );
}
