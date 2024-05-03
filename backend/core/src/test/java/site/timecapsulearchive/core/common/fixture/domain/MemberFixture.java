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