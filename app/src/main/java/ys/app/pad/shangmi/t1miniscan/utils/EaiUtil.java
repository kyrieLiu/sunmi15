package ys.app.pad.shangmi.t1miniscan.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class EaiUtil {

    public static final String LED_NFC_NAME = "/sys/class/leds/led_nfc/brightness";
    public static final String LED_CAM_NAME = "/sys/class/leds/led_cam/brightness";

    private static boolean      circle = false;

    private static final String TAG = "EaiUtil";

    public static void changeNFCLed(String changecolor, String changefilename) {

        File changeNFCLedsFile = new File(changefilename);

        if (changeNFCLedsFile.exists()) {

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(changeNFCLedsFile);
                fileOutputStream.write(changecolor.getBytes());
                fileOutputStream.flush();
            } catch (Exception e) {
                Log.d(TAG, " Nfc Camera Lamp Exception  "+e.getMessage());
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        fileOutputStream = null;
                    }catch(Exception e) {
                        Log.d(TAG, " Nfc Camera Lamp close Exception  "+e.getMessage());
                    }
                }
            }
        }
    }

    public static void controlLamp(String status, long lightTime, long putoutTime, String changeFileName) {

        long    startTime;
        long    endTime;
        boolean isOn = true;

        if (status.equals("1")) {
            circle = true;
            changeNFCLed("1", changeFileName);
            startTime = System.currentTimeMillis();

            while (circle) {
                if (isOn) {
                    endTime = System.currentTimeMillis();
                    if ((endTime-startTime) > lightTime){
                        isOn = false;
                        changeNFCLed("0", changeFileName);
                        startTime = System.currentTimeMillis();
                    }
                } else {
                    endTime = System.currentTimeMillis();
                    if ((endTime-startTime) > putoutTime){
                        isOn = true;
                        changeNFCLed("1", changeFileName);
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        } else {
            circle = false;
            changeNFCLed("0", changeFileName);
        }
    }
}
