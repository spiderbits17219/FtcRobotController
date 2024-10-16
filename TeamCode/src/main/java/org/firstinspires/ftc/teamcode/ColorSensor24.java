//package org.firstinspires.ftc.teamcode;
//
//public class ColorSensor24 {
//
//    import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//    public class ColorSensor {
//
//        private ColorSensor colorSensor;
//
//        public ColorSensor(HardwareMap hardwareMap) {
//            colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
//        }
//
//        public Color getColor() {
//            int red = colorSensor.red();
//            int green = colorSensor.green();
//            int blue = colorSensor.blue();
//
//            if (red > green && red > blue) {
//                return Color.RED;
//            } else if (blue > green && blue > red) {
//                return Color.BLUE;
//            } else if (green > red && green > blue) {
//                return Color.YELLOW;
//            } else {
//                return Color.UNKNOWN;
//            }
//        }
//
//        public enum Color {
//            RED,
//            BLUE,
//            YELLOW,
//            UNKNOWN
//        }
//    }
//}
