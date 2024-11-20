package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class Teleop24Practice extends LinearOpMode {

    //     // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    private DcMotor liftMotor3 = null;
    private DcMotor liftMotor = null;
    private DcMotor liftMotor2 = null;
    //  private DcMotor intake = null;



    //    private boolean intakeRun = false;
    private boolean negative = false;

    public enum LiftState {
        LIFT_RETRACT,
        LIFT_EXTEND
    }
    LiftState liftState = LiftState.LIFT_EXTEND;

    ElapsedTime liftTimer = new ElapsedTime();





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



//        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        //         // Wait for the game to start (driver presses PLAY)
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



//            if(gamepad2.right_stick_y > 0.15){
//                liftMotor.setPower(gamepad2.right_stick_y);
//                liftMotor2.setPower(gamepad2.right_stick_y);
//            }
//            else if(gamepad2.right_stick_y < -0.15){
//                liftMotor2.setPower(gamepad2.right_stick_y);
//                liftMotor.setPower(gamepad2.right_stick_y);
//            }
//            else{
//                liftMotor2.setPower(0);
//                liftMotor.setPower(0);
//            }
//
            if(gamepad2.right_stick_y > 0.15){
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


            telemetry.update();

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }

    }

}
