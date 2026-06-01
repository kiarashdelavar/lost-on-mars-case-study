public class RingBuffer {

    private final TelemetryEntry[] buffer;
    private final int capacity;
    private int head;
    private int count;

    public RingBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }

        this.buffer = new TelemetryEntry[capacity];
        this.capacity = capacity;
        this.head = 0;
        this.count = 0;
    }

    public synchronized void write(TelemetryEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Telemetry entry cannot be null.");
        }

        buffer[head] = entry;
        head = (head + 1) % capacity;
        count++;
    }

    public synchronized TelemetryEntry read(int sequenceNumber) {
        if (!isAvailable(sequenceNumber)) {
            throw new IllegalArgumentException("Sequence number is not available.");
        }

        int index = sequenceNumber % capacity;
        return buffer[index];
    }

    public synchronized boolean isAvailable(int sequenceNumber) {
        int oldestAvailable = Math.max(0, count - capacity);
        int newestAvailable = count - 1;

        return sequenceNumber >= oldestAvailable && sequenceNumber <= newestAvailable;
    }

    public synchronized int size() {
        return Math.min(count, capacity);
    }

    public synchronized int totalWrites() {
        return count;
    }

    public int capacity() {
        return capacity;
    }
}