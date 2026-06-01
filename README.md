# Lost on Mars Case Study

This repository contains my solution for the RoboTeam Twente **Lost on Mars** software case study.

The assignment is split into two parts:

1. Designing an autonomous navigation system for a Mars rover.
2. Fixing and improving a telemetry ring buffer implementation.

## Project Overview

The rover must navigate through outdoor terrain without GPS and without a live video feed.
The system design focuses on real-time mapping, localization, sensor fusion, and fault recovery.

The ring buffer part focuses on correctly storing and reading the last N telemetry entries using absolute sequence numbers.

## Repository Structure

```text
.
├── README.md
├── Assignment1_Mars_Rover_Design.md
├── Assignment2_RingBuffer.md
├── src/
│   ├── RingBuffer.java
│   └── TelemetryEntry.java
└── test/
    └── RingBufferTest.java
```

## Topics Covered

- Autonomous rover navigation
- Real-time mapping
- SLAM and localization
- Sensor fusion
- Wheel slip handling
- Sensor noise recovery
- Fixed-size ring buffer
- Absolute sequence number tracking
- Data availability checks
- Structured telemetry entries

## Assignment 1: Rover Navigation Design

This part explains how the rover can:

- Build a map of the terrain in real time.
- Estimate its own position and orientation.
- Continue operating when wheel slip or sensor noise occurs.

The proposed solution uses multiple sensors such as LiDAR, stereo cameras, IMU, and wheel encoders.
The data is fused to create a reliable estimate of the rover state and to build a local terrain map.

## Assignment 2: Telemetry Ring Buffer

This part analyzes the bug in the original ring buffer implementation.

The original `read()` method only used:

```java
sequenceNumber % capacity
```

This is not enough after the buffer wraps around, because old entries may already be overwritten.

The fixed solution tracks which absolute sequence numbers are still available before allowing a read operation.

## How to Run

The Java files are placed in the `src` folder.

Example compile command:

```bash
javac src/*.java
```

## Author

Kiarash Delavar
