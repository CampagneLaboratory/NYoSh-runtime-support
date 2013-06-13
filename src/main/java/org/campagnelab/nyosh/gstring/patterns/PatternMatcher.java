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
    public static List<Component> split(String value) {
        Matcher m = null;
        Pattern p;
        p = Pattern.compile("\\$\\{([^\\$\\{\\}]+)\\}");

        List<Component> result = new ArrayList<Component>();
        m = p.matcher(value);
        int lastEnd = 0;
        String[] literals = p.split(value);
        int literalIndex = 0;
        while (m.find()) {
            String group1 = m.group(1);
            String literal="";
            // string group2 = m.group(2);
            //info "group2: " + group2;
            if (literals.length > literalIndex && literals[literalIndex].length() > 0) {
                literal = literals[literalIndex];

                result.add(Component.createLiteral(literal));
            }
            System.out.println("group1: " + group1 + " separator: " + literal);

            result.add(Component.createVarRef(group1));


            literalIndex++;
        }
        return result;
    }


}
