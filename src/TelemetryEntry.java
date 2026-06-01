public class TelemetryEntry {

    private final long timestamp;
    private final String sensorId;
    private final float reading;

    public TelemetryEntry(long timestamp, String sensorId, float reading) {
        if (sensorId == null || sensorId.isBlank()) {
            throw new IllegalArgumentException("Sensor ID cannot be empty.");
        }

        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.reading = reading;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public float getReading() {
        return reading;
    }

    @Override
    public String toString() {
        return "TelemetryEntry{" +
                "timestamp=" + timestamp +
                ", sensorId='" + sensorId + '\'' +
                ", reading=" + reading +
                '}';
    }
}