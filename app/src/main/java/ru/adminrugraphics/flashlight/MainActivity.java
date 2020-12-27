package ru.adminrugraphics.flashlight;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

//import android.annotation.SuppressLint; // Это убирает предупреждения
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private ImageButton mTorchOnOffButton, mIbTorch_touch;
    private Boolean isTorchOn;
    private Boolean isTimerOn;
    private Boolean blockOff;
    private Boolean do_not_turn_rotate; // чтобы не отключать фонарь при запуске активности Rotate
    SharedPreferences sp;   // для Активити настроек
    public int seconds;
    public TextView mTvTimerOn;
    MyCountDownTimer myCountDownTimer;
    public SwitchCompat mTimerSwitch;
    ImageView imageView2;
    boolean isCameraFlash;

   // @SuppressLint("ClickableViewAccessibility") // Это убирает предупреждения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIbTorch_touch = findViewById(R.id.ib_torch_tap);
        mIbTorch_touch.setImageResource(R.drawable.tap);
        mTorchOnOffButton = findViewById(R.id.ib_torch);
        mTvTimerOn = findViewById(R.id.id_timer_on);
        mTvTimerOn.setTypeface(Typeface.createFromAsset(getAssets(), "calculator.otf"));
        mTimerSwitch = findViewById(R.id.timer_switch);
        imageView2 = findViewById(R.id.imageView2);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        isTorchOn = sp.getBoolean("key_torch_on", false);
        isTimerOn = sp.getBoolean("key_timer_on", false);
        seconds = Integer.parseInt(sp.getString("key_second", "33"));


        isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isCameraFlash) {
            new AlertDialog.Builder( this)
                    .setTitle(R.string. error_title )
                    //.setMessage(R.string.error_title)
                    .setPositiveButton(R.string. exit_message , (dialog, which) -> finish())
                    .setIcon(android.R.drawable.ic_dialog_alert )
                    .show();
        }


        // region Сенсорная Кнопка
        mIbTorch_touch.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // нажатие
                    if (!isTorchOn) {
                        mIbTorch_touch.setImageResource(R.drawable.tap_tapping);
                        turnOnFlash();
                    }
                    break;
                case MotionEvent.ACTION_UP: // отпускание
                    v.performClick();
                    if (!isTorchOn) {
                        mIbTorch_touch.setImageResource(R.drawable.tap);
                        turnOffFlash();
                    }
                    break;
            }
            return true;
        });
        // endregion


        mTorchOnOffButton.setOnClickListener(v -> {
            isTorchOn = !isTorchOn;
            if (isTorchOn) {
                mTorchOnOffButton.setImageResource(R.drawable.fl_on);
                turnOnFlash();
                if(mTimerSwitch.isChecked()) {
                    myCountDownTimer.start();
                }
            } else {
                mTorchOnOffButton.setImageResource(R.drawable.fl_off);
                turnOffFlash();
                myCountDownTimer.cancel();
                String a = Integer.toString(seconds);
                mTvTimerOn.setText(a);
            }
        });

        mTimerSwitch.setChecked(isTimerOn);
        mTimerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String a = Integer.toString(seconds);
            if (isChecked) {
                startTimer();
                isTimerOn = true;
                mTvTimerOn.setText(a);
            } else {
                isTimerOn = false;
                mTvTimerOn.setText(a);
                myCountDownTimer.cancel();
            }
        });
    }

    public void startTimer () {
        String a = Integer.toString(seconds);
        mTvTimerOn.setText(a);
        myCountDownTimer = new MyCountDownTimer(seconds*1000, 1000);
        if(isTorchOn){
            myCountDownTimer.start();
        }
        myCountDownTimer.onTick(10);
    }


    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long m) {
            String a = Long.toString( m / 1000);
            mTvTimerOn.setText(a);
        }
        @Override
        public void onFinish() {
            isTorchOn = false;
            mTorchOnOffButton.setImageResource(R.drawable.fl_off);
            mTimerSwitch.setChecked(false);
            turnOffFlash();
            mTvTimerOn.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        do_not_turn_rotate = true;
        if (!sp.getBoolean("key_block_off", false)) {
            imageView2.setVisibility(View.INVISIBLE);
        } else {
            imageView2.setVisibility(View.VISIBLE);
        }

        boolean flagKeepScreenOn = sp.getBoolean("key_keep_screen", true);
        seconds = Integer.parseInt(sp.getString("key_second", "33"));

        if(flagKeepScreenOn) {  // Отключена блокировка экрана
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        mTvTimerOn.setText(MessageFormat.format("{0}", seconds));
        myCountDownTimer = new MyCountDownTimer(seconds*1000, 1000);

        if(isTimerOn) {
            startTimer();
            mTimerSwitch.setChecked(true);
        }

        if (isTorchOn) {
            //Получаем для приложения параметры камеры:
            mTorchOnOffButton.setImageResource(R.drawable.fl_on);
            turnOnFlash();
        } else {
            mTorchOnOffButton.setImageResource(R.drawable.fl_off);
             turnOffFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onPause() {
        super.onPause();
        blockOff = sp.getBoolean("key_block_off", false);
        if(!blockOff) turnOffFlash();
        myCountDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blockOff = sp.getBoolean("key_block_off", false);
        if (!blockOff) if (do_not_turn_rotate) {
            turnOffFlash();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (do_not_turn_rotate) turnOffFlash();

    }

    private void turnOnFlash() {
        if (isCameraFlash){
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
           // String cameraId; // Usually back camera is at 0 position.
            try {
               String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);   //Turn ON
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlash() {
        if (isCameraFlash){
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            //String cameraId; // Usually back camera is at 0 position.
            try {
                String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);   //Turn OFF
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

    }


    // region  переходы на ддр. Активити и BroadcastReceiver
    public void onClickSetting(View view) {
        Intent intent = new Intent(this, PrefActivity.class);
        startActivity(intent);
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            int level = i.getIntExtra("level", 0);
            //int tmp = i.getIntExtra("temperature", 0);
            TextView tvBat = findViewById(R.id.tv_bat_vol);
            /*TextView tvTerm = findViewById(R.id.tv_term_vol);
            tvTerm.setTypeface(Typeface.createFromAsset(getAssets(), "calculator.otf"));
            tvTerm.setText(Integer.toString(tmp/10) + "°C");*/
            tvBat.setText(MessageFormat.format("{0}%", Integer.toString(level)));
            tvBat.setTypeface(Typeface.createFromAsset(getAssets(), "calculator.otf"));

        }
    };

    public void onClickAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onClickScreenLight(View view) {
        Intent intent = new Intent(this, ScreenLightActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickRotate(View view) {
        blockOff = true;
        do_not_turn_rotate = false;
        Intent intent = new Intent(this, RotateActivity.class);
        startActivity(intent);
        finish();
    }
    // endregion

}


   /* //Чтобы из кода узнать текущую ориентацию, можно создать следующую функцию:
    private boolean getScreenOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return true; //"Портретная ориентация"
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return false;// "Альбомная ориентация"
        else
            return true;
    }*/

