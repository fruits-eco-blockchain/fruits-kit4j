package fruits.kit.entity;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import fruits.kit.crypto.FruitsCrypto;

/**
 * The Class FruitsTimestamp.
 */
public final class FruitsTimestamp {
    
    /** The timestamp. */
    private final int timestamp;
    
    /** The date. */
    private final Date date;

    /**
     * Instantiates a new fruits timestamp.
     *
     * @param timestamp The Fruits Epoch Time (number of seconds since Fruits epoch)
     */
    private FruitsTimestamp(int timestamp) {
        this.timestamp = timestamp;
        this.date = FruitsCrypto.getInstance().fromFruitsTimeToDate(timestamp);
    }

    /**
     * Instantiates a new fruits timestamp.
     *
     * @param date The Java Date object to be represented
     */
    private FruitsTimestamp(Date date) {
        this.timestamp = FruitsCrypto.getInstance().toFruitsTime(date);
        this.date = date;
    }

    /**
     * From fruits timestamp.
     *
     * @param secondsSinceEpoch The Fruits Epoch Time (number of seconds since Fruits epoch)
     * @return the fruits timestamp
     */
    public static FruitsTimestamp fromFruitsTimestamp(int secondsSinceEpoch) {
        return new FruitsTimestamp(secondsSinceEpoch);
    }

    /**
     * From date.
     *
     * @param date The Java Date object to be represented
     * @return the fruits timestamp
     */
    public static FruitsTimestamp fromDate(Date date) {
        return new FruitsTimestamp(date);
    }

    /**
     * Now.
     *
     * @return the fruits timestamp
     */
    public static FruitsTimestamp now() {
        return new FruitsTimestamp(FruitsCrypto.getInstance().currentFruitsTime());
    }

    /**
     * Gets the timestamp.
     *
     * @return The fruits timestamp (number of seconds since Fruits epoch)
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the as date.
     *
     * @return The timestamp as a pre Java 8 Date object, mainly intended for Android usage as older versions of Android do not have the Java 8 Time API
     */
    public Date getAsDate() {
        return date;
    }

    /**
     * Gets the as instant.
     *
     * @return The timestamp as a Java 8 Instant
     */
    public Instant getAsInstant() {
        return date.toInstant();
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(timestamp);
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FruitsTimestamp && Objects.equals(timestamp, ((FruitsTimestamp)obj).timestamp);
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return String.valueOf(getTimestamp());
    }
}
