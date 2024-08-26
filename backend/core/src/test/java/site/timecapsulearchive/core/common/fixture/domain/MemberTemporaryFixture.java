package site.timecapsulearchive.core.common.fixture.domain;

import java.nio.charset.StandardCharsets;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;

public class MemberTemporaryFixture {

    private static final AESEncryptionManager aesEncryptionManager = UnitTestDependency.aesEncryptionManager();
    private static final HashEncryptionManager hashEncryptionManager = UnitTestDependency.hashEncryptionManager();

    public static MemberTemporary memberTemporary(Long memberId) {
        byte[] emailBytes = "test_email@gmail.com".getBytes(StandardCharsets.UTF_8);

        return MemberTemporary.builder()
            .authId(memberId + "test_auth_id")
            .tag(memberId + "test_tag")
            .email(aesEncryptionManager.encryptWithPrefixIV(emailBytes))
            .emailHash(hashEncryptionManager.encrypt(emailBytes))
            .nickname(memberId + "test_nickname")
            .profileUrl(memberId + "test_profile_url")
            .socialType(SocialType.GOOGLE)
            .build();
    }
}
