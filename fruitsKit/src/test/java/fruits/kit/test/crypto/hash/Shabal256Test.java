package fruits.kit.test.crypto.hash;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.crypto.hash.shabal.Shabal256;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.Assert.assertEquals;

public abstract class Shabal256Test {
    private MessageDigest shabal256;
    protected abstract MessageDigest getShabal256();

    @Before
    public void setUp() {
        shabal256 = getShabal256();
    }

    @Test
    public void testHash() {
        assertEquals("aec750d11feee9f16271922fbaf5a9be142f62019ef8d720f858940070889014", Hex.toHexString(shabal256.digest(stringToBytes(""))));
        assertEquals("10e237979a7233aa6a9377ff6a4b2541f890f67107fe0c89008fdd2c48e4cfe5", Hex.toHexString(shabal256.digest(stringToBytes("Testing"))));
        assertEquals("5e12f9ff4b88c316da52caf843a0072e2f73da1c34d50606628cbdc7339ee862", Hex.toHexString(shabal256.digest(stringToBytes("Fruitscoin!"))));
        assertEquals("64e224c8935ecdf3b04231fbc2f52f921a2d8a7212a8eaf68fd4d1deefab92b8", Hex.toHexString(shabal256.digest(stringToBytes("Fruits Apps Team"))));
    }

    @Test
    public void testClone() {
        Shabal256 shabal256 = new Shabal256();
        shabal256.update("testTestTest".getBytes(StandardCharsets.UTF_8));
        Shabal256 newShabal256 = shabal256.clone();
        String expectedHash = "bed0eb7ff7d5cfd3e91a6001fb5f346581e40c40fde4b9525bcf7fffa305593b";
        assertEquals(expectedHash, FruitsCrypto.getInstance().toHexString(shabal256.digest()));
        assertEquals(expectedHash, FruitsCrypto.getInstance().toHexString(newShabal256.digest()));
    }

    private byte[] stringToBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
