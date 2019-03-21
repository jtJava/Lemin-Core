package us.lemin.core.utils;

import com.google.common.base.Joiner;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {
    public static final Pattern URL_REGEX = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
    public static final Pattern IP_REGEX = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    public static String buildString(String[] args, int start) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }

    public static String joinListGrammaticallyWithJava(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.size() > 1
                ? String.join(", ", list.subList(0, list.size() - 1))
                .concat(String.format("%s and ", list.size() > 2 ? "," : ""))
                .concat(list.get(list.size() - 1))
                : list.get(0);
    }

    public static String joinListGrammaticallyWithGuava(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.size() > 1
                ? Joiner.on(", ").join(list.subList(0, list.size() - 1))
                .concat(String.format("%s and ", list.size() > 2 ? "," : ""))
                .concat(list.get(list.size() - 1))
                : list.get(0);
    }
}
