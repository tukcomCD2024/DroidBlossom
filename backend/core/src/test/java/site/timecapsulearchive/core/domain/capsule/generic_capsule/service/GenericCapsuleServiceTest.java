package site.timecapsulearchive.core.domain.capsule.generic_capsule.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.exception.NoCapsuleAuthorityException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video.VideoRepository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleOpenRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

public class GenericCapsuleServiceTest {

    private final CapsuleRepository capsuleRepository = mock(CapsuleRepository.class);
    private final MemberFriendRepository memberFriendRepository = mock(
        MemberFriendRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupCapsuleOpenRepository groupCapsuleOpenRepository = mock(
        GroupCapsuleOpenRepository.class);
    private final GeoTransformManager geoTransformManager = mock(GeoTransformManager.class);
    private final ImageRepository imageRepository = mock(ImageRepository.class);
    private final VideoRepository videoRepository = mock(VideoRepository.class);
    private final CapsuleSkinRepository capsuleSkinRepository = mock(CapsuleSkinRepository.class);

    private final CapsuleService capsuleService = new CapsuleService(capsuleRepository,
        memberFriendRepository, memberGroupRepository, groupCapsuleOpenRepository,
        geoTransformManager, imageRepository, videoRepository, capsuleSkinRepository);

    private final Member member = MemberFixture.member(1);
    private final CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);

    @ParameterizedTest
    @EnumSource(value = CapsuleType.class, names = {"PUBLIC", "GROUP"})
    void 사용자는_공개와_그룹_캡슐을_신고하면_신고_횟수가_증가한다(CapsuleType capsuleType) {
        //given
        Long capsuleId = 1L;
        Capsule capsule = CapsuleFixture.capsule(member, capsuleSkin, capsuleType);
        given(capsuleRepository.findById(capsuleId)).willReturn(Optional.of(capsule));

        //when
        capsuleService.declarationCapsule(capsuleId);

        //then
        assertEquals(1L, capsule.getDeclarationCount());
    }

    @ParameterizedTest
    @EnumSource(value = CapsuleType.class, names = {"SECRET", "TREASURE", "ALL"})
    void 사용자는_공개와_그룹_캡슐이_아닌_캡슐을_신고하면_예외가_발생한다(CapsuleType capsuleType) {
        //given
        Long capsuleId = 1L;
        Capsule capsule = CapsuleFixture.capsule(member, capsuleSkin, capsuleType);
        given(capsuleRepository.findById(capsuleId)).willReturn(Optional.of(capsule));

        //when & then
        assertThatThrownBy(() -> capsuleService.declarationCapsule(capsuleId))
            .isInstanceOf(NoCapsuleAuthorityException.class)
            .hasMessage(ErrorCode.NO_CAPSULE_AUTHORITY_ERROR.getMessage());

    }
}
