package org.firstinspires.ftc.teamcode;


import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;


public class LEDRevBlinkinV2 {

        /** Configuration File
         ** Control Hub:
         * Servo Port 05: leftlights
         * Expansion Hub
         * Servo Port 05: rightlights
         */

        @Disabled
        @TeleOp(group = "Examples")
        public class ColorBlinkinSensorCode extends LinearOpMode {

            // Declare the sensors and hardware
            ColorSensor sensorColor;
            DistanceSensor sensorDistance;
            RevBlinkinLedDriver leftLights;
            RevBlinkinLedDriver rightLights;

            boolean blinkinTimer = false;
            int blinkinDelay = 2000; // Don't recommend using during teleop

            @Override
            public void runOpMode() throws InterruptedException {

                sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
                sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");

                float hsvValues[] = {0F, 0F, 0F};
                final float values[] = hsvValues;

                final double SCALE_FACTOR = 255;

                int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
                final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

                initHardware();

                waitForStart();

                while (opModeIsActive()) {

                    Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                            (int) (sensorColor.green() * SCALE_FACTOR),
                            (int) (sensorColor.blue() * SCALE_FACTOR),
                            hsvValues);

                    int detectedColor = getDetectedColor();

                    setBlinkinColor(detectedColor);
                }
            }

            private void setBlinkinColor(int detectedColor) {
                switch (detectedColor) {
                    case 1: // Red
                        leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                        rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                        break;
                    case 2: // Yellow
                        leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                        rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                        break;
                    case 3: // Blue
                        leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                        rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                        break;
                    case 4: // White
                        leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                        rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
                        break;
                    default:
                        leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                        rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                        break;
                }
            }

            private int getDetectedColor() {
                int red = sensorColor.red();
                int green = sensorColor.green();
                int blue = sensorColor.blue();

                if (red > green && red > blue) {
                    return 1; // Red
                } else if (green > red && green > blue) {
                    return 2; // Yellow
                } else if (blue > red && blue > green) {
                    return 3; // Blue
                } else {
                    return 4; // White
                }
            }

            private void initHardware() {
                leftLights = hardwareMap.get(RevBlinkinLedDriver.class, "leftLights");
                rightLights = hardwareMap.get(RevBlinkinLedDriver.class, "rightLights");
            }

            private void resetBlinkin() {
                leftLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK); // Turn off LEDs
                rightLights.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
            }
        }

        private void waitForStart() {
        }


    }
