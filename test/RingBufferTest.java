import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest {

    @Test
    void testWriteAndReadBeforeWrapAround() {
        RingBuffer buffer = new RingBuffer(4);

        buffer.write(new TelemetryEntry(1000L, "IMU", 1.5f));
        buffer.write(new TelemetryEntry(1001L, "LIDAR", 2.5f));

        assertEquals("IMU", buffer.read(0).getSensorId());
        assertEquals(1.5f, buffer.read(0).getReading());
        assertEquals("LIDAR", buffer.read(1).getSensorId());
        assertEquals(2.5f, buffer.read(1).getReading());
    }

    @Test
    void testWrapAroundOverwritesOldestEntry() {
        RingBuffer buffer = new RingBuffer(4);

        buffer.write(new TelemetryEntry(1000L, "S1", 10.0f));
        buffer.write(new TelemetryEntry(1001L, "S2", 20.0f));
        buffer.write(new TelemetryEntry(1002L, "S3", 30.0f));
        buffer.write(new TelemetryEntry(1003L, "S4", 40.0f));
        buffer.write(new TelemetryEntry(1004L, "S5", 50.0f));

        assertFalse(buffer.isAvailable(0));
        assertTrue(buffer.isAvailable(1));
        assertTrue(buffer.isAvailable(2));
        assertTrue(buffer.isAvailable(3));
        assertTrue(buffer.isAvailable(4));

        assertEquals(20.0f, buffer.read(1).getReading());
        assertEquals(30.0f, buffer.read(2).getReading());
        assertEquals(40.0f, buffer.read(3).getReading());
        assertEquals(50.0f, buffer.read(4).getReading());
    }

    @Test
    void testReadOverwrittenSequenceThrowsException() {
        RingBuffer buffer = new RingBuffer(4);

        buffer.write(new TelemetryEntry(1000L, "S1", 10.0f));
        buffer.write(new TelemetryEntry(1001L, "S2", 20.0f));
        buffer.write(new TelemetryEntry(1002L, "S3", 30.0f));
        buffer.write(new TelemetryEntry(1003L, "S4", 40.0f));
        buffer.write(new TelemetryEntry(1004L, "S5", 50.0f));

        assertThrows(IllegalArgumentException.class, () -> buffer.read(0));
    }

    @Test
    void testReadFutureSequenceThrowsException() {
        RingBuffer buffer = new RingBuffer(4);

        buffer.write(new TelemetryEntry(1000L, "S1", 10.0f));

        assertThrows(IllegalArgumentException.class, () -> buffer.read(1));
    }

    @Test
    void testInvalidCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer(0));
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer(-1));
    }

    @Test
    void testNullTelemetryEntryThrowsException() {
        RingBuffer buffer = new RingBuffer(4);

        assertThrows(IllegalArgumentException.class, () -> buffer.write(null));
    }

    @Test
    void testTelemetryEntryStoresValuesCorrectly() {
        TelemetryEntry entry = new TelemetryEntry(12345L, "CAMERA", 7.25f);

        assertEquals(12345L, entry.getTimestamp());
        assertEquals("CAMERA", entry.getSensorId());
        assertEquals(7.25f, entry.getReading());
    }

    @Test
    void testEmptySensorIdThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new TelemetryEntry(1000L, "", 1.0f));
        assertThrows(IllegalArgumentException.class, () -> new TelemetryEntry(1000L, "   ", 1.0f));
        assertThrows(IllegalArgumentException.class, () -> new TelemetryEntry(1000L, null, 1.0f));
    }

    @Test
    void testSizeAndTotalWrites() {
        RingBuffer buffer = new RingBuffer(4);

        assertEquals(0, buffer.size());
        assertEquals(0, buffer.totalWrites());

        buffer.write(new TelemetryEntry(1000L, "S1", 10.0f));
        buffer.write(new TelemetryEntry(1001L, "S2", 20.0f));
        buffer.write(new TelemetryEntry(1002L, "S3", 30.0f));
        buffer.write(new TelemetryEntry(1003L, "S4", 40.0f));
        buffer.write(new TelemetryEntry(1004L, "S5", 50.0f));

        assertEquals(4, buffer.size());
        assertEquals(5, buffer.totalWrites());
        assertEquals(4, buffer.capacity());
    }
}