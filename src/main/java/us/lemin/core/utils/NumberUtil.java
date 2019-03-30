package us.lemin.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {
    public Integer getInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
