# Assignment 2 - Telemetry Ring Buffer

## Problem Summary

The rover stores telemetry values in a fixed-size ring buffer.

The buffer keeps only the last N entries.

When the buffer becomes full, the oldest value is overwritten.

The original code has a problem in the `read()` method.

It uses:

```java
int index = sequenceNumber % capacity;
```

This works only before the buffer wraps around.

After the buffer wraps, some old sequence numbers are no longer available, but the method still returns a value from the same index.

This means the method can return new data for an old sequence number.

## Trace Example

Capacity: `4`

Values written in order:

```text
10, 20, 30, 40, 50
```

### Initial State

```text
buffer = [empty, empty, empty, empty]
head = 0
count = 0
```

### After write(10)

```text
buffer = [10, empty, empty, empty]
head = 1
count = 1
```

### After write(20)

```text
buffer = [10, 20, empty, empty]
head = 2
count = 2
```

### After write(30)

```text
buffer = [10, 20, 30, empty]
head = 3
count = 3
```

### After write(40)

```text
buffer = [10, 20, 30, 40]
head = 0
count = 4
```

### After write(50)

The buffer wraps around.

The value `10` is overwritten by `50`.

```text
buffer = [50, 20, 30, 40]
head = 1
count = 5
```

At this point, the buffer contains sequence numbers:

```text
sequence 1 -> 20
sequence 2 -> 30
sequence 3 -> 40
sequence 4 -> 50
```

Sequence number `0` is no longer available.

### Reads

| Read Call | Index Used | Returned Value | Correct? | Explanation |
|---|---:|---:|---|---|
| `read(0)` | `0 % 4 = 0` | `50` | No | Sequence 0 was overwritten. |
| `read(1)` | `1 % 4 = 1` | `20` | Yes | Sequence 1 is available. |
| `read(2)` | `2 % 4 = 2` | `30` | Yes | Sequence 2 is available. |
| `read(3)` | `3 % 4 = 3` | `40` | Yes | Sequence 3 is available. |
| `read(4)` | `4 % 4 = 0` | `50` | Yes | Sequence 4 is stored at index 0. |

## What Is the Oldest Available Sequence Number?

The newest available sequence number is:

```text
count - 1
```

The oldest available sequence number is:

```text
max(0, count - capacity)
```

Example:

```text
capacity = 4
count = 5

oldestAvailable = 1
newestAvailable = 4
```

Available sequence numbers:

```text
1, 2, 3, 4
```

## isAvailable Method

```java
public boolean isAvailable(int sequenceNumber) {
    int oldestAvailable = Math.max(0, count - capacity);
    int newestAvailable = count - 1;

    return sequenceNumber >= oldestAvailable
        && sequenceNumber <= newestAvailable;
}
```

## Fixed read Method

```java
public int read(int sequenceNumber) {
    if (!isAvailable(sequenceNumber)) {
        throw new IllegalArgumentException(
            "Sequence number is not available."
        );
    }

    int index = sequenceNumber % capacity;
    return buffer[index];
}
```

This prevents overwritten sequence numbers from returning incorrect values.

## Thread Safety

The solution is not fully thread-safe if multiple threads access the buffer at the same time.

One thread may write while another thread is reading.

A simple Java solution is to use `synchronized` methods.

```java
public synchronized void write(int value) {
    ...
}

public synchronized int read(int sequenceNumber) {
    ...
}

public synchronized boolean isAvailable(int sequenceNumber) {
    ...
}
```

## Structured Telemetry Entries

The buffer should store:

- timestamp
- sensor ID
- float reading

Example:

```java
public class TelemetryEntry {

    private final long timestamp;
    private final String sensorId;
    private final float reading;

    public TelemetryEntry(
            long timestamp,
            String sensorId,
            float reading) {

        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.reading = reading;
    }
}
```

The buffer type becomes:

```java
private TelemetryEntry[] buffer;
```

## Buffer Size Calculation

Telemetry rate:

```text
100 writes per second
```

Required history:

```text
30 seconds
```

Calculation:

```text
100 × 30 = 3000
```

Required capacity:

```text
3000 telemetry entries
```

For a full six-hour competition:

```text
6 × 60 × 60 = 21600 seconds

21600 × 100 = 2,160,000 entries
```

However, only the last 30 seconds are required.

Therefore:

```text
Required buffer size = 3000 entries
```

## Conclusion

The original implementation overwrites data correctly but does not verify whether a requested sequence number is still available.

The fixed solution introduces availability checking before reading data.

The design can also be extended to support structured telemetry entries and thread-safe access.

For 100 writes per second and 30 seconds of retained history, the required buffer capacity is 3000 entries.
