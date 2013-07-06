package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStationLCD;

public class EchoLocationer {

    AnalogChannel distanceSensor;
    double distance = 0;
    double voltage;
    final double voltsToMeters = (1024.0 / 500.0); // conversion factor for ultrasonic sensor

    public EchoLocationer() {
        distanceSensor = new AnalogChannel(RobotMap.UltrasonicModule, RobotMap.UltrasonicSensor);
    }

    public double getDistance() {
        voltage = distanceSensor.getAverageVoltage();
        distance = voltage * voltsToMeters;
        distance += 0.98425; //adds length of platform under baskets
        DriverStationLCD.getInstance().println(RobotMap.blobsLine, 1,
                "distance: " + distance);
        return distance;
    }
}