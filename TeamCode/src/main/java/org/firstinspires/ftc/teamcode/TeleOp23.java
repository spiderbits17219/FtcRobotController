package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class TeleOp23 extends LinearOpMode {

    //     // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;


    private DcMotor intake = null;
    private DcMotor liftMotor = null;
    private DcMotor liftMotor2 = null;

    private Servo liftLeft = null;
    private Servo liftRight = null;
    private Servo airplane = null;
    private Servo stageDoor = null;

    private boolean intakeRun = false;
    private boolean negative = false;


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
        intake = hardwareMap.get(DcMotor.class, "intake");
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        liftMotor2 = hardwareMap.get(DcMotor.class, "liftMotor2");
//
//        liftLeft = hardwareMap.get(Servo.class, "liftLeft");
//        liftRight = hardwareMap.get(Servo.class, "liftRight");
//        airplane = hardwareMap.get(Servo.class, "airplane");
        stageDoor = hardwareMap.get(Servo.class, "stageDoor");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor2.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        liftMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        stageDoor.setPosition(-0.5);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        double intakePower = 0;

        //         // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


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
                frontLeftPower = 1;
                frontRightPower = 1;
                backLeftPower = -1;
                backRightPower = -1;
            }
            if (gamepad1.dpad_right) {
                frontLeftPower = -1;
                frontRightPower = -1;
                backLeftPower = 1;
                backRightPower = 1;
            }

            if (gamepad1.left_trigger > 0) {
                frontLeftPower = frontLeftPower / 3;
                frontRightPower = frontRightPower / 3;
                backLeftPower = backLeftPower / 3;
                backRightPower = backRightPower / 3;
            }


            if (gamepad2.dpad_up) {
                intakeRun = true;
                negative = false;
            }
            if (gamepad2.dpad_down) {
                intakeRun = true;
                negative = true;
            }
            if (gamepad2.dpad_left) {
                intakeRun = false;
            }

            if (intakeRun == false) {
                intake.setPower(0);
            }
            if (intakeRun == true && negative == true) {
                intake.setPower(-1);
            }
            if (intakeRun == true && negative == false) {
                intake.setPower(1);
            }


            liftMotor.setPower(gamepad2.right_stick_y / 0.5);
            liftMotor2.setPower(gamepad2.right_stick_y / 0.5);

//            if (gamepad2.left_trigger > 0) {
//                liftLeft.setPosition(0);
//                liftRight.setPosition(0);
//            }
//
//            if (gamepad2.right_trigger > 0) {
//                liftLeft.setPosition(-0.1);
//                liftRight.setPosition(0.1);
//            }
//
//            if (gamepad2.a) {
//                airplane.setPosition(0.2);
//            }

            if (gamepad2.x) {
                stageDoor.setPosition(-0.5);
                telemetry.addData("pos", stageDoor.getPosition());
            }

            if (gamepad2.y) {
                stageDoor.setPosition(1);
                telemetry.addData("pos", stageDoor.getPosition());
            }

            telemetry.update();

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
            intake.setPower(intakePower);
        }

    }

}
