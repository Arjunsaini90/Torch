package bilboa.selfietorch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Build;

public class SelfieTorchOnOffActivity extends AppCompatActivity {

    private static CameraManager CamManager;
    private static String CameraIdFrontFacing = null;
    private boolean IsTorchOn = true;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_torch_on_off);

        checkRunTimePermission();



        final Button ButtonOnOff = (Button) findViewById(R.id.c_ButtonOnOff);

        //get front facing camera
        CamManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String[] CameraIdList = null;
        try {
            CameraIdList = CamManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        for(String CameraId : CameraIdList)
        {
            try {
                CameraCharacteristics CamCharacteristics = CamManager.getCameraCharacteristics(CameraId);
                if(CamCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
                {
                    CameraIdFrontFacing = CameraId;
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        if(CameraIdFrontFacing == null)
        {
            return;
        }

        ButtonOnOff.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){

                int brightness = getScreenBrightness();
                Log.e("@@brightness", String.valueOf(brightness));

                if(IsTorchOn)
                {
                    IsTorchOn=false;


                    ScreenBrightness(255,SelfieTorchOnOffActivity.this);
                }else
                {
                    IsTorchOn=true;
                 //   int brightness = getScreenBrightness();

                    ScreenBrightness(10,SelfieTorchOnOffActivity.this);
                }
            }
        });
    }

    private static void handleActionTurnOnFlashLight(Context context){

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CamManager.setTorchMode(CameraIdFrontFacing, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void handleActionTurnOffFlashLight(Context context){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CamManager.setTorchMode(CameraIdFrontFacing, false);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean ScreenBrightness(int level, Context context) {

        try {
            android.provider.Settings.System.putInt(
                    SelfieTorchOnOffActivity.this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS, level);


            android.provider.Settings.System.putInt(context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            android.provider.Settings.System.putInt(
                    SelfieTorchOnOffActivity.this.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    level);


            return true;
        }

        catch (Exception e) {
            Log.e("Screen Brightness", "error changing screen brightness");
            return false;
        }
    }

    // Get the screen current brightness
    protected int getScreenBrightness(){
        /*
            public static int getInt (ContentResolver cr, String name, int def)
                Convenience function for retrieving a single system settings value as an integer.
                Note that internally setting values are always stored as strings; this function
                converts the string to an integer for you. The default value will be returned
                if the setting is not defined or not an integer.

            Parameters
                cr : The ContentResolver to access.
                name : The name of the setting to retrieve.
                def : Value to return if the setting is not defined.
            Returns
                The setting's current value, or 'def' if it is not defined or not a valid integer.
        */
        int brightnessValue = Settings.System.getInt(
                SelfieTorchOnOffActivity.this.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_SETTINGS};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];



                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {

                        }
                    }
                }
            }


        }
    }

}
