package fruits.kit.crypto;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsEncryptedMessage;

@SuppressWarnings("WeakerAccess")
abstract class AbstractFruitsCrypto implements FruitsCrypto {
    protected byte[] stringToBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] getPublicKey(String passphrase) {
        return getPublicKey(getPrivateKey(passphrase));
    }

    @Override
    public FruitsAddress getFruitsAddressFromPassphrase(String passphrase) {
        return getFruitsAddressFromPrivate(getPrivateKey(passphrase));
    }

    @Override
    public FruitsAddress getFruitsAddressFromPrivate(byte[] privateKey) {
        return getFruitsAddressFromPublic(getPublicKey(privateKey));
    }

    @Override
    public byte[] getSharedSecret(String myPassphrase, byte[] theirPublicKey) {
        return getSharedSecret(getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] sign(byte[] message, String passphrase) {
        return sign(message, getPrivateKey(passphrase));
    }

    @Override
    public byte[] sign(String message, String passphrase) {
        return sign(stringToBytes(message), getPrivateKey(passphrase));
    }

    @Override
    public byte[] sign(String message, byte[] privateKey) {
        return sign(stringToBytes(message), privateKey);
    }

    @Override
    public byte[] signTransaction(String passphrase, byte[] unsignedTransaction) {
        return signTransaction(getPrivateKey(passphrase), unsignedTransaction);
    }

    @Override
    public byte[] aesEncrypt(byte[] plaintext, byte[] signingKey) throws IllegalArgumentException {
        return aesEncrypt(plaintext, signingKey, new byte[32]);
    }

    @Override
    public byte[] aesEncrypt(String plaintext, byte[] signingKey) throws IllegalArgumentException {
        return aesEncrypt(stringToBytes(plaintext), signingKey);
    }

    @Override
    public byte[] aesEncrypt(String plaintext, byte[] signingKey, byte[] nonce) throws IllegalArgumentException {
        return aesEncrypt(stringToBytes(plaintext), signingKey, nonce);
    }

    @Override
    public byte[] aesSharedEncrypt(byte[] plaintext, String myPassphrase, byte[] theirPublicKey) {
        return aesSharedEncrypt(plaintext, getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] aesSharedEncrypt(byte[] plaintext, String myPassphrase, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedEncrypt(plaintext, getPrivateKey(myPassphrase), theirPublicKey, nonce);
    }

    @Override
    public byte[] aesSharedEncrypt(byte[] plaintext, byte[] myPrivateKey, byte[] theirPublicKey) {
        return aesEncrypt(plaintext, getSharedSecret(myPrivateKey, theirPublicKey));
    }

    @Override
    public byte[] aesSharedEncrypt(byte[] plaintext, byte[] myPrivateKey, byte[] theirPublicKey, byte[] nonce) {
        return aesEncrypt(plaintext, getSharedSecret(myPrivateKey, theirPublicKey), nonce);
    }

    @Override
    public byte[] aesSharedEncrypt(String plaintext, String myPassphrase, byte[] theirPublicKey) {
        return aesSharedEncrypt(stringToBytes(plaintext), getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] aesSharedEncrypt(String plaintext, String myPassphrase, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedEncrypt(stringToBytes(plaintext), getPrivateKey(myPassphrase), theirPublicKey, nonce);
    }

    @Override
    public byte[] aesSharedEncrypt(String plaintext, byte[] myPrivateKey, byte[] theirPublicKey) {
        return aesSharedEncrypt(stringToBytes(plaintext), myPrivateKey, theirPublicKey);
    }

    @Override
    public byte[] aesSharedEncrypt(String plaintext, byte[] myPrivateKey, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedEncrypt(stringToBytes(plaintext), myPrivateKey, theirPublicKey, nonce);
    }

    @Override
    public byte[] aesDecrypt(byte[] encrypted, byte[] signingKey) throws IllegalArgumentException {
        return aesDecrypt(encrypted, signingKey, new byte[32]);
    }

    @Override
    public byte[] aesDecrypt(String encrypted, byte[] signingKey) throws IllegalArgumentException {
        return aesDecrypt(stringToBytes(encrypted), signingKey);
    }

    @Override
    public byte[] aesDecrypt(String encrypted, byte[] signingKey, byte[] nonce) throws IllegalArgumentException {
        return aesDecrypt(stringToBytes(encrypted), signingKey, nonce);
    }

    @Override
    public byte[] aesSharedDecrypt(byte[] encrypted, String myPassphrase, byte[] theirPublicKey) {
        return aesSharedDecrypt(encrypted, getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] aesSharedDecrypt(byte[] encrypted, String myPassphrase, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedDecrypt(encrypted, getPrivateKey(myPassphrase), theirPublicKey, nonce);
    }

    @Override
    public byte[] aesSharedDecrypt(byte[] encrypted, byte[] myPrivateKey, byte[] theirPublicKey) {
        return aesDecrypt(encrypted, getSharedSecret(myPrivateKey, theirPublicKey));
    }

    @Override
    public byte[] aesSharedDecrypt(byte[] encrypted, byte[] myPrivateKey, byte[] theirPublicKey, byte[] nonce) {
        return aesDecrypt(encrypted, getSharedSecret(myPrivateKey, theirPublicKey), nonce);
    }

    @Override
    public byte[] aesSharedDecrypt(String encrypted, String myPassphrase, byte[] theirPublicKey) {
        return aesSharedDecrypt(stringToBytes(encrypted), getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] aesSharedDecrypt(String encrypted, String myPassphrase, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedDecrypt(stringToBytes(encrypted), getPrivateKey(myPassphrase), theirPublicKey, nonce);
    }

    @Override
    public byte[] aesSharedDecrypt(String encrypted, byte[] myPrivateKey, byte[] theirPublicKey) {
        return aesSharedDecrypt(stringToBytes(encrypted), myPrivateKey, theirPublicKey);
    }

    @Override
    public byte[] aesSharedDecrypt(String encrypted, byte[] myPrivateKey, byte[] theirPublicKey, byte[] nonce) {
        return aesSharedDecrypt(stringToBytes(encrypted), myPrivateKey, theirPublicKey, nonce);
    }

    @Override
    public FruitsEncryptedMessage encryptBytesMessage(byte[] message, String myPassphrase, byte[] theirPublicKey) {
        return encryptBytesMessage(message, getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public FruitsEncryptedMessage encryptTextMessage(String message, String myPassphrase, byte[] theirPublicKey) {
        return encryptTextMessage(message, getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public byte[] decryptMessage(FruitsEncryptedMessage message, String myPassphrase, byte[] theirPublicKey) {
        return decryptMessage(message, getPrivateKey(myPassphrase), theirPublicKey);
    }

    @Override
    public Date fromFruitsTimeToDate(int fruitsTime) {
        return new Date(fromFruitsTime(fruitsTime));
    }

    @Override
    public int toFruitsTime(Date date) {
        return toFruitsTime(date.getTime());
    }

    @Override
    public int currentFruitsTime() {
        return toFruitsTime(System.currentTimeMillis());
    }

    @Override
    public long bytesToLongBE(byte[] bytes) {
        return bytesToLongBE(bytes, 0);
    }

    @Override
    public long bytesToLongLE(byte[] bytes) {
        return bytesToLongLE(bytes, 0);
    }

    @Override
    public byte[] longToBytesBE(long l) {
        byte[] result = new byte[8];
        longToBytesBE(l, result, 0);
        return result;
    }

    @Override
    public byte[] longToBytesLE(long l) {
        byte[] result = new byte[8];
        longToBytesLE(l, result, 0);
        return result;
    }

    @Override
    public int bytesToIntBE(byte[] bytes) {
        return bytesToIntBE(bytes, 0);
    }

    @Override
    public int bytesToIntLE(byte[] bytes) {
        return bytesToIntLE(bytes, 0);
    }

    @Override
    public byte[] intToBytesBE(int i) {
        byte[] result = new byte[4];
        intToBytesBE(i, result, 0);
        return result;
    }

    @Override
    public byte[] intToBytesLE(int i) {
        byte[] result = new byte[4];
        intToBytesLE(i, result, 0);
        return result;
    }

    @Override
    public byte[] calculateGenerationSignature(byte[] lastGenSig, FruitsAddress lastGenerator) {
        return calculateGenerationSignature(lastGenSig, lastGenerator.getSignedLongId());
    }

    @Override
    public BigInteger calculateHit(FruitsAddress accountId, long nonce, byte[] genSig, int scoop, int pocVersion) {
        return calculateHit(accountId.getSignedLongId(), nonce, genSig, scoop, pocVersion);
    }

    @Override
    public BigInteger calculateDeadline(FruitsAddress accountId, long nonce, byte[] genSig, int scoop, long baseTarget, int pocVersion) {
        return calculateDeadline(accountId.getSignedLongId(), nonce, genSig, scoop, baseTarget, pocVersion);
    }
}
