package fruits.kit.test;

import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.crypto.FruitsCrypto;
import fruits.kit.entity.FruitsEncryptedMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FruitsCryptoTest { // TODO more unit tests
    private byte[] stringToBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    private final FruitsCrypto fruitsCrypto = FruitsCrypto.getInstance();

    @Test
    public void testEncryptTextMessage() {
        String message = "Test message";

        fruitsCrypto.setNativeEnabled(false);
        byte[] myPrivateKey = fruitsCrypto.getPrivateKey("example1");
        byte[] myPublicKey = fruitsCrypto.getPublicKey(myPrivateKey);
        byte[] theirPrivateKey = fruitsCrypto.getPrivateKey("example2");
        byte[] theirPublicKey = fruitsCrypto.getPublicKey(theirPrivateKey);

        fruitsCrypto.setNativeEnabled(true);
        if (fruitsCrypto.nativeEnabled()) {
            byte[] myPrivateKey_native = fruitsCrypto.getPrivateKey("example1");
            byte[] myPublicKey_native = fruitsCrypto.getPublicKey(myPrivateKey);
            byte[] theirPrivateKey_native = fruitsCrypto.getPrivateKey("example2");
            byte[] theirPublicKey_native = fruitsCrypto.getPublicKey(theirPrivateKey);

            assertArrayEquals(myPrivateKey, myPrivateKey_native);
            assertArrayEquals(myPublicKey, myPublicKey_native);
            assertArrayEquals(theirPrivateKey, theirPrivateKey_native);
            assertArrayEquals(theirPublicKey, theirPublicKey_native);
        }

        FruitsEncryptedMessage fruitsEncryptedMessage = fruitsCrypto.encryptTextMessage(message, myPrivateKey, theirPublicKey);

        String result1 = new String(fruitsCrypto.decryptMessage(fruitsEncryptedMessage, myPrivateKey, theirPublicKey));
        String result2 = new String(fruitsCrypto.decryptMessage(fruitsEncryptedMessage, theirPrivateKey, myPublicKey));

        assertEquals(message, result1);
        assertEquals(message, result2);
    }

    private void reverse(byte[] array) {
        for(int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = (byte) temp;
        }
    }

    @Test
    public void testGetSharedKey() {
        byte[] sharedKey = fruitsCrypto.parseHexString("f057d9854fa4d7cf86a822500dae7b6c3325a21a1f13f5fc98b587bc0569113a");

        byte[] privateKey1 = fruitsCrypto.getPrivateKey("passphrase1");
        byte[] privateKey2 = fruitsCrypto.getPrivateKey("passphrase2");
        byte[] publicKey1 = fruitsCrypto.getPublicKey(privateKey1);
        byte[] publicKey2 = fruitsCrypto.getPublicKey(privateKey2);

        fruitsCrypto.setNativeEnabled(false);
        byte[] sharedKey1 = fruitsCrypto.getSharedSecret(privateKey1, publicKey2);
        byte[] sharedKey2 = fruitsCrypto.getSharedSecret(privateKey2, publicKey1);
        assertArrayEquals(sharedKey, sharedKey1);
        assertArrayEquals(sharedKey, sharedKey2);

        fruitsCrypto.setNativeEnabled(true);
        if (fruitsCrypto.nativeEnabled()) {
            byte[] sharedKey1_native = fruitsCrypto.getSharedSecret(privateKey1, publicKey2);
            byte[] sharedKey2_native = fruitsCrypto.getSharedSecret(privateKey2, publicKey1);
            assertArrayEquals(sharedKey1, sharedKey1_native);
            assertArrayEquals(sharedKey2, sharedKey2_native);
        }
    }

    @Test
    public void testSignAndVerify() {
        fruitsCrypto.setNativeEnabled(false);
        byte[] myMessage = "A Message".getBytes(StandardCharsets.UTF_8);
        byte[] myPrivateKey = fruitsCrypto.getPrivateKey("example1");
        byte[] myPublic = fruitsCrypto.getPublicKey(myPrivateKey);
        byte[] signature = fruitsCrypto.sign(myMessage, myPrivateKey);
        Assert.assertTrue(fruitsCrypto.verify(signature, myMessage, myPublic, true));
        reverse(signature);
        Assert.assertFalse(fruitsCrypto.verify(signature, myMessage, myPublic, true));

        fruitsCrypto.setNativeEnabled(true);
        if (fruitsCrypto.nativeEnabled()) {
            reverse(signature); // Undo previous reverse
            byte[] nativeSignature = fruitsCrypto.sign(myMessage, myPrivateKey);
            byte[] nativePublicKey = fruitsCrypto.getPublicKey(myPrivateKey);
            Assert.assertArrayEquals(myPublic, nativePublicKey);
            Assert.assertArrayEquals(signature, nativeSignature);
            Assert.assertTrue(fruitsCrypto.verify(signature, myMessage, myPublic, true));
            reverse(signature);
            Assert.assertFalse(fruitsCrypto.verify(signature, myMessage, myPublic, true));
        }
    }

    @Test
    public void testCryptoSha256() {
        MessageDigest sha256 = fruitsCrypto.getSha256();
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", Hex.toHexString(sha256.digest(stringToBytes(""))));
        assertEquals("e806a291cfc3e61f83b98d344ee57e3e8933cccece4fb45e1481f1f560e70eb1", Hex.toHexString(sha256.digest(stringToBytes("Testing"))));
        assertEquals("78fee77474501292d9de467e6ca2e981c60e04639bc33fc67759b1bcf369959e", Hex.toHexString(sha256.digest(stringToBytes("Fruitscoin!"))));
        assertEquals("a09c4287181af2b93bae15f9fe6fd322d5c7f8059fbe36fc837358f575f0c278", Hex.toHexString(sha256.digest(stringToBytes("Fruits Apps Team"))));
    }

    @Test
    public void testCryptoShabal256() {
        MessageDigest shabal256 = fruitsCrypto.getShabal256();
        assertEquals("aec750d11feee9f16271922fbaf5a9be142f62019ef8d720f858940070889014", Hex.toHexString(shabal256.digest(stringToBytes(""))));
        assertEquals("10e237979a7233aa6a9377ff6a4b2541f890f67107fe0c89008fdd2c48e4cfe5", Hex.toHexString(shabal256.digest(stringToBytes("Testing"))));
        assertEquals("5e12f9ff4b88c316da52caf843a0072e2f73da1c34d50606628cbdc7339ee862", Hex.toHexString(shabal256.digest(stringToBytes("Fruitscoin!"))));
        assertEquals("64e224c8935ecdf3b04231fbc2f52f921a2d8a7212a8eaf68fd4d1deefab92b8", Hex.toHexString(shabal256.digest(stringToBytes("Fruits Apps Team"))));
    }

    @Test
    public void testCryptoRipemd160() {
        MessageDigest ripemd160 = fruitsCrypto.getRipeMD160();
        assertEquals("9c1185a5c5e9fc54612808977ee8f548b2258d31", Hex.toHexString(ripemd160.digest(stringToBytes(""))));
        assertEquals("01743c6e71742ed72d6c51537f1790a462b82c82", Hex.toHexString(ripemd160.digest(stringToBytes("Testing"))));
        assertEquals("4cb59e4d11867827d7d22e1034e450662866252a", Hex.toHexString(ripemd160.digest(stringToBytes("Fruitscoin!"))));
        assertEquals("920ca6a7f2bfcb86d1299ec57d3dc8c7324e6ffb", Hex.toHexString(ripemd160.digest(stringToBytes("Fruits Apps Team"))));
    }

    @Test
    public void testCryptoMd5() {
        MessageDigest md5 = fruitsCrypto.getMD5();
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Hex.toHexString(md5.digest(stringToBytes(""))));
        assertEquals("fa6a5a3224d7da66d9e0bdec25f62cf0", Hex.toHexString(md5.digest(stringToBytes("Testing"))));
        assertEquals("6405b553f87644ad14d97bc2eb88fc0c", Hex.toHexString(md5.digest(stringToBytes("Fruitscoin!"))));
        assertEquals("0735ccd9ace226e3fab21b44ca876f88", Hex.toHexString(md5.digest(stringToBytes("Fruits Apps Team"))));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBytesToLongBE_tooShortArray() {
        assertEquals(0x0000000000000000L, fruitsCrypto.bytesToLongBE(new byte[7]));
    }

    @Test
    public void testBytesToLongBE() {
        assertEquals(0x0100000000000000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0100000000000000")));
        assertEquals(0x0123000000000000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123000000000000")));
        assertEquals(0x0123450000000000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123450000000000")));
        assertEquals(0x0123456700000000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123456700000000")));
        assertEquals(0x0123456789000000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123456789000000")));
        assertEquals(0x0123456789ab0000L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123456789ab0000")));
        assertEquals(0x0123456789abcd00L, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123456789abcd00")));

        assertEquals(0x00000000000000efL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("00000000000000ef")));
        assertEquals(0x000000000000cdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("000000000000cdef")));
        assertEquals(0x0000000000abcdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0000000000abcdef")));
        assertEquals(0x0000000089abcdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0000000089abcdef")));
        assertEquals(0x0000006789abcdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0000006789abcdef")));
        assertEquals(0x0000456789abcdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0000456789abcdef")));
        assertEquals(0x0023456789abcdefL, fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0023456789abcdef")));

        assertEquals(Long.parseUnsignedLong("0123456789abcdef", 16), fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("0123456789abcdef")));
        assertEquals(Long.parseUnsignedLong("efcdab8967452301", 16), fruitsCrypto.bytesToLongBE(fruitsCrypto.parseHexString("efcdab8967452301")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBytesToLongLE_tooShortArray() {
        assertEquals(0x0000000000000000L, fruitsCrypto.bytesToLongLE(new byte[7]));
    }

    @Test
    public void testBytesToLongLE() {
        assertEquals(0x0000000000000001L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0100000000000000")));
        assertEquals(0x0000000000002301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123000000000000")));
        assertEquals(0x0000000000452301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123450000000000")));
        assertEquals(0x0000000067452301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123456700000000")));
        assertEquals(0x0000008967452301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123456789000000")));
        assertEquals(0x0000ab8967452301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123456789ab0000")));
        assertEquals(0x00cdab8967452301L, fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123456789abcd00")));

        assertEquals(Long.parseUnsignedLong("ef00000000000000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("00000000000000ef")));
        assertEquals(Long.parseUnsignedLong("efcd000000000000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("000000000000cdef")));
        assertEquals(Long.parseUnsignedLong("efcdab0000000000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0000000000abcdef")));
        assertEquals(Long.parseUnsignedLong("efcdab8900000000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0000000089abcdef")));
        assertEquals(Long.parseUnsignedLong("efcdab8967000000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0000006789abcdef")));
        assertEquals(Long.parseUnsignedLong("efcdab8967450000", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0000456789abcdef")));
        assertEquals(Long.parseUnsignedLong("efcdab8967452300", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0023456789abcdef")));

        assertEquals(Long.parseUnsignedLong("efcdab8967452301", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("0123456789abcdef")));
        assertEquals(Long.parseUnsignedLong("0123456789abcdef", 16), fruitsCrypto.bytesToLongLE(fruitsCrypto.parseHexString("efcdab8967452301")));
    }

    @Test
    public void testLongToBytesBE() {
        assertEquals("0000000000000001", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000000000000001L)));
        assertEquals("0000000000002301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000000000002301L)));
        assertEquals("0000000000452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000000000452301L)));
        assertEquals("0000000067452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000000067452301L)));
        assertEquals("0000008967452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000008967452301L)));
        assertEquals("0000ab8967452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x0000ab8967452301L)));
        assertEquals("00cdab8967452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(0x00cdab8967452301L)));

        assertEquals("ef00000000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("ef00000000000000", 16))));
        assertEquals("efcd000000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcd000000000000", 16))));
        assertEquals("efcdab0000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab0000000000", 16))));
        assertEquals("efcdab8900000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab8900000000", 16))));
        assertEquals("efcdab8967000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab8967000000", 16))));
        assertEquals("efcdab8967450000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab8967450000", 16))));
        assertEquals("efcdab8967452300", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab8967452300", 16))));

        assertEquals("0123456789abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("0123456789abcdef", 16))));
        assertEquals("efcdab8967452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesBE(Long.parseUnsignedLong("efcdab8967452301", 16))));
    }

    @Test
    public void testLongToBytesLE() {
        assertEquals("0100000000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000000000000001L)));
        assertEquals("0123000000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000000000002301L)));
        assertEquals("0123450000000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000000000452301L)));
        assertEquals("0123456700000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000000067452301L)));
        assertEquals("0123456789000000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000008967452301L)));
        assertEquals("0123456789ab0000", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x0000ab8967452301L)));
        assertEquals("0123456789abcd00", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(0x00cdab8967452301L)));

        assertEquals("00000000000000ef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("ef00000000000000", 16))));
        assertEquals("000000000000cdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcd000000000000", 16))));
        assertEquals("0000000000abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab0000000000", 16))));
        assertEquals("0000000089abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab8900000000", 16))));
        assertEquals("0000006789abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab8967000000", 16))));
        assertEquals("0000456789abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab8967450000", 16))));
        assertEquals("0023456789abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab8967452300", 16))));

        assertEquals("efcdab8967452301", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("0123456789abcdef", 16))));
        assertEquals("0123456789abcdef", fruitsCrypto.toHexString(fruitsCrypto.longToBytesLE(Long.parseUnsignedLong("efcdab8967452301", 16))));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBytesToIntBE_tooShortArray() {
        assertEquals(0x00000000, fruitsCrypto.bytesToIntBE(new byte[3]));
    }

    @Test
    public void testBytesToIntBE() {
        assertEquals(0x89000000, fruitsCrypto.bytesToIntBE(fruitsCrypto.parseHexString("89000000")));
        assertEquals(0x89ab0000, fruitsCrypto.bytesToIntBE(fruitsCrypto.parseHexString("89ab0000")));
        assertEquals(0x89abcd00, fruitsCrypto.bytesToIntBE(fruitsCrypto.parseHexString("89abcd00")));
        assertEquals(0x89abcdef, fruitsCrypto.bytesToIntBE(fruitsCrypto.parseHexString("89abcdef")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBytesToIntLE_tooShortArray() {
        assertEquals(0x00000000, fruitsCrypto.bytesToIntLE(new byte[3]));
    }

    @Test
    public void testBytesToIntLE() {
        assertEquals(0x00000001, fruitsCrypto.bytesToIntLE(fruitsCrypto.parseHexString("01000000")));
        assertEquals(0x00002301, fruitsCrypto.bytesToIntLE(fruitsCrypto.parseHexString("01230000")));
        assertEquals(0x00452301, fruitsCrypto.bytesToIntLE(fruitsCrypto.parseHexString("01234500")));
        assertEquals(0x67452301, fruitsCrypto.bytesToIntLE(fruitsCrypto.parseHexString("01234567")));
    }

    @Test
    public void testIntToBytesBE() {
        assertEquals("00000001", fruitsCrypto.toHexString(fruitsCrypto.intToBytesBE(0x00000001)));
        assertEquals("00002301", fruitsCrypto.toHexString(fruitsCrypto.intToBytesBE(0x00002301)));
        assertEquals("00452301", fruitsCrypto.toHexString(fruitsCrypto.intToBytesBE(0x00452301)));
        assertEquals("67452301", fruitsCrypto.toHexString(fruitsCrypto.intToBytesBE(0x67452301)));
    }

    @Test
    public void testIntToBytesLE() {
        assertEquals("01000000", fruitsCrypto.toHexString(fruitsCrypto.intToBytesLE(0x00000001)));
        assertEquals("01230000", fruitsCrypto.toHexString(fruitsCrypto.intToBytesLE(0x00002301)));
        assertEquals("01234500", fruitsCrypto.toHexString(fruitsCrypto.intToBytesLE(0x00452301)));
        assertEquals("01234567", fruitsCrypto.toHexString(fruitsCrypto.intToBytesLE(0x67452301)));
    }
}
