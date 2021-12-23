package gov.nsa.kore.ng.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RegexUtil {
    public static final Predicate<String> WHITESPACE = Pattern.compile("^\\s*$").asMatchPredicate();
    public static final Predicate<String> DOUBLE = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*").asMatchPredicate();
    public static final Predicate<String> BOOL = Pattern.compile("(?i)(true|yes|false|no)").asMatchPredicate();
    public static final Predicate<String> TRUE = Pattern.compile("(?i)(true|yes)").asMatchPredicate();
}
