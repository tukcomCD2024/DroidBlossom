package site.timecapsulearchive.core.common.fixture;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

public class MemberFixture {

    private static final HashEncryptionManager hashEncryptionManager = UnitTestDependency.hashEncryptionManager();

    /**
     * 테스트 픽스처 - 멤버 마다 상이한 값을 위한 dataPrefix를 주면 멤버를 생성한다.
     * <br><u><b>주의</b></u> - 테스트에서 같은 prefix를 사용하면 오류가 발생하므로 서로 다른 prefix를 쓰도록 해야함.
     * @param dataPrefix prefix
     * @return {@code Member} 테스트 픽스처
     */
    public static Member member(int dataPrefix) {
        Member member = Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname(dataPrefix + "testNickname")
            .email(dataPrefix + "test@google.com")
            .authId(dataPrefix + "test")
            .profileUrl(dataPrefix + "test.com")
            .tag(dataPrefix + "testTag")
            .build();

        byte[] number = getPhoneBytes(dataPrefix);
        member.updatePhoneHash(hashEncryptionManager.encrypt(number));

        return member;
    }

    /**
     * 테스트 픽스처 - 멤버마다 상이한 번호를 위해 dataPrefix를 주면 해당 dataPrefix에 대한 핸드폰 번호 바이트를 반환한다.
     * <br><u><b>주의</b></u> - 테스트에서 같은 prefix를 사용하면 오류가 발생하므로 서로 다른 prefix를 쓰도록 해야함.
     * @param dataPrefix prefix
     * @return 핸드폰 번호 바이트
     */
    public static byte[] getPhoneBytes(int dataPrefix) {
        return ("0" + (1000000000 + dataPrefix)).getBytes(StandardCharsets.UTF_8);
    }

    public static List<ByteArrayWrapper> getPhones() {
        return List.of(
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341234")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341235")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341236")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341237")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341238")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341239")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341240")),
            new ByteArrayWrapper(MemberFixture.getPhoneBytes("01012341241"))
        );
    }

    private static byte[] getPhoneBytes(String phone) {
        return hashEncryptionManager.encrypt(phone.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 테스트 픽스처 - 크기와 멤버 마다 상이한 값을 위한 startDataPrefix를 주면 멤버들을 생성한다.
     * <br><u><b>주의</b></u> - 테스트에서 같은 prefix를 사용하면 오류가 발생하므로 서로 다른 prefix를 쓰도록 해야함.
     * @param size 생성할 멤버의 크기
     * @param startDataPrefix 시작 prefix
     * @return {@code List<Member>} 테스트 픽스처들
     */
    public static List<Member> members(int size, int startDataPrefix) {
        List<Member> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(member(startDataPrefix++));
        }

        return result;
    }
}