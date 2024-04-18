package site.timecapsulearchive.core.domain.capsule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.repository.GroupCapsuleOpenQueryRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class GroupCapsuleOpenService {

    private final GroupCapsuleOpenQueryRepository repository;
    private final MemberRepository memberRepository;

    public void bulkSave(final List<Long> groupMemberIds, final Capsule capsule) {
        final List<Member> members = groupMemberIds.stream()
            .map(
                id -> memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new))
            .toList();

        repository.bulkSave(members, capsule);
    }
}
