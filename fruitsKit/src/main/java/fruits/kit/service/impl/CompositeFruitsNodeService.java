package fruits.kit.service.impl;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;
import java.util.stream.Collectors;

import fruits.kit.entity.*;
import fruits.kit.entity.response.*;
import fruits.kit.service.FruitsNodeService;
import fruits.kit.util.FruitsKitUtils;

public class CompositeFruitsNodeService implements FruitsNodeService {
    private final FruitsNodeService[] fruitsNodeServices;

    /**
     * @param fruitsNodeServices The fruits node services this will wrap, in order of priority
     */
    public CompositeFruitsNodeService(FruitsNodeService... fruitsNodeServices) {
        if (fruitsNodeServices == null || fruitsNodeServices.length == 0) throw new IllegalArgumentException("No Fruits Node Services Provided");
        this.fruitsNodeServices = fruitsNodeServices;
    }

    private <T> Single<T> compositeSingle(Collection<Single<T>> singles) {
        return Single.create((SingleEmitter<T> emitter) -> {
            AtomicInteger errorCount = new AtomicInteger(0);
            CompositeDisposable compositeDisposable = new CompositeDisposable();
            emitter.setCancellable(compositeDisposable::dispose);
            for (Single<T> single : singles) {
                compositeDisposable.add(single.subscribe(emitter::onSuccess, error -> {
                    synchronized (errorCount) {
                        if (errorCount.incrementAndGet() == singles.size()) { // Every single has errored
                            emitter.tryOnError(error);
                        }
                    }
                }));
            }
        })
                .subscribeOn(FruitsKitUtils.defaultFruitsNodeServiceScheduler());
    }

    private synchronized <T> void doIfUsedObservable(ObservableEmitter<T> emitter, AtomicInteger usedObservable, AtomicReferenceArray<Disposable> disposables, int myI, Runnable runnable) {
        int used = usedObservable.get();
        if (used == -1) {
            // We are the first!
            usedObservable.set(myI);
            runnable.run();
            // Kill all of the others.
            Disposable myDisposable = disposables.get(myI);
            disposables.set(myI, null);
            emitter.setCancellable(() -> {
                if (myDisposable != null) {
                    myDisposable.dispose();
                }
            }); // Calling this calls the previous one, so all of the others get disposed.
        } else if (used == myI) {
            // We are the used observable.
            runnable.run();
        }
    }

    private <T> Observable<T> compositeObservable(List<Observable<T>> observables) {
        return Observable.create((ObservableEmitter<T> emitter) -> {
            AtomicInteger usedObservable = new AtomicInteger(-1);
            AtomicInteger errorCount = new AtomicInteger(0);
            AtomicReferenceArray<Disposable> disposables = new AtomicReferenceArray<>(observables.size());
            emitter.setCancellable(() -> {
                for (int i = 0; i < disposables.length(); i++) {
                    Disposable disposable = disposables.get(i);
                    if (disposable != null) disposable.dispose();
                }
            });
            for (int i = 0; i < observables.size(); i++) {
                final int myI = i;
                Observable<T> observable = observables.get(i);
                disposables.set(myI, observable.subscribe(t -> doIfUsedObservable(emitter, usedObservable, disposables, myI, () -> emitter.onNext(t)),
                        error -> {
                            synchronized (errorCount) {
                                if (errorCount.incrementAndGet() == observables.size() || usedObservable.get() == myI) { // Every single has errored
                                    emitter.tryOnError(error);
                                }
                            }
                        },
                        () -> doIfUsedObservable(emitter, usedObservable, disposables, myI, emitter::onComplete)));
            }
        })
                .subscribeOn(FruitsKitUtils.defaultFruitsNodeServiceScheduler());
    }

    private <T, U> List<U> map(T[] ts, Function<T, U> mapper) {
        return Arrays.stream(ts)
                .map(mapper)
                .collect(Collectors.toList());
    }

    private <T> Single<T> performFastest(Function<FruitsNodeService, Single<T>> function) {
        return compositeSingle(map(fruitsNodeServices, function));
    }

    private <T> Observable<T> performFastestObservable(Function<FruitsNodeService, Observable<T>> function) {
        return compositeObservable(map(fruitsNodeServices, function));
    }

    private <T> Single<T> performOnOne(Function<FruitsNodeService, Single<T>> function) {
        List<Single<T>> singles = map(fruitsNodeServices, function);
        for (int i = singles.size() - 2; i >= 0; i--) {
            singles.set(i, singles.get(i).onErrorResumeNext(singles.get(i+1)));
        }
        return singles.get(0);
    }

    @Override
    public Single<Block> getBlock(FruitsID block) {
        return performFastest(service -> service.getBlock(block));
    }

    @Override
    public Single<Block> getBlock(int height) {
        return performFastest(service -> service.getBlock(height));
    }

    @Override
    public Single<Block> getBlock(FruitsTimestamp timestamp) {
        return performFastest(service -> service.getBlock(timestamp));
    }

