package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Teleop24Official extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private CRServo intake= null;
    private Servo brake= null;

    private DcMotor liftMotor3 = null;
    private DcMotor liftMotor = null;
    private DcMotor liftMotor2 = null;

    private boolean negative = false;

    public enum LiftState {
        LIFT_RETRACT,
        LIFT_EXTEND
    }
    LiftState liftState = LiftState.LIFT_EXTEND;
    ElapsedTime liftTImer = new ElapsedTime();

    private int initialLiftPosition;
    private int desiredLiftPositionCt; // Declare as a global variable
    private double kP = 0.001;  // Proportional gain for PID

    // Constants for calculations
    private static final double ENCODER_TICKS_PER_REVOLUTION = 537.6; // Example value
    private static final double WHEEL_DIAMETER_INCHES = 1.70; // Example value for the diameter of the lift pulley
    private double COUNTS_PER_INCH;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Calculate COUNTS_PER_INCH
        double wheelCircumference = Math.PI * WHEEL_DIAMETER_INCHES; // Circumference in inches
        COUNTS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / wheelCircumference; // Calculate counts per inch

        // Initialize the hardware variables
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        liftMotor3 = hardwareMap.get(DcMotor.class, "liftMotor3");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        liftMotor2 = hardwareMap.get(DcMotor.class, "liftMotor2");
        intake = hardwareMap.get(CRServo.class, "intake");
        brake = hardwareMap.get(Servo.class, "brake");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor2.setDirection(DcMotor.Direction.FORWARD);
        liftMotor3.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Record initial lift encoder position without resetting it
        initialLiftPosition = liftMotor.getCurrentPosition();
        desiredLiftPositionCt = initialLiftPosition; // Set initial desired position

        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //0 means no button pressed, 1 means A, 2 means B, 3 means Y, 4 means X
        int buttonPressed  = 0;



        waitForStart();
        runtime.reset();

        liftMotor.setPower(0);
        liftMotor2.setPower(0);
        liftMotor3.setPower(0);

       final int initialLiftPosition = liftMotor.getCurrentPosition();

        while (opModeIsActive()) {
            // Drive controls
            double xpower = gamepad1.left_stick_x;
            double ypower = -gamepad1.left_stick_y;
            double rpower = gamepad1.right_stick_x;
            double denom = Math.max(Math.abs(ypower) + Math.abs(xpower) - Math.abs(rpower), 1);

            double frontLeftPower = (xpower + ypower + rpower) / denom;
            double frontRightPower = (ypower - xpower - rpower) / denom;
            double backLeftPower = (ypower - xpower + rpower) / denom;
            double backRightPower = (ypower + xpower - rpower) / denom;
            double liftPower = 0;

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
                frontLeftPower /= 3;
                frontRightPower /= 3;
                backLeftPower /= 3;
                backRightPower /= 3;
            }

            if (gamepad2.dpad_up) {
                liftMotor3.setPower(1);
            } else if (gamepad2.dpad_down) {
                liftMotor3.setPower(-1);
            } else {
                liftMotor3.setPower(0);
            }
            int currentLiftPositionCt = 0;
            int error = 0;

            // Setting desired lift positions based on button presses
            if (gamepad2.a) {
                buttonPressed = 1;
            } else if (gamepad2.b) {
                buttonPressed = 2;
            } else if (gamepad2.y) {
                buttonPressed = 3;
            } else if (gamepad2.right_stick_y > 0.15 || gamepad2.right_stick_y > 0.15) {
                buttonPressed = 0;

            }

            switch(buttonPressed) {
                case 0:
                    liftPower = manualLift(gamepad2.right_stick_y);
                    break;
                case 1:
                    desiredLiftPositionCt = initialLiftPosition;  // Reset to initial
                    currentLiftPositionCt = liftMotor.getCurrentPosition();
                    error = desiredLiftPositionCt - currentLiftPositionCt;
                    liftPower = error * kP;
                    break;
                case 2:
                    desiredLiftPositionCt = initialLiftPosition + (int)(3 * COUNTS_PER_INCH);
                    currentLiftPositionCt = liftMotor.getCurrentPosition();
                    error = desiredLiftPositionCt - currentLiftPositionCt;
                    liftPower = error * kP;
                    break;
                case 3:
                    desiredLiftPositionCt = initialLiftPosition + (int)(6 * COUNTS_PER_INCH);
                    currentLiftPositionCt = liftMotor.getCurrentPosition();
                    error = desiredLiftPositionCt - currentLiftPositionCt;
                    liftPower = error * kP;
                    break;

                default:
                    // code block
            }


            if (gamepad1.right_trigger > 0){
                intake.setPower(1);
            }
            else {
               intake.setPower(0);
            }

            if (gamepad1.left_trigger > 0){
                intake.setPower(-1);
            }

            else{
                intake.setPower(0);
            }

            if (gamepad1.a) {
                brake.setPosition(-0.2);
            }






            // PID control: calculate error and proportional response


            // Apply the same power to both lift motors
            liftMotor.setPower(liftPower);
            liftMotor2.setPower(liftPower);

            telemetry.addData("Lift Error", error);
            telemetry.addData("Lift Power", liftPower);
            telemetry.addData("Initial Lift Pos", initialLiftPosition);
            telemetry.addData("Desired Lift Pos", desiredLiftPositionCt);
            telemetry.addData("Current Lift Pos", currentLiftPositionCt);
            telemetry.update();
            telemetry.addData("liftPower", liftPower);





            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

        }
    }

    double manualLift(double val) {
        if(val > 0.15 || val < -0.15){
            return -val;
        }
            return 0;
        }
    }