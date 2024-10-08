package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
@Autonomous
public class PIDTest extends LinearOpMode {

    float Kp = 0.0006f;
    float Ki = 0;
    float Kd = 0;
    float integralSum = 0;
    float lastError = 0;
    ElapsedTime timer = new ElapsedTime();

    private DcMotor frontLeft  = null;
    private DcMotor frontRight   = null;
    private DcMotor backLeft  = null;
    private DcMotor backRight = null;

    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 3.779 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
@Override
    public void runOpMode(){

    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
    backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    backRight = hardwareMap.get(DcMotor.class, "backRight");


    // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
    // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
    // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
    frontLeft.setDirection(DcMotor.Direction.REVERSE);
    frontRight.setDirection(DcMotor.Direction.FORWARD);
    backLeft.setDirection(DcMotor.Direction.REVERSE);
    backRight.setDirection(DcMotor.Direction.FORWARD);

    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    // Send telemetry message to indicate successful Encoder reset
    telemetry.addData("Starting at",  "%7d :%7d",
            frontLeft.getCurrentPosition(),
            frontRight.getCurrentPosition(),
            backLeft.getCurrentPosition(),
            backRight.getCurrentPosition());
    telemetry.update();

    // Wait for the game to start (driver presses PLAY)
    waitForStart();

    // Step through each leg of the path,
    // Note: Reverse movement is obtained by setting a negative distance (not speed)
    encoderDrive(DRIVE_SPEED,  3,  3, 3, 3, 20);

    telemetry.addData("Path", "Complete");
    telemetry.update();
    sleep(1000);  // pause to display final telemetry message.
}
    public void encoderDrive(double speed,
                             double leftFInches, double rightFInches, double leftBInches, double rightBInches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newRightBackTarget;
        int newLeftBackTarget;

            if (opModeIsActive()) {

                // Determine new target position, and pass to motor controller
                newLeftFrontTarget = frontLeft.getCurrentPosition() + (int)(leftFInches * COUNTS_PER_INCH);
                newRightFrontTarget = frontRight.getCurrentPosition() + (int)(rightFInches * COUNTS_PER_INCH);
                newRightBackTarget = backRight.getCurrentPosition() + (int)(rightBInches * COUNTS_PER_INCH);
                newLeftBackTarget = backLeft.getCurrentPosition() + (int)(leftBInches * COUNTS_PER_INCH);

                frontLeft.setTargetPosition(newLeftFrontTarget);
                frontRight.setTargetPosition(newRightFrontTarget);
                backLeft.setTargetPosition(newLeftBackTarget);
                backRight.setTargetPosition(newRightBackTarget);

                // reset the timeout time and start motion.
                ElapsedTime runtime = new ElapsedTime();
                runtime.reset();



                // keep looping while we are still active, and there is time left, and both motors are running.
                // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
                // its target position, the motion will stop.  This is "safer" in the event that the robot will
                // always end the motion as soon as possible.
                // However, if you require that BOTH motors have finished their moves before the robot continues
                // onto the next step, use (isBusy() || isBusy()) in the loop test.

                float goalValue = 2000;
                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        frontLeft.getCurrentPosition() < goalValue) {

                    // Display it for the driver.
                    telemetry.addData("Running to",  " %7d :%7d :%7d :%7d", newLeftFrontTarget, newLeftBackTarget,
                            newRightFrontTarget, newRightBackTarget);
                    telemetry.addData("Currently at",  " at %7d :%7d :%7d :%7d",
                            frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                            backLeft.getCurrentPosition(), backRight.getCurrentPosition());
                    telemetry.update();

                    // obtain the encoder position
                    float encoderPosition = frontRight.getCurrentPosition();
                    // calculate the error
                    float error = goalValue - encoderPosition;

                    float derivative = (float) ((error - lastError) / timer.seconds());

                    // sum of all error over time
                    integralSum = integralSum + error * (float)timer.seconds();

                    float out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

                    frontRight.setPower(out);
                    frontLeft.setPower(out);
                    backRight.setPower(out);
                    backLeft.setPower(out);

                    lastError = error;

                    timer.reset();
                    telemetry.update();
                }

                // Stop all motion;
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

                sleep(250);   // optional pause after each move.
            }
    }
}




