package kz.cake.web.helper;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class StringUtils {
    public static String getColumnName(String param) {
        String[] attributes = param.split("(?=\\p{Upper})");
        if (attributes.length <= 0) return param;
        return String.join("_", Arrays.stream(attributes).map(a -> a.toLowerCase()).collect(Collectors.toList()));
    }

    public static String getFieldName(String column) {
        String[] attributes = column.split("_");
        return attributes[0] + Character.toUpperCase(attributes[1].charAt(0)) + attributes[1].substring(1);
    }

    public static String skipFirstElement(String param) {
        String[] arr = param.split(",");
        String result = "";
        for (int i = 1; i < arr.length; i++) {
            if (i == arr.length - 1) {
                result += arr[i];
            } else {
                result += String.format("%s,", arr[i]);
            }
        }
        return result;
    }
}
