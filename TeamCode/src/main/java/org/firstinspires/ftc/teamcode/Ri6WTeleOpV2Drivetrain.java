package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Ri6WTeleOpV2Drivetrain extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private Servo servo;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        servo = hardwareMap.get(Servo.class, "servo");

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            double leftStickX = gamepad1.left_stick_x;
            double leftStickY = gamepad1.left_stick_y;
            double rightStickX = gamepad1.right_stick_x;

            frontLeftMotor.setPower(leftStickY + leftStickX + rightStickX);
            frontRightMotor.setPower(leftStickY - leftStickX + rightStickX);
            backLeftMotor.setPower(leftStickY - leftStickX - rightStickX);
            backRightMotor.setPower(leftStickY + leftStickX - rightStickX);

            if (gamepad1.a) {
                servo.setPosition(0.5);
            } else if (gamepad1.b) {
                servo.setPosition(1);
            }

            telemetry.addData("frontLeftPower", frontLeftMotor.getPower());
            telemetry.addData("frontRightPower", frontRightMotor.getPower());
            telemetry.addData("backLeftPower", backLeftMotor.getPower());
            telemetry.addData("backRightPower", backRightMotor.getPower());
            telemetry.update();
        }
    }
}