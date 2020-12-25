package ru.adminrugraphics.flashlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class RotateActivity extends AppCompatActivity {


    private Boolean flagKeepScreenOn;
    SharedPreferences sp;   // для Активити настроек
    private CameraManager mCameraManager;  // камера Для версии выше api-22

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Это чтобы экран не гас
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // это скрывает статус бар
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }


    //Чтобы из кода узнать текущую ориентацию, можно создать следующую функцию:
    private boolean getScreenOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return true; //"Портретная ориентация"
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return false;// "Альбомная ориентация"
        else
            return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("FlashLightActivity", "старт в ROTATE   onResume() ");
        flagKeepScreenOn = sp.getBoolean("key_keep_screen", true);

        if (flagKeepScreenOn) {    // Отключена блокировка экрана
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getScreenOrientation()) {
            turnOnFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();;
    }

    @Override
    protected void onStop() {
        super.onStop();
        turnOffFlash();
    }



    private void turnOnFlash() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null; // Usually back camera is at 0 position.
        try {
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, true);   //Turn ON
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlash() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null; // Usually back camera is at 0 position.
        try {
            cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, false);   //Turn OFF
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override  // Возврат на предыдущую активити через кнпку "назад"
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}