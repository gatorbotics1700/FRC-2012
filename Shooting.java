package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class Shooting {
    
    Encoder encoderTop;
    Encoder encoderBottom;
    SpeedPID ShooterPIDtop;
    SpeedPID ShooterPIDbottom;
    Jaguar shooterJaguarTop;
    Jaguar shooterJaguarBottom;
    Joystick shooterJoystick;
    CounterBase.EncodingType quadratureFactor;
    boolean shootingIs;
    //EchoLocationer ultrasonicSensor;
    
    boolean ShooterSpeedCorrect;
    double errorThreshold;
    //double gravity = 9.8;
    
    double OutputSpeed = 0.2;
    double OutputSpeed2 = 0.2; //FIX ME
    boolean isAuto = true;
    private final double deadbandManualSpeed = 0.1;
    
    // PID-FeedFWD gains for both motors
    double Kp = 0.02;
    double Ki = 0.001;
    double Kd = 0;
    double Kff = 1/21.2; // Empirically determined using Shooting.manual method

    //constructor
    public Shooting(Joystick shootingJoystick){ 
        quadratureFactor = CounterBase.EncodingType.k1X;
        encoderTop = new Encoder (RobotMap.ShootingEncoderTopa, RobotMap.ShootingEncoderTopb, true, quadratureFactor);
        encoderBottom = new Encoder (RobotMap.ShootingEncoderBottoma, RobotMap.ShootingEncoderBottomb, false, quadratureFactor);
        shooterJoystick = shootingJoystick;
        shooterJaguarTop = new Jaguar(RobotMap.TopShooterMotor);
        shooterJaguarBottom = new Jaguar(RobotMap.BottomShooterMotor);
        encoderTop.start();
        encoderBottom.start();
        ShooterPIDtop = new SpeedPID(Kp, Ki, Kd, Kff, new PIDShooterEncoder(encoderTop), shooterJaguarTop); //change gains when testing
        ShooterPIDtop.enable();
        ShooterPIDtop.reset();
        ShooterPIDbottom = new SpeedPID(Kp, Ki, Kd, Kff, new PIDShooterEncoder(encoderBottom), shooterJaguarBottom); //change gains when testing
        ShooterPIDbottom.enable();
        ShooterPIDbottom.reset();
        //ultrasonicSensor = new EchoLocationer();
   }
    
    private static final double SPIN = 0.6;
    
    public void manual(){//manually shoots with the z-axis on the shooting joystick        
        if (ShooterPIDtop.isEnable())
            ShooterPIDtop.disable();
        if (ShooterPIDbottom.isEnable())
            ShooterPIDbottom.disable();
        
        double speed = (1 - shooterJoystick.getZ())/2;
        speed = Math.max(0, speed - deadbandManualSpeed);
        shooterJaguarTop.set(SPIN * speed);
        shooterJaguarBottom.set(speed); 
        isAuto = false;
        this.updatePrint();
    }
    
    // all ultrasonic values
//    final double HEIGHTOFHOOP = 2.4892; // in meters
//    final double HEIGHTOFROBOT = 0.8636;
//    final double ANGLE = Math.PI / 3; // in radians; 60 degrees coming out of the robot
//    private final double height = HEIGHTOFHOOP - HEIGHTOFROBOT;
       
    
    //This commented out code controls the speed of the shooter using the distance from the basket
//    public void ultrasonicAuto() {
//        if (!ShooterPIDtop.isEnable())
//            ShooterPIDtop.enable();
//        if (!ShooterPIDbottom.isEnable())
//            ShooterPIDbottom.enable();
//        double distance = ultrasonicSensor.getDistance();
//        DriverStationLCD.getInstance().println(RobotMap.distanceLine, 1,
//            "distance: " + distance);
//        double tanHL = (Math.tan(ANGLE)) - (height/distance);
//        double BottomOfFraction = 2 * (Math.cos(ANGLE)) * (Math.cos(ANGLE)) * tanHL;
//        double TopOfFraction = gravity * distance;
//        OutputSpeed = Math.sqrt(TopOfFraction / BottomOfFraction); // meters per second
//        OutputSpeed += 5.5; //multiply by constant determined experimentally - the ideal output speed is too small
//    }
       
    public void autoShoot(double autoSpeed){// autonomous shooting from everywhere - pass in speed
        if (!ShooterPIDtop.isEnable())
            ShooterPIDtop.enable();
        if (!ShooterPIDbottom.isEnable())
            ShooterPIDbottom.enable();       
        ShooterPIDtop.setSetpoint(SPIN * autoSpeed); //backspin number may not be right
        ShooterPIDbottom.setSetpoint(autoSpeed);
        isAuto = true;
        this.updatePrint();
    }  
    
    private static final double ADJUSTPERCENT = 0.005;

    //toggle buttons when not in manualor autonomous; adjusts previously set speed
    public void adjust() {
        boolean update = false;
        double multiplier = 1;
        if (shooterJoystick.getRawButton(RobotMap.ShootingJoystick_AdjustUp)) {
            multiplier = 1 + ADJUSTPERCENT;
            update = true;
        } else if (shooterJoystick.getRawButton(RobotMap.ShootingJoystick_AdjustDown)) {
            multiplier = 1 - ADJUSTPERCENT;
            update = true;
        }
        if (update) {
            if (isAuto) {
                ShooterPIDtop.setSetpoint((multiplier) * ShooterPIDtop.getSetpoint());
                ShooterPIDbottom.setSetpoint((multiplier) * ShooterPIDbottom.getSetpoint());
            } else {
                shooterJaguarTop.set((multiplier) * shooterJaguarTop.get());
                shooterJaguarBottom.set((multiplier) * shooterJaguarBottom.get());
            }
        }
        this.updatePrint();
    }
   
   public void updatePrint(){
       if (isAuto){
           //Shooter is in autoShoot mode
           DriverStationLCD.getInstance().println(RobotMap.shooterLine1, 1, 
                "(A) S OutSpd " + ShooterPIDbottom.getSetpoint());
           DriverStationLCD.getInstance().println(RobotMap.shooterLine2, 1,
                   "(A) S spd: " + encoderBottom.getRate());
       } else {
           //Shooter is in manual
           DriverStationLCD.getInstance().println(RobotMap.shooterLine1, 1, 
                "(M) S jag: " + shooterJaguarBottom.get());
           DriverStationLCD.getInstance().println(RobotMap.shooterLine2, 1,
                   "(M) S spd: " + encoderBottom.getRate());
       }
   }
}