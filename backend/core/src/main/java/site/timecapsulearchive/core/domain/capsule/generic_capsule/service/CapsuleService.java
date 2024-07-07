package site.timecapsulearchive.core.domain.capsule.generic_capsule.service;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CoordinateRangeDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.common.supplier.ZonedDateTimeSupplier;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final MemberFriendRepository memberFriendRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository;
    private final GeoTransformManager geoTransformManager;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;

    /**
     * 현재 위치에서 범위 내에 있는 AR 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param dto         범위 내의 캡슐 조회 요청 dto
     * @param capsuleType 캡슐의 타입
     * @return NearbyARCapsuleResponse 현재 위치 {@code dto.latitude()}, {@code dto.longitude()}에서 반경
     * {@code dto.distance()} 안에 캡슐 목록을 조회한다. 응답 좌표는 SRID 4326이다.
     */
    public List<NearbyARCapsuleSummaryDto> findARCapsuleByCurrentLocationAndCapsuleType(
        final Long memberId,
        final CoordinateRangeDto dto,
        final CapsuleType capsuleType
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        return capsuleRepository.findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType);
    }

    public List<NearbyCapsuleSummaryDto> findCapsuleByCurrentLocationAndCapsuleType(
        final Long memberId,
        final CoordinateRangeDto dto,
        final CapsuleType capsuleType
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        return capsuleRepository.findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType);
    }

    public Capsule findCapsuleByMemberIdAndCapsuleId(final Long memberId, final Long capsuleId) {
        return capsuleRepository.findCapsuleByMemberIdAndCapsuleId(memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }

    @Transactional
    public void updateIsOpenedTrue(final Long memberId, final Long capsuleId) {
        int isOpenedTrue = capsuleRepository.updateIsOpenedTrue(memberId, capsuleId);

        if (isOpenedTrue != 1) {
            throw new CapsuleNotFondException();
        }
    }

    @Transactional
    public Capsule saveCapsule(
        final CapsuleCreateRequestDto dto,
        final Member foundMember,
        final CapsuleSkin foundCapsuleSkin,
        final CapsuleType capsuleType
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());
        final Capsule capsule = dto.toCapsule(point, foundMember, foundCapsuleSkin, capsuleType);

        capsuleRepository.save(capsule);

        return capsule;
    }

    /**
     * 지도에서 캡슐을 찾기 위해 사용자의 현재 위치에서 특정 반경에서 친구들의 캡슐들을 요약 조회한다
     *
     * @param memberId 사용자 아이디
     * @param dto      현재 위치와 반경
     * @return 지도용 캡슐 요약 조회들
     */
    @Transactional(readOnly = true)
    public List<NearbyCapsuleSummaryDto> findFriendsCapsulesByCurrentLocation(
        Long memberId,
        CoordinateRangeDto dto
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        final List<Long> friendIds = memberFriendRepository.findFriendIdsByOwnerId(memberId);

        return capsuleRepository.findFriendsCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            friendIds,
            mbr
        );
    }

    /**
     * AR로 캡슐을 찾기 위해 사용자의 현재 위치에서 특정 반경에서 친구들의 캡슐들을 요약 조회한다
     *
     * @param memberId 사용자 아이디
     * @param dto      현재 위치와 반경
     * @return AR용 캡슐 요약 조회들
     */
    public List<NearbyARCapsuleSummaryDto> findFriendsARCapsulesByCurrentLocation(
        Long memberId,
        CoordinateRangeDto dto
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, dto.distance());

        final List<Long> friendIds = memberFriendRepository.findFriendIdsByOwnerId(memberId);

        return capsuleRepository.findFriendsARCapsulesByCurrentLocation(
            friendIds,
            mbr
        );
    }

    @Transactional
    public void deleteRelatedAllCapsuleByMemberId(
        final Long memberId,
        final ZonedDateTime deletedAt
    ) {
        imageRepository.deleteByMemberId(memberId, deletedAt);
        videoRepository.deleteByMemberId(memberId, deletedAt);
        capsuleSkinRepository.deleteByMemberId(memberId, deletedAt);
        capsuleRepository.deleteExcludeGroupCapsuleByMemberId(memberId, deletedAt);
    }

    @Transactional
    public void deleteCapsule(
        final Long memberId,
        final Long capsuleId,
        final CapsuleType capsuleType
    ) {
        final ZonedDateTime deletedAt = ZonedDateTimeSupplier.utc().get();

        if (capsuleType.isGroupCapsule()) {
            validateGroupCapsuleOwnership(memberId, capsuleId);
        }

        deleteCapsuleAssets(memberId, capsuleId, deletedAt);
    }

    private void validateGroupCapsuleOwnership(final Long memberId, final Long capsuleId) {
        final Capsule capsule = capsuleRepository.findCapsuleByMemberIdAndCapsuleId(memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
        final Long groupId = capsule.getGroup().getId();

        final MemberGroup memberGroup = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(memberId, groupId)
            .orElseThrow(CapsuleNotFondException::new);
        memberGroup.checkDeleteGroupCapsuleAuthority();
    }

    private void deleteCapsuleAssets(final Long memberId, final Long capsuleId, final ZonedDateTime deletedAt) {
        imageRepository.deleteByMemberIdAndCapsuleId(memberId, capsuleId, deletedAt);
        videoRepository.deleteByMemberIdAndCapsuleId(memberId, capsuleId, deletedAt);
        groupCapsuleOpenRepository.deleteByMemberIdAndCapsuleId(memberId, capsuleId, deletedAt);
        capsuleRepository.deleteByMemberIdAndCapsuleId(memberId, capsuleId, deletedAt);
    }

}
