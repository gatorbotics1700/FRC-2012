package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

public class SimpleDrive {
    
    private static final double SLOWFACTOR = 3;
    private static final double DISTANCE_TO_DRIVE = 1.95; //2.2 meters from bumper at front of key
    Joystick leftJoystick;
    Joystick rightJoystick;
    Jaguar leftMotors;
    Jaguar rightMotors;
    private Encoder driveEncoderLeft;//, driveEncoderRight;
    
    double commandedLeftMotorSpeed;
    double commandedRightMotorSpeed;
    
    Encoder driveEncoder1;
    Encoder driveEncoder2;
    CounterBase.EncodingType factor;
    
    //constructor    
    public SimpleDrive(Joystick ldrivejoystick, Joystick rdrivejoystick) {
        factor = EncodingType.k1X;
        leftMotors = new Jaguar(RobotMap.LeftDriveMotors);
        rightMotors = new Jaguar(RobotMap.RightDriveMotors);
        leftJoystick = ldrivejoystick;
        rightJoystick = rdrivejoystick;
        driveEncoderLeft = new Encoder(RobotMap.driveEncodera, RobotMap.driveEncoderb, true);//FIX ME
        driveEncoderLeft.reset();
        driveEncoderLeft.start();
        driveEncoderLeft.setDistancePerPulse(0.00278 * 0.25 * 1.1);//FIX ME?
        //driveEncoderLeft = driveEncoderL;
        //driveEncoderRight = driveEncoderR;
    }

    public void ArcadeDrive(){
        //double speed = leftJoystick.getY(); //use these two lines for arcadeDrive
        //double turnRate = rightJoystick.getY();
        commandedLeftMotorSpeed = leftJoystick.getY(); //use these two lines for tankDrive
        commandedRightMotorSpeed = rightJoystick.getY();
        //commandedLeftMotorSpeed = speed * (1 + turnRate);
        //commandedRightMotorSpeed = speed * (1 - turnRate);
        //System.out.println("leftJoystick: " + speed + "rightJoystick: " + turnRate + "leftmotor: " + commandedLeftMotorSpeed + "rightMotor: " + commandedRightMotorSpeed);
        
        if (leftJoystick.getRawButton(RobotMap.LeftDriveJoystick_SlowDrive)){
            commandedLeftMotorSpeed /=SLOWFACTOR; //Used when driving over the ramp
            commandedRightMotorSpeed /=SLOWFACTOR;
        }
        if (leftJoystick.getRawButton(RobotMap.LeftDriveJoystick_QuickTurnClockwise)){
            System.out.println("QUICK TURN CLOCKWISE");
            commandedLeftMotorSpeed = -1.0;
            commandedRightMotorSpeed = 1.0;
        }
        if (leftJoystick.getRawButton(RobotMap.LeftDriveJoystick_QuickTurnCounterClockwise)){
            System.out.println("QUICK TURN COUNTERCLOCKWISE");
            commandedLeftMotorSpeed = 1.0;
            commandedRightMotorSpeed = -1.0;
        }
        leftMotors.set((-commandedLeftMotorSpeed));
        rightMotors.set((commandedRightMotorSpeed));
        double distanceL = driveEncoderLeft.getDistance();

        //System.out.println("encoderdistance: " + distanceL + " rate: " + driveEncoderLeft.getRate() + " motorL: " + leftMotors.get() + " R: " + rightMotors.get());

    }
    
    public void autonomousInit() {
              driveEncoderLeft.reset();
              //driveEncoderRight.reset();
              driveEncoderLeft.start();
              //driveEncoderRight.start();
              // driveEncoderRight.setDistancePerPulse(0.00278 * 0.25);//FIX ME?
    }
    
    public boolean auto() {
      double distanceL = driveEncoderLeft.getDistance();
      //double distanceR = driveEncoderLeft.getDistance();
      System.out.println("encoderdistance: " + distanceL + " rate: " + driveEncoderLeft.getRate() + " motorL: " + leftMotors.get() + " R: " + rightMotors.get());
      if (distanceL <= DISTANCE_TO_DRIVE) {
        leftMotors.set(1);
        rightMotors.set(-1);
      }
      //if (distanceR <= DISTANCE_TO_DRIVE) {
      //  rightMotors.set(-0.5);
      //}
      if (distanceL >= DISTANCE_TO_DRIVE) {
        this.stop();
        return true;
      } else {
        return false;
      } 
      
      
      //resettingEncoder();
    }
    
    public void resettingEncoder() {
        driveEncoderLeft.reset();
    }
    
    public void stop() {
        leftMotors.set(0.0);
        rightMotors.set(0.0);
    }
}
