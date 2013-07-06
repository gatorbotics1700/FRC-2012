package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

public class ArcadeDrive {
    
    Joystick speedJoystick;
    Joystick steerJoystick;
    Jaguar leftMotors;
    Jaguar rightMotors;
    
    double currentLeftMotorSpeed;
    double currentRightMotorSpeed;
    
    double commandedLeftMotorSpeed;
    double commandedRightMotorSpeed;
    
    double turnOverflowAmount;
    double turnOverflowScale = 0.6;
    
    Encoder driveEncoder1;
    Encoder driveEncoder2;
    CounterBase.EncodingType factor;
    
    //constructor    
    public ArcadeDrive(Joystick throttleJoystick, Joystick turnJoystick){
        factor = EncodingType.k1X;
        leftMotors = new Jaguar(RobotMap.LeftDriveMotors);
        rightMotors = new Jaguar(RobotMap.RightDriveMotors);
        speedJoystick = throttleJoystick;
        steerJoystick = turnJoystick;
    }
    
    private static final double DEADBAND = 0.02;
    double scaledValue;
    private double correctedValue(double rawValue){
        if (rawValue > DEADBAND){
            scaledValue = (rawValue - DEADBAND)/(1-DEADBAND);
        } else if (rawValue < -DEADBAND){
            scaledValue = (rawValue + DEADBAND)/(1-DEADBAND);
        } else {
            scaledValue = 0;
        }
        return scaledValue;
    }
    
    
    private static final double SLOWFACTOR = 3;
    public void arcadeDrive(){
        double speed = speedJoystick.getY();
        double turnRate = steerJoystick.getY();
        commandedLeftMotorSpeed = speed * (1 + turnRate);
        commandedRightMotorSpeed = speed * (1 - turnRate);
        
        if(commandedLeftMotorSpeed > 1) {
          turnOverflowAmount = commandedLeftMotorSpeed - 1;
          commandedRightMotorSpeed = commandedRightMotorSpeed - (turnOverflowAmount*turnOverflowScale);
        } else if(commandedLeftMotorSpeed < -1) {
          turnOverflowAmount = commandedLeftMotorSpeed + 1;
          commandedRightMotorSpeed = commandedRightMotorSpeed - (turnOverflowAmount*turnOverflowScale);
        } else if(commandedRightMotorSpeed > 1) {
          turnOverflowAmount = commandedRightMotorSpeed - 1;
          commandedLeftMotorSpeed = commandedLeftMotorSpeed - (turnOverflowAmount*turnOverflowScale);
	} else if(commandedRightMotorSpeed < -1) {
          turnOverflowAmount = commandedRightMotorSpeed + 1;
          commandedLeftMotorSpeed = commandedLeftMotorSpeed - (turnOverflowAmount*turnOverflowScale); 
        }
      //if one is positive and one is negative: turning
        if (commandedLeftMotorSpeed * commandedRightMotorSpeed < 0) {
          if (commandedLeftMotorSpeed * speed < 0) {
            commandedLeftMotorSpeed = 0;
          } else {
            commandedRightMotorSpeed = 0;
          }
        }
        
        //if one of the sides is moving but other side is standing still
        if(speed != 0 && (commandedLeftMotorSpeed == 0 || commandedRightMotorSpeed == 0)) {
          //if left side isn't moving
          if (commandedLeftMotorSpeed == 0) {
            if (speed > 0) {
              commandedLeftMotorSpeed = -0.075;
            } else {
              commandedLeftMotorSpeed = 0.075;
            }
          } else {
            if (speed > 0) {
              commandedRightMotorSpeed = 0.075;
            } else {
              commandedLeftMotorSpeed = 0.075;
            }
          }
        }
          
        if (speedJoystick.getRawButton(RobotMap.LeftDriveJoystick_SlowDrive)){
          commandedLeftMotorSpeed /=SLOWFACTOR; //Used when driving over the ramp
          commandedRightMotorSpeed /=SLOWFACTOR;
        }
          if (commandedLeftMotorSpeed > 1) {
            commandedLeftMotorSpeed = 1.0;
          }
          if (commandedLeftMotorSpeed < -1) {
            commandedLeftMotorSpeed = -1.0;
          }
          if (commandedRightMotorSpeed > 1) {
            commandedRightMotorSpeed = 1.0;
          }
          if (commandedRightMotorSpeed < -1) {
            commandedRightMotorSpeed = -1.0;
          }
        leftMotors.set((commandedLeftMotorSpeed));
        rightMotors.set((commandedRightMotorSpeed));
    }
  }