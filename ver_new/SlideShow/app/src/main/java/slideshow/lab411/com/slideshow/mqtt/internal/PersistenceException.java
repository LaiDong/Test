package slideshow.lab411.com.slideshow.mqtt.internal;

/**
 * Created by ld on 07/02/2018.
 */

public class PersistenceException extends Exception {

    /**
     * Creates a persistence exception with the given error message
     * @param message The error message to display
     */
    public PersistenceException(String message) {
        super(message);
    }

    /** Serialisation ID**/
    private static final long serialVersionUID = 5326458803268855071L;

}
