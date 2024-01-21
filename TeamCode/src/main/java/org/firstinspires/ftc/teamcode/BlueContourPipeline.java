package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

// Credits to team 7303 RoboAvatars, adjusted by team 3954 Pink to the Future, readjusted by team 17219 SpiderBits

public class BlueContourPipeline extends OpenCvPipeline {

    public static Scalar lowHSV = new Scalar(96, 69, 137);
    public static Scalar highHSV = new Scalar(103, 118, 255);

    // Volatile because accessed by OpMode without sync
    public volatile boolean error = false;
    public volatile Exception debug;

    private double borderLeftX;     //fraction of pixels from the left side of the cam to skip
    private double borderRightX;    //fraction of pixels from the right of the cam to skip
    private double borderTopY;      //fraction of pixels from the top of the cam to skip
    private double borderBottomY;   //fraction of pixels from the bottom of the cam to skip

    private int CAMERA_WIDTH;
    private int CAMERA_HEIGHT;

    private int loopCount = 0;
    private int nLoopCount = 0;

    private final Mat mat = new Mat();
    private final Mat processed = new Mat();

    private Rect maxRect = new Rect(600,1,1,1);

    private double maxArea = 0;
    private boolean first = false;
    private final Object sync = new Object();

    public BlueContourPipeline(double borderLeftX, double borderRightX, double borderTopY, double borderBottomY) {
        this.borderLeftX = borderLeftX;
        this.borderRightX = borderRightX;
        this.borderTopY = borderTopY;
        this.borderBottomY = borderBottomY;
    }

    public void configureBorders(double borderLeftX, double borderRightX, double borderTopY, double borderBottomY) {
        this.borderLeftX = borderLeftX;
        this.borderRightX = borderRightX;
        this.borderTopY = borderTopY;
        this.borderBottomY = borderBottomY;
    }

    @Override
    public Mat processFrame(Mat input) {
        CAMERA_WIDTH = input.width();
        CAMERA_HEIGHT = input.height();
        try {
            // Process Image
            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
            Core.inRange(mat, lowHSV, highHSV, processed);
            // Remove Noise
            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());
            // GaussianBlur
            Imgproc.GaussianBlur(processed, processed, new Size(5.0, 15.0), 0.00);
            // Find Contours
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            // Draw Contours
            Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));

                // Loop Through Contours
                for (MatOfPoint contour: contours) {
                    Point[] contourArray = contour.toArray();

                    // Bound Rectangle if Contour is Large Enough
                    if (contourArray.length >= 50) {
                        MatOfPoint2f areaPoints = new MatOfPoint2f(contourArray);
                        Rect rect = Imgproc.boundingRect(areaPoints);

                        if (                        rect.area() > maxArea
                                && (rect.x + (rect.width / 2.0)  > 0) && (rect.x + (rect.width / 2.0)  < CAMERA_WIDTH)
                                && (rect.y + (rect.height / 2.0) > 0) && (rect.y + (rect.height / 2.0) < CAMERA_HEIGHT)

                                || loopCount - nLoopCount   > 10
                                && (rect.x + (rect.width / 2.0)  > 0) && (rect.x + (rect.width / 2.0)  < CAMERA_WIDTH)
                                && (rect.y + (rect.height / 2.0) > 0) && (rect.y + (rect.height / 2.0) < CAMERA_HEIGHT)
                        ){
                            maxArea = rect.area();
                            maxRect = rect;
                            nLoopCount++;
                            loopCount = nLoopCount;
                            first = true;
                        }
                        else if(loopCount - nLoopCount > 10){
                            maxArea = new Rect().area();
                            maxRect = new Rect();
                        }


                        areaPoints.release();
                    }
                    contour.release();
                }

                if (contours.isEmpty()) {
                    maxRect = new Rect(600,1,1,1);
                }
            // Draw Rectangles If Area Is At Least 900
            if (first && maxRect.area() > 1000) {
                Imgproc.rectangle(input, maxRect, new Scalar(0, 255, 0), 3);
            }

            loopCount++;
        } catch (Exception e) {
            debug = e;
            error = true;
        }
        return input;
    }
    /*
    Synchronize these operations as the user code could be incorrect otherwise, i.e a property is read
    while the same rectangle is being processed in the pipeline, leading to some values being not
    synced.
     */

    public int getRectHeight() {
        synchronized (sync) {
            return maxRect.height;
        }
    }
    public int getRectWidth() {
        synchronized (sync) {
            return maxRect.width;
        }
    }
    public int getRectX() {
        synchronized (sync) {
            return maxRect.x;
        }
    }
    public int getRectY() {
        synchronized (sync) {
            return maxRect.y;
        }
    }
    public double getRectMidpointX() {
        synchronized (sync) {
            return getRectX() + (getRectWidth() / 2.0);
        }
    }
    public double getRectMidpointY() {
        synchronized (sync) {
            return getRectY() + (getRectHeight() / 2.0);
        }
    }
    public Point getRectMidpointXY() {
        synchronized (sync) {
            return new Point(getRectMidpointX(), getRectMidpointY());
        }
    }
    public double getAspectRatio() {
        synchronized (sync) {
            return getRectArea() / (CAMERA_HEIGHT * CAMERA_WIDTH);
        }
    }

    public double getRectRatio(){
        synchronized (sync) {
            return maxRect.x / maxRect.y;
        }
    }
    public double getRectArea() {
        synchronized (sync) {
            return maxRect.area();
        }
    }

}
