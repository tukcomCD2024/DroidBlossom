package site.timecapsulearchive.core.global.security.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.error.exception.EncryptionException;

@Component
@RequiredArgsConstructor
public class HashEncryptionManager {

    private static final String HASH_ALGORITHM = "SHA-256";

    private final HashProperties hashProperties;

    public byte[] encrypt(byte[] plaintText) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        }

        digest.update(hashProperties.salt().getBytes());

        return digest.digest(plaintText);
    }
}
