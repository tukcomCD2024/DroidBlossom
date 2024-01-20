package site.timecapsulearchive.core.domain.member.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MakeRandomNickNameUtil {

    private static final long DIVIDED_UNIT = 1000;
    private static final int FIRST_VALUE = 0;

    private MakeRandomNickNameUtil() {
    }


    public static String makeRandomNickName() {
        List<String> nick = new ArrayList<>(RandomNickName.NICK.getValue());
        List<String> name = new ArrayList<>(RandomNickName.NAME.getValue());
        Collections.shuffle(nick);
        Collections.shuffle(name);

        // 0 ~ 999
        String uniqueNumber = String.valueOf(System.currentTimeMillis() % DIVIDED_UNIT);

        return nick.get(FIRST_VALUE) + name.get(FIRST_VALUE) + uniqueNumber;
    }
}