    @Override
    public Single<Block[]> getBlocks(int firstIndex, int lastIndex) {
        return performFastest(service -> service.getBlocks(firstIndex, lastIndex));
    }

    @Override
    public Single<Constants> getConstants() {
        return performFastest(FruitsNodeService::getConstants);
    }

    @Override
    public Single<Account> getAccount(FruitsAddress accountId) {
        return getAccount(accountId, null, null, null);
    }

    @Override
    public Single<Account> getAccount(FruitsAddress accountId, Integer height, Boolean estimateCommitment, Boolean getCommittedAmount) {
        return performFastest(service -> service.getAccount(accountId, height, estimateCommitment, getCommittedAmount));
    }

    @Override
    public Single<AT[]> getAccountATs(FruitsAddress accountId) {
        return performFastest(service -> service.getAccountATs(accountId));
    }

    @Override
    public Single<Block[]> getAccountBlocks(FruitsAddress accountId) {
        return performFastest(service -> service.getAccountBlocks(accountId));
    }

    @Override
    public Single<FruitsID[]> getAccountTransactionIDs(FruitsAddress accountId) {
        return performFastest(service -> service.getAccountTransactionIDs(accountId));
    }

    @Override
    public Single<Transaction[]> getAccountTransactions(FruitsAddress accountId, Integer firstIndex, Integer lastIndex, Boolean includeIndirect) {
        return performFastest(service -> service.getAccountTransactions(accountId, firstIndex, lastIndex, includeIndirect));
    }

    @Override
    public Single<Transaction[]> getUnconfirmedTransactions(FruitsAddress accountId) {
        return performFastest(service -> service.getUnconfirmedTransactions(accountId));
    }

    @Override
    public Single<FruitsAddress[]> getAccountsWithRewardRecipient(FruitsAddress accountId) {
        return performFastest(service -> service.getAccountsWithRewardRecipient(accountId));
    }

    @Override
    public Single<AssetBalance[]> getAssetBalances(FruitsID assetId, Integer firstIndex, Integer lastIndex) {
        return performFastest(service -> service.getAssetBalances(assetId, firstIndex, lastIndex));
    }

    @Override
    public Single<Asset> getAsset(FruitsID assetId) {
        return performFastest(service -> service.getAsset(assetId));
    }

    @Override
    public Single<AssetTrade[]> getAssetTrades(FruitsID assetId, FruitsAddress account, Integer firstIndex, Integer lastIndex) {
        return performFastest(service -> service.getAssetTrades(assetId, account, firstIndex, lastIndex));
    }

    @Override
    public Single<AssetOrder[]> getAskOrders(FruitsID assetId) {
        return performFastest(service -> service.getAskOrders(assetId));
    }

    @Override
    public Single<AssetOrder[]> getBidOrders(FruitsID assetId) {
        return performFastest(service -> service.getBidOrders(assetId));
    }

    @Override
    public Single<AT> getAt(FruitsAddress at) {
    	return getAt(at, true);
    }

	@Override
	public Single<AT> getAt(FruitsAddress at, boolean includeDetails) {
        return performFastest(service -> service.getAt(at, includeDetails));
	}

    @Override
    public Single<FruitsAddress[]> getAtIds() {
        return performFastest(service -> service.getAtIds());
    }

    @Override
    public Single<Transaction> getTransaction(FruitsID transactionId) {
        return performFastest(service -> service.getTransaction(transactionId));
    }

    @Override
    public Single<Transaction> getTransaction(byte[] fullHash) {
        return performFastest(service -> service.getTransaction(fullHash));
    }

    @Override
    public Single<byte[]> getTransactionBytes(FruitsID transactionId) {
        return performFastest(service -> service.getTransactionBytes(transactionId));
    }

