package fruits.kit.service;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import fruits.kit.entity.*;
import fruits.kit.entity.response.*;
import fruits.kit.service.impl.CompositeFruitsNodeService;
import fruits.kit.service.impl.HttpFruitsNodeService;

/**
 * The Interface FruitsNodeService.
 */
public interface FruitsNodeService extends AutoCloseable {
    
    /**
     * Get a block via a block ID.
     *
     * @param block The block ID of the requested block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(FruitsID block);

    /**
     * Get the block at a specific height.
     *
     * @param height The height of the block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(int height);

    /**
     * Get the block at the specified timestamp.
     *
     * @param timestamp The timestamp of the block
     * @return The block details, wrapped in a Single
     */
    Single<Block> getBlock(FruitsTimestamp timestamp);

    /**
     * Gets all the blocks between the first index and last index.
     * @param firstIndex The index from the most recent blocks (0 would be the most recent block)
     * @param lastIndex The end index from the most recent blocks
     * @return The blocks, wrapped in a single
     */
    Single<Block[]> getBlocks(int firstIndex, int lastIndex);

    /**
     * Get the Constants in use by the node.
     *
     * @return The constants, wrapped in a single
     */
    Single<Constants> getConstants();
    
    /**
     * Get the account details of the specified account.
     *
     * @param accountId The address of the account
     * @param height the height
     * @param estimateCommitment the estimate commitment
     * @param getCommittedAmount the get committed amount
     * @return The account details, wrapped in a single
     */
    Single<Account> getAccount(FruitsAddress accountId, Integer height, Boolean estimateCommitment, Boolean getCommittedAmount);
    
    /**
     * Get the account details of the specified account.
     *
     * @param accountId The address of the account
     * @return The account details, wrapped in a single
     */
    Single<Account> getAccount(FruitsAddress accountId);

    /**
     * Get the ATs created by the account.
     *
     * @param accountId The address of the account
     * @return A list of the ATs, wrapped in a single
     */
    Single<AT[]> getAccountATs(FruitsAddress accountId);

    /**
     * Get the blocks forged by an account.
     *
     * @param accountId The address of the account
     * @return The blocks, wrapped in a single
     */
    Single<Block[]> getAccountBlocks(FruitsAddress accountId); // TODO timestamp, firstIndex, lastIndex

    /**
     * Get the transaction IDs of an account.
     *
     * @param accountId The address of the account
     * @return The account's transaction IDs, wrapped in a single
     */
    Single<FruitsID[]> getAccountTransactionIDs(FruitsAddress accountId); // TODO filtering

    /**
     * Get the transactions of an account.
     *
     * @param accountId The address of the account
     * @param firstIndex the first index
     * @param lastIndex the last index
     * @param includeIndirect the include indirect
     * @return The account's transactions, wrapped in a single
     */
    Single<Transaction[]> getAccountTransactions(FruitsAddress accountId, Integer firstIndex, Integer lastIndex, Boolean includeIndirect);

    /**
     * Get the unconfirmed transactions of an account.
     *
     * @param accountId The address of the account
     * @return The account's transactions, wrapped in a single
     */
    Single<Transaction[]> getUnconfirmedTransactions(FruitsAddress accountId);

    /**
     * Get the list of accounts which have their reward recipient set to the specified account.
     *
     * @param accountId The address of the account
     * @return The list of account IDs with reward recipients set to the account, wrapped in a single
     */
    Single<FruitsAddress[]> getAccountsWithRewardRecipient(FruitsAddress accountId);

    /**
     * Get the given asset.
     *
     * @param assetId The asset id
     * @return The asset, wrapped in a single
     */
    Single<Asset> getAsset(FruitsID assetId);

    /**
     * Get the accounts holding the given asset.
     *
     * @param assetId The asset id
     * @param firstIndex the first index
     * @param lastIndex the last index
     * @return The asset balances of accounts holding the asset, wrapped in a single
     */
    Single<AssetBalance[]> getAssetBalances(FruitsID assetId, Integer firstIndex, Integer lastIndex);

    /**
     * Get the trades for a given asset.
     *
     * @param assetId The asset id
     * @param account The account of interest (optional)
     * @param firstIndex The first index (optional for pagination)
     * @param lastIndex The last index (optional for pagination)
     * @return A list trades, wrapped in a single
     */
    // TODO TEST
    Single<AssetTrade[]> getAssetTrades(FruitsID assetId, FruitsAddress account, Integer firstIndex, Integer lastIndex);

    /**
     * Get the ask orders for a given asset.
     *
     * @param assetId The asset id
     * @return A list trades, wrapped in a single
     */
    Single<AssetOrder[]> getAskOrders(FruitsID assetId);

    /**
     * Get the bid orders for a given asset.
     *
     * @param assetId The asset id
     * @return A list trades, wrapped in a single
     */
    Single<AssetOrder[]> getBidOrders(FruitsID assetId);

