package kz.cake.web.helper;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class StringUtils {
    public static String getColumnName(String param) {
        String[] attributes = param.split("(?=\\p{Upper})");
        if (attributes.length <= 0) return param;
        return String.join("_", Arrays.stream(attributes).map(a -> a.toLowerCase()).collect(Collectors.toList()));
    }
}
