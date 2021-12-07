package fruits.kit.entity;

import java.util.Objects;

/**
 * The Class FruitsID.
 */
public final class FruitsID {
    /**
     * Stored as a signed long (because java) but should be used as an unsigned long
     * using Convert.toUnsignedLong and Convert.parseUnsignedLong (FRS methods)
     */
    private final long id;

    /**
     * Instantiates a new fruits ID.
     *
     * @param unsignedLongId The numeric ID of the transaction, account, block, etc.
     */
    private FruitsID(String unsignedLongId) {
        this.id = Long.parseUnsignedLong(unsignedLongId);
    }

    /**
     * Instantiates a new fruits ID.
     *
     * @param signedLongID The numeric ID of the transaction, account, block, etc as a signed long - they are normally expressed as an unsigned long.
     */
    private FruitsID(long signedLongID) {
        this.id = signedLongID;
    }

    /**
     * From long.
     *
     * @param unsignedLongId The numeric ID of the transaction, account, block, etc.
     * @return the fruits ID
     */
    public static FruitsID fromLong(String unsignedLongId) {
        if (unsignedLongId == null) return null;
        return new FruitsID(unsignedLongId);
    }

    /**
     * From long.
     *
     * @param signedLongId The numeric ID of the transaction, account, block, etc.
     * @return the fruits ID
     */
    public static FruitsID fromLong(long signedLongId) {
        return new FruitsID(signedLongId);
    }

    /**
     * Gets the id.
     *
     * @return The unsigned long numeric ID
     */
    public String getID() {
        return Long.toUnsignedString(id);
    }

    /**
     * Gets the signed long id.
     *
     * @return The signed long ID
     */
    public long getSignedLongId() {
        return id;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return getID();
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof FruitsID && Objects.equals(getID(), ((FruitsID) obj).getID());
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
