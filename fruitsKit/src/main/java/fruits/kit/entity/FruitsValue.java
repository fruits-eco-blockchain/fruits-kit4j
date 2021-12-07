package fruits.kit.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;

import fruits.kit.util.FruitsKitUtils;

public final class FruitsValue implements Comparable<FruitsValue> {
    private static final int decimals = 8;

    public static final FruitsValue ZERO = FruitsValue.fromPlanck(0);
    
    private final BigInteger planck;

    private FruitsValue(BigInteger planck) {
        this.planck = planck;
    }

    /**
     * @param planck The number of planck
     * @return The FruitsValue representing this number of planck, or a FruitsValue representing 0 Fruits if the string could not be parsed
     */
    public static FruitsValue fromPlanck(String planck) {
        if (planck == null) return ZERO;
        if (planck.toLowerCase(Locale.ENGLISH).endsWith(" planck")) {
            planck = planck.substring(0, planck.length() - 7);
        }
        try {
            return fromPlanck(new BigInteger(planck));
        } catch (NumberFormatException e) {
            return fromPlanck(BigInteger.ZERO);
        }
    }

    /**
     * @param planck The number of planck
     * @return The FruitsValue representing this number of planck
     */
    public static FruitsValue fromPlanck(long planck) {
        return fromPlanck(BigInteger.valueOf(planck));
    }

    public static FruitsValue fromPlanck(BigInteger planck) {
        if (planck == null) return ZERO;
        return new FruitsValue(planck);
    }

    /**
     * @param fruits The number of fruits
     * @return The FruitsValue representing this number of fruits, or a FruitsValue representing 0 Fruits if the string could not be parsed
     */
    public static FruitsValue fromFruits(String fruits) {
        if (fruits == null) return ZERO;
        if (fruits.toLowerCase(Locale.ENGLISH).endsWith(" " + FruitsKitUtils.getValueSuffix().toLowerCase(Locale.ENGLISH))) {
            fruits = fruits.substring(0, fruits.length() - 7);
        }
        try {
            return fromFruits(new BigDecimal(fruits));
        } catch (NumberFormatException e) {
            return fromPlanck(BigInteger.ZERO);
        }
    }

    /**
     * @param fruits The number of fruits
     * @return The FruitsValue representing this number of fruits
     */
    public static FruitsValue fromFruits(double fruits) {
        return fromFruits(BigDecimal.valueOf(fruits));
    }

    public static FruitsValue fromFruits(BigDecimal fruits) {
        if (fruits == null) return ZERO;
        return new FruitsValue(fruits.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
    }

    private static BigDecimal roundToThreeDP(BigDecimal in) {
        if (in.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return in.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    }

    @Override
    public String toString() {
        return toFormattedString();
    }

    /**
     * @return The value with the "FRUITS" suffix and rounded to 3 decimal places
     */
    public String toFormattedString() {
        return roundToThreeDP(toFruits()).toPlainString() + " " + FruitsKitUtils.getValueSuffix();
    }

    /**
     * @return The value without the "FRUITS" suffix and without rounding
     */
    public String toUnformattedString() {
        return toFruits().stripTrailingZeros().toPlainString();
    }

    /**
     * @return A BigInteger representing the number of planck
     */
    public BigInteger toPlanck() {
        return planck;
    }

    public BigDecimal toFruits() {
        return new BigDecimal(planck, decimals);
    }

    public FruitsValue add(FruitsValue other) {
        return fromPlanck(planck.add(other.planck));
    }

    public FruitsValue subtract(FruitsValue other) {
        return fromPlanck(planck.subtract(other.planck));
    }

    public FruitsValue multiply(long multiplicand) {
        return fromPlanck(planck.multiply(BigInteger.valueOf(multiplicand)));
    }

    public FruitsValue multiply(double multiplicand) {
        return fromFruits(toFruits().multiply(BigDecimal.valueOf(multiplicand)));
    }

    public FruitsValue multiply(BigInteger multiplicand) {
        return fromPlanck(planck.multiply(multiplicand));
    }

    public FruitsValue multiply(BigDecimal multiplicand) {
        return fromFruits(toFruits().multiply(multiplicand));
    }

    public FruitsValue divide(long divisor) {
        return fromPlanck(planck.divide(BigInteger.valueOf(divisor)));
    }

    public FruitsValue divide(double divisor) {
        return fromFruits(toFruits().divide(BigDecimal.valueOf(divisor), decimals, RoundingMode.HALF_UP));
    }
    
    public FruitsValue divide(BigInteger divisor) {
        return fromPlanck(planck.divide(divisor));
    }

    public FruitsValue divide(BigDecimal divisor) {
        return fromFruits(toFruits().divide(divisor, decimals, RoundingMode.HALF_UP));
    }

    public FruitsValue abs() {
        return fromPlanck(planck.abs());
    }

    @Override
    public int compareTo(FruitsValue other) {
        if (other == null) return 1;
        return planck.compareTo(other.planck);
    }

    public static FruitsValue min(FruitsValue a, FruitsValue b) {
        return (a.compareTo(b) <= 0) ? a : b;
    }

    public static FruitsValue max(FruitsValue a, FruitsValue b) {
        return (a.compareTo(b) >= 0) ? a : b;
    }

    /**
     * @return The number of Fruits as a double
     */
    public double doubleValue() { // TODO test
        return toFruits().doubleValue();
    }

    /**
     * @return The number of planck as a long
     */
    public long longValue() { // TODO test
        return toPlanck().longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FruitsValue that = (FruitsValue) o;

        return planck != null ? planck.equals(that.planck) : that.planck == null;
    }

    @Override
    public int hashCode() {
        return planck != null ? planck.hashCode() : 0;
    }
}
