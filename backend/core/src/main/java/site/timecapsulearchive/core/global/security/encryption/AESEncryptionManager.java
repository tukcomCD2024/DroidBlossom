package site.timecapsulearchive.core.global.security.encryption;

import io.jsonwebtoken.io.Decoders;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.error.exception.AESEncryptionException;

@Component
public final class AESEncryptionManager {
    private static final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final SecureRandom secureRandom = new SecureRandom();

    private final SecretKey key;

    public AESEncryptionManager(AESKeyProperties properties) {
        byte[] decodedKey = Decoders.BASE64.decode(properties.secretKey());
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public byte[] encryptWithPrefixIV(byte[] pText) throws AESEncryptionException {
        byte[] iv = getRandomNonce();
        byte[] cipherText = encrypt(pText, iv);

        return ByteBuffer.allocate(iv.length + cipherText.length)
            .put(iv)
            .put(cipherText)
            .array();
    }

    private byte[] encrypt(byte[] pText, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            return cipher.doFinal(pText);
        } catch (Exception e) {
            throw new AESEncryptionException(e);
        }
    }

    private byte[] getRandomNonce() {
        byte[] nonce = new byte[IV_LENGTH_BYTE];
        secureRandom.nextBytes(nonce);
        return nonce;
    }

    public String decryptWithPrefixIV(byte[] cText) throws AESEncryptionException {
        ByteBuffer bb = ByteBuffer.wrap(cText);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        return decrypt(cipherText, iv);
    }

    private String decrypt(byte[] cText, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            byte[] plainText = cipher.doFinal(cText);

            return new String(plainText, UTF_8);
        } catch (Exception e) {
            throw new AESEncryptionException(e);
        }
    }
}

