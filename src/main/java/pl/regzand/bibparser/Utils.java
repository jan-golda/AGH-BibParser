package pl.regzand.bibparser;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All-static utility class
 */
public class Utils {

    // ^(?<first>(?:[A-Z][^,\s]*\s+)+)?(?<von>(?:[^,\s]+\s+)+)?(?<last>(?:[^,\s]+\s*)+)$
    private static final Pattern NAME1_PATTERN = Pattern.compile("^(?<first>(?:[A-Z][^,\\s]*\\s+)+)?(?<von>(?:[^,\\s]+\\s+)+)?(?<last>(?:[^,\\s]+\\s*)+)$");

    // ^(?<von>(?:\S+\s+)*(?:[^A-Z]\S*\s+))?(?<last>[^,]+),(?:(?<jr>.+)?,)?(?<first>[^,]+)?$
    private static final Pattern NAME2_PATTERN = Pattern.compile("^(?<von>(?:\\S+\\s+)*(?:[^A-Z]\\S*\\s+))?(?<last>[^,]+),(?:(?<jr>.+)?,)?(?<first>[^,]+)?$");

    /**
     * Returns if there is at least one equal string in A and B.
     *
     * @param A first array of values
     * @param B second array of values
     *
     * @return if there is at least one equal string in A and B
     */
    public static boolean hasCommon(String[] A, String[] B) {
        for (String a : A)
            for (String b : B)
                if (a.equals(b))
                    return true;
        return false;
    }

    /**
     * <p>Returns given name in format <code>FirstName LastName</code>.</p>
     * <pre>Acceptable formats:
     *   First von Last
     *   von Last, First
     *   von Last, Jr, First
     * </pre>
     *
     * @param name string to be parsed
     *
     * @return name in <code>FirstName LastName</code> format, or original name value if name pattern was not recognised
     */
    public static String parseName(String name) {
        name = name.trim();

        // try first pattern
        Matcher matcher = NAME1_PATTERN.matcher(name);

        // if needed try second pattern
        if (!matcher.matches())
            matcher = NAME2_PATTERN.matcher(name);

        // if there still is no match
        if (!matcher.matches())
            return name;

        String first = matcher.group("first");
        String last = matcher.group("last");

        if (first == null)
            return last.trim();

        return first.trim() + " " + last.trim();
    }

    /**
     * Returns body of first <code>{}</code> brackets found in scanner.
     *
     * @param scanner containing text with brackets (without first bracket), will be modified
     *
     * @return body of first <code>{}</code> brackets found in scanner
     */
    public static String scanToClosingBracket(Scanner scanner) {
        StringBuilder out = new StringBuilder();

        // set bracket index
        int index = 1;

        // while still inside outer brackets
        while (index > 0) {

            // if there is nothing on input, return
            if (!scanner.hasNext())
                return out.toString();

            // get next character
            char c = scanner.findWithinHorizon(".", 0).charAt(0);

            // update index
            if (c == '{')
                index++;
            if (c == '}')
                index--;

            // add character
            out.append(c);
        }

        // delete closing bracket
        out.deleteCharAt(out.length() - 1);

        // return bracket body
        return out.toString();
    }

}
