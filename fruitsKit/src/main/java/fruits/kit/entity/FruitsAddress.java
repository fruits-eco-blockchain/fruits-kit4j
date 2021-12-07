package fruits.kit.entity;

import java.util.Objects;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.util.FruitsKitUtils;

public final class FruitsAddress {

    /**
     * Stored without "FRUITS-" prefix.
     */
    private final String address;
    private final FruitsID numericID;
    private byte[] publicKey;

    private FruitsAddress(FruitsID fruitsID) {
        this.numericID = fruitsID;
        this.address = FruitsCrypto.getInstance().rsEncode(numericID);
    }

    /**
     * @param fruitsID The numeric id that represents this Fruits Address
     * @return A FruitsAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static FruitsAddress fromId(FruitsID fruitsID) {
        return new FruitsAddress(fruitsID);
    }

    /**
     * @param signedLongId The numeric id that represents this Fruits Address, as a signed long
     * @return A FruitsAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static FruitsAddress fromId(long signedLongId) {
        return new FruitsAddress(FruitsID.fromLong(signedLongId));
    }

    /**
     * @param unsignedLongId The numeric id that represents this Fruits Address
     * @return A FruitsAddress object that represents the specified numericId
     * @throws NumberFormatException if the numericId is not a valid number
     * @throws IllegalArgumentException if the numericId is outside the range of accepted numbers (less than 0 or greater than / equal to 2^64)
     */
    public static FruitsAddress fromId(String unsignedLongId) {
        return new FruitsAddress(FruitsID.fromLong(unsignedLongId));
    }

    public static FruitsAddress fromRs(String RS) throws IllegalArgumentException {
        for(String addressPrefix : FruitsKitUtils.getValidAddressPrefixes()) {
            if (RS.startsWith(addressPrefix+"-")) {
                RS = RS.substring(addressPrefix.length() + 1);
                break;
            }
        }
        // Remove the public key if present
        String shortRS = RS.substring(0, 29);
        FruitsAddress address = new FruitsAddress(FruitsCrypto.getInstance().rsDecode(shortRS));
        
        if(RS.length() > 30) {
            // check if there is a public key
            String publicKeyBase36 = RS.substring(30);
            byte []publicKey = FruitsCrypto.getInstance().parseBase36String(publicKeyBase36);
            
            FruitsAddress addressCheck = FruitsCrypto.getInstance().getFruitsAddressFromPublic(publicKey);
            if(addressCheck.getFruitsID().getSignedLongId() != address.getFruitsID().getSignedLongId()) {
                throw new IllegalArgumentException("Reed-Solomon address and public key mismatch");
            }
            address.setPublicKey(publicKey);
        }
        
        return address;
    }

    /**
     * Try to parse an input as either a numeric ID or an RS address.
     *
     * @param input the numeric ID or RS address of the Fruits address
     * @return a FruitsAddress if one could be parsed from the input, null otherwise
     */
    public static FruitsAddress fromEither(String input) {
        if (input == null) return null;
        try {
            return FruitsAddress.fromId(FruitsID.fromLong(input));
        } catch (IllegalArgumentException e1) {
            try {
                return FruitsAddress.fromRs(input);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    /**
     * @return The FruitsID of this address
     */
    public FruitsID getFruitsID() {
        return numericID;
    }

    /**
     * @return The unsigned long numeric ID this FruitsAddress points to
     */
    public String getID() {
        return numericID.getID();
    }

    /**
     * @return The signed long numeric ID this FruitsAddress points to
     */
    public long getSignedLongId() {
        return numericID.getSignedLongId();
    }
    
    /**
     * @return The public key or null if not set 
     */
    public byte[] getPublicKey() {
        return publicKey;
    }
    
    /**
     * @return The public key or null if not set 
     */
    public String getPublicKeyString() {
        return publicKey == null ? null : Hex.toHexString(publicKey);
    }

    /**
     * Sets the public key.
     *
     * @param publicKey the new public key
     */
    public void setPublicKey(byte []publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return The ReedSolomon encoded address, without the "FRUITS-" prefix
     */
    public String getRawAddress() {
        return address;
    }

    /**
     * @return The ReedSolomon encoded address, with the "FRUITS-" prefix
     */
    public String getFullAddress() {
        if (address == null || address.length() == 0) {
            return "";
        } else {
            return FruitsKitUtils.getAddressPrefix() +  "-" + address;
        }
    }
    
    /**
     * @return The ReedSolomon encoded address, with the prefix and base36 encoded public key suffix
     */
    public String getExtendedAddress() {
    	String extendedAddress = getFullAddress();
    	if(publicKey != null) {
    		extendedAddress += "-" + FruitsCrypto.getInstance().toBase36String(publicKey);
    	}
    	
    	return extendedAddress;
    }

    @Override
    public String toString() {
        return getFullAddress();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FruitsAddress && Objects.equals(numericID, ((FruitsAddress) obj).numericID);
    }

    @Override
    public int hashCode() {
        return numericID.hashCode();
    }
}
