package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;

public class RobotMap {

    //make the constructor private, so no one can instantiate this class
    private RobotMap() {
    }
    //print locations
    public static final DriverStationLCD.Line shooterLine1 = DriverStationLCD.Line.kMain6,
            shooterLine2 = DriverStationLCD.Line.kUser2,
            turretLine = DriverStationLCD.Line.kUser3,
            distanceLine = DriverStationLCD.Line.kUser4,
            encoderLine = DriverStationLCD.Line.kUser5,
            blobsLine = DriverStationLCD.Line.kUser6;
    // joystick ports
    public static final int
            LeftDriveJoystick = 1,
            RightDriveJoystick = 2,
            ShooterJoystick = 3;
    //joystick buttons
    public static final int
            ShootingJoystick_ShooterAuto = 7,
            ShootingJoystick_Manual = 10,
            ShootingJoystick_KillButton = 2,
            ShootingJoystick_AdjustUp = 9,
            ShootingJoystick_AdjustDown = 8,
            ShootingJoystick_TurretAuto = 6,
            ShootingJoystick_Midpoint = 11, //brings turret to the midpoint deterined in calibration
            ShootingJoystick_ResetDriveEncoder = 3,
            ShootingJoystick_GearboxShifter = 1,
            
            LeftDriveJoystick_ConveyorUp = 1,
            LeftDriveJoystick_SlowDrive = 2,
            LeftDriveJoystick_QuickTurnClockwise = 8,
            LeftDriveJoystick_QuickTurnCounterClockwise = 9,
            RightDriveJoystick_RampDown = 1,
            RightDriveJoystick_RampUp = 3,
            RightDriveJoystick_RampStop = 2;
    // PWM connections
    public static final int //Drive motors
            LeftDriveMotors = 1,
            RightDriveMotors = 2,
            //Shooting motors
            TopShooterMotor = 8,
            BottomShooterMotor = 9,
            //Conveyor belt motors
            ConveyorMotor = 4,
            ConveyorFrontMotor = 5,
            //Turret motor
            TurretJaguar = 3,
            //Ramp window motor
            RampPusherDowner = 10;
    // Encoders
    public static final int //Turret encoder
            TurretEncodera = 13,
            TurretEncoderb = 12,
            //Two shooting encoders
            ShootingEncoderTopa = 11,
            ShootingEncoderTopb = 10,
            ShootingEncoderBottoma = 8,
            ShootingEncoderBottomb = 9,
            
            driveEncodera = 4,
            driveEncoderb = 5;
    //Limit switches
    public static final int limitLeft = 1,
            limitRight = 2,
            UpperRampLimit = 3,
            LowerRampLimit = 7;
    //limit switches for autonomous 
    public static final int AutonomousLeft = 4,
            AutonomousRight = 5,
            AutonomousMiddle = 6;
    //ultrasonic sensor
    public static final int UltrasonicModule = 1,
            UltrasonicSensor = 6;
    
    public static final int pressureSwitchChannel=1,
            compressorRelayChannel = 2, 
            solenoidModule = 2,
            channel = 7;
}