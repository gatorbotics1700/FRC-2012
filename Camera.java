package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD; 
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*; //using ColorImage, NIVisionException, BinaryImage, ParticleAnalysisReport, CriteriaCollection, NIVision

public class Camera {

    AxisCamera trackerCamera;
    double degreesOff;
    int i = 0;
    private ParticleAnalysisReport shinyRectangles[];
    CriteriaCollection cc;
    private static final double FIELDOFVIEW = 57; //in degrees
    private static final double numXPixels = 160; //change based on resolution

    // constructor
    public Camera() {
        //Define camera and set specs
        trackerCamera = AxisCamera.getInstance("10.17.0.12");
        trackerCamera.writeResolution(AxisCamera.ResolutionT.k160x120);
        trackerCamera.writeBrightness(15);
        trackerCamera.writeCompression(10);
    }

    public double findDegreesOff() {

        //Initialize variables
        ColorImage image = null;
        BinaryImage shinyImage = null;
        BinaryImage bigShinyImage = null;
        BinaryImage convexHullShinyImage = null;
        int yval = 0;  //all centered blobs must be below yval = 0 (in pixels) where yval increases as you go down the screen


        //create the criteria for image collection
        cc = new CriteriaCollection();
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 40, 400, false);

        try {
            image = trackerCamera.getImage();

            //HSL values for the target
            shinyImage = image.thresholdHSL(100, 150, 25, 255, 5, 255);
            i++;

            bigShinyImage = shinyImage.removeSmallObjects(false, 1); //gets rid of small particles; note erosion number
            convexHullShinyImage = bigShinyImage.convexHull(false); //fills in blobs
            shinyRectangles = convexHullShinyImage.getOrderedParticleAnalysisReports(4);
//            //if (i % 100 == 50) {
//            //    // every 100 images, save each step
//            //    shinyImage.write("/tmp/shinyImage.png");
//                bigShinyImage.write("/tmp/bigShinyImage.png");
//                convexHullShinyImage.write("/tmp/convexHullImage.png");
//                System.out.println("n blobs: " + shinyRectangles.length);
//            }

            DriverStationLCD.getInstance().println(RobotMap.distanceLine, 1,
                    "blobs: " + shinyRectangles.length); // print info to driver's station

            //This strategy tracks the lowest target.
            if (shinyRectangles.length > 0) {
                for (int j = 0; j < shinyRectangles.length; j++) {
                    ParticleAnalysisReport shinyRectangle = shinyRectangles[j];
                    if ((shinyRectangle.center_mass_y > yval)) { // Sets the degreesOff to the difference between the center of the lowest rectangle and the turret
                        degreesOff = -((FIELDOFVIEW / numXPixels) * ((shinyRectangle.imageWidth / 2.0) - shinyRectangle.center_mass_x)); //check sign
                        yval = shinyRectangle.center_mass_y;
                    }
                }
            } else { // if there are no targets that we can see, don't move the turret
                degreesOff = 0;
            }
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (image != null) { // if pictures exist, free the processing space
                    image.free();
                    bigShinyImage.free();
                    convexHullShinyImage.free();
                    shinyImage.free();
                }
            } catch (NIVisionException ex) {
            }
        }
        return degreesOff;
    }
}