    @Override
    public Single<byte[]> generateTransaction(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransaction(recipient, senderPublicKey, amount, fee, deadline, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipient, senderPublicKey, amount, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipient, senderPublicKey, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipientAddress, recipientPublicKey, senderPublicKey, amount, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipientAddress, byte[] recipientPublicKey, byte[] senderPublicKey, FruitsValue fee, int deadline, String message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipientAddress, recipientPublicKey, senderPublicKey, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipient, senderPublicKey, amount, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, byte[] message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithMessage(recipient, senderPublicKey, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithEncryptedMessage(recipient, senderPublicKey, amount, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessage(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithEncryptedMessage(recipient, senderPublicKey, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithEncryptedMessageToSelf(recipient, senderPublicKey, amount, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<byte[]> generateTransactionWithEncryptedMessageToSelf(FruitsAddress recipient, byte[] senderPublicKey, FruitsValue fee, int deadline, FruitsEncryptedMessage message, String referencedTransactionFullHash) {
        return performFastest(service -> service.generateTransactionWithEncryptedMessageToSelf(recipient, senderPublicKey, fee, deadline, message, referencedTransactionFullHash));
    }

    @Override
    public Single<FeeSuggestion> suggestFee() {
        return performFastest(FruitsNodeService::suggestFee);
    }

    @Override
    public Observable<MiningInfo> getMiningInfo() {
        return performFastestObservable(FruitsNodeService::getMiningInfo);
    }

    @Override
    public Single<TransactionBroadcast> broadcastTransaction(byte[] transactionBytes) {
        return performFastest(service -> service.broadcastTransaction(transactionBytes));
    }

    @Override
    public Single<FruitsAddress> getRewardRecipient(FruitsAddress account) {
        return performFastest(service -> service.getRewardRecipient(account));
    }

    @Override
    public Single<Long> submitNonce(String passphrase, String nonce, FruitsID accountId) {
        return performOnOne(service -> service.submitNonce(passphrase, nonce, accountId));
    }

    @Override
    public Single<byte[]> generateMultiOutTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, Map<FruitsAddress, FruitsValue> recipients) throws IllegalArgumentException {
        return performFastest(service -> service.generateMultiOutTransaction(senderPublicKey, fee, deadline, recipients));
    }

    @Override
    public Single<byte[]> generateMultiOutSameTransaction(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee, int deadline, Set<FruitsAddress> recipients) throws IllegalArgumentException {
        return performFastest(service -> service.generateMultiOutSameTransaction(senderPublicKey, amount, fee, deadline, recipients));
    }

    @Override
    public Single<byte[]> generateCreateATTransaction(byte[] senderPublicKey, FruitsValue fee, int deadline, String name, String description, byte[] creationBytes) {
        return performFastest(service -> service.generateCreateATTransaction(senderPublicKey, fee, deadline, name, description, creationBytes));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransaction(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateTransferAssetTransaction(senderPublicKey, recipient, assetId, quantity, fee, deadline));
    }

    @Override
    public Single<byte[]> generateIssueAssetTransaction(byte[] senderPublicKey, String name, String description, FruitsValue quantity, int decimals, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateIssueAssetTransaction(senderPublicKey, name, description, quantity, decimals, fee, deadline));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransactionWithMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, String message) {
        return performFastest(service -> service.generateTransferAssetTransactionWithMessage(senderPublicKey, recipient, assetId, quantity, fee, deadline, message));
    }

    @Override
    public Single<byte[]> generateTransferAssetTransactionWithEncryptedMessage(byte[] senderPublicKey, FruitsAddress recipient, FruitsID assetId, FruitsValue quantity, FruitsValue fee, int deadline, FruitsEncryptedMessage message) {
        return performFastest(service -> service.generateTransferAssetTransactionWithEncryptedMessage(senderPublicKey, recipient, assetId, quantity, fee, deadline, message));
    }

    @Override
    public Single<byte[]> generatePlaceAskOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generatePlaceAskOrderTransaction(senderPublicKey, assetId, quantity, price, fee, deadline));
    }

    @Override
    public Single<byte[]> generatePlaceBidOrderTransaction(byte[] senderPublicKey, FruitsID assetId, FruitsValue quantity, FruitsValue price, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generatePlaceBidOrderTransaction(senderPublicKey, assetId, quantity, price, fee, deadline));
    }

    @Override
    public Single<byte[]> generateCancelAskOrderTransaction(byte[] senderPublicKey, FruitsID orderId, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateCancelAskOrderTransaction(senderPublicKey, orderId, fee, deadline));
    }
    
    @Override
    public Single<byte[]> generateCancelBidOrderTransaction(byte[] senderPublicKey, FruitsID orderId, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateCancelBidOrderTransaction(senderPublicKey, orderId, fee, deadline));
    }

    @Override
    public Single<byte[]> generateSubscriptionCreationTransaction(byte[] senderPublicKey, FruitsValue amount, int frequency, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateSubscriptionCreationTransaction(senderPublicKey, amount, frequency, fee, deadline));
    }

    @Override
    public Single<byte[]> generateSubscriptionCancelTransaction(byte[] senderPublicKey, FruitsID subscription, FruitsValue fee, int deadline) {
        return performFastest(service -> service.generateSubscriptionCancelTransaction(senderPublicKey, subscription, fee, deadline));
    }

    @Override
    public void close() throws Exception {
        for (FruitsNodeService fruitsNodeService : fruitsNodeServices) {
            fruitsNodeService.close();
        }
    }

	@Override
	public Single<byte[]> generateTransactionSetRewardRecipient(FruitsAddress recipient, byte[] senderPublicKey,
			FruitsValue fee, int deadline) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<byte[]> generateTransactionAddCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee,
			int deadline) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Single<byte[]> generateTransactionRemoveCommitment(byte[] senderPublicKey, FruitsValue amount, FruitsValue fee,
			int deadline) {
		// TODO Auto-generated method stub
		return null;
	}
}
