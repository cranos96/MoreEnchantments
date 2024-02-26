package cn.feng.enchant.util;

/**
 * @author ChengFeng
 * @since 2024/2/25
 **/
public class TimerUtil {
    private long last;
    public TimerUtil() {
        reset();
    }

    public boolean hasDelayed(long ms) {
        return System.currentTimeMillis() - last >= ms;
    }

    public void reset() {
        last = System.currentTimeMillis();
    }

    public long getLast() {
        return last;
    }
}
