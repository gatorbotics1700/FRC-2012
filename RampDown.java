package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DigitalInput;

public class RampDown {
    private static final int STATE_STOPPED = 0;
    private static final int STATE_UP = 1;
    private static final int STATE_DOWN = 2;
    Jaguar motor;
    Joystick joystick;
    DigitalInput lsTop;
    int state;
    private static final double MOTOR_UP_SPEED = 0.75;
    private static final double MOTOR_DOWN_SPEED = -1.0;

    //constructor
    public RampDown(Joystick buttonsJoystick) {
        joystick= buttonsJoystick;
        motor = new Jaguar(RobotMap.RampPusherDowner);
        lsTop = new DigitalInput(RobotMap.UpperRampLimit);
        state = STATE_STOPPED;
    }

    public void RunPeriodic() { // during teleop, change state and update the motor speed
        //System.out.println("ramp down state: " + state + "ls val: " + lsTop.get());
        //this.ManualState();
        //this.AdjustMotor();
        this.AdjustMotor2();
        
    }

    // this method sets the motor values depending on the desired direction 
    private void AdjustMotor() {
        switch (state) {
            case STATE_UP:  
                if (lsTop.get() == false) {
                        motor.set(MOTOR_UP_SPEED);
                } else {
                    state = STATE_STOPPED;
                }
                break;
                
            case STATE_STOPPED:
                motor.set(0);
                break;

            case STATE_DOWN:
                //if (lsTop.get() == false) {
                    motor.set(MOTOR_DOWN_SPEED);
                //} else {
                //    state = STATE_STOPPED;
                //}
                break;

            default:
                state = STATE_STOPPED;
                motor.set(0);
                break;
        }
    }
    
    private void ManualState() {
        //Updates only when button is held. Default is to bring the mechanism up unless the limit switch is hit
        if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampDown) == true){
            state = STATE_DOWN;
        } else if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampStop) == true) {
            state = STATE_STOPPED;
        } else {
            state = STATE_UP;
            }
        }
    
        private void ManualStateButtons() { // this method for testing
        //Moves arm down if down button pressed, up with up button pressed, default is not moving
            if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampDown) == true){
            state = STATE_DOWN;
        } else if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampUp) == true) {
            state = STATE_UP;
        } else if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampStop) == true) {
            state = STATE_STOPPED;
        }
        else {
            state = STATE_STOPPED;
            }
        }
    
    public void auto(){
        state = STATE_DOWN;
        this.AdjustMotor();
    }
    private void AdjustMotor2(){
        double motorSetSpeed;
        if (joystick.getRawButton(RobotMap.RightDriveJoystick_RampDown) == true){
            motorSetSpeed = MOTOR_DOWN_SPEED;
        } else if (lsTop.get() == false){
            motorSetSpeed = MOTOR_UP_SPEED;
        } else {
            motorSetSpeed = 0;
        }
        motor.set(motorSetSpeed);
        System.out.println("motor set speed: " + motorSetSpeed + " ls " + lsTop.get());
    //code for ramp pusher downer created on 10/6/12

}
}
