package org.juitar.query.api.model;

/**
 * @author sha1n
 * @since 6/13/14
 */
public enum OrderDirection {
    ASC("asc"),
    DESC("desc");

    private final String str;

    OrderDirection(String str) {
        this.str = str;

    }

    @Override
    public final String toString() {
        return str;
    }
}
