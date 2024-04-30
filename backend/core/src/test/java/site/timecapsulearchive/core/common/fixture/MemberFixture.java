package site.timecapsulearchive.core.common.fixture;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

public class MemberFixture {

    private static final HashEncryptionManager hashEncryptionManager = UnitTestDependency.hashEncryptionManager();

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