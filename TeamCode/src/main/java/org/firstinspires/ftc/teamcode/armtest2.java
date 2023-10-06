package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class armtest2 extends LinearOpMode {

    //
//    //     // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //    private DcMotor frontLeft = null;
//    private DcMotor frontRight = null;
//    private DcMotor backLeft = null;
//    private DcMotor backRight = null;
    private DcMotor armMotor = null;
    private Servo leftServo = null;
    private Servo rightServo = null;
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;

    private double servoPos;

    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 3.779 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    public double armPower(int encoderTicks){
        double k = 0;
        double power = k * Math.cos((encoderTicks / COUNTS_PER_MOTOR_REV ) * 360) / 12;
        return power;
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
//        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
//        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
//        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        backRight = hardwareMap.get(DcMotor.class, "backRight");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");
//
//        frontLeft.setDirection(DcMotor.Direction.REVERSE);
//        frontRight.setDirection(DcMotor.Direction.FORWARD);
//        backLeft.setDirection(DcMotor.Direction.REVERSE);
//        backRight.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        leftServo.setDirection(Servo.Direction.REVERSE);
        rightServo.setDirection(Servo.Direction.FORWARD);

//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //         // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            float xpower = gamepad1.left_stick_x;
            float ypower = -gamepad1.left_stick_y;
            float rpower = gamepad1.right_stick_x;
            float denom = Math.max(Math.abs(ypower) + Math.abs(xpower) - Math.abs(rpower), 1);

            float frontLeftPower = (xpower + ypower + rpower) / denom;
            float frontRightPower = (ypower - xpower - rpower) / denom;
            float backLeftPower = (ypower - xpower + rpower) / denom;
            float backRightPower = (ypower + xpower - rpower) / denom;

//            if(gamepad2.left_stick_y != 0){
//                armPower = gamepad2.left_stick_y;
//            }
//
//            frontLeft.setPower(frontLeftPower);
//            frontRight.setPower(frontRightPower);
//            backLeft.setPower(backLeftPower);
//            backRight.setPower(backRightPower);


            if(gamepad2.dpad_down){
                armMotor.setPower(0.2);
                armMotor.setTargetPosition(-5);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }else if(gamepad2.dpad_up){
                armMotor.setPower(-0.8);
                armMotor.setTargetPosition(-50);
                armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            if (gamepad2.a) {
                rightServo.setPosition(0.2);
            }
            if (gamepad2.b) {
                rightServo.setPosition(1.5);
            }
            if(gamepad2.x){
                leftServo.setPosition(-0.2);
            }
            if(gamepad2.y){
                leftServo.setPosition(-1.5);
            }

        }
    }

}