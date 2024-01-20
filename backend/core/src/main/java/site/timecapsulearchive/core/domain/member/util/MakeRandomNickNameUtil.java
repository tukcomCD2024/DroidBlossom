package site.timecapsulearchive.core.domain.member.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class MakeRandomNickNameUtil {

    private static final long DIVIDED_UNIT = 1000;
    private static final int NUMBER_RANGE = 52;

    private MakeRandomNickNameUtil() {
    }


    public static String makeRandomNickName() {
        List<String> nick = new ArrayList<>(RandomNickName.NICK.getValue());
        List<String> name = new ArrayList<>(RandomNickName.NAME.getValue());

        // 0 ~ 999
        String uniqueNumber = String.valueOf(System.currentTimeMillis() % DIVIDED_UNIT);

        return nick.get(randomNumber()) + name.get(randomNumber()) + uniqueNumber;
    }

    private static int randomNumber() {
        return ThreadLocalRandom.current().nextInt(NUMBER_RANGE);
    }
}
