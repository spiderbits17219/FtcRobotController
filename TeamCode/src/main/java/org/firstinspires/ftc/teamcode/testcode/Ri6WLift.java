//package org.firstinspires.ftc.teamcode;
//
//public class Ri6WLift {
//
//    package src.main.java;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//
//    public class Lift extends Linear
//            OpMode {
//
//        private DcMotor liftMotor;
//        private Servo liftServo;
//
//        @Override
//        public void runOpMode() throws InterruptedException {
//            liftMotor = hardwareMap.get(DcMotor.class, "lift_motor");
//            liftServo = hardwareMap.get(Servo.class, "lift_servo");
//
//            liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//
//            waitForStart();
//
//            while (opModeIsActive()) {
//                if (gamepad1.a) {
//                    liftMotor.setPower(1);
//                } else if (gamepad1.b) {
//                    liftMotor.setPower(-1);
//                } else {
//                    liftMotor.setPower(0);
//                }
//
//                if (gamepad1.x) {
//                    liftServo.setPosition(0.5);
//                } else {
//                    liftServo.setPosition(0);
//                }
//
//                telemetry.addData("Lift Motor Power", liftMotor.getPower());
//                telemetry.addData("Lift Servo Position", liftServo.getPosition());
//                telemetry.update();
//            }
//        }
//    }
//
//}
