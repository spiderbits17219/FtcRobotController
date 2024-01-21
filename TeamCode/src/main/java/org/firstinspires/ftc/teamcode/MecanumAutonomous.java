package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.*;

public class MecanumAutonomous extends LinearOpMode {
    ElapsedTime timer = new ElapsedTime();
    private DcMotorEx frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    BNO055IMU imu;
    private PID xControl, yControl, thetaControl;

    @Override
    public void runOpMode() {
        // Initialize your hardware
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backLeftMotor = hardwareMap.get(DcMotorEx.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRight");
        backRightMotor = hardwareMap.get(DcMotorEx.class, "backRight");
        imu = hardwareMap.get(BNO055IMU.class, "IMU");


        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Initialize your PID controllers
        xControl = new PID(0.1, 0.01, 0.1); // Replace with your own PID gains
        yControl = new PID(0.1, 0.01, 0.1); // Replace with your own PID gains
        thetaControl = new PID(0.1, 0.01, 0.1); // Replace with your own PID gains

        // Wait for the game to start (driver presses PLAY)

        //TODO figure out if this is good
        waitForStart();

        // Call the moveTo method with your target positions and orientation
        moveTo(1.0, 1.0, 0.0); // Replace with your own target positions and orientation
    }
    public double getRobotTheta() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle; // Assuming the robot's rotation is around the Z-axis
    }
    public double getRobotY() {
        int leftAv = ((backLeftMotor.getCurrentPosition()+frontLeftMotor.getCurrentPosition())/2);
        int rightAv = (backRightMotor.getCurrentPosition()+backLeftMotor.getCurrentPosition())/2;
        return (leftAv+rightAv)/2;

    }

    public void moveTo(double xTarget, double yTarget, double thetaTarget) {
        double tolerance = 0.01; // Replace with your own tolerance value

        while (opModeIsActive()) {
            // Get current state of the robot
            double xRobotPosition = (0); //TODO replace with external encoder for x/y movement
            double yRobotPosition = getRobotY(); // Replace with method to get current y position
            double thetaRobotPosition = getRobotTheta(); // Replace with method to get current orientation

            // Calculate control outputs
            double x = xControl.calculate(xTarget, xRobotPosition);
            double y = yControl.calculate(yTarget, yRobotPosition);
            double t = thetaControl.calculate(thetaTarget, thetaRobotPosition);

            // Rotate the vector
            double angle = Math.toRadians(45.0); // Replace with the current orientation of the robot if necessary
            double x_rotated = x * Math.cos(angle) - y * Math.sin(angle);
            double y_rotated = x * Math.sin(angle) + y * Math.cos(angle);

            // Apply control outputs to the motors
            frontLeftMotor.setPower(x_rotated + y_rotated + t);
            backLeftMotor.setPower(x_rotated - y_rotated + t);
            frontRightMotor.setPower(x_rotated - y_rotated - t);
            backRightMotor.setPower(x_rotated + y_rotated - t);

            // Exit the loop if the robot is close enough to the target
            if (Math.abs(xTarget - xRobotPosition) < tolerance &&
                    Math.abs(yTarget - yRobotPosition) < tolerance &&
                    Math.abs(thetaTarget - thetaRobotPosition) < tolerance) {
                break;
            }
        }
    }

    public class PID{
        private double kp, ki, kd;
        private double integral, previous_error;

        public PID(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
        }

        public double calculate(double target, double current) {
            double error = target - current;

            integral += error / timer.seconds();
            double derivative = (error - previous_error)/timer.seconds();
            previous_error = error;
            timer.reset();
            return (kp * error) + (ki * integral) + (kd * derivative);

        }
    }
}
