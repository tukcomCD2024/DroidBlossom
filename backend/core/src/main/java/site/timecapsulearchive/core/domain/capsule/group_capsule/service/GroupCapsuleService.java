package site.timecapsulearchive.core.domain.capsule.group_capsule.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.exception.GroupCapsuleOpenNotFoundException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.MyGroupCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenQueryRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupCapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository;

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

    public GroupCapsuleDetailDto findGroupCapsuleDetailByGroupIdAndCapsuleId(
        final Long capsuleId
    ) {
        final GroupCapsuleDetailDto detailDto = groupCapsuleQueryRepository.findGroupCapsuleDetailDtoByCapsuleId(
                capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (capsuleNotOpened(detailDto)) {
            return detailDto.excludeDetailContents();
        }

        return detailDto;
    }

    private boolean capsuleNotOpened(final GroupCapsuleDetailDto detailDto) {
        if (detailDto.capsuleDetailDto().dueDate() == null) {
            return false;
        }

        return !detailDto.capsuleDetailDto().isOpened() || detailDto.capsuleDetailDto().dueDate()
            .isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }

    public GroupCapsuleSummaryDto findGroupCapsuleSummaryByGroupIDAndCapsuleId(
        final Long capsuleId) {
        return groupCapsuleQueryRepository.findGroupCapsuleSummaryDtoByCapsuleId(capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }

    /**
     * 사용자가 만든 모든 그룹 캡슐을 조회한다.
     *
     * @param memberId  조회할 사용자 아이디
     * @param size      조회할 캡슐 크기
     * @param createdAt 조회를 시작할 캡슐의 생성 시간, 첫 조회라면 현재 시간, 이후 조회라면 맨 마지막 데이터의 시간
     * @return 사용자가 생성한 그룹 캡슐 목록
     */
    public Slice<MyGroupCapsuleDto> findMyGroupCapsuleSlice(
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
    public void openGroupCapsule(final Long memberId, final Long capsuleId) {
        Capsule groupCapsule = capsuleRepository.findGroupCapsuleByMemberIdAndCapsuleId(memberId, capsuleId)
            .orElseThrow(CapsuleNotFondException::new);

        if (groupCapsule.getDueDate() == null) {
            groupCapsule.open();
            return;
        }

        List<GroupCapsuleOpen> groupCapsuleOpens = groupCapsuleOpenRepository.findByCapsuleId(
            capsuleId);
        if (groupCapsuleOpens.isEmpty()) {
            throw new GroupCapsuleOpenNotFoundException();
        }

        boolean allGroupMemberOpened = groupCapsuleOpens.stream()
            .allMatch(groupCapsuleOpen -> {
                if (groupCapsuleOpen.matched(capsuleId, memberId)) {
                    groupCapsuleOpen.open();
                }

                return groupCapsuleOpen.getIsOpened();
            });

        if (allGroupMemberOpened) {
            groupCapsule.open();
        }
    }
}

