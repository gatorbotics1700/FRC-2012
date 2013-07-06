package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

 /*
  * @author 14crobison
  */
 
public class Pneumatics {
     private Solenoid shifter;
     private Compressor NCompressor;

 
    public Pneumatics(int pressureSwitchChannel, int compressorRelayChannel, int solenoidModule, int solenoidChannel) { //pressure switch channel, compressor relay channel, solenoid slot number, solenoid channel number
         shifter = new Solenoid(solenoidModule, solenoidChannel);
         NCompressor = new Compressor(pressureSwitchChannel, compressorRelayChannel);
         NCompressor.start();
     }
 
    public void shifting(boolean highGear) {
        shifter.set(highGear);
     }
  }
