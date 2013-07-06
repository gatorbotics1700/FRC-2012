/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Main extends IterativeRobot {
    //Classes
    SimpleDrive robotDriving;
    Shooting basketShooter;
    TurretManager turret;
    ConveyorBelt conveyor;
    RampDown rampPusher;
    Pneumatics nPneumatics;
    
    //Objects
    Joystick lDriveJoystick;
    Joystick rDriveJoystick;
    Joystick ShootingJoystick;
    Timer timer;
    Encoder driveEncoderLeft, driveEncoderright;
    
    private static double AUTOKEYSPEED = 9.5; // make 14.6 speed for set distance of 4 m with wheels touching the front of the key
    private static double AUTOFARSPEED = 30; // FIX ME was 25
    private static double STOPSPEED = 0.0; //Stop motors
    private static double initSpeed = 1;

    public void robotInit() {
        //Initializing objects
        if (nPneumatics==null)
            nPneumatics = new Pneumatics(RobotMap.pressureSwitchChannel,
                RobotMap.compressorRelayChannel, 
                RobotMap.solenoidModule,
                RobotMap.channel);
        
//        ShootingJoystick = new Joystick(RobotMap.ShooterJoystick);
//        lDriveJoystick = new Joystick(RobotMap.LeftDriveJoystick);
//        rDriveJoystick = new Joystick(RobotMap.RightDriveJoystick);
//        //driveEncoderRight = new Encoder();//FIX ME
//        timer = new Timer();
//
//        //Initializing classes
//        robotDriving = new SimpleDrive(lDriveJoystick, rDriveJoystick);
//        conveyor = new ConveyorBelt(lDriveJoystick);
//        turret = new TurretManager(ShootingJoystick);
//        basketShooter = new Shooting(ShootingJoystick);
//        rampPusher = new RampDown(rDriveJoystick);
//        
    }

    public void autonomousInit() {
         timer.reset();
         timer.start();
         robotDriving.autonomousInit(); //FIXME
    }

    public void autonomousPeriodic() { // called periodically during autonomous
        basketShooter.autoShoot(AUTOKEYSPEED);
        System.out.print("t: " + timer.get());
        if (turret.runAutonomousPeriodic()) {
            System.out.print("aim done ");
            if (timer.get() > 0.5) {//1.5; remove, replace with turret.runAutonomousPeriodic() once we have targets
                System.out.print("conveyor ");
                conveyor.auto();
                if (timer.get() > 4) {//5
                    System.out.print("driving back ");
                    if (robotDriving.auto()) { //robot moves to bridge, proceeds when reaches correct distance
                        System.out.print("Sspeed up ");
                        basketShooter.autoShoot(AUTOFARSPEED);
                        rampPusher.auto();
                        conveyor.auto();
                    }
                }
            }
        }
        System.out.println();
    }

    public void disabledPeriodic() { // called periodically during teleop when teleop is disabled?
    }

    public void teleopInit() {
    }

    public void teleopPeriodic() { // called periodically during teleop
    
        if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_KillButton)) {
            turret.stopAll();
            basketShooter.autoShoot(STOPSPEED);
        } else if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_Manual)) {
            basketShooter.manual();
            turret.manual();
        } else if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_Midpoint)) {
            basketShooter.adjust();
            turret.goToCenter();
        } else {
            if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_ShooterAuto)) {
               basketShooter.autoShoot(AUTOKEYSPEED); // may need changing
            } else {
                basketShooter.adjust();
            }
            
            if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_TurretAuto)) {
               turret.auto();
            } else {
              turret.manual();
            }
        }
        conveyor.manual();
        
        robotDriving.ArcadeDrive();
        rampPusher.RunPeriodic();
        basketShooter.updatePrint();
        
        //Print to DriverStation
        DriverStationLCD.getInstance().updateLCD();
        
       if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_ResetDriveEncoder)){
           robotDriving.resettingEncoder();
       }
       
       if (ShootingJoystick.getRawButton(RobotMap.ShootingJoystick_GearboxShifter)){
           System.out.println("Shifting button pressed");
           nPneumatics.shifting(true);
       } else {
           System.out.println("Shifting button unpressed");
           nPneumatics.shifting(false);
       }
    }
}