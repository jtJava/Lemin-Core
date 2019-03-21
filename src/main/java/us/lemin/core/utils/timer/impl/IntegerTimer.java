package us.lemin.core.utils.timer.impl;

import us.lemin.core.utils.time.TimeUtil;
import us.lemin.core.utils.timer.AbstractTimer;

import java.util.concurrent.TimeUnit;

public class IntegerTimer extends AbstractTimer {
    public IntegerTimer(TimeUnit unit, int amount) {
        super(unit, amount);
    }

    @Override
    public String formattedExpiration() {
        return TimeUtil.formatTimeMillis(expiry - System.currentTimeMillis());
    }

    @Override
    public String formattedClock() {
        return TimeUtil.formatTimeMillisToClock(expiry - System.currentTimeMillis());
    }
}
