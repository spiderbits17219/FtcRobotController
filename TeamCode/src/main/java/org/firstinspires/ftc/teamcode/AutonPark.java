package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class AutonPark {

    @Autonomous(name="SimplePark", group="Autonomous")
    public class Main extends LinearOpMode {

        private DcMotor leftMotor;
        private DcMotor rightMotor;
        private Servo armServo;

        @Override
        public void runOpMode() {
            leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
            rightMotor = hardwareMap.get(DcMotor.class, "right_motor");
            armServo = hardwareMap.get(Servo.class, "arm_servo");

            Object DcMotorSimple = null;
            waitForStart();

            // Move forward for a short distance
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.5);
            sleep(1000); // Sleep for 1 second

            // Stop the motors
            leftMotor.setPower(0);
            rightMotor.setPower(0);

            // Extend the arm servo
            armServo.setPosition(0.5);
            sleep(500); // Sleep for 0.5 seconds

            // Retract the arm servo
            armServo.setPosition(0.0);
            sleep(500); // Sleep for 0.5 seconds

            // Move backwards for a short distance
            leftMotor.setPower(-0.5);
            rightMotor.setPower(-0.5);
            sleep(1000); // Sleep for 1 second

            // Stop the motors
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }
    }
}
