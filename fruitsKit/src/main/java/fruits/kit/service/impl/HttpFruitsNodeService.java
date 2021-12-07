package fruits.kit.service.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.net.SocketFactory;

import org.bouncycastle.util.encoders.Hex;

import fruits.kit.entity.*;
import fruits.kit.entity.response.*;
import fruits.kit.entity.response.http.*;
import fruits.kit.service.FruitsApiException;
import fruits.kit.service.FruitsNodeService;
import fruits.kit.util.FruitsKitUtils;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public final class HttpFruitsNodeService implements FruitsNodeService {
    private FruitsAPIService fruitsAPIService;

    public HttpFruitsNodeService(String nodeAddress, String userAgent) {
    	
    	SocketFactory socketFactory = SocketFactory.getDefault();
    	Dns dns = Dns.SYSTEM;
    	
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
        		.socketFactory(socketFactory)
        		.dns(dns)
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().header("User-Agent", userAgent).build()))
                .build();

        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(nodeAddress)
                .addConverterFactory(GsonConverterFactory.create(FruitsKitUtils.buildGson().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

        fruitsAPIService = retrofit.create(FruitsAPIService.class);
    }

    private <T> Single<T> assign(Single<T> source) {
        return source.map(this::checkFnsResponse).subscribeOn(FruitsKitUtils.defaultFruitsNodeServiceScheduler());
    }

    private <T> Observable<T> assign(Observable<T> source) {
        return source.subscribeOn(FruitsKitUtils.defaultFruitsNodeServiceScheduler());
    }

    private <T> T checkFnsResponse(T source) throws FRSError {
        if (source instanceof FRSResponse) {
            ((FRSResponse) source).throwIfError();
        }
        return source;
    }

    @Override
    public Single<Block> getBlock(FruitsID block) {
        return assign(fruitsAPIService.getBlock(FruitsKitUtils.getEndpoint(), block.getID(), null, null, false))
                .map(Block::new);
    }

    @Override
    public Single<Block> getBlock(int height) {
        return assign(fruitsAPIService.getBlock(FruitsKitUtils.getEndpoint(), null, String.valueOf(height), null, false))
                        .map(Block::new);
    }

    @Override
    public Single<Block> getBlock(FruitsTimestamp timestamp) {
        return assign(fruitsAPIService.getBlock(FruitsKitUtils.getEndpoint(), null, null,
                String.valueOf(timestamp.getTimestamp()), false)).map(Block::new);
    }

    @Override
    public Single<Block[]> getBlocks(int firstIndex, int lastIndex) {
        return assign(fruitsAPIService.getBlocks(FruitsKitUtils.getEndpoint(), String.valueOf(firstIndex),
                String.valueOf(lastIndex), null))
                        .map(response -> Arrays.stream(response.getBlocks()).map(Block::new)
                                .collect(Collectors.toList()).toArray(new Block[0]));
    }

    @Override
    public Single<Constants> getConstants() {
        return assign(fruitsAPIService.getConstants(FruitsKitUtils.getEndpoint())).map(Constants::new);
    }

    @Override
    public Single<Account> getAccount(FruitsAddress accountId) {
    	return getAccount(accountId, null, null, null);
    }

    @Override
    public Single<Account> getAccount(FruitsAddress accountId, Integer height, Boolean estimateCommitment, Boolean getCommittedAmount) {
        return assign(fruitsAPIService.getAccount(FruitsKitUtils.getEndpoint(), accountId.getID(),
        		height==null ? null : String.valueOf(height), estimateCommitment==null ? null : String.valueOf(estimateCommitment),
        				getCommittedAmount==null ? null : String.valueOf(getCommittedAmount) )).map(Account::new);
    }

    @Override
    public Single<AT[]> getAccountATs(FruitsAddress accountId) {
        return assign(fruitsAPIService.getAccountATs(FruitsKitUtils.getEndpoint(), accountId.getID()))
                .map(response -> Arrays.stream(response.getATs()).map(AT::new).toArray(AT[]::new));
    }

    @Override
    public Single<Block[]> getAccountBlocks(FruitsAddress accountId) {
        return assign(fruitsAPIService.getAccountBlocks(FruitsKitUtils.getEndpoint(), accountId.getID(), null, null,
                null, null)).map(response -> Arrays.stream(response.getBlocks()).map(Block::new).toArray(Block[]::new));
    }

    @Override
    public Single<FruitsID[]> getAccountTransactionIDs(FruitsAddress accountId) {
        return assign(fruitsAPIService.getAccountTransactionIDs(FruitsKitUtils.getEndpoint(), accountId.getID(), null,
                null, null, null, null, null))
                        .map(response -> Arrays.stream(response.getTransactionIds()).map(FruitsID::fromLong)
                                .toArray(FruitsID[]::new));
    }

    @Override
    public Single<Transaction[]> getAccountTransactions(FruitsAddress accountId, Integer firstIndex, Integer lastIndex, Boolean includeIndirect) {
        return assign(fruitsAPIService.getAccountTransactions(FruitsKitUtils.getEndpoint(), accountId.getID(), null,
                null, null, firstIndex!=null ? firstIndex.toString() : null, lastIndex!=null ? lastIndex.toString() : null, null,
                		includeIndirect!=null && includeIndirect ? "true" : "false"))
                        .map(response -> Arrays.stream(response.getTransactions()).map(Transaction::new)
                                .toArray(Transaction[]::new));
    }

    @Override
    public Single<Transaction[]> getUnconfirmedTransactions(FruitsAddress accountId) {
        return assign(fruitsAPIService.getUnconfirmedTransactions(FruitsKitUtils.getEndpoint(), accountId==null ? null : accountId.getID()))
                .map(response -> Arrays.stream(response.getUnconfirmedTransactions()).map(Transaction::new)
                        .toArray(Transaction[]::new));
    }

    @Override
    public Single<FruitsAddress[]> getAccountsWithRewardRecipient(FruitsAddress accountId) {
        return assign(fruitsAPIService.getAccountsWithRewardRecipient(FruitsKitUtils.getEndpoint(), accountId.getID()))
                .map(response -> Arrays.stream(response.getAccounts()).map(FruitsAddress::fromEither)
                        .toArray(FruitsAddress[]::new));
    }

    @Override
    public Single<Asset> getAsset(FruitsID assetId) {
        return assign(fruitsAPIService.getAsset(FruitsKitUtils.getEndpoint(), assetId.getID())).map(Asset::new);
    }

    @Override
    public Single<AssetBalance[]> getAssetBalances(FruitsID assetId, Integer firstIndex, Integer lastIndex) {
        return assign(fruitsAPIService.getAssetAccounts(FruitsKitUtils.getEndpoint(), assetId.getID(), firstIndex, lastIndex))
                .map(response -> Arrays.stream(response.getAccountsAsset()).map(AssetBalance::new)
                        .toArray(AssetBalance[]::new));
    }

    @Override
    public Single<AssetTrade[]> getAssetTrades(FruitsID assetId, FruitsAddress account, Integer firstIndex, Integer lastIndex) {
        return assign(fruitsAPIService.getAssetTrades(FruitsKitUtils.getEndpoint(), assetId.getID(), account!=null ? account.getID() : null, firstIndex, lastIndex))
                .map(response -> Arrays.stream(response.getTrades()).map(AssetTrade::new).toArray(AssetTrade[]::new));
    }

    @Override
    public Single<AssetOrder[]> getAskOrders(FruitsID assetId) {
        return assign(fruitsAPIService.getAskOrders(FruitsKitUtils.getEndpoint(), assetId.getID()))
                .map(response -> Arrays.stream(response.getOrders()).map(AssetOrder::new).toArray(AssetOrder[]::new));
    }

    @Override
    public Single<AssetOrder[]> getBidOrders(FruitsID assetId) {
        return assign(fruitsAPIService.getBidOrders(FruitsKitUtils.getEndpoint(), assetId.getID()))
                .map(response -> Arrays.stream(response.getOrders()).map(AssetOrder::new).toArray(AssetOrder[]::new));
    }

    @Override
    public Single<AT> getAt(FruitsAddress atId) {
        return getAt(atId, true);
    }
    
    @Override
    public Single<AT> getAt(FruitsAddress atId, boolean includeDetails) {
        return assign(fruitsAPIService.getAt(FruitsKitUtils.getEndpoint(), atId.getID(), includeDetails)).map(AT::new);
    }

    @Override
    public Single<FruitsAddress[]> getAtIds() {
        return assign(fruitsAPIService.getAtIds(FruitsKitUtils.getEndpoint())).map(
                response -> Arrays.stream(response.getAtIds()).map(FruitsAddress::fromId).toArray(FruitsAddress[]::new));
    }

    @Override
    public Single<Transaction> getTransaction(FruitsID transactionId) {
        return assign(fruitsAPIService.getTransaction(FruitsKitUtils.getEndpoint(), transactionId.getID(), null))
                .map(Transaction::new);
    }

    @Override
    public Single<Transaction> getTransaction(byte[] fullHash) {
        return assign(fruitsAPIService.getTransaction(FruitsKitUtils.getEndpoint(), null, Hex.toHexString(fullHash)))
                .map(Transaction::new);
    }

    @Override
    public Single<byte[]> getTransactionBytes(FruitsID transactionId) {
        return assign(fruitsAPIService.getTransactionBytes(FruitsKitUtils.getEndpoint(), transactionId.getID()))
                .map(response -> Hex.decode(response.getTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransaction(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                amount.toPlanck().toString(), null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionAddCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.addCommitment(FruitsKitUtils.getEndpoint(),
                amount.toPlanck().toString(), null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionRemoveCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.removeCommitment(FruitsKitUtils.getEndpoint(),
                amount.toPlanck().toString(), null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionSetRewardRecipient(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.setRewardRecipient(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                amount != null ? amount.toPlanck().toString() : null, null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, message, true, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMessage(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, message, true, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipientAddress.getID(), Hex.toHexString(recipientPublicKey),
                amount != null ? amount.toPlanck().toString() : null, null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, message, true, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMessage(FruitsKitUtils.getEndpoint(), recipientAddress.getID(), Hex.toHexString(recipientPublicKey),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, message, true, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }


    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                amount != null ? amount.toPlanck().toString() : null, null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, Hex.toHexString(message), false, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMessage(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, Hex.toHexString(message), false, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                amount.toPlanck().toString(), null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, null, null, null, message.isText(), Hex.toHexString(message.getData()),
                Hex.toHexString(message.getNonce()), null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMessage(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, null, null, null, message.isText(), Hex.toHexString(message.getData()),
                Hex.toHexString(message.getNonce()), null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMoney(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                amount.toPlanck().toString(), null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, null, null, null, null, null, null, null, message.isText(),
                Hex.toHexString(message.getData()), Hex.toHexString(message.getNonce())))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return assign(fruitsAPIService.sendMessage(FruitsKitUtils.getEndpoint(), recipient.getID(), recipient.getPublicKeyString(),
                 null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(),
                deadline, referencedTransactionFullHash, false, null, null, null, null, null, null, null, message.isText(),
                Hex.toHexString(message.getData()), Hex.toHexString(message.getNonce())))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateIssueAssetTransaction(byte[] senderPublicKey, String name, String description, FruitsValue quantity, int decimals, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.issueAsset(FruitsKitUtils.getEndpoint(), name, description, quantity.toPlanck().toString(),
                decimals, null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransaction(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.transferAsset(FruitsKitUtils.getEndpoint(), recipient.getID(), assetId.getID(), null, quantity.toPlanck().toString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransactionWithMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, String message) {
        return assign(fruitsAPIService.transferAsset(FruitsKitUtils.getEndpoint(), recipient.getID(), assetId.getID(), null, quantity.toPlanck().toString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, message, true, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransactionWithEncryptedMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, FruitsEncryptedMessage message) {
        return assign(fruitsAPIService.transferAsset(FruitsKitUtils.getEndpoint(), recipient.getID(), assetId.getID(), null, quantity.toPlanck().toString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null,
                 message.isText(), Hex.toHexString(message.getData()), Hex.toHexString(message.getNonce()), null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generatePlaceAskOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.placeAskOrder(FruitsKitUtils.getEndpoint(), assetId.getID(), null, quantity.toPlanck().toString(), price.toPlanck().toString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generatePlaceBidOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.placeBidOrder(FruitsKitUtils.getEndpoint(), assetId.getID(), null, quantity.toPlanck().toString(), price.toPlanck().toString(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateCancelAskOrderTransaction(byte[] senderPublicKey, FruitsID orderId, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.cancelAskOrder(FruitsKitUtils.getEndpoint(), orderId.getID(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateCancelBidOrderTransaction(byte[] senderPublicKey, FruitsID orderId, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.cancelBidOrder(FruitsKitUtils.getEndpoint(), orderId.getID(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateSubscriptionCreationTransaction(byte[] senderPublicKey, FruitsValue amount, int frequency, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.createSubscription(FruitsKitUtils.getEndpoint(),
                null, null, amount.toPlanck().toString(), frequency,
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<byte[]> generateSubscriptionCancelTransaction(byte[] senderPublicKey, FruitsID subscription, FruitsValue fee, int deadline) {
        return assign(fruitsAPIService.cancelSubscription(FruitsKitUtils.getEndpoint(), subscription.getID(),
                null, Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), deadline, null, false, null, null, null, null, null, null, null, null, null, null))
                .map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public Single<FeeSuggestion> suggestFee() {
        return assign(fruitsAPIService.suggestFee(FruitsKitUtils.getEndpoint())).map(FeeSuggestion::new);
    }

    @Override
    public Observable<MiningInfo> getMiningInfo() {
        AtomicReference<MiningInfoResponse> miningInfo = new AtomicReference<>();
        return assign(Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMapSingle(l -> fruitsAPIService.getMiningInfo(FruitsKitUtils.getEndpoint()).map(this::checkFnsResponse))
                .filter(newMiningInfo -> {
                    synchronized (miningInfo) {
                        if (miningInfo.get() == null
                                || !Objects.equals(miningInfo.get().getGenerationSignature(),
                                        newMiningInfo.getGenerationSignature())
                                || !Objects.equals(miningInfo.get().getHeight(), newMiningInfo.getHeight())) {
                            miningInfo.set(newMiningInfo);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }).map(MiningInfo::new));
    }

    @Override
    public Single<TransactionBroadcast> broadcastTransaction(byte[] transactionBytes) {
        if (transactionBytes.length >= 2560) {
          return assign(fruitsAPIService.broadcastTransactionBig(FruitsKitUtils.getEndpoint(), Hex.toHexString(transactionBytes)))
              .map(TransactionBroadcast::new);          
        }
        return assign(fruitsAPIService.broadcastTransaction(FruitsKitUtils.getEndpoint(), Hex.toHexString(transactionBytes)))
                        .map(TransactionBroadcast::new);
    }

    @Override
    public Single<FruitsAddress> getRewardRecipient(FruitsAddress account) {
        return assign(fruitsAPIService.getRewardRecipient(FruitsKitUtils.getEndpoint(), account.getID()))
                .map(response -> FruitsAddress.fromEither(response.getRewardRecipient()));
    }

    @Override
    public Single<Long> submitNonce(String passphrase, String nonce, FruitsID accountId) {
        return assign(fruitsAPIService.submitNonce(FruitsKitUtils.getEndpoint(), passphrase, nonce,
                accountId == null ? null : accountId.getID(), "")).map(submitNonceResponse -> {
                    if (!Objects.equals(submitNonceResponse.getResult(), "success")) {
                        throw new FruitsApiException("Failed to submit nonce: " + submitNonceResponse.getResult());
                    }
                    return submitNonceResponse;
                }).map(SubmitNonceResponse::getDeadline);
    }

    @Override
    public Single<byte[]> generateMultiOutTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, Map<FruitsAddress, FruitsValue> recipients) throws IllegalArgumentException {
        return Single.fromCallable(() -> {
            StringBuilder recipientsString = new StringBuilder();
            if (recipients.size() > 64 || recipients.size() < 2) {
                throw new IllegalArgumentException("Must have 2-64 recipients, had " + recipients.size());
            }
            for (Map.Entry<FruitsAddress, FruitsValue> recipient : recipients.entrySet()) {
                recipientsString.append(recipient.getKey().getID()).append(":").append(recipient.getValue().toPlanck())
                        .append(";");
            }
            recipientsString.setLength(recipientsString.length() - 1);
            return recipientsString;
        }).flatMap(recipientsString -> assign(
                fruitsAPIService.sendMoneyMulti(FruitsKitUtils.getEndpoint(), null, Hex.toHexString(senderPublicKey),
                        fee.toPlanck().toString(), String.valueOf(deadline), null, false, recipientsString.toString()))
                                .map(response -> Hex.decode(response.getUnsignedTransactionBytes())));
    }

    @Override
    public Single<byte[]> generateMultiOutSameTransaction(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, Set<FruitsAddress> recipients) throws IllegalArgumentException {
        return Single.fromCallable(() -> {
            StringBuilder recipientsString = new StringBuilder();
            if (recipients.size() > 128 || recipients.size() < 2) {
                throw new IllegalArgumentException("Must have 2-128 recipients, had " + recipients.size());
            }
            for (FruitsAddress recipient : recipients) {
                recipientsString.append(recipient.getID()).append(";");
            }
            recipientsString.setLength(recipientsString.length() - 1);
            return recipientsString;
        }).flatMap(recipientsString -> assign(fruitsAPIService.sendMoneyMultiSame(FruitsKitUtils.getEndpoint(), null,
                Hex.toHexString(senderPublicKey), fee.toPlanck().toString(), String.valueOf(deadline), null, false,
                recipientsString.toString(), amount.toPlanck().toString()))
                        .map(response -> Hex.decode(response.getUnsignedTransactionBytes())));
    }

    @Override
    public Single<byte[]> generateCreateATTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, String name, String description, byte[] creationBytes) {
        // TODO: making it backward compatible for small AT codes
        if (creationBytes.length >= 2560) {
          return assign(fruitsAPIService.createATProgramBig(FruitsKitUtils.getEndpoint(), Hex.toHexString(senderPublicKey),
              fee.toPlanck().toString(), deadline, false, name, description, Hex.toHexString(creationBytes), null,
              null, 0, 0, 0, null)).map(response -> {
                  if (response.getError() != null)
                      throw new IllegalArgumentException(response.getError());
                  return response;
              }).map(response -> Hex.decode(response.getUnsignedTransactionBytes()));          
        }
        return assign(fruitsAPIService.createATProgram(FruitsKitUtils.getEndpoint(), Hex.toHexString(senderPublicKey),
                fee.toPlanck().toString(), deadline, false, name, description, Hex.toHexString(creationBytes), null,
                null, 0, 0, 0, null)).map(response -> {
                    if (response.getError() != null)
                        throw new IllegalArgumentException(response.getError());
                    return response;
                }).map(response -> Hex.decode(response.getUnsignedTransactionBytes()));
    }

    @Override
    public void close() {
        // Nothing to close.
    }

    private interface FruitsAPIService {
        @GET("{endpoint}?requestType=getBlock")
        Single<BlockResponse> getBlock(@Path("endpoint") String endpoint, @Query("block") String blockId,
                @Query("height") String blockHeight, @Query("timestamp") String timestamp,
                @Query("includeTransactions") boolean includeTransactions);

        @GET("{endpoint}?requestType=getBlockId")
        Single<BlockIDResponse> getBlockID(@Path("endpoint") String endpoint, @Query("height") String blockHeight);

        @GET("{endpoint}?requestType=getBlocks")
        Single<BlocksResponse> getBlocks(@Path("endpoint") String endpoint, @Query("firstIndex") String firstIndex,
                @Query("lastIndex") String lastIndex, @Query("includeTransactions") String[] transactions);

        @GET("{endpoint}?requestType=getConstants")
        Single<ConstantsResponse> getConstants(@Path("endpoint") String endpoint);

        @GET("{endpoint}?requestType=getAccount")
        Single<AccountResponse> getAccount(@Path("endpoint") String endpoint, @Query("account") String accountId,
        		@Query("height") String height, @Query("estimateCommitment") String calculateCommitment, @Query("getCommittedAmount") String getCommittedAmount);

        @GET("{endpoint}?requestType=getAccountATs")
        Single<AccountATsResponse> getAccountATs(@Path("endpoint") String endpoint, @Query("account") String accountId);

        @GET("{endpoint}?requestType=getAccountBlockIds")
        Single<AccountBlockIDsResponse> getAccountBlockIDs(@Path("endpoint") String endpoint,
                @Query("account") String accountId, @Query("timestamp") String timestamp,
                @Query("firstIndex") String firstIndex, @Query("lastIndex") String lastIndex);

        @GET("{endpoint}?requestType=getAccountBlocks")
        Single<AccountBlocksResponse> getAccountBlocks(@Path("endpoint") String endpoint,
                @Query("account") String accountId, @Query("timestamp") String timestamp,
                @Query("firstIndex") String firstIndex, @Query("lastIndex") String lastIndex,
                @Query("includeTransactions") String[] includedTransactions);

        @GET("{endpoint}?requestType=getAccountTransactionIds")
        Single<AccountTransactionIDsResponse> getAccountTransactionIDs(@Path("endpoint") String endpoint,
                @Query("account") String accountId, @Query("timestamp") String timestamp, @Query("type") String type,
                @Query("subtype") String subtype, @Query("firstIndex") String firstIndex,
                @Query("lastIndex") String lastIndex, @Query("numberOfConfirmations") String numberOfConfirmations);

        @GET("{endpoint}?requestType=getAccountTransactions")
        Single<AccountTransactionsResponse> getAccountTransactions(@Path("endpoint") String endpoint,
                @Query("account") String accountId, @Query("timestamp") String timestamp, @Query("type") String type,
                @Query("subtype") String subtype, @Query("firstIndex") String firstIndex,
                @Query("lastIndex") String lastIndex, @Query("numberOfConfirmations") String numberOfConfirmations,
                @Query("includeIndirect") String includeIndirect);

        @GET("{endpoint}?requestType=getUnconfirmedTransactions")
        Single<AccountUnconfirmedTransactionsResponse> getUnconfirmedTransactions(@Path("endpoint") String endpoint,
                @Query("account") String accountId);

        @GET("{endpoint}?requestType=getAccountsWithRewardRecipient")
        Single<AccountsWithRewardRecipientResponse> getAccountsWithRewardRecipient(@Path("endpoint") String endpoint,
                @Query("account") String accountId);

        @GET("{endpoint}?requestType=getAsset")
        Single<AssetResponse> getAsset(@Path("endpoint") String endpoint, @Query("asset") String assetId);

        @GET("{endpoint}?requestType=getAssetAccounts")
        Single<AccountsAssetResponse> getAssetAccounts(@Path("endpoint") String endpoint,
                @Query("asset") String assetId, @Query("firstIndex") Integer firstIndex, @Query("lastIndex") Integer lastIndex);

        @GET("{endpoint}?requestType=getTrades")
        Single<AssetTradesResponse> getAssetTrades(@Path("endpoint") String endpoint, @Query("asset") String assetId, @Query("account") String account, @Query("firstIndex") Integer firstIndex, @Query("lastIndex") Integer lastIndex);
        
        @GET("{endpoint}?requestType=getAskOrders")
        Single<AskOrdersResponse> getAskOrders(@Path("endpoint") String endpoint, @Query("asset") String assetId);
        
        @GET("{endpoint}?requestType=getBidOrders")
        Single<BidOrdersResponse> getBidOrders(@Path("endpoint") String endpoint, @Query("asset") String assetId);
        
        @GET("{endpoint}?requestType=getAT")
        Single<ATResponse> getAt(@Path("endpoint") String endpoint, @Query("at") String atId, @Query("includeDetails") boolean includeDetails);

        @GET("{endpoint}?requestType=getATIds")
        Single<AtIDsResponse> getAtIds(@Path("endpoint") String endpoint);

        @GET("{endpoint}?requestType=getTransaction")
        Single<TransactionResponse> getTransaction(@Path("endpoint") String endpoint,
                @Query("transaction") String transaction, @Query("fullHash") String fullHash);

        @GET("{endpoint}?requestType=getTransactionBytes")
        Single<TransactionBytesResponse> getTransactionBytes(@Path("endpoint") String endpoint,
                @Query("transaction") String transaction);

        @POST("{endpoint}?requestType=sendMoney")
        Single<GenerateTransactionResponse> sendMoney(@Path("endpoint") String endpoint,
                @Query("recipient") String recipient, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("amountNQT") String amount, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=addCommitment")
        Single<GenerateTransactionResponse> addCommitment(@Path("endpoint") String endpoint,
                @Query("amountNQT") String amount, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=removeCommitment")
        Single<GenerateTransactionResponse> removeCommitment(@Path("endpoint") String endpoint,
                @Query("amountNQT") String amount, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=setRewardRecipient")
        Single<GenerateTransactionResponse> setRewardRecipient(@Path("endpoint") String endpoint,
                @Query("recipient") String recipient, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=sendMessage")
        Single<GenerateTransactionResponse> sendMessage(
                @Path("endpoint") String endpoint, @Query("recipient") String recipient, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=issueAsset")
        Single<GenerateTransactionResponse> issueAsset(@Path("endpoint") String endpoint,
                @Query("name") String name, @Query("description") String description,
                @Query("quantityQNT") String quantity, @Query("decimals") int decimals, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);
        
        @POST("{endpoint}?requestType=transferAsset")
        Single<GenerateTransactionResponse> transferAsset(@Path("endpoint") String endpoint,
                @Query("recipient") String recipient, @Query("asset") String asset, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("quantityQNT") String quantity, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=placeAskOrder")
        Single<GenerateTransactionResponse> placeAskOrder(@Path("endpoint") String endpoint,
                @Query("asset") String asset, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("quantityQNT") String quantity, @Query("priceNQT") String price, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=placeBidOrder")
        Single<GenerateTransactionResponse> placeBidOrder(@Path("endpoint") String endpoint,
                @Query("asset") String asset, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("quantityQNT") String quantity, @Query("priceNQT") String price, @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=cancelAskOrder")
        Single<GenerateTransactionResponse> cancelAskOrder(@Path("endpoint") String endpoint,
                @Query("order") String order,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);
                
        
        @POST("{endpoint}?requestType=cancelBidOrder")
        Single<GenerateTransactionResponse> cancelBidOrder(@Path("endpoint") String endpoint,
                @Query("order") String order,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=sendMoneySubscription")
        Single<GenerateTransactionResponse> createSubscription(@Path("endpoint") String endpoint,
                @Query("recipient") String recipient, @Query("recipientPublicKey") String recipientPublicKey,
                @Query("amountNQT") String amount, @Query("frequency") int frequency,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @POST("{endpoint}?requestType=subscriptionCancel")
        Single<GenerateTransactionResponse> cancelSubscription(@Path("endpoint") String endpoint,
                @Query("subscription") String subscription,
                @Query("secretPhrase") String secretPhrase,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("message") String message,
                @Query("messageIsText") Boolean messageIsText, @Query("messageToEncrypt") String messageToEncrypt,
                @Query("messageToEncryptIsText") Boolean messageToEncryptIsText,
                @Query("encryptedMessageData") String encryptedMessageData,
                @Query("encryptedMessageNonce") String encryptedMessageNonce,
                @Query("messageToEncryptToSelf") String messageToEncryptToSelf,
                @Query("messageToEncryptToSelfIsText") Boolean messageToEncryptToSelfIsText,
                @Query("encryptedToSelfMessageData") String encryptedToSelfMessageData,
                @Query("encryptedToSelfMessageNonce") String encryptedToSelfMessageNonce);

        @GET("{endpoint}?requestType=suggestFee")
        Single<SuggestFeeResponse> suggestFee(@Path("endpoint") String endpoint);

        @GET("{endpoint}?requestType=getMiningInfo")
        Single<MiningInfoResponse> getMiningInfo(@Path("endpoint") String endpoint);

        @POST("{endpoint}?requestType=broadcastTransaction")
        Single<BroadcastTransactionResponse> broadcastTransaction(@Path("endpoint") String endpoint,
                @Query("transactionBytes") String transactionBytes);

        @POST("{endpoint}?requestType=broadcastTransaction")
        Single<BroadcastTransactionResponse> broadcastTransactionBig(@Path("endpoint") String endpoint,
                @Body String transactionBytes);

        @GET("{endpoint}?requestType=getRewardRecipient")
        Single<RewardRecipientResponse> getRewardRecipient(@Path("endpoint") String endpoint,
                @Query("account") String account);

        @POST("{endpoint}?requestType=submitNonce")
        Single<SubmitNonceResponse> submitNonce(@Path("endpoint") String endpoint,
                @Query("secretPhrase") String passphrase, @Query("nonce") String nonce,
                @Query("accountId") String accountId, @Query("blockheight") String blockheight);

        @POST("{endpoint}?requestType=sendMoneyMulti")
        Single<GenerateTransactionResponse> sendMoneyMulti(@Path("endpoint") String endpoint,
                @Query("secretPhrase") String secretPhrase, @Query("publicKey") String publicKey,
                @Query("feeNQT") String feeNQT, @Query("deadline") String deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("recipients") String recipients);

        @POST("{endpoint}?requestType=sendMoneyMultiSame")
        Single<GenerateTransactionResponse> sendMoneyMultiSame(@Path("endpoint") String endpoint,
                @Query("secretPhrase") String secretPhrase, @Query("publicKey") String publicKey,
                @Query("feeNQT") String feeNQT, @Query("deadline") String deadline,
                @Query("referencedTransactionFullHash") String referencedTransactionFullHash,
                @Query("broadcast") boolean broadcast, @Query("recipients") String recipients,
                @Query("amountNQT") String amountNQT);

        @POST("{endpoint}?requestType=createATProgram")
        Single<CreateATResponse> createATProgram(@Path("endpoint") String endpoint,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("broadcast") boolean broadcast, @Query("name") String name,
                @Query("description") String description, @Query("creationBytes") String creationBytes,
                @Query("code") String code, @Query("data") String data, @Query("dpages") int dpages,
                @Query("cspages") int cspages, @Query("uspages") int uspages,
                @Query("minActivationAmountNQT") String minActivationAmountNQT);
        
        @POST("{endpoint}?requestType=createATProgram")
        Single<CreateATResponse> createATProgramBig(@Path("endpoint") String endpoint,
                @Query("publicKey") String publicKey, @Query("feeNQT") String fee, @Query("deadline") int deadline,
                @Query("broadcast") boolean broadcast, @Query("name") String name,
                @Query("description") String description, @Body String creationBytes,                
                @Query("code") String code, @Query("data") String data, @Query("dpages") int dpages,
                @Query("cspages") int cspages, @Query("uspages") int uspages,
                @Query("minActivationAmountNQT") String minActivationAmountNQT);
    }
}
