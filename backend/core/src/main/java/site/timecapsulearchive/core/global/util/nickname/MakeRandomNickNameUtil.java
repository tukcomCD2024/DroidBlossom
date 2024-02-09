package site.timecapsulearchive.core.global.util.nickname;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class MakeRandomNickNameUtil {

    private static final long DIVIDED_UNIT = 1000;
    private static final int NUMBER_RANGE = 52;

    private MakeRandomNickNameUtil() {
    }

    public static String makeRandomNickName() {
        final List<String> nick = new ArrayList<>(RandomNickName.NICK.getValue());
        final List<String> name = new ArrayList<>(RandomNickName.NAME.getValue());

        final String uniqueNumber = String.valueOf(System.currentTimeMillis() % DIVIDED_UNIT);

        return nick.get(randomNumber()) + name.get(randomNumber()) + uniqueNumber;
    }

    private static int randomNumber() {
        return ThreadLocalRandom.current().nextInt(NUMBER_RANGE);
    }
}
