package org.campagnelab.nyosh.gstring.patterns;

/**
 * @author Fabien Campagne
 *         Date: 6/12/13
 *         Time: 8:37 PM
 */
public class Component {

    private boolean isPattern;
    private String payLoad;

    public String getPayLoad() {
        return payLoad;
    }

    public static Component createVarRef(String variableName) {
        Component c = new Component();
        c.payLoad = variableName;
        c.isPattern = true;
        return c;
    }

    public static Component createLiteral(String literal) {
        Component c = new Component();
        c.payLoad = literal;
        c.isPattern = false;
        return c;

    }

    public boolean isPattern() {

        return isPattern;
    }
}
