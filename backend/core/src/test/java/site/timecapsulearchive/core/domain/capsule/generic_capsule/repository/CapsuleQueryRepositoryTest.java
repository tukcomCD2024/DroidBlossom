package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.common.RepositoryTest;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleFixture;
import site.timecapsulearchive.core.common.fixture.domain.CapsuleSkinFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFriendFixture;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

/**
 * 캡슐 쿼리 테스트
 * <ol>
 *    <li>내가 만든 캡슐만 조회</li>
 *    <ol>
 *        <li>내가 만든 캡슐들 타입별로 조회 되는지 테스트</li>
 *        <li>내가 만든 캡슐만 조회되는지 테스트</li>
 *        <li>조회된 캡슐이 최소 사각형 내부에 있는지 테스트</li>
 *    </ol>
 *    <li>친구가 만든 캡슐만 조회</li>
 *    <ol>
 *        <li>친구가 만든 캡슐만 조회되는지 테스트</li>
 *        <li>조회된 캡슐이 최소 사각형 내부에 있는지 테스트</li>
 *    </ol>
 * </ol>
 */
@TestConstructor(autowireMode = AutowireMode.ALL)
class CapsuleQueryRepositoryTest extends RepositoryTest {

    private final CapsuleQueryRepository capsuleQueryRepository;
    private final GeoTransformManager geoTransformManager = UnitTestDependency.geoTransformManager();

    private Long memberId;
    private List<Long> friendIds;
    private List<Long> myCapsuleIds;
    private List<Long> friendCapsuleIds;
    private Point point;

    CapsuleQueryRepositoryTest(EntityManager entityManager) {
        this.capsuleQueryRepository = new CapsuleQueryRepository(
            new JPAQueryFactory(entityManager));
    }

    @BeforeEach
    @Transactional
    void setup(@Autowired EntityManager entityManager) {
        //테스트할 사용자
        Member member = MemberFixture.member(0);
        entityManager.persist(member);
        memberId = member.getId();

        //캡슐 스킨
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);
        entityManager.persist(capsuleSkin);

        //공개 캡슐
        List<Capsule> friendCapsules = CapsuleFixture.capsules(10, member, capsuleSkin,
            CapsuleType.PUBLIC);
        friendCapsules.forEach(entityManager::persist);
        point = friendCapsules.get(0)
            .getPoint();

        //비밀 캡슐
        List<Capsule> secretCapsules = CapsuleFixture.capsules(10, member, capsuleSkin,
            CapsuleType.SECRET);
        secretCapsules.forEach(entityManager::persist);

        //사용자가 만든 캡슐 아이디들
        myCapsuleIds = Stream.concat(secretCapsules.stream(), friendCapsules.stream())
            .map(Capsule::getId)
            .toList();

        //친구
        List<Member> friends = MemberFixture.members(1, 20);
        friends.forEach(entityManager::persist);

        //친구가 만든 캡슐 아이디들
        friendIds = friends.stream()
            .map(Member::getId)
            .toList();

        //친구 관계 & 친구들의 캡슐
        friendCapsuleIds = new ArrayList<>();
        for (Member friend : friends) {
            MemberFriend memberFriend = MemberFriendFixture.memberFriend(member, friend);
            entityManager.persist(memberFriend);

            MemberFriend friendMember = MemberFriendFixture.memberFriend(friend, member);
            entityManager.persist(friendMember);

            CapsuleSkin friendSkin = CapsuleSkinFixture.capsuleSkin(friend);
            entityManager.persist(friendSkin);

            Capsule capsule = CapsuleFixture.capsule(friend, friendSkin, CapsuleType.PUBLIC);
            entityManager.persist(capsule);

            friendCapsuleIds.add(capsule.getId());
        }
    }

    @Test
    void 지도용_비밀_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.SECRET;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyCapsuleSummaryDto> capsules = capsuleQueryRepository.findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(capsuleType));
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void AR용_비밀_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.SECRET;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyARCapsuleSummaryDto> capsules = capsuleQueryRepository.findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(capsuleType));
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void 지도용_공개_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.PUBLIC;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyCapsuleSummaryDto> capsules = capsuleQueryRepository.findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(capsuleType));
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void AR용_공개_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.PUBLIC;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyARCapsuleSummaryDto> capsules = capsuleQueryRepository.findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(capsuleType));
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void 지도용_전체_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.ALL;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyCapsuleSummaryDto> capsules = capsuleQueryRepository.findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(
                c -> c.capsuleType().equals(CapsuleType.PUBLIC) ||
                    c.capsuleType().equals(CapsuleType.SECRET) ||
                    c.capsuleType().equals(CapsuleType.GROUP)
            );
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void AR용_전체_타입으로_사용자가_만든_캡슐을_조회하면_해당_타입에_맞는_사용자가_만든_캡슐만_나온다() {
        //given
        CapsuleType capsuleType = CapsuleType.ALL;
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyARCapsuleSummaryDto> capsules = capsuleQueryRepository.findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            memberId, mbr, capsuleType
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(
                c -> c.capsuleType().equals(CapsuleType.PUBLIC) ||
                    c.capsuleType().equals(CapsuleType.SECRET) ||
                    c.capsuleType().equals(CapsuleType.GROUP)
            );
            assertThat(capsules).allMatch(c -> myCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void 지도용_친구가_만든_캡슐을_조회하면_친구가_만든_캡슐만_나온다() {
        //given
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyCapsuleSummaryDto> capsules = capsuleQueryRepository.findFriendsCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
            friendIds, mbr
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> friendCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(CapsuleType.PUBLIC));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }

    @Test
    void AR용_친구가_만든_캡슐을_조회하면_친구가_만든_캡슐만_나온다() {
        //given
        Polygon mbr = geoTransformManager.getDistanceMBROf3857(point, 3);

        //when
        List<NearbyARCapsuleSummaryDto> capsules = capsuleQueryRepository.findFriendsARCapsulesByCurrentLocation(
            friendCapsuleIds, mbr
        );

        //then
        SoftAssertions.assertSoftly(softly -> {
            assertThat(capsules).allMatch(c -> friendCapsuleIds.contains(c.id()));
            assertThat(capsules).allMatch(c -> c.capsuleType().equals(CapsuleType.PUBLIC));
            assertThat(capsules).allMatch(c -> mbr.contains(c.point()));
        });
    }
}