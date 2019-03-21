package us.lemin.core.utils.timer;

public interface Timer {
    boolean isActive(boolean autoReset);

    boolean isActive();

    String formattedExpiration();

    String formattedClock();

    void reset();
}
