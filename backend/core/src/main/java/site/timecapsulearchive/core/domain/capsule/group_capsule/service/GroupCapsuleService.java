package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CombinedGroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CombinedGroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleOpenStateDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleWithMemberDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupCapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;
    private final MemberGroupRepository memberGroupRepository;

    @Transactional
    public Capsule saveGroupCapsule(
        final GroupCapsuleCreateRequestDto dto,
        final Member member,
        final Group group,
        final CapsuleSkin capsuleSkin,
        final Point point
    ) {
        final Capsule capsule = dto.toGroupCapsule(member, capsuleSkin, group,
            CapsuleType.GROUP, point);

        capsuleRepository.save(capsule);

        return capsule;
    }

    public CombinedGroupCapsuleDetailDto findGroupCapsuleDetailByGroupIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        final GroupCapsuleWithMemberDetailDto groupCapsuleWithMemberDetailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        List<GroupCapsuleMemberDto> groupCapsuleMembers = memberGroupRepository.findGroupCapsuleMembers(
            groupCapsuleWithMemberDetailDto.groupCapsuleDetailDto().groupId(), capsuleId);

        GroupCapsuleMemberDto requestMember = groupCapsuleMembers.stream()
            .filter(dto -> memberId.equals(dto.id()))
            .findAny()
            .orElseThrow(NoGroupAuthorityException::new);

        Boolean hasEditPermission = requestMember.id()
            .equals(groupCapsuleWithMemberDetailDto.groupCapsuleDetailDto().creatorId());
        Boolean hasDeletePermission = hasEditPermission || requestMember.isGroupOwner();

        if (capsuleNotOpened(groupCapsuleWithMemberDetailDto)) {
            GroupCapsuleWithMemberDetailDto excludeDetailContentsDto = groupCapsuleWithMemberDetailDto.excludeDetailContents();
            return CombinedGroupCapsuleDetailDto.create(
                excludeDetailContentsDto.groupCapsuleDetailDto(),
                excludeDetailContentsDto.members(),
                requestMember.isOpened(),
                hasEditPermission,
                hasDeletePermission
            );
        }

        return CombinedGroupCapsuleDetailDto.create(
            groupCapsuleWithMemberDetailDto.groupCapsuleDetailDto(),
            groupCapsuleWithMemberDetailDto.members(),
            requestMember.isOpened(),
            hasEditPermission,
            hasDeletePermission
        );
    }

    private boolean capsuleNotOpened(final GroupCapsuleWithMemberDetailDto detailDto) {
        if (detailDto.groupCapsuleDetailDto().dueDate() == null) {
            return false;
        }

        return !detailDto.groupCapsuleDetailDto().isCapsuleOpened()
            || detailDto.groupCapsuleDetailDto().dueDate()
            .isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }

    /**
     * 그룹 캡슐의 요약 정보를 조회한다.
     * <br>
     *
     * @param memberId  사용자 아이디
     * @param capsuleId 조회할 캡슐 아이디
     * @return 그룹 캡슐의 요약 정보(캡슐, 그룹원)
     * @throws NoGroupAuthorityException 그룹에 대한 권한이 존재하지 않으면 예외가 발생한다.
     */
    public CombinedGroupCapsuleSummaryDto findGroupCapsuleSummary(
        final Long memberId,
        final Long capsuleId
    ) {
        GroupCapsuleSummaryDto groupCapsuleSummaryDto = groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        List<GroupCapsuleMemberDto> groupCapsuleMembers = memberGroupRepository.findGroupCapsuleMembers(
            groupCapsuleSummaryDto.groupId(), capsuleId);

        GroupCapsuleMemberDto requestMember = groupCapsuleMembers.stream()
            .filter(dto -> memberId.equals(dto.id()))
            .findAny()
            .orElseThrow(NoGroupAuthorityException::new);

        Boolean hasEditPermission = requestMember.id().equals(groupCapsuleSummaryDto.creatorId());
        Boolean hasDeletePermission = hasEditPermission || requestMember.isGroupOwner();

        return CombinedGroupCapsuleSummaryDto.create(
            groupCapsuleSummaryDto,
            groupCapsuleMembers,
            requestMember.isOpened(),
            hasEditPermission,
            hasDeletePermission
        );
    }

    /**
     * 사용자가 만든 모든 그룹 캡슐을 조회한다.
     *
     * @param memberId  조회할 사용자 아이디
     * @param size      조회할 캡슐 크기
     * @param createdAt 조회를 시작할 캡슐의 생성 시간, 첫 조회라면 현재 시간, 이후 조회라면 맨 마지막 데이터의 시간
     * @return 사용자가 생성한 그룹 캡슐 목록
     */
    public Slice<CapsuleBasicInfoDto> findMyGroupCapsuleSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupCapsuleQueryRepository.findMyGroupCapsuleSlice(memberId, size, createdAt);
    }

    /**
     * 해당 그룹원이 그룹 캡슐을 개봉한다. 모든 그룹원이 개봉하면 캡슐이 개봉된다.
     *
     * @param memberId  캡슐을 개봉할 그룹원
     * @param capsuleId 개봉할 캡슐 아이디
     */
    @Transactional
    public GroupCapsuleOpenStateDto openGroupCapsule(final Long memberId, final Long capsuleId) {
        Capsule groupCapsule = capsuleRepository.findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (groupCapsule.isNotCapsuleOpened()) {
            return GroupCapsuleOpenStateDto.notOpened(false);
        }

        if (groupCapsule.isNotTimeCapsule()) {
            groupCapsule.open();
            return GroupCapsuleOpenStateDto.opened();
        }

        boolean allGroupMemberOpened = groupCapsule.isAllGroupMemberOpened(memberId, capsuleId);
        if (!allGroupMemberOpened) {
            return GroupCapsuleOpenStateDto.notOpened(true);
        }

        groupCapsule.open();
        return GroupCapsuleOpenStateDto.opened();
    }

    public Slice<CapsuleBasicInfoDto> findGroupCapsuleSlice(final GroupCapsuleSliceRequestDto dto) {
        checkGroupAuthority(dto.memberId(), dto.groupId());

        return groupCapsuleQueryRepository.findGroupCapsuleSlice(dto);
    }

    private void checkGroupAuthority(Long memberId, Long groupId) {
        boolean isGroupMember = memberGroupRepository.existMemberGroupByMemberIdAndGroupId(memberId,
            groupId);
        if (!isGroupMember) {
            throw new NoGroupAuthorityException();
        }
    }

    public List<GroupCapsuleMemberDto> findGroupCapsuleMembers(
        final Long memberId,
        final Long capsuleId,
        final Long groupId
    ) {
        List<GroupCapsuleMemberDto> groupCapsuleMembers = groupCapsuleOpenRepository.findGroupCapsuleMembers(
            capsuleId, groupId);

        boolean isGroupMember = groupCapsuleMembers.stream()
            .anyMatch(groupCapsuleMember -> groupCapsuleMember.id().equals(memberId));
        if (!isGroupMember) {
            throw new NoGroupAuthorityException();
        }

        return groupCapsuleMembers;
    }

    @Transactional
    public void deleteRelatedAllOwnerGroupCapsule(
        final List<Group> allOwnerGroups,
        final ZonedDateTime deletedAt
    ) {
        final List<Long> groupIds = allOwnerGroups.stream()
            .map(Group::getId)
            .toList();

        final List<Capsule> groupCapsules = capsuleRepository.findCapsulesByGroupIds(groupIds);
        final List<Long> groupCapsuleIds = groupCapsules.stream()
            .map(Capsule::getId)
            .toList();

        videoRepository.deleteByCapsuleIds(groupCapsuleIds, deletedAt);
        imageRepository.deleteByCapsuleIds(groupCapsuleIds, deletedAt);
        groupCapsuleOpenRepository.deleteByCapsuleIds(groupCapsuleIds, deletedAt);
        groupCapsules.forEach(capsuleRepository::delete);
    }
}

