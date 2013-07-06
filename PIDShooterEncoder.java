package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;

//This class creates an encoder to be used as a PID input source
public class PIDShooterEncoder implements PIDSource {

    Encoder encoder;
    public static final double RADIUS = 0.0254 * 2; // radius in meters (2 inches)

    public PIDShooterEncoder(Encoder encoder) {

        this.encoder = encoder;
        encoder.setDistancePerPulse(0.00278 * RADIUS * 2 * Math.PI);
    }

    //returns the rate
    public double pidGet() { // meters per second
        return encoder.getRate();
    }
}
