# fruitskit4j

Fruitscoin Java Development Framework

## Usage

* Commonly used Fruits objects / entities / helper classes can be found in the [entity](fruitsKit/src/main/java/fruits/kit/entity) package.

* For locally-performed cryptographic operations such as encrypting/decrypting, signing/verifying, etc please see the [FruitsCrypto](fruitsKit/src/main/java/fruits/kit/fruits/FruitsCrypto.java) interface (Use `FruitsCrypto.getInstance()` to obtain a singleton instance)

* For Fruits Node API calls such as making transactions and looking at blocks/accounts/transactions, please see the [FruitsNodeService](fruitsKit/src/main/java/fruits/kit/service/FruitsNodeService.java) interface. (Use `FruitsNodeService.getInstance("http://nodeAddress.com:8125")` to obtain an instance.)

The `FruitsNodeService` wraps the returned values in RxJava Singles. You can create your own `SchedulerAssigner` to automatically make all returned values subscribe on the specified schedulers. If you don't want to use RxJava, call `toFuture()` on any Single.

GSON is used for JSON serialization/deserialization. To obtain a `GsonBuilder` customized to serialize/deserialize FruitsKit entities, call `FruitsKitUtils.buildGson()`.

## Examples

* Sending a transaction containing an encrypted message

```java
import fruits.kit.fruits.FruitsCrypto;
import fruits.kit.entity.FruitsValue;
import fruits.kit.entity.HexStringByteArray;
import fruits.kit.entity.response.FRSError;
import fruits.kit.entity.response.BroadcastTransactionResponse;
import fruits.kit.service.FruitsNodeService;
import io.reactivex.disposables.Disposable;

public class TransactionSender {
    /**
     * Example which sends 1 FRUITS to another account with a fee of 0.1 FRUITS
     * and includes an encrypted message which only the sender or recipient
     * can read. Performs all cryptographic functions (encrypting the message
     * and signing the transaction) offline so does not send the passphrase
     * to the node (which would be a huge security risk!)
     */
    public void sendTransactionWithEncryptedMessage() {
        // Obtain handles to services
        FruitsCrypto fruitsCrypto = FruitsCrypto.getInstance();
        FruitsNodeService fruitsNodeService = FruitsNodeService.getInstance("https://testnet.fwllet.net");

        String passphrase = "passphrase"; // Your fruits wallet passphrase
        byte[] recipientPublicKey = new HexStringByteArray("AABBCC112233").getBytes(); // Recipient public key

        // Generate the transaction without signing it
        Disposable disposable = fruitsNodeService.generateTransactionWithEncryptedMessage(fruitsCrypto.getFruitsAddressFromPublic(recipientPublicKey), fruitsCrypto.getPublicKey(passphrase), FruitsValue.fromFruits(1), FruitsValue.fromFruits(0.1), 1440, fruitsCrypto.encryptTextMessage("Sent from fruitskit4j!", passphrase, recipientPublicKey))
                .flatMap(response -> {
                    // Now we need to locally sign the transaction.
                    // Get the unsigned transaction bytes from the node's response
                    byte[] unsignedTransactionBytes = response.getUnsignedTransactionBytes().getBytes();
                    // Locally sign the transaction using our passphrase
                    byte[] signedTransactionBytes = fruitsCrypto.signTransaction(passphrase, unsignedTransactionBytes);
                    // Broadcast the transaction through the node, still not sending it any sensitive information. Use this as the result of the flatMap so we do not have to call subscribe() twice
                    return fruitsNodeService.broadcastTransaction(signedTransactionBytes);
                })
                .subscribe(this::onTransactionSent, this::handleError);
    }
    
    private void onTransactionSent(BroadcastTransactionResponse response) {
        // Get the transaction ID of the newly sent transaction!
        System.out.println("Transaction sent! Transaction ID: " + response.getTransactionID().getID());
    }
    
    private void handleError(Throwable t) {
        if (t instanceof FRSError) {
            System.out.println("Caught FRS Error: " + ((FRSError) t).getDescription());
        } else {
            t.printStackTrace();
        }
    }
}
``` 
