package pl.regzand.bibparser;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void scanToClosingBracket() {

        // create tests
        Map<String, String> tests = new HashMap<>();

        //@formatter:off

        // single brackets body
        tests.put("}",              "");
        tests.put("abc def}",       "abc def");
        tests.put("{foo}bar}",      "{foo}bar");
        tests.put("{foo}{bar}}",    "{foo}{bar}");
        tests.put("{foo}\"bar\"}",  "{foo}\"bar\"");
        tests.put("\"{ad}\"}",      "\"{ad}\"");

        // multiple brackets bodies
        tests.put("aaa}{bbb}",      "aaa");
        tests.put("{xyz}se}{a{a}a}","{xyz}se");
        tests.put("a}{b}{c}{d}",    "a");

        // no closing brackets
        tests.put("",               "");
        tests.put("aa",             "aa");
        tests.put("{aaa} bbb",      "{aaa} bbb");

        //@formatter:on

        // run tests
        tests.forEach((k, v) -> assertEquals(v, Utils.scanToClosingBracket(new Scanner(k))));

    }

    @Test
    void hasCommon() {

        // empty arrays
        assertFalse(Utils.hasCommon(new String[]{}, new String[]{}));
        assertFalse(Utils.hasCommon(new String[]{"foo", "bar"}, new String[]{}));
        assertFalse(Utils.hasCommon(new String[]{}, new String[]{"foo", "bar"}));

        // arrays without common element
        assertFalse(Utils.hasCommon(new String[]{"a", "b", "c"}, new String[]{"aa", "zz"}));
        assertFalse(Utils.hasCommon(new String[]{"A", "b ddd", "c"}, new String[]{"x", "y", "z", "a"}));

        // arrays with common elements
        assertTrue(Utils.hasCommon(new String[]{"a", "b", "c"}, new String[]{"x", "b", "t"}));
        assertTrue(Utils.hasCommon(new String[]{"a", "b", "c"}, new String[]{"z", "c", "a"}));
        assertTrue(Utils.hasCommon(new String[]{"a", "b", "c"}, new String[]{"w", "t", "c"}));

    }

    @Test
    void parseName() {

        // create tests
        Map<String, String> tests = new HashMap<>();

        //@formatter:off

        // first pattern
        tests.put("AA BB",              "AA BB");
        tests.put("AA",                 "AA");
        tests.put("AA bb",              "AA bb");
        tests.put("aa",                 "aa");
        tests.put("AA bb CC",           "AA CC");
        tests.put("AA bb CC dd EE",     "AA EE");

        // second pattern
        tests.put("bb CC, AA",          "AA CC");
        tests.put("bb CC, aa",          "aa CC");
        tests.put("bb CC dd EE, AA",    "AA EE");
        tests.put("bb, AA",             "AA bb");
        tests.put("BB,",                "BB");

        // third pattern
        tests.put("bb CC,XX, AA",       "AA CC");
        tests.put("bb CC,xx, AA",       "AA CC");
        tests.put("BB,, AA",            "AA BB");

        //@formatter:on

        // run tests
        tests.forEach((k, v) -> assertEquals(v, Utils.parseName(k)));

    }

}