package org.firstinspires.ftc.teamcode.testcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm {

//import LinearOpMode;
//import TeleOp;
//import DcMotor;


    public static class EncoderOpmode extends LinearOpMode {
        @Override
        public void runOpMode() throws InterruptedException {

            // Find a motor in the hardware map named "Arm Motor"
            HardwareMap hardwareMap = null;
            DcMotor motor;
            motor = hardwareMap.dcMotor.get("Arm Motor");

            // Reset the motor encoder so that it reads zero ticks
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            waitForStart();

            while (opModeIsActive()) {
                double CPR = 28; // Assuming 28 counts per revolution, you need to update this

                // Get the current position of the motor
                int position = motor.getCurrentPosition();
                double revolutions = position / CPR;

                double angle = revolutions * 360;
                double angleNormalized = angle % 360;

                // Show the position of the motor on telemetry
                telemetry.addData("Encoder Position", position);
                telemetry.addData("Encoder Revolutions", revolutions);
                telemetry.addData("Encoder Angle (Degrees)", angle);
                telemetry.addData("Encoder Angle - Normalized (Degrees)", angleNormalized);
                telemetry.update();
            }
        }
    }

    private void waitForStart() {
    }}