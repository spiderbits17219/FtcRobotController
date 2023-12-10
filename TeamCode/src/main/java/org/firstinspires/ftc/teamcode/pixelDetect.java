package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class pixelDetect extends OpenCvPipeline {


    enum Place{
        LEFT, CENTER, RIGHT
    }


    private Place place;

    static final Scalar RED = new Scalar(255, 0, 0);
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);


    //left area of each point, width and height of the subregion
    static final Point REGION1_LEFT = new Point(0,20);
    static final Point REGION2_LEFT = new Point(100,20);
    static final Point REGION3_LEFT = new Point(200,20);
    static final int REGION_WIDTH = 90;
    static final int REGION_HEIGHT = 200;

    static final int width = 240;

    static Point region1_pointA = new Point(REGION1_LEFT.x, REGION1_LEFT.y);
    static Point region1_pointB = new Point(REGION1_LEFT.x + REGION_WIDTH, REGION1_LEFT.y + REGION_HEIGHT);

    static final Rect leftRect = new Rect(region1_pointA, region1_pointB);
    static Point region2_pointA = new Point(REGION2_LEFT.x, REGION2_LEFT.y);
    static Point region2_pointB = new Point(REGION2_LEFT.x + REGION_WIDTH, REGION2_LEFT.y + REGION_HEIGHT);
    static final Rect centerRect = new Rect(region2_pointA, region2_pointB);
    static Point region3_pointA = new Point(REGION3_LEFT.x, REGION3_LEFT.y);
    static Point region3_pointB = new Point(REGION3_LEFT.x + REGION_WIDTH, REGION3_LEFT.y + REGION_HEIGHT);
    static final Rect rightRect = new Rect(region3_pointA, region3_pointB);

    static double THRESH = 0.35;

    public pixelDetect(Telemetry t){
        telemetry = t;
    }

    Mat processed = new Mat();
    public Mat processFrame(Mat input){
        Mat mat = new Mat();

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(90, 20, 10);
        Scalar highHSV = new Scalar(6, 100, 70);

        Core.inRange(mat, lowHSV, highHSV, processed);

        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, new Mat());
        Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, new Mat());

        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(input, contours, -1, new Scalar(255, 0, 0));

//        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
//        Rect[] boundRect = new Rect[contours.size()];
//
//        for(int i = 0; i < contours.size(); i++){
//            contoursPoly[i] = new MatOfPoint2f();
//            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
//            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//        }
//
//        double left_x = 0.25 * width;
//        double right_x = 0.75 * width;
//
//        boolean left = false;
//        boolean right = false;
//
//        for(int i = 0; i != boundRect.length; i++){
//            if(boundRect[i].x < left_x)
//                left = true;
//            if(boundRect[i].x > right_x)
//                right = true;
//         Imgproc.rectangle(mat, boundRect[i], new Scalar(0.5, 76.9, 89.8), 4);
//
//        }

//        if(left) place = Place.LEFT;
//
//        if(right) place = Place.RIGHT;


//        //use this to draw a box around the identified region
//
//
//        Mat left = mat.submat(leftRect);
//        Mat center = mat.submat(centerRect);
//        Mat right = mat.submat(rightRect);
//
//        double leftVal = Core.sumElems(left).val[0] / leftRect.area() / 255;
//        double centerVal = Core.sumElems(center).val[0] / centerRect.area() / 255;
//        double rightVal =  Core.sumElems(right).val[0] / rightRect.area() / 255;
//
//        left.release();
//        center.release();
//        right.release();
//
//        boolean pixLeft = leftVal > THRESH;
//        boolean pixCenter = centerVal > THRESH;
//        boolean pixRight = rightVal > THRESH;
//
//        Imgproc.rectangle(mat, region2_pointA, region2_pointB, GREEN, 3);
//        Imgproc.rectangle(mat, region3_pointA, region3_pointB, BLUE, 3);
//
//
//        if(pixLeft && (leftVal > centerVal) && (leftVal > rightVal)){
//            place = Place.LEFT;
//            telemetry.addData("Location", "left");
//            Imgproc.rectangle(mat, region1_pointA, region1_pointB, RED, 3);
//
//        }else if(pixCenter && (centerVal > leftVal) && (centerVal > rightVal)){
//            place = Place.CENTER;
//            telemetry.addData("Location", "center");
//            Imgproc.rectangle(mat, region2_pointA, region2_pointB, RED, 3);
//        }else{
//
//            place = Place.RIGHT;
//            telemetry.addData("Location", "right");
//            Imgproc.rectangle(mat, region2_pointA, region2_pointB, RED, 3);
//        }
//
//        telemetry.update();
//
        return mat;

    }

    public Place getPlace(){
        return this.place;
    }

}

