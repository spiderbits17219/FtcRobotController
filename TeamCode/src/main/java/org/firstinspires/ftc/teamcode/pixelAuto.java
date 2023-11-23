package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="pixelAuto", group = "Auto")
public class pixelAuto extends LinearOpMode {
    OpenCvCamera cam;
    @Override
    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id",
                hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pixelDetect detect = new pixelDetect(telemetry);

        cam.setPipeline(detect);
        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){

            @Override
            public void onOpened() {
                cam.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }

        });

        waitForStart();
        switch (detect.getPlace()){
            case LEFT:
                telemetry.addData("yay", 1);
                telemetry.update();
            break;

            case CENTER:
            break;

            case RIGHT:
            break;

        }
        cam.stopStreaming();
    }

}
