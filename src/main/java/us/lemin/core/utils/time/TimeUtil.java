package us.lemin.core.utils.time;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class TimeUtil {
    public static String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0) {
            return "0 seconds";
        }

        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long day = hours / 24;
        hours = hours % 24;
        long years = day / 365;
        day = day % 365;

        StringBuilder time = new StringBuilder();

        if (years != 0) {
            time.append(years).append(years == 1 ? " year " : " years ");
        }

        if (day != 0) {
            time.append(day).append(day == 1 ? " day " : " days ");
        }

        if (hours != 0) {
            time.append(hours).append(hours == 1 ? " hour " : " hours ");
        }

        if (minutes != 0) {
            time.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }

        if (seconds != 0) {
            time.append(seconds).append(seconds == 1 ? " second " : " seconds ");
        }

        return time.toString().trim();
    }

    private String parseTimeSpec(long time, String spec) {
        return time + " " + (time == 1 ? spec : spec + "s") + " ";
    }

    public String formatTimeSeconds(long seconds) {
        return formatTimeMillis(seconds * 1000);
    }

    public String formatTimeMillisToClock(long millis) {
        return millis / 1000L <= 0 ? "0:00" : String.format("%01d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public String formatTimeSecondsToClock(long seconds) {
        return formatTimeMillisToClock(seconds * 1000);
    }

    public static long parseTime(String time) {
        long totalTime = 0L;
        boolean found = false;
        Matcher matcher = Pattern.compile("\\d+\\D+").matcher(time);

        while (matcher.find()) {
            String s = matcher.group();
            Long value = Long.parseLong(s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0]);
            String type = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];

            switch (type) {
                case "s": {
                    totalTime += value;
                    found = true;
                    continue;
                }
                case "m": {
                    totalTime += value * 60L;
                    found = true;
                    continue;
                }
                case "h": {
                    totalTime += value * 60L * 60L;
                    found = true;
                    continue;
                }
                case "d": {
                    totalTime += value * 60L * 60L * 24L;
                    found = true;
                    continue;
                }
                case "w": {
                    totalTime += value * 60L * 60L * 24L * 7L;
                    found = true;
                    continue;
                }
                case "mo": {
                    totalTime += value * 60L * 60L * 24L * 30L;
                    found = true;
                    continue;
                }
                case "y": {
                    totalTime += value * 60L * 60L * 24L * 365L;
                    found = true;
                }
            }
        }

        return found ? (totalTime * 1000L) + 1000L : -1L;
    }
    /*public int parseTime(String time) {
        if (time.equals("0") || time.equals("")) {
            return 0;
        }

        String[] lifeMatch = new String[]{"y", "M", "w", "d", "h", "m", "s"};
        int[] lifeInterval = new int[]{31536000, 2592000, 604800, 86400, 3600, 60, 1};
        int seconds = 0;

        for (int i = 0; i < lifeMatch.length; i++) {
            Matcher matcher = Pattern.compile("([0-9]*)" + lifeMatch[i]).matcher(time);

            while (matcher.find()) {
                seconds += Integer.parseInt(matcher.group(1)) * lifeInterval[i];
            }
        }

        return seconds;
    }*/

    public String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;

        if (toDate.equals(fromDate)) {
            return "now";
        } else {
            if (toDate.after(fromDate)) {
                future = true;
            }

            StringBuilder sb = new StringBuilder();
            int[] types = new int[]{1, 2, 5, 11, 12, 13};
            String[] names = new String[]{"year", "years", "month", "months", "day", "days", "hour", "hours",
                    "minute", "minutes", "second", "seconds"};
            int accuracy = 0;

            for (int i = 0; i < types.length && accuracy <= 2; ++i) {
                int diff = dateDiff(types[i], fromDate, toDate, future);

                if (diff > 0) {
                    ++accuracy;
                    sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
                }
            }

            return sb.length() == 0 ? "now" : sb.toString().trim();
        }
    }

    private int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;

        long savedDate;

        for (savedDate = fromDate.getTimeInMillis(); future && !fromDate.after(toDate) || !future && !fromDate.before
                (toDate); ++diff) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
        }

        --diff;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }
}