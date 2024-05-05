package site.timecapsulearchive.core.common.fixture.domain;

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
     * <code>Member</code> 에 <code>dataPrefix</code> 를 붙여서 만들어진 Member를 반환한다
     * <p>
     * <b><u>주의</u> </b> - 하나의 테스트에서 여러 개의 멤버를 생성한다면 서로 다른 <code>dataPrefix</code> 필요
     *
     * @param dataPrefix 생성될 <code>Member</code> 에 붙일 prefix
     * @return <code>dataPrefix</code>가 붙어서 생성된 <code>Member</code>
     */
    public static Member member(int dataPrefix) {
        byte[] number = getPhoneBytes(dataPrefix);

        Member member = Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname(dataPrefix + "testNickname")
            .email(dataPrefix + "test@google.com")
            .authId(dataPrefix + "test")
            .profileUrl(dataPrefix + "test.com")
            .tag(dataPrefix + "testTag")
            .phone_hash(hashEncryptionManager.encrypt(number))
            .build();

        return member;
    }

    public static byte[] getPhoneBytes(int dataPrefix) {
        return ("0" + (1000000000 + dataPrefix)).getBytes(StandardCharsets.UTF_8);
    }

    public static List<ByteArrayWrapper> getPhones(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new ByteArrayWrapper(MemberFixture.getPhoneBytes(count)))
            .toList();
    }

    private static byte[] getPhoneBytes(String phone) {
        return hashEncryptionManager.encrypt(phone.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <code>Member</code> 에 <code>start</code>가 <code>count</code>까지
     * 하나씩 증가된 것을 붙여서 만들어진 Member를 반환한다
     * <p>
     * <b><u>주의</u> </b> - 하나의 테스트에서 여러 개의 멤버를 생성한다면 서로 다른 <code>dataPrefix</code> 필요
     *
     * @param start 시작할 prefix
     * @param count 반환받을 <code>Member</code> 개수
     * @return start가 하나씩 증가되어 붙여서 만들어진 <code>Member</code>들
     */
    public static List<Member> members(int start, int count) {
        List<Member> result = new ArrayList<>();
        for (int index = start; index < start + count; index++) {
            result.add(member(index));
        }

        return result;
    }

    public static List<byte[]> getPhoneBytesList(int start, int count) {
        List<byte[]> result = new ArrayList<>();
        for (int index = start; index < start + count; index++) {
            result.add(getPhoneBytes(index));
        }

        return result;
    }
}