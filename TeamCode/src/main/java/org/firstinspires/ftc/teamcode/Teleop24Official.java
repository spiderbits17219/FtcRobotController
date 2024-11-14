package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class Teleop24Official extends LinearOpMode {

    //     // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    private DcMotor liftMotor3 = null;
    private DcMotor liftMotor = null;
    private DcMotor liftMotor2 = null;

    private ColorSensor sensorColor;
   private DistanceSensor sensorDistance;
   private RevBlinkinLedDriver leftLights;
  private  RevBlinkinLedDriver rightLights;



    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        liftMotor3 = hardwareMap.get(DcMotor.class, "liftMotor3");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        liftMotor2 = hardwareMap.get(DcMotor.class, "liftMotor2");
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");


        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;

        final double SCALE_FACTOR = 255;

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        initHardware();

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftMotor2.setDirection(DcMotor.Direction.REVERSE);
        liftMotor3.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);





// Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        liftMotor.setPower(0);
        liftMotor2.setPower(0);
        liftMotor3.setPower(0);


        int liftMotorStartPosition = liftMotor.getCurrentPosition();
        int liftMotor2StartPosition = liftMotor2.getCurrentPosition();
        int liftMotor3StartPosition = liftMotor3.getCurrentPosition();


        while (opModeIsActive()) {
            double xpower = gamepad1.left_stick_x;
            double ypower = -gamepad1.left_stick_y;
            double rpower = gamepad1.right_stick_x;
            double denom = Math.max(Math.abs(ypower) + Math.abs(xpower) - Math.abs(rpower), 1);

            double frontLeftPower = (xpower + ypower + rpower) / denom;
            double frontRightPower = (ypower - xpower - rpower) / denom;
            double backLeftPower = (ypower - xpower + rpower) / denom;
            double backRightPower = (ypower + xpower - rpower) / denom;


            if (gamepad1.dpad_up) {
                frontLeftPower = 1;
                frontRightPower = 1;
                backLeftPower = 1;
                backRightPower = 1;
            }
            if (gamepad1.dpad_down) {
                frontLeftPower = -1;
                frontRightPower = -1;
                backLeftPower = -1;
                backRightPower = -1;
            }
            if (gamepad1.dpad_left) {
                frontLeftPower = -1;
                frontRightPower = 1;
                backLeftPower = 1;
                backRightPower = -1;
            }
            if (gamepad1.dpad_right) {
                frontLeftPower = 1;
                frontRightPower = -1;
                backLeftPower = -1;
                backRightPower = 1;
            }

            if (gamepad1.left_trigger > 0) {
                frontLeftPower = frontLeftPower / 3;
                frontRightPower = frontRightPower / 3;
                backLeftPower = backLeftPower / 3;
                backRightPower = backRightPower / 3;
            }
//

            if (gamepad2.dpad_up) {
                liftMotor3.setPower(1);
            }
            else if (gamepad2.dpad_down) {
                liftMotor3.setPower(-1);
            }
            else {
                liftMotor3.setPower(0);
            }



            if (gamepad2.a) {
                liftMotor.setTargetPosition(liftMotorStartPosition);
                liftMotor2.setTargetPosition(liftMotor2StartPosition);
                liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                liftMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if (gamepad2.x) {
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            else if(gamepad2.right_stick_y > 0.15){
                liftMotor.setPower(gamepad2.right_stick_y);
                liftMotor2.setPower(gamepad2.right_stick_y);
            }
            else if(gamepad2.right_stick_y < -0.15){
                liftMotor2.setPower(gamepad2.right_stick_y);
                liftMotor.setPower(gamepad2.right_stick_y);
            }
            else{
                liftMotor2.setPower(0);
                liftMotor.setPower(0);
            }

            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            int detectedColor = getDetectedColor();

            setBlinkinColor(detectedColor);

            telemetry.update();

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

        }


    }
    private void initHardware() {
        leftLights = hardwareMap.get(RevBlinkinLedDriver.class, "leftLights");
        rightLights = hardwareMap.get(RevBlinkinLedDriver.class, "rightLights");
    }

    private void setBlinkinColor(int detectedColor) {
        switch (detectedColor) {
            case 1: // Red
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                break;
            case 2: // Yellow
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                break;
            case 3: // Blue
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                break;
            case 4: // White
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                break;
            default:
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                break;
        }
    }

    private int getDetectedColor() {
        int red = sensorColor.red();
        int green = sensorColor.green();
        int blue = sensorColor.blue();

        if (red > green && red > blue) {
            return 1; // Red
        } else if (green > red && green > blue) {
            return 2; // Yellow
        } else if (blue > red && blue > green) {
            return 3; // Blue
        } else {
            return 4; // White
        }
    }
}
