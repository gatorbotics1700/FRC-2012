package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class TurretManager {

    private Jaguar turretJaguar;
    private Joystick turretJoystick;
    private Camera turretCamera;
    private Encoder turretEncoder;
    private final double DEADBAND = 0.1;
    private DigitalInput plusLimit;
    private DigitalInput minusLimit;
    private PIDController turretPID;
    private int calibrationState = 1;
    private double plusEncoderLimit = 10000000;
    private double minusEncoderLimit = -10000000;
    private double Midpoint;
    private static final double AWAYFROMCENTER = 3.0; //change me with the PID

    //constructor
    public TurretManager(Joystick shootingJoystick) {
        plusLimit = new DigitalInput(RobotMap.limitLeft);
        minusLimit = new DigitalInput(RobotMap.limitRight);
        turretJaguar = new Jaguar(RobotMap.TurretJaguar);
        turretJoystick = shootingJoystick;
        turretEncoder = new Encoder(RobotMap.TurretEncodera, RobotMap.TurretEncoderb, false); //true/false from motor orientation
        turretEncoder.reset();
        turretEncoder.start();
        turretCamera = new Camera();
        turretEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        turretPID = new PIDController(0.002, 0.00002, 0.000005, turretEncoder, turretJaguar); //adjust gains with gear ratio
        turretPID.reset();
    }

    //only used in manual; makes sure the turret does not turn beyond the limit switches
    private void checkVelocityAndSetJaguar(double velocity) {
        if ((plusLimit.get() && velocity > 0) || (minusLimit.get() && velocity < 0)) {
            turretJaguar.set(0);
        } else {
            turretJaguar.set(velocity * 0.8);
        }
    }

    //default method that disables the turret
    public void stopAll() {
        if (turretPID.isEnable()) {
            turretPID.disable();
        }
        turretJaguar.set(0);
    }

    //Deadband method that returns the adjusted value
    public double getJoystickValueWithDeadband() {
        double rawValue = turretJoystick.getX();
        double outputValue = 0;
        if (rawValue > DEADBAND) {
            outputValue = rawValue - DEADBAND;
        } else if (rawValue < -DEADBAND) {
            outputValue = rawValue + DEADBAND;
        }
        return outputValue;
    }

    //manually adjusts turret based on x-axis of joystick 
    public void manual() {//NOT PID controlled
        if (turretPID.isEnable()) {
            turretPID.disable();
        }
        if (turretJoystick.getRawButton(4)) {
            stopAll();
        }
        double Xvalue = getJoystickValueWithDeadband();
        this.checkVelocityAndSetJaguar(Xvalue);
        double degrees = turretCamera.findDegreesOff(); // for info purposes only
        DriverStationLCD.getInstance().println(RobotMap.turretLine, 1,
                "degreesOff: " + degrees);
        DriverStationLCD.getInstance().println(RobotMap.encoderLine, 1,
                "Encoder: " + turretEncoder.get());
    }
    //automatically turns the turret based on the target tracking HSL
    private static final double GEARRATIO = 170 / 12; //turret gear ratio

    public boolean auto() {
        if (!turretPID.isEnable()) {
            turretPID.enable();
        }
        double degreesToTurn = turretCamera.findDegreesOff();
        double delta = degreesToTurn * GEARRATIO;
        double newPosition = turretEncoder.get() + delta;
        //Uses PID
        if (newPosition > plusEncoderLimit) {
            newPosition = plusEncoderLimit;
        } else if (newPosition < minusEncoderLimit) {
            newPosition = minusEncoderLimit;
        }
        turretPID.setSetpoint(newPosition);
        DriverStationLCD.getInstance().println(RobotMap.turretLine, 1,
                "degreesOff: " + degreesToTurn);
        DriverStationLCD.getInstance().println(RobotMap.encoderLine, 1,
                "Encoder: " + turretEncoder.get());
        return Math.abs(degreesToTurn) < AWAYFROMCENTER;
    }
    private static final int CENTEROFFSET = -10;
    //goes to the midpoint

    public void goToCenter() {
        if (!turretPID.isEnable()) {
            turretPID.enable();
        }
        turretPID.setSetpoint(Midpoint + CENTEROFFSET);
    }
    //finds the midpoint between the two limit switches; called in autonomous
    final double CALIBRATIONJAGUARSPEED = 0.9;

    public void runCalibration() {
        // turns camera on
        turretCamera.findDegreesOff();

        if (calibrationState == 1) {
            if (plusLimit.get()) {
                plusEncoderLimit = turretEncoder.get();
                turretJaguar.set(-CALIBRATIONJAGUARSPEED);
                calibrationState++;
                System.out.println("Limit 1: " + plusEncoderLimit);
            } else {
                turretJaguar.set(CALIBRATIONJAGUARSPEED);
            }
        }
        if (calibrationState == 2) {
            if (minusLimit.get()) {
                minusEncoderLimit = turretEncoder.get();
                calibrationState++;
                System.out.println("Limit 2: " + minusEncoderLimit);
                turretJaguar.set(0);
                Midpoint = (plusEncoderLimit + minusEncoderLimit) / 2;
            } else {
                turretJaguar.set(-CALIBRATIONJAGUARSPEED);
            }
        }
        if (calibrationState == 3) {
            turretPID.enable();
            turretPID.setSetpoint(Midpoint + CENTEROFFSET);
            calibrationState++;
        }
        if (calibrationState == 4) {
            if (Math.abs(turretEncoder.get() - turretPID.getSetpoint()) < 10) {
                calibrationState++;
            }
        }
    }

    //returns true if the calibration in autonomous is finished
    public boolean CalibrationDone() {
        return calibrationState == 5;
    }

    //runs turret in autonomous
    public boolean runAutonomousPeriodic() {
        return this.auto();
    }
}