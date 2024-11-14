package org.firstinspires.ftc.teamcode.testcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LEDRevBlinkin {


    @TeleOp(name="RevBlinkinLEDController", group="Linear Opmode")
    public class Main extends LinearOpMode {

        private ElapsedTime runtime = new ElapsedTime();
        private DcMotor leftDrive = null;
        private DcMotor rightDrive = null;

        private Servo armServo = null;
        private ColorSensor colorSensor = null;
        private Servo blinkin = null;

        private final int RED = 1;
        private final int YELLOW = 2;
        private final int BLUE = 3;
        private final int OFF = 0;

        @Override
        public void runOpMode() {
            telemetry.addData("Status", "Initialized");
            telemetry.update();


            leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
            rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
            armServo = hardwareMap.get(Servo.class, "arm_servo");
            colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
            blinkin = hardwareMap.get(Servo.class, "blinkin");

            leftDrive.setDirection(DcMotor.Direction.REVERSE);

            waitForStart();
            runtime.reset();

            while (opModeIsActive()) {
                int detectedColor = getDetectedColor();

                setBlinkinColor(detectedColor);

                telemetry.addData("Status", "Running");
                telemetry.addData("Runtime", runtime.toString());
                telemetry.addData("Detected Color", detectedColor);
                telemetry.update();
            }
        }

        private int getDetectedColor() {
            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();

            if (red > green && red > blue) {
                return RED;
            } else if (green > red && green > blue) {
                return YELLOW;
            } else if (blue > red && blue > green) {
                return BLUE;
            } else {
                return OFF;
            }
        }

        private void setBlinkinColor(int color) {
            switch (color) {
                case RED:
                    blinkin.setPosition(0.5);
                    break;
              case YELLOW:
                    blinkin.setPosition(0.25);
                    break;
                case BLUE:
                    blinkin.setPosition(0.75);
                    break;
                case OFF:
                    blinkin.setPosition(0);
                    break;
                default:
                    blinkin.setPosition(0);
            }
        }
    }
}
