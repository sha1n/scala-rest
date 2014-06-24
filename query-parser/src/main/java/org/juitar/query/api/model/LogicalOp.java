package org.juitar.query.api.model;

/**
 * @author sha1n
 * @since 6/12/14
 */
public enum LogicalOp {
    AND("and"),
    OR("or");

    private final String str;

    LogicalOp(String str) {
        this.str = str;
    }


    @Override
    public final String toString() {
        return str;
    }
}
