# Assignment 1 - Mars Rover Design

## Introduction

The goal of this system is to help the rover move between waypoints without GPS and without a live video feed.

The rover must build a map of the environment, estimate its own position, and continue working when sensors produce incorrect data.

My design uses multiple sensors and combines their data to create a reliable navigation system.

## Rover Navigation Architecture

The diagram below shows the main navigation flow of the rover.

![Mars Rover Navigation Architecture](diagrams/diagram1.png)

Sensor data first comes from LiDAR, stereo camera, IMU, and wheel encoders.

The data goes through sensor preprocessing and then into the EKF sensor fusion layer.

The EKF helps create a better estimate of the rover position and orientation.

SLAM uses this estimate to build and update the map.

The map contains an occupancy grid and elevation information, so the rover can avoid obstacles, hills, and unsafe terrain.

The A* path planner uses the map to find a safe route to the next waypoint.

The rover controller then moves the rover based on the planned path.

The health monitor checks sensor confidence, localization drift, and possible failures.

If the data becomes unreliable, the rover can slow down, stop, or enter safe mode.

## Sensors

I would use the following sensors:

### LiDAR

LiDAR measures distances to objects around the rover.

It helps create a 3D map of the environment and detect obstacles.

### Stereo Camera

A stereo camera can estimate depth and identify terrain features.

It helps the rover understand the environment.

### IMU

The IMU measures acceleration and rotation.

It helps estimate the rover orientation.

### Wheel Encoders

Wheel encoders measure wheel movement.

They help estimate how far the rover has travelled.

## Sensor Fusion

No single sensor is perfect.

For this reason, I would combine data from all sensors.

The LiDAR provides accurate distance measurements.

The camera provides visual information.

The IMU provides orientation information.

The wheel encoders provide movement information.

An Extended Kalman Filter (EKF) can combine these measurements to produce a more reliable estimate of the rover position.

## Map Representation

I would use an occupancy grid map.

The map is divided into small cells.

Each cell stores whether the area is free, occupied, or unknown.

This map is simple, efficient, and commonly used in mobile robotics.

For terrain information, elevation map can be used to identify steep hills, mounds of dirt, and unsafe terrain.

The rover can avoid areas that are difficult to cross.

## Localization and Navigation

The rover needs to know where it is inside the map.

I would use a SLAM system.

SLAM stands for Simultaneous Localization and Mapping.

The rover builds the map while estimating its own position.

The navigation system can then plan a path to the next waypoint while avoiding obstacles.

### Path Planning

After localization, the rover must plan a safe route to the next waypoint.

I would use A* path planning because it is efficient and commonly used in robotics.

The planner can use the occupancy grid map to avoid obstacles and find a safe path.

If a new obstacle appears, the path can be recalculated.

## Handling Wheel Slip and Sensor Noise

Wheel slip can happen on loose soil or grass.

In this situation, wheel encoder data may become inaccurate.

The system can compare wheel encoder measurements with LiDAR and camera observations.

If the measurements differ significantly, wheel slip may be detected.

Sensor noise can be reduced using filtering techniques such as the Extended Kalman Filter.

## Recovery from Sensor Failure

A sensor may temporarily fail or provide incorrect data.

If a camera fails, the rover can continue using LiDAR, IMU, and wheel encoders.

If LiDAR data becomes unavailable, the rover can rely on camera-based localization.

The system should continuously monitor sensor health.

When a failed sensor becomes available again, it can be reintroduced into the sensor fusion process.

Localization drift can occur after long periods of driving.

To reduce drift, the rover can compare current LiDAR and camera observations with previously mapped landmarks.

When a known landmark is detected, the rover can correct its estimated position.


## Conclusion

This design combines LiDAR, stereo cameras, IMU, and wheel encoders to create a reliable navigation system.

Sensor fusion improves accuracy, while SLAM allows the rover to build a map and localize itself.

The system can continue operating even when wheel slip, sensor noise, or temporary sensor failures occur.