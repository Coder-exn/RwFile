package com.hzy.rwfile;

/**
 * @author hzy
 * @since 28/3/2021
 */
public enum RwMode {
    /**
     * Read mode 'r'.
     */
    READ("r"),
    /**
     * Write mode 'w'.
     */
    WRITE("w"),
    /**
     * Write mode with appendable 'a'.
     */
    APPEND("a")
    ;

    /**
     * The value of the mode.
     */
    private final String value;

    /**
     * A new RwMode instance.
     *
     * @param str String value.
     */
    RwMode(String str) {
        this.value = str;
    }

    /**
     *
     * @return The value string
     */
    public String getValue() {
        return this.value;
    }
}
