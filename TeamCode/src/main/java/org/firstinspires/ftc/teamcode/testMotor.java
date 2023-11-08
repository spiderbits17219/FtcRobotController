//package org.firstinspires.ftc.teamcode;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.PIDCoefficients;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Range;
//
//@Config
//@Autonomous
//
//public class testMotor extends LinearOpMode {
//
//    DcMotor testMotor;
//
//    double integral = 0;
//    double repetitions = 0;
//   public static PIDCoefficients testPID = new PIDCoefficients(0,0,0 );
//    FtcDashboard dashboard;
//    public static double TARGET_POS = 100; // ticks
//    ElapsedTime PIDTimer = new ElapsedTime();
//
//
//    @Override
//    public void runOpMode() {
//        testMotor = hardwareMap.dcMotor.get("testMotor");
//        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        dashboard = FtcDashboard.getInstance();
//
//        waitForStart();
//
//        void moveTestMotor(TARGET_POS);
//
//        (double targetPosition){
//            double error = targetPosition - testMotor.getCurrentPosition();
//            double lastError = 0;
//            while (Math.abs(error) <= 13 && repetitions < 40) {
//                error = targetPosition - testMotor.getCurrentPosition();
//                double changeInError = error - lastError;
//                integral += error * PIDTimer.time();
//                double derivative = changeInError / PIDTimer.time();
//                double P = testPID.p * error;
//                double I = testPID.i * integral;
//                double D = testPID.d * derivative;
//                testMotor.setPower(P + I + D);
//                lastError = error;
//                repetitions++;
//                PIDTimer.reset();
//
//
//            }
//        }
//    }
//}
