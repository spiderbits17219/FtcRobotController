package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static java.lang.Thread.sleep;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ColorSensorV224 {

    @Autonomous(name = "Color Sensor Test", group = "Sensor")
    public class ColorSensorTest extends LinearOpMode {

        private ColorSensor colorSensor;

        @Override
        public void runOpMode() throws InterruptedException {
            colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

            waitForStart();

            while (opModeIsActive()) {
                // Read the color sensor values
                int redValue = colorSensor.red();
                int greenValue = colorSensor.green();
                int blueValue = colorSensor.blue();
                int alphaValue = colorSensor.alpha();

                telemetry.addData("Red", redValue);
                telemetry.addData("Green", greenValue);
                telemetry.addData("Blue", blueValue);
                telemetry.addData("Alpha", alphaValue);
                telemetry.update();

                if (redValue > greenValue && redValue > blueValue) {
                    telemetry.addData("Detected Color", "Red");
                } else if (greenValue > redValue && greenValue > blueValue) {
                    telemetry.addData("Detected Color", "Green");
                } else if (blueValue > redValue && blueValue > greenValue) {
                    telemetry.addData("Detected Color", "Blue");
                } else {
                    telemetry.addData("Detected Color", "Unknown");
                }

                sleep(100);
            }
        }
    }

}
