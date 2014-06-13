package org.juitar.query.api.model;

/**
 * @author sha1n
 * @since 6/12/14
 */
public enum CompConditionOp {
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    EQ("="),
    NE("!=");

    private final String str;

    private CompConditionOp(String str) {
        this.str = str;
    }

    @Override
    public final String toString() {
        return str;
    }
}
