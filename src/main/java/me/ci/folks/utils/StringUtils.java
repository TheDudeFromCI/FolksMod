package me.ci.folks.utils;

public final class StringUtils {

    public static String toSnakeCase(String s) {
        StringBuilder sb = new StringBuilder();

        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (i > 0 && Character.isUpperCase(c[i])
                && !(i + 1 < c.length && Character.isUpperCase(c[i])))
                sb.append('_');

            sb.append(Character.toLowerCase(c[i]));
        }

        return sb.toString();
    }

    private StringUtils() {
    }
}
