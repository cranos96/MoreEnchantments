package cn.feng.enchant.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class RandomUtil {
    public static int randomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
