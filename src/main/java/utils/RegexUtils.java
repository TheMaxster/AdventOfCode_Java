package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The RegexUtils for the AoC setup.
 *
 * @author mglembock
 */
public class RegexUtils {

    public static String match(
            final String regex,
            final String input
    ) {
        final var m = Pattern.compile(regex).matcher(input);

        return m.find() ? m.group() : null;
    }

    public static List<String> matchAll(
            final String regex,
            final String input
    ) {
        final var m = Pattern.compile(regex).matcher(input);
        final var list = new ArrayList<String>();

        while (m.find()) {
            list.add(m.group());
        }

        return list;
    }

    public static boolean matches(
            final String regex,
            final String input
    ) {
        return Pattern.compile(regex).matcher(input).find();
    }

    public static List<String> matchGroups(
            final String regex,
            final String input
    ) {
        final var m = Pattern.compile(regex).matcher(input);
        final var l = new ArrayList<String>();

        if (!m.find()) {
            return null;
        }

        for (var i = 1; i <= m.groupCount(); i++) {
            final var val = m.group(i);

            if (val != null) {
                l.add(val);
            }
        }

        return l;
    }

}
