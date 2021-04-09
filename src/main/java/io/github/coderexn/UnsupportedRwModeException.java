package io.github.coderexn;

/**
 * Thrown when some mode required not enabled.
 * @see RwMode
 *
 * @author hzy
 * @since 3/27/2021
 */
public class UnsupportedRwModeException extends RwFileException {
    /**
     * Construct an {@code UnsupportedRwModeException} with a message.
     * @param message A message string
     */
    public UnsupportedRwModeException(String message) {
        super(message);
    }
}
