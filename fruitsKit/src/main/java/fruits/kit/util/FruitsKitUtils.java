package fruits.kit.util;

import com.google.gson.GsonBuilder;

import fruits.kit.entity.response.http.attachment.MultiOutAttachmentResponse;
import fruits.kit.entity.response.http.attachment.TransactionAttachmentAndAppendagesResponse;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public final class FruitsKitUtils {
    private static final AtomicReference<String> endpoint = new AtomicReference<>("fruits");
    private static final AtomicReference<String> addressPrefix = new AtomicReference<>("FRUITS");
    private static final ArrayList<String> validAddressPrefixes = new ArrayList<String>();
    static {
        validAddressPrefixes.add(addressPrefix.get());
    }
    private static final AtomicReference<String> valueSuffix = new AtomicReference<>("FRUITS");

    public static GsonBuilder buildGson(GsonBuilder builder) {
        return builder
                // Response entities
                .registerTypeAdapter(TransactionAttachmentAndAppendagesResponse.class, TransactionAttachmentAndAppendagesResponse.SERIALIZER)
                .registerTypeAdapter(TransactionAttachmentAndAppendagesResponse.class, TransactionAttachmentAndAppendagesResponse.DESERIALIZER)
                .registerTypeAdapter(MultiOutAttachmentResponse.MultiOutRecipient.class, MultiOutAttachmentResponse.MultiOutRecipient.SERIALIZER)
                .registerTypeAdapter(MultiOutAttachmentResponse.MultiOutRecipient.class, MultiOutAttachmentResponse.MultiOutRecipient.DESERIALIZER)
                ;
    }

    public static String getEndpoint() {
        return endpoint.get();
    }

    public static void setEndpoint(String newEndpoint) {
        if (newEndpoint != null) {
            endpoint.set(newEndpoint);
        }
    }

    public static String getAddressPrefix() {
        return addressPrefix.get();
    }
    
    public static ArrayList<String> getValidAddressPrefixes() {
        return validAddressPrefixes;
    }

    public static void setAddressPrefix(String newAddressPrefix) {
        if (newAddressPrefix != null) {
            addressPrefix.set(newAddressPrefix);
        }
        addAddressPrefix(newAddressPrefix);
    }
    
    public static void addAddressPrefix(String newAddressPrefix) {
        validAddressPrefixes.add(newAddressPrefix);
    }

    public static String getValueSuffix() {
        return valueSuffix.get();
    }

    public static void setValueSuffix(String newValueSuffix) {
        if (newValueSuffix != null) {
            valueSuffix.set(newValueSuffix);
        }
    }

    public static Scheduler defaultFruitsNodeServiceScheduler() {
        return Schedulers.io();
    }

    public static GsonBuilder buildGson() {
        return buildGson(new GsonBuilder());
    }
}