    /**
     * Get the details of an AT.
     *
     * @param at The address of the AT
     * @return The details of the AT, wrapped in a single
     */
    Single<AT> getAt(FruitsAddress at);
    
    /**
     * Get the details of an AT.
     *
     * @param at The address of the AT
     * @param includeDetails the include details
     * @return The details of the AT, wrapped in a single
     */
    Single<AT> getAt(FruitsAddress at, boolean includeDetails);

    /**
     * Get the list of addresses of all ATs.
     *
     * @return The list of AT addresses, wrapped in a single
     */
    Single<FruitsAddress[]> getAtIds();

    /**
     * Get the details of a transaction.
     *
     * @param transactionId The ID of the transaction
     * @return The transaction details, wrapped in a single
     */
    Single<Transaction> getTransaction(FruitsID transactionId);

    /**
     * Get the details of a transaction.
     *
     * @param fullHash The full hash of the transaction
     * @return The transaction details, wrapped in a single
     */
    Single<Transaction> getTransaction(byte[] fullHash);

    /**
     * Get the transaction bytes.
     *
     * @param transactionId The ID of the transaction
     * @return The transaction bytes, wrapped in a single
     */
    Single<byte[]> getTransactionBytes(FruitsID transactionId);

    /**
     * Generate a add commitment transaction.
     *
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to commit
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionAddCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline);

    /**
     * Generate a add commitment transaction.
     *
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to remove
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionRemoveCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline);

    /**
     * Generate a simple transaction (only sending money).
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransaction(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String referencedTransactionFullHash);

    /**
     * Generate a transaction to set the reward recipient.
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionSetRewardRecipient(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline);

    /**
     * Generate a transaction with a plaintext message.
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with a plaintext message.
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with a plaintext message which also sets the recipient's public key in the chain.
     *
     * @param recipientAddress The recipient's address
     * @param recipientPublicKey The public key of the recipient, to be set in the chain
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with a plaintext message which also sets the recipient's public key in the chain.
     *
     * @param recipientAddress The recipient's address
     * @param recipientPublicKey The public key of the recipient, to be set in the chain
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with a plaintext message.
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with a plaintext message.
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with an encrypted message (can be read by sender and recipient).
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with an encrypted message (can be read by sender and recipient).
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with an encrypted-to-self message (can be read by only sender).
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param amount The amount to send
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction (Use sender public key and sender private key to encrypt)
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash);

    /**
     * Generate a transaction with an encrypted-to-self message (can be read by only sender).
     *
     * @param recipient The recipient
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction (Use sender public key and sender private key to encrypt)
     * @param referencedTransactionFullHash the referenced transaction full hash
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash);

    /**
     * Get the currently suggested transaction fees, which are calculated based on current network congestion -.
     *
     * @return Suggested transaction fees - Priority, standard and cheap in descending speed and cost, wrapped in a single
     */
    Single<FeeSuggestion> suggestFee();

    /**
     * Get the current mining info.
     *
     * @return An observable that returns the current mining info when it changes.
     */
    Observable<MiningInfo> getMiningInfo();

    /**
     * Broadcast a transaction on the network.
     *
     * @param transactionBytes The signed transaction bytes
     * @return The number of peers this transaction was broadcast to, wrapped in a single
     */
    Single<TransactionBroadcast> broadcastTransaction(byte[] transactionBytes);

    /**
     * Get the reward recipient of the account.
     *
     * @param account The account
     * @return The reward recipient, wrapped in a single
     */
    Single<FruitsAddress> getRewardRecipient(FruitsAddress account);

    /**
     * Submit a nonce for mining.
     *
     * @param passphrase The passphrase of the miner (if solo mining) or null if pool mining
     * @param nonce The nonce that results in the deadline you want to submit
     * @param accountId The account ID of the miner
     * @return The calculated deadline, wrapped in a single
     */
    Single<Long> submitNonce(String passphrase, String nonce, FruitsID accountId);

