package org.campagnelab.nyosh.gstring.patterns;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Fabien Campagne
 *         Date: 6/12/13
 *         Time: 8:44 PM
 */
public class PatternMatcherTest {

    @org.junit.Test
    public void testSplit() throws Exception {
        PatternMatcher matcher = PatternMatcher.matchVariables();
        assertEquals("ref=a literal=  ref=b literal=...", convert(matcher.split("${a} ${b}...")));

        assertEquals("ref=a ref=b", convert(matcher.split("${a}${b}")));
        assertEquals("ref=a literal=  ref=b", convert(matcher.split("${a} ${b}")));
        assertEquals("ref=a literal=   ref=b", convert(matcher.split("${a}  ${b}")));
        assertEquals("ref=a ref=b literal=   ref=c literal=AAA ref=d", convert(matcher.split("${a}${b}  ${c}AAA${d}")));
    }

    @org.junit.Test
    public void testSplitOperators() throws Exception {
        final String[] operators = {"A", "B"};
        assertEquals("((\\A)|(\\B))", PatternMatcher.buildPattern(operators));
        PatternMatcher matcher = PatternMatcher.matchOperators();

        assertEquals("literal=a ref=; literal=b ref=|| literal=c d", convert(matcher.split("a;b||c d")));

    }

    public String convert(List<Component> components) {

        StringBuilder
                builder = new StringBuilder();
        for (Component c : components) {
            builder.append(
                    c.isVarRef() ? "ref=" : "literal=");
            builder.append(c.getPayLoad());
            builder.append(" ");
        }
        final String s = builder.toString();
        return s.substring(0, s.length()-1);
    }

}
