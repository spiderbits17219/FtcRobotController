package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class TestBlinkin {

    public class Main extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {
            Servo ledStrip = hardwareMap.get(Servo.class, "ledStrip");
            ledStrip.setPosition(0.5);
            waitForStart();
            while (opModeIsActive()) {
                ledStrip.setPosition(0.5);
            }
        }
    }
}