package org.campagnelab.nyosh.gstring.patterns;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Fabien Campagne
 *         Date: 6/12/13
 *         Time: 8:44 PM
 */
public class PatternMatcherTest {
   PatternMatcher matcher=new PatternMatcher();
    @org.junit.Test
    public void testSplit() throws Exception {
         assertEquals("ref=a ref=b ",convert(matcher.split("${a}${b}")));
         assertEquals("ref=a literal=  ref=b ",convert(matcher.split("${a} ${b}")));
         assertEquals("ref=a literal=   ref=b ",convert(matcher.split("${a}  ${b}")));
         assertEquals("ref=a ref=b literal=   ref=c literal=AAA ref=d ",convert(matcher.split("${a}${b}  ${c}AAA${d}")));
    }
    public String convert(List<Component> components) {

       StringBuilder
               builder=new StringBuilder();
        for (Component c: components) {
            builder.append(
                    c.isVarRef()? "ref=": "literal=");
            builder.append(c.getPayLoad());
            builder.append(" ");



        }
        return builder.toString();
    }

}