    /**
     * Generate a multi-out transaction.
     *
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param recipients A map of recipients and how much they get. Length must be 2-64 inclusive
     * @return The unsigned transaction bytes, wrapped in a single
     * @throws IllegalArgumentException If the number of recipients is not in the range of 2-64 inclusive
     */
    Single<byte[]> generateMultiOutTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, Map<FruitsAddress, FruitsValue> recipients) throws IllegalArgumentException;

    /**
     * Generate a multi-out transaction.
     *
     * @param senderPublicKey The public key of the sender
     * @param amount The amount each recipient gets
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param recipients A list of recipients. Each will get the amount specified. Length must be 2-128 inclusive
     * @return The unsigned transaction bytes, wrapped in a single
     * @throws IllegalArgumentException If the number of recipients is not in the range of 2-128 inclusive
     */
    Single<byte[]> generateMultiOutSameTransaction(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, Set<FruitsAddress> recipients);

    /**
     * Generate the transaction for creating an AT.
     *
     * @param senderPublicKey The public key of the sender
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param name The name of the AT
     * @param description The description of the AT
     * @param creationBytes The creation bytes of the AT (if pre-calculated and not using the following fields)
     * @return The unsigned transaction bytes, wrapped in a single
     */
    Single<byte[]> generateCreateATTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, String name, String description, byte[] creationBytes);

    /**
     * Generate the transaction for transfering assets.
     *
     * @param senderPublicKey The public key of the sender
     * @param name the name
     * @param description the description
     * @param quantity The quantity to transfer
     * @param decimals the decimals
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateIssueAssetTransaction(byte[] senderPublicKey, String name, String description, FruitsValue quantity, int decimals, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for transfering assets.
     *
     * @param senderPublicKey The public key of the sender
     * @param recipient The recipient
     * @param assetId The ID of the asset being transfered
     * @param quantity The quantity to transfer
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateTransferAssetTransaction(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for an ask order.
     *
     * @param senderPublicKey The public key of the sender
     * @param recipient the recipient
     * @param assetId The ID of the asset being transfered
     * @param quantity The order quantity
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The message to include in the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateTransferAssetTransactionWithMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, String message);

    /**
     * Generate the transaction for an ask order.
     *
     * @param senderPublicKey The public key of the sender
     * @param recipient the recipient
     * @param assetId The ID of the asset being transfered
     * @param quantity The order quantity
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @param message The encrypted message to include in the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateTransferAssetTransactionWithEncryptedMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, FruitsEncryptedMessage message);

    /**
     * Generate the transaction for an ask order.
     *
     * @param senderPublicKey The public key of the sender
     * @param assetId The ID of the asset being transfered
     * @param quantity The order quantity
     * @param price The order price
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generatePlaceAskOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for a bid order.
     *
     * @param senderPublicKey The public key of the sender
     * @param assetId The ID of the asset being transfered
     * @param quantity The order quantity
     * @param price The order price
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generatePlaceBidOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for cancelling an ask order.
     *
     * @param senderPublicKey The public key of the sender
     * @param orderID the order ID
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateCancelAskOrderTransaction(byte[] senderPublicKey, FruitsID orderID, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for cancelling a bid order.
     *
     * @param senderPublicKey The public key of the sender
     * @param orderID the order ID
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateCancelBidOrderTransaction(byte[] senderPublicKey, FruitsID orderID, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for creating subscription.
     *
     * @param senderPublicKey The public key of the sender
     * @param amount Amount of subscription in plancks
     * @param frequency Frequency in which you send amount, seconds
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateSubscriptionCreationTransaction(byte[] senderPublicKey, FruitsValue amount, int frequency, FruitsValue fee, int deadline);

    /**
     * Generate the transaction for cancelling a subscription.
     *
     * @param senderPublicKey The public key of the sender
     * @param subscription The ID of the subscription you want to cancel
     * @param fee The transaction fee
     * @param deadline The deadline for the transaction
     * @return The unsigned transaction bytes, wrapped in a single
     */
    // TODO TEST
    Single<byte[]> generateSubscriptionCancelTransaction(byte[] senderPublicKey, FruitsID subscription, FruitsValue fee, int deadline);

    /**
     * Gets the single instance of FruitsNodeService.
     *
     * @param nodeAddress the node address
     * @return single instance of FruitsNodeService
     */
    static FruitsNodeService getInstance(String nodeAddress) {
        return getInstance(nodeAddress, null);
    }

    /**
     * Gets the single instance of FruitsNodeService.
     *
     * @param nodeAddress the node address
     * @param userAgent the user agent
     * @return single instance of FruitsNodeService
     */
    static FruitsNodeService getInstance(String nodeAddress, String userAgent) {
        if (userAgent == null) userAgent = "fruitskit4j/" + fruits.kit.Constants.VERSION;
        return new HttpFruitsNodeService(nodeAddress, userAgent);
    }

    /**
     * Gets the composite instance.
     *
     * @param nodeAddresses the node addresses
     * @return the composite instance
     */
    static FruitsNodeService getCompositeInstance(String... nodeAddresses) {
        return getCompositeInstanceWithUserAgent(null, nodeAddresses);
    }

    /**
     * Gets the composite instance with user agent.
     *
     * @param userAgent the user agent
     * @param nodeAddresses the node addresses
     * @return the composite instance with user agent
     */
    static FruitsNodeService getCompositeInstanceWithUserAgent(String userAgent, String... nodeAddresses) {
        if (nodeAddresses.length == 0) throw new IllegalArgumentException("No node addresses specified");
        if (nodeAddresses.length == 1) return getInstance(nodeAddresses[0], userAgent);
        return new CompositeFruitsNodeService(Arrays.stream(nodeAddresses)
                .map(nodeAddress -> getInstance(nodeAddress, userAgent))
                .toArray(FruitsNodeService[]::new));
    }
}
