package site.timecapsulearchive.core.global.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public final class TagGenerator {

    private static final int SIZE = 6;
    private static final int BOUND = 10;
    private static final String EMAIL_DELIMITER = "@";
    private static final String HYPHEN = "-";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 이메일과 소셜 타입(대문자)으로 태그를 생성한다.
     *
     * @param email      이메일
     * @param socialType 소셜 타입
     * @return 생성된 태그 ex) {@code test1234@gmail.com, SocialType.GOOGLE} ->
     * {@code "test1234-123456GG"}
     */
    public static String generate(final String email, final SocialType socialType) {
        final String randomInts = generateRandomInts();

        final String[] splitEmail = email.split(EMAIL_DELIMITER);

        return splitEmail[0]
            + HYPHEN
            + randomInts
            + Character.toUpperCase(splitEmail[1].charAt(0))
            + socialType.name().charAt(0);
    }

    private static String generateRandomInts() {
        return secureRandom
            .ints(SIZE, 0, BOUND)
            .mapToObj(String::valueOf)
            .collect(Collectors.joining());
    }

    /**
     * 이메일과 소셜 타입(소문자)으로 태그를 생성한다.
     *
     * @param email      이메일
     * @param socialType 소셜 타입
     * @return 생성된 태그 ex) {@code test1234@gmail.com, SocialType.GOOGLE} ->
     * {@code "test1234-123456gg"}
     */
    public static String lowercase(final String email, final SocialType socialType) {
        final String randomInts = generateRandomInts();

        final String[] splitEmail = email.split(EMAIL_DELIMITER);

        return splitEmail[0]
            + HYPHEN
            + randomInts
            + Character.toLowerCase(splitEmail[1].charAt(0))
            + Character.toLowerCase(socialType.name().charAt(0));
    }
}
