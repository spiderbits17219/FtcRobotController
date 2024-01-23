package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Config
@Autonomous
public class PIDTestWGyro extends LinearOpMode {

    float Kp = 0;
    //0.0006f

    float KpA;
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
    BNO055IMU imu;
    private Orientation lastAngle = new Orientation();
    private double currAngle = 0;

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

    //define and init IMU
    imu = hardwareMap.get(BNO055IMU.class, "IMU");
    BNO055IMU.Parameters imuParameters = new BNO055IMU.Parameters();
    imu.initialize(imuParameters);
    imuParameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;


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
    ctrlDrive(DRIVE_SPEED,  5,  5, 5, 5, 200, 20);

    telemetry.addData("Path", "Complete");
    telemetry.update();
    sleep(1000);  // pause to display final telemetry message.
}

    public void ctrlDrive(double speed,
                             double leftFInches, double rightFInches, double leftBInches, double rightBInches,
                          float goalValue, double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newRightBackTarget;
        int newLeftBackTarget;

            if (opModeIsActive()) {

                // Determine new target position, and pass to motor controller
                newLeftFrontTarget = frontLeft.getCurrentPosition() + (int)(leftFInches);
                newRightFrontTarget = frontRight.getCurrentPosition() + (int)(rightFInches);
                newRightBackTarget = backRight.getCurrentPosition() + (int)(rightBInches);
                newLeftBackTarget = backLeft.getCurrentPosition() + (int)(leftBInches);

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
                float startingAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

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
                    float currentAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                    // calculate the error
                    float angleError = startingAngle - currentAngle;
                    float error = goalValue - encoderPosition;

                    float derivative = (float) ((error - lastError) / timer.seconds());

                    // sum of all error over time
                    integralSum = integralSum + error * (float)timer.seconds();

                    float out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

                    float angleOut = Kp * angleError;

                    float outputL = out - angleOut;
                    float outputR = out + angleOut;

                    if(outputL > 1){
                        outputL = 1;
                        outputR /= outputL;
                    }else if(outputR > 1){
                        outputR = 1;
                        outputL /= outputR;
                    }

                    frontRight.setPower(outputR);
                    frontLeft.setPower(outputL);
                    backRight.setPower(outputR);
                    backLeft.setPower(outputL);

                    lastError = error;

                    timer.reset();
                    telemetry.update();
                }

                // Stop all motion;
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
            }
    }

    public void resetAngle(){
        lastAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        currAngle = 0;
    }

    public double getAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
         return currAngle;
    }

    public double angleWrap(double deg){
        if(deg > 180){
            deg -= 360;
        } else if(deg <= -180){
            deg += 360;
        }
        return deg;
    }


}




