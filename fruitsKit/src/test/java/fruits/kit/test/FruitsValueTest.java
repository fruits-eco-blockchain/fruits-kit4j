package fruits.kit.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.entity.FruitsValue;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FruitsValueTest {
    @Test
    public void testConstructors() {
        assertEquals("123456789", FruitsValue.fromFruits("1.23456789").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromFruits("1.23456789 fruits").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromFruits("1.23456789 FRUITS").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromFruits(1.23456789).toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromFruits(new BigDecimal("1.23456789")).toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromPlanck("123456789").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromPlanck("123456789 planck").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromPlanck("123456789 PLANCK").toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromPlanck(123456789).toPlanck().toString());
        assertEquals("123456789", FruitsValue.fromPlanck(new BigInteger("123456789")).toPlanck().toString());

        // Test null -> 0
        assertEquals(FruitsValue.ZERO, FruitsValue.fromPlanck((String) null));
        assertEquals(FruitsValue.ZERO, FruitsValue.fromPlanck((BigInteger) null));
        assertEquals(FruitsValue.ZERO, FruitsValue.fromFruits((String) null));
        assertEquals(FruitsValue.ZERO, FruitsValue.fromFruits((BigDecimal) null));
    }

    @Test
    public void testToString() {
        // Positive
        assertEquals("1 FRUITS", FruitsValue.fromFruits(1).toString());
        assertEquals("1 FRUITS", FruitsValue.fromFruits(1).toFormattedString());
        assertEquals("1", FruitsValue.fromFruits(1).toUnformattedString());
        assertEquals("1 FRUITS", FruitsValue.fromFruits(1.00000001).toString());
        assertEquals("1 FRUITS", FruitsValue.fromFruits(1.00000001).toFormattedString());
        assertEquals("1.00000001", FruitsValue.fromFruits(1.00000001).toUnformattedString());
        assertEquals("1.235 FRUITS", FruitsValue.fromPlanck(123456789).toString());
        // Negative
        assertEquals("-1 FRUITS", FruitsValue.fromFruits(-1).toString());
        assertEquals("-1 FRUITS", FruitsValue.fromFruits(-1).toFormattedString());
        assertEquals("-1", FruitsValue.fromFruits(-1).toUnformattedString());
        assertEquals("-1 FRUITS", FruitsValue.fromFruits(-1.00000001).toString());
        assertEquals("-1 FRUITS", FruitsValue.fromFruits(-1.00000001).toFormattedString());
        assertEquals("-1.00000001", FruitsValue.fromFruits(-1.00000001).toUnformattedString());
        assertEquals("-1.235 FRUITS", FruitsValue.fromPlanck(-123456789).toString());
    }

    @Test
    public void testToFruits() {
        assertEquals(BigDecimal.valueOf(100000000, 8), FruitsValue.fromFruits(1).toFruits());
        assertEquals(BigDecimal.valueOf(-100000000, 8), FruitsValue.fromFruits(-1).toFruits());
    }

    @Test
    public void testAdd() {
        assertEquals(FruitsValue.fromFruits(1), FruitsValue.fromFruits(0.5).add(FruitsValue.fromFruits(0.5)));
        assertEquals(FruitsValue.fromFruits(0), FruitsValue.fromFruits(-0.5).add(FruitsValue.fromFruits(0.5)));
        assertEquals(FruitsValue.fromFruits(-1), FruitsValue.fromFruits(-0.5).add(FruitsValue.fromFruits(-0.5)));
    }

    @Test
    public void testSubtract() {
        assertEquals(FruitsValue.fromFruits(1), FruitsValue.fromFruits(1.5).subtract(FruitsValue.fromFruits(0.5)));
        assertEquals(FruitsValue.fromFruits(0), FruitsValue.fromFruits(0.5).subtract(FruitsValue.fromFruits(0.5)));
        assertEquals(FruitsValue.fromFruits(-1), FruitsValue.fromFruits(-0.5).subtract(FruitsValue.fromFruits(0.5)));
    }

    @Test
    public void testMultiply() {
        // Positive + positive
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(2).multiply(5));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(4).multiply(2.5));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(2).multiply(BigInteger.valueOf(5)));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(4).multiply(BigDecimal.valueOf(2.5)));

        // Positive + negative
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(2).multiply(-5));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(4).multiply(-2.5));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(2).multiply(BigInteger.valueOf(-5)));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(4).multiply(BigDecimal.valueOf(-2.5)));

        // Negative + positive
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(-2).multiply(5));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(-4).multiply(2.5));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(-2).multiply(BigInteger.valueOf(5)));
        assertEquals(FruitsValue.fromFruits(-10), FruitsValue.fromFruits(-4).multiply(BigDecimal.valueOf(2.5)));

        // Negative + negative
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(-2).multiply(-5));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(-4).multiply(-2.5));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(-2).multiply(BigInteger.valueOf(-5)));
        assertEquals(FruitsValue.fromFruits(10), FruitsValue.fromFruits(-4).multiply(BigDecimal.valueOf(-2.5)));
    }
    
    @Test
    public void testDivide() {
        // Positive + positive
        assertEquals(FruitsValue.fromFruits(0.4), FruitsValue.fromFruits(2).divide(5));
        assertEquals(FruitsValue.fromFruits(1.6), FruitsValue.fromFruits(4).divide(2.5));
        assertEquals(FruitsValue.fromFruits(0.4), FruitsValue.fromFruits(2).divide(BigInteger.valueOf(5)));
        assertEquals(FruitsValue.fromFruits(1.6), FruitsValue.fromFruits(4).divide(BigDecimal.valueOf(2.5)));

        // Positive + negative
        assertEquals(FruitsValue.fromFruits(-0.4), FruitsValue.fromFruits(2).divide(-5));
        assertEquals(FruitsValue.fromFruits(-1.6), FruitsValue.fromFruits(4).divide(-2.5));
        assertEquals(FruitsValue.fromFruits(-0.4), FruitsValue.fromFruits(2).divide(BigInteger.valueOf(-5)));
        assertEquals(FruitsValue.fromFruits(-1.6), FruitsValue.fromFruits(4).divide(BigDecimal.valueOf(-2.5)));

        // Negative + positive
        assertEquals(FruitsValue.fromFruits(-0.4), FruitsValue.fromFruits(-2).divide(5));
        assertEquals(FruitsValue.fromFruits(-1.6), FruitsValue.fromFruits(-4).divide(2.5));
        assertEquals(FruitsValue.fromFruits(-0.4), FruitsValue.fromFruits(-2).divide(BigInteger.valueOf(5)));
        assertEquals(FruitsValue.fromFruits(-1.6), FruitsValue.fromFruits(-4).divide(BigDecimal.valueOf(2.5)));

        // Negative + negative
        assertEquals(FruitsValue.fromFruits(0.4), FruitsValue.fromFruits(-2).divide(-5));
        assertEquals(FruitsValue.fromFruits(1.6), FruitsValue.fromFruits(-4).divide(-2.5));
        assertEquals(FruitsValue.fromFruits(0.4), FruitsValue.fromFruits(-2).divide(BigInteger.valueOf(-5)));
        assertEquals(FruitsValue.fromFruits(1.6), FruitsValue.fromFruits(-4).divide(BigDecimal.valueOf(-2.5)));

        // Recurring divisions
        assertEquals(FruitsValue.fromPlanck(33333333), FruitsValue.fromFruits(1).divide(3));
        assertEquals(FruitsValue.fromPlanck(66666666), FruitsValue.fromFruits(2).divide(3));

        // Divisor < 1
        assertEquals(FruitsValue.fromFruits(3), FruitsValue.fromFruits(1).divide(1.0/3.0));
    }

    @Test
    public void testAbs() {
        assertEquals(FruitsValue.fromFruits(1), FruitsValue.fromFruits(1).abs());
        assertEquals(FruitsValue.fromFruits(1), FruitsValue.fromFruits(-1).abs());
        assertEquals(FruitsValue.fromFruits(0), FruitsValue.fromFruits(0).abs());
    }

    @Test
    public void testMin() {
        assertEquals(FruitsValue.fromFruits(1), FruitsValue.min(FruitsValue.fromFruits(1), FruitsValue.fromFruits(2)));
        assertEquals(FruitsValue.fromFruits(-2), FruitsValue.min(FruitsValue.fromFruits(-1), FruitsValue.fromFruits(-2)));
    }

    @Test
    public void testMax() {
        assertEquals(FruitsValue.fromFruits(2), FruitsValue.max(FruitsValue.fromFruits(1), FruitsValue.fromFruits(2)));
        assertEquals(FruitsValue.fromFruits(-1), FruitsValue.max(FruitsValue.fromFruits(-1), FruitsValue.fromFruits(-2)));
    }
}
