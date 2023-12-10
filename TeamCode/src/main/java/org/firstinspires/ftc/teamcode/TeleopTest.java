package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class TeleopTest extends LinearOpMode{


    //     // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

//    private intakeMotor = null;
//    private liftMotor1 = null;
//    private liftMotor2 = null;
//    private droneServo = null;
//    private rightServo = null;
//    private leftServo = null;
//    private newServo = null;




    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft  = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        //armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        //servoName = hardwareMap.get(Servo.class, "leftServo");
        //servoName = hardwareMap.get(Servo.class, "rightServo");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        //        armMotor.setDirection(DcMotor.Direction.FORWARD);
        //        servoName.setDirection(Servo.Direction.REVERSE);
        //        servoName.setDirection(Servo.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //         // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            double xpower = gamepad1.left_stick_x;
            double  ypower = -gamepad1.left_stick_y;
            double  rpower = gamepad1.right_stick_x;
            double denom = Math.max(Math.abs(ypower)+Math.abs(xpower)-Math.abs(rpower),1);

            double frontLeftPower = (xpower + ypower + rpower)/denom;
            double frontRightPower = (ypower - xpower - rpower)/denom;
            double backLeftPower = (ypower - xpower + rpower)/denom;
            double backRightPower = (ypower + xpower - rpower)/denom;

            if(gamepad1.left_trigger > 0){
                frontLeftPower = frontLeftPower / 3;
                frontRightPower = frontRightPower / 3;
                backLeftPower = backLeftPower / 3;
                backRightPower = backRightPower / 3;
            }

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            //if(gamepad2._______){
                //armMotor = armMotorPower;
                //servoName = ();
                //servoName = ();

        }
    }
}
