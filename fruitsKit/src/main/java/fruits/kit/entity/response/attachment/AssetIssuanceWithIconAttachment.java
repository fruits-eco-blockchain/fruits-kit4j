package fruits.kit.entity.response.attachment;

import fruits.kit.entity.response.TransactionAttachment;

public class AssetIssuanceWithIconAttachment extends TransactionAttachment {
    private String name;
    private String description;
    private int decimals;
    private String quantityQNT;
    private String icon;

    public AssetIssuanceWithIconAttachment(int version, String name, String description, int decimals, String quantityQNT, String icon) {
        super(version);
        this.name = name;
        this.description = description;
        this.decimals = decimals;
        this.quantityQNT = quantityQNT;
    }
    //TODO constructor for FnsApi
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDecimals() {
        return decimals;
    }

    public String getQuantityQNT() {
        return quantityQNT;
    }
    
    public String getIcon() {
    	return icon;
    }
}
