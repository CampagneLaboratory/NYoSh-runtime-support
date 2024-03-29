package org.campagnelab.nyosh.gstring.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabien Campagne
 *         Date: 6/12/13
 *         Time: 8:37 PM
 */
public class PatternMatcher {
    public static PatternMatcher matchVariables() {
        PatternMatcher matcher = new PatternMatcher();
        matcher.pattern = "\\$\\{([^\\$\\{\\}]+)\\}";
        return matcher;
    }

    public static PatternMatcher matchOperators() {
        PatternMatcher matcher = new PatternMatcher();
        matcher.pattern = "(\\|\\||\\&\\&|\\;)";
        return matcher;
    }

    /**
     * Match a set of operators. For instance, {";","&&","||"} will match three BASH operators
     *
     * @param operators array of operators, characters are non-escaped.
     * @return The matcher configured with the operator pattern.
     */
    public static PatternMatcher matchOperators(String[] operators) {
        PatternMatcher matcher = new PatternMatcher();
        String sb = buildPattern(operators);
        matcher.pattern = sb;
        return matcher;
    }

    public static String buildPattern(String[] operators) {
        StringBuffer sb = new StringBuffer("((");
        boolean first = true;
        for (String operator : operators) {
            if (!first) {
                sb.append(")|(");

            }
            for (char c : operator.toCharArray()) {
                sb.append("\\" + c);
            }
            first = false;
        }
        sb.append("))");
        return sb.toString();
    }

    private String pattern = "\\$\\{([^\\$\\{\\}]+)\\}";

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<Component> split(String value) {
        Matcher m = null;
        Pattern p;
        p = Pattern.compile(pattern);

        List<Component> result = new ArrayList<Component>();
        m = p.matcher(value);
        int lastEnd = 0;
        String[] literals = p.split(value);
        int literalIndex = 0;
        while (m.find()) {
            String group1 = m.group(1);
            String literal = "";
            // string group2 = m.group(2);
            //info "group2: " + group2;
            if (literals.length > literalIndex && literals[literalIndex].length() > 0) {
                literal = literals[literalIndex];

                result.add(Component.createLiteral(literal));
            }
            result.add(Component.createVarRef(group1));


            literalIndex++;
        }
        if (literals.length > literalIndex && literals[literalIndex].length() > 0) {
            String literal = literals[literalIndex];
            result.add(Component.createLiteral(literal));
        }
        return result;
    }


}
