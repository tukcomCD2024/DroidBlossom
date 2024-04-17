package site.timecapsulearchive.core.common.fixture;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
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

    public static List<byte[]> getPhones() {
        return List.of(
            MemberFixture.getPhoneBytes("01012341234"),
            MemberFixture.getPhoneBytes("01012341235"),
            MemberFixture.getPhoneBytes("01012341236"),
            MemberFixture.getPhoneBytes("01012341237"),
            MemberFixture.getPhoneBytes("01012341238"),
            MemberFixture.getPhoneBytes("01012341239"),
            MemberFixture.getPhoneBytes("01012341240"),
            MemberFixture.getPhoneBytes("01012341241")
        );
    }

    private static byte[] getPhoneBytes(String phone) {
        return hashEncryptionManager.encrypt(phone.getBytes(StandardCharsets.UTF_8));
    }

}