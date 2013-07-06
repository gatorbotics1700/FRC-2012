package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;

public class ConveyorBelt {

    Jaguar conveyorMotor;
    Jaguar conveyorFrontMotor;
    Joystick Buttons;
    private static final double SPEED = 0.85;

    //constructor
    public ConveyorBelt(Joystick buttonsJoystick) {
        conveyorMotor = new Jaguar(RobotMap.ConveyorMotor);
        Buttons = buttonsJoystick;
    }

    //Conveyor belt is controlled by the driver when not shooting
    public void manual() {

        if (Buttons.getRawButton(RobotMap.LeftDriveJoystick_ConveyorUp)) {
            conveyorMotor.set(SPEED);
        } else {
            conveyorMotor.set(0);
        }
    }

    //Conveyor belt is called in autonomous
    public void auto() {
        conveyorMotor.set(SPEED * 0.75);
    }

    public void stop() {
        conveyorMotor.set(0);
    }
}