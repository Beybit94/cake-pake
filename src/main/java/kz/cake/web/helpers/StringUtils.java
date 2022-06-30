package kz.cake.web.helpers;

import kz.cake.web.entity.Languages;
import kz.cake.web.helpers.constants.Language;
import kz.cake.web.service.LanguagesService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {
    public static String encryptPassword(String param) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = md.digest(param.getBytes(StandardCharsets.UTF_8));
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static String[] splitByUppercase(String param) {
        return param.split("(?=\\p{Upper})");
    }

    public static String getFieldName(String column) {
        if (!column.contains("_")) return column;

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

    public static String currencyFormat(BigDecimal price) {
        Locale locale;
        if(CurrentSession.Instance.getCurrentLanguageCode().equals(Language.en)){
            locale = new Locale(CurrentSession.Instance.getCurrentLanguageCode(),"US");
        }else{
            locale = new Locale(CurrentSession.Instance.getCurrentLanguageCode(),"RU");
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(price);
    }
}
