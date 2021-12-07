package fruits.kit.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fruits.kit.entity.FruitsTimestamp;

import java.sql.Date;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FruitsTimestampTest {
    @Test
    public void testFruitsTimestampFromEpochTime() {
        FruitsTimestamp fruitsTimestamp = FruitsTimestamp.fromFruitsTimestamp(123456789);
        assertEquals(123456789, fruitsTimestamp.getTimestamp());
        assertEquals(1531179189000L, fruitsTimestamp.getAsDate().toInstant().toEpochMilli());
    }

    @Test
    public void testFruitsTimestampFromDate() {
        FruitsTimestamp fruitsTimestamp = FruitsTimestamp.fromDate(Date.from(Instant.ofEpochMilli(1531179189000L)));
        assertEquals(123456789, fruitsTimestamp.getTimestamp());
        assertEquals(1531179189000L, fruitsTimestamp.getAsDate().toInstant().toEpochMilli());
    }
}
