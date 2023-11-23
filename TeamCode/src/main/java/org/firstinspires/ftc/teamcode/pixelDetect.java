package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;


public class pixelDetect extends OpenCvPipeline {

    OpenCvInternalCamera camera;
    pixelDetect pipeline;

    enum Place{
        LEFT, CENTER, RIGHT
    }
    private Place place;
    Mat mat = new Mat();

    static final Scalar RED = new Scalar(255, 0, 0);

    //left area of each point, width and height of the subregion
    static final Point REGION1_LEFT = new Point(0,0);
    static final Point REGION2_LEFT = new Point(0,0);
    static final Point REGION3_LEFT = new Point(0,0);
    static final int REGION_WIDTH = 0;
    static final int REGION_HEIGHT = 0;

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
    public Mat processFrame(Mat input){
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(0, 240, 160);
        Scalar highHSV = new Scalar(30, 255, 255);
        Core.inRange(mat, lowHSV, highHSV, mat);

        //use this to draw a box around the identified region


        Mat left = mat.submat(leftRect);
        Mat center = mat.submat(centerRect);
        Mat right = mat.submat(rightRect);

        double leftVal = Core.sumElems(left).val[0] / leftRect.area() / 255;
        double centerVal = Core.sumElems(center).val[0] / centerRect.area() / 255;
        double rightVal =  Core.sumElems(right).val[0] / rightRect.area() / 255;

        left.release();
        center.release();
        right.release();

        boolean pixLeft = leftVal > THRESH;
        boolean pixCenter = centerVal > THRESH;
        boolean pixRight = rightVal > THRESH;

        if(pixLeft && (leftVal > centerVal) && (leftVal > rightVal)){
            place = Place.LEFT;
            Imgproc.rectangle(input, region1_pointA, region1_pointB, RED, 2);
            telemetry.addData("Location", "left");

        }else if(pixCenter && (centerVal > leftVal) && (centerVal > rightVal)){
            place = Place.CENTER;
            Imgproc.rectangle(input, region2_pointA, region2_pointB, RED, 2);
            telemetry.addData("Location", "center");
        }else{
            place = Place.RIGHT;
            Imgproc.rectangle(input, region3_pointA, region3_pointB, RED, 2);
            telemetry.addData("Location", "right");
        }
        telemetry.update();

        return mat;
    }

    public Place getPlace(){
        return place;
    }

}

