//package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.Servo;

//public class TestBlinkin {

    //public static class Main extends LinearOpMode {

        //@Override
        //public void runOpMode() throws InterruptedException {
            //Servo ledStrip = hardwareMap.get(Servo.class, "ledStrip");
            //ledStrip.setPosition(0.5);
            //waitForStart();
            //while (opModeIsActive()) {
                //ledStrip.setPosition(0.5);
            //}
        //}
    //}
//}

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
public class Main extends LinearOpMode {
    ColorSensor colorSensor;
    @Override
    public void runOpMode() throws InterruptedException {
        colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
        waitForStart();
        while (opModeIsActive()) {
            if (colorSensor.red() > colorSensor.blue()) {
                telemetry.addData("Color", "Red");
            } else if (colorSensor.blue() > colorSensor.red()) {
                telemetry.addData("Color", "Blue");
            }
            telemetry.update();
        }
    }
}
