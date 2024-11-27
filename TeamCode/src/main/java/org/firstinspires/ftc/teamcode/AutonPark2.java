//package org.firstinspires.ftc.teamcode;
//
//public class AutonPark2 {
//
//    package src.main.java;
//
//import com.qualcomm.hardware.bosch.BNO055IMU;
//import com.qualcomm.hardware.rev.RevBlinkinLED;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
//import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
//
//    @Autonomous(name = "RedAuto", group = "Autonomous")
//    public class RedAuto extends LinearOpMode {
//
//        // Declare OpMode members.
//        private ElapsedTime runtime = new ElapsedTime();
//        private DcMotor leftFrontDrive = null;
//        private DcMotor rightFrontDrive = null;
//        private DcMotor leftRearDrive = null;
//        private DcMotor rightRearDrive = null;
//        private DcMotor armMotor = null;
//        private Servo clawServo = null;
//        private Servo intakeServo = null;
//        private Servo wobbleServo = null;
//        private RevBlinkinLED blinkinLED = null;
//
//        // IMU stuff
//        BNO055IMU imu;
//        Orientation lastAngles = new Orientation();
//        double globalAngle, power = .30, correction;
//
//        // State used for determining which cycle to run in autonomous
//        private int cycle = 0;
//
//        @Override
//        public void runOpMode() {
//            telemetry.addData("Status", "Initialized");
//            telemetry.update();
//
//            // Initialize the hardware variables. Note that the strings used here as parameters
//            // to 'get' must correspond to the names assigned during the robot configuration
//            // step (using the FTC Robot Controller app on the phone).
//            leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFrontDrive");
//            rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
//            leftRearDrive  = hardwareMap.get(DcMotor.class, "leftRearDrive");
//            rightRearDrive = hardwareMap.get(DcMotor.class, "rightRearDrive");
//            armMotor = hardwareMap.get(DcMotor.class, "armMotor");
//            clawServo = hardwareMap.get(Servo.class, "clawServo");
//            intakeServo = hardwareMap.get(Servo.class, "intakeServo");
//            wobbleServo = hardwareMap.get(Servo.class, "wobbleServo");
//            blinkinLED = hardwareMap.get(RevBlinkinLED.class, "blinkinLED");
//
//            // Most robots need the motor on one side to be reversed to drive forward
//            // Reverse the motor that runs backwards when connected directly to the battery
//            leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
//            leftRearDrive.setDirection(DcMotorSimple.Direction.REVERSE);
//            rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
//            rightRearDrive.setDirection(DcMotorSimple.Direction.FORWARD);
//            armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
//
//            // Set zero power behavior for all motors
//            leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            leftRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            rightRearDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//            //  The following code is for the IMU, not the main code
//            // Initialize the IMU sensor
//            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//            parameters.accelUnit          = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
//            parameters.loggingEnabled      = true;
//            parameters.loggingTag          = "IMU";
//            parameters.accelerationIntegrationAlgorithm = new com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator();
//            imu = hardwareMap.get(BNO055IMU.class, "imu");
//            imu.initialize(parameters);
//            // Make sure the IMU is calibrated properly
//            while(!imu.isGyroCalibrated()) {
//                sleep(50);
//            }
//            //  End of IMU code
//            telemetry.addData("Status", "Calibrated");
//            telemetry.update();
//
//            // Wait for the game to start (driver presses PLAY)
//            waitForStart();
//            runtime.reset();
//
//            // Run until the end of the match (driver presses STOP)
//            while (opModeIsActive()) {
//                //  Cycle 0 - Move to the wobble goal and drop wobble
//                if (cycle == 0) {
//                    // Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    // Stop the robot
//                    stopDrive();
//                    // Move forward to align with wobble goal
//                    strafeRight(0.3, 1);
//                    // Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    // Stop the robot
//                    stopDrive();
//                    // Move forward to align with wobble goal
//                    strafeRight(0.3, 1);
//                    // Open the claw
//                    clawServo.setPosition(1);
//                    // Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    // Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    // Close the claw
//                    clawServo.setPosition(0.2);
//                    // Pause for a second
//                    sleep(1000);
//                    // Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    // Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    // Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 1 - Intake rings
//                if (cycle == 1) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 2 - Move to the wobble goal and drop wobble
//                if (cycle == 2) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 3 - Intake rings
//                if (cycle == 3) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 4 - Move to the wobble goal and drop wobble
//                if (cycle == 4) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 5 - Intake rings
//                if (cycle == 5) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 6 - Move to the wobble goal and drop wobble
//                if (cycle == 6) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 7 - Intake rings
//                if (cycle == 7) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 8 - Move to the wobble goal and drop wobble
//                if (cycle == 8) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 9 - Intake rings
//                if (cycle == 9) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 10 - Move to the wobble goal and drop wobble
//                if (cycle == 10) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 11 - Intake rings
//                if (cycle == 11) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 12 - Move to the wobble goal and drop wobble
//                if (cycle == 12) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 13 - Intake rings
//                if (cycle == 13) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 14 - Move to the wobble goal and drop wobble
//                if (cycle == 14) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 15 - Intake rings
//                if (cycle == 15) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 16 - Move to the wobble goal and drop wobble
//                if (cycle == 16) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 17 - Intake rings
//                if (cycle == 17) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 18 - Move to the wobble goal and drop wobble
//                if (cycle == 18) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1000);
//                    //  Turn to face the intake zone
//                    turnRight(0.3, 90);
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 19 - Intake rings
//                if (cycle == 19) {
//                    //  Lower the arm to intake
//                    armMotor.setPower(-0.3);
//                    sleep(500);
//                    //  Set the intake servo to intake
//                    intakeServo.setPosition(0.8);
//                    //  Move forward to intake rings
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Move back to the starting position
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//                    leftRearDrive.setPower(-power);
//                    rightRearDrive.setPower(-power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Cycle to the next cycle
//                    cycle++;
//                }
//                //  Cycle 20 - Move to the wobble goal and drop wobble
//                if (cycle == 20) {
//                    //  Turn to face the wobble goal
//                    turnLeft(0.3, 90);
//                    //  Move forward for 1.5 seconds
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1500);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Move forward again
//                    leftFrontDrive.setPower(power);
//                    rightFrontDrive.setPower(power);
//                    leftRearDrive.setPower(power);
//                    rightRearDrive.setPower(power);
//                    sleep(1000);
//                    //  Stop the robot
//                    stopDrive();
//                    //  Move forward to align with wobble goal
//                    strafeLeft(0.3, 1);
//                    //  Open the claw
//                    clawServo.setPosition(1);
//                    //  Lower arm
//                    armMotor.setPower(-0.5);
//                    sleep(1000);
//                    //  Raise the arm to drop wobble
//                    armMotor.setPower(0.5);
//                    sleep(500);
//                    //  Close the claw
//                    clawServo.setPosition(0.2);
//                    //  Pause for a second
//                    sleep(1000);
//                    //  Move back and turn toward the intake zone
//                    leftFrontDrive.setPower(-power);
//                    rightFrontDrive.setPower(-power);
//
//
//                }
