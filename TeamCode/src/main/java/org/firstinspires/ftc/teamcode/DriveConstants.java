package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveConstants {

    static final float Kp = 0.0006f;
    static final float Ki = 0;
    static final float Kd = 0;

    static final double COUNTS_PER_MOTOR_REV = 537.7;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 3.779;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
}

//    public void PIDDrive(float goalValue, double timeoutS){
//
//        ElapsedTime runtime = new ElapsedTime();
//        runtime.reset();
//
//        while (opModeIsActive() &&
//                (runtime.seconds() < timeoutS) &&
//                (frontLeft.getCurrentPosition() < goalValue) && (frontRight.getCurrentPosition() < goalValue) && (backLeft.getCurrentPosition() < goalValue) && (backRight.getCurrentPosition() < goalValue)) {
//            {
//
//                // obtain the encoder position
//                float encoderPosition = frontRight.getCurrentPosition();
//                // calculate the error
//                float error = goalValue - encoderPosition;
//
//                float derivative = (float) ((error - lastError) / timer.seconds());
//
//                // sum of all error over time
//                integralSum = integralSum + error * (float) timer.seconds();
//
//                float out = (DriveConstants.Kp * error) + (DriveConstants.Ki * integralSum) + (DriveConstants.Kd * derivative);
//
//                frontRight.setPower(out);
//                frontLeft.setPower(out);
//                backRight.setPower(out);
//                backLeft.setPower(out);
//
//                lastError = error;
//
//                timer.reset();
//                telemetry.update();
//            }
//
//            // Stop all motion;
//            frontLeft.setPower(0);
//            frontRight.setPower(0);
//            backLeft.setPower(0);
//            backRight.setPower(0);
//
//            sleep(250);   // optional pause after each move.
//        }
//    }