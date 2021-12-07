package fruits.kit.entity.response;

import java.util.Arrays;

import fruits.kit.entity.FruitsAddress;
import fruits.kit.entity.FruitsID;
import fruits.kit.entity.response.http.ConstantsResponse;
import fruits.kit.entity.response.http.TransactionSubtypeResponse;
import fruits.kit.entity.response.http.TransactionTypeResponse;

public class Constants {
    private final int maxBlockPayloadLength;
    private final int maxArbitraryMessageLength;
    private final FruitsID genesisBlockId;
    private final FruitsAddress genesisAccountId;
    private final TransactionType[] transactionTypes;

    public Constants(int maxBlockPayloadLength, int maxArbitraryMessageLength, FruitsAddress genesisAccountId, FruitsID genesisBlockId, TransactionType[] transactionTypes) {
        this.maxBlockPayloadLength = maxBlockPayloadLength;
        this.maxArbitraryMessageLength = maxArbitraryMessageLength;
        this.genesisAccountId = genesisAccountId;
        this.genesisBlockId = genesisBlockId;
        this.transactionTypes = transactionTypes;
    }

    public Constants(ConstantsResponse constantsResponse) {
        this.maxBlockPayloadLength = constantsResponse.getMaxBlockPayloadLength();
        this.maxArbitraryMessageLength = constantsResponse.getMaxArbitraryMessageLength();
        this.genesisBlockId = FruitsID.fromLong(constantsResponse.getGenesisBlockId());
        this.genesisAccountId = FruitsAddress.fromEither(constantsResponse.getGenesisAccountId());
        this.transactionTypes = Arrays.stream(constantsResponse.getTransactionTypes())
                .map(TransactionType::new)
                .toArray(TransactionType[]::new);
    }

    public int getMaxBlockPayloadLength() {
        return maxBlockPayloadLength;
    }

    public int getMaxArbitraryMessageLength() {
        return maxArbitraryMessageLength;
    }

    public FruitsAddress getGenesisAccountId() {
        return genesisAccountId;
    }

    public FruitsID getGenesisBlockId() {
        return genesisBlockId;
    }

    public TransactionType[] getTransactionTypes() {
        return transactionTypes;
    }

    public static class TransactionType {
        private final String description;
        private final int type;
        private final Subtype[] subtypes;

        public TransactionType(String description, int type, Subtype[] subtypes) {
            this.description = description;
            this.type = type;
            this.subtypes = subtypes;
        }

        public TransactionType(TransactionTypeResponse transactionTypeResponse) {
            this.description = transactionTypeResponse.getDescription();
            this.type = transactionTypeResponse.getValue();
            this.subtypes = Arrays.stream(transactionTypeResponse.getSubtypes())
                    .map(Subtype::new)
                    .toArray(Subtype[]::new);
        }

        public String getDescription() {
            return description;
        }

        public int getType() {
            return type;
        }

        public Subtype[] getSubtypes() {
            return subtypes;
        }

        public static class Subtype {
            private final String description;
            private final int subtype;

            public Subtype(String description, int subtype) {
                this.description = description;
                this.subtype = subtype;
            }

            public Subtype(TransactionSubtypeResponse transactionSubtypeResponse) {
                this.description = transactionSubtypeResponse.getDescription();
                this.subtype = transactionSubtypeResponse.getValue();
            }

            public String getDescription() {
                return description;
            }

            public int getSubtype() {
                return subtype;
            }
        }
    }
}
