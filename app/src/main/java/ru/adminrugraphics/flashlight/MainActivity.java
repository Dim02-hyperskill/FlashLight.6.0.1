package ru.adminrugraphics.flashlight;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint; // Это убирает предупреждения
import android.app.Activity;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private ImageButton mTorchOnOffButton, mIbTorch_touch;
    private Boolean isTorchOn;
    private Boolean isTimerOn;
    private Boolean blockOff;
    private Boolean do_not_turn_rotate; // чтобы не отключать фонарь при запуске активности Rotate
    SharedPreferences sp, sPref;   // для Активити настроек
    public int seconds;
    private EditText edText;
    MyCountDownTimer myCountDownTimer;
    public SwitchCompat mTimerSwitch;
    ImageView imageView2;
    boolean isCameraFlash;
    final String SAVED_TEXT = "saved_text";
    SharedPreferences.Editor ed;

    //@SuppressLint("ClickableViewAccessibility") // Это убирает предупреждения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIbTorch_touch = findViewById(R.id.ib_torch_tap);
        mIbTorch_touch.setImageResource(R.drawable.tap);
        mTorchOnOffButton = findViewById(R.id.ib_torch);

        mTimerSwitch = findViewById(R.id.timer_switch);
        imageView2 = findViewById(R.id.imageView2);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        edText = findViewById(R.id.edText);

        isTorchOn = sp.getBoolean("key_torch_on", false);
        isTimerOn = sp.getBoolean("key_timer_on", false);
        //seconds = Integer.parseInt(sp.getString("key_second", "33"));


        seconds = loadParam();
       // myCountDownTimer = new MyCountDownTimer(seconds*1000, 1000);
        edText.setText(MessageFormat.format("{0}", seconds));

        // region Сохранение параметра


        // endregion

        // region проверка на наличие камеры
        isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!isCameraFlash) {
            new AlertDialog.Builder( this)
                    .setTitle(R.string. error_title )
                    //.setMessage(R.string.error_title)
                    .setPositiveButton(R.string. exit_message , (dialog, which) -> finish())
                    .setIcon(android.R.drawable.ic_dialog_alert )
                    .show();
        }
        // endregion

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


        // region Ввод цифр в таймер
/*       edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(!edText.getText().toString().equals("")) {
                    seconds = Integer.parseInt(edText.getText().toString()); // Get value from EditText into variable
                    edText.setSelection(edText.getText().length());

                }
            }
        });*/
        //endregion




        // region Кнопка и переключатель
        mTorchOnOffButton.setOnClickListener(v -> { // Кнопка
            closeKeyboard();
            String a = Integer.toString(seconds);
            isTorchOn = !isTorchOn;
            if (isTorchOn) {
                mTorchOnOffButton.setImageResource(R.drawable.fl_on);
                turnOnFlash();
                if(mTimerSwitch.isChecked()) {
                    startTimer();
                    edText.setText(a);
                }

            } else {
                mTorchOnOffButton.setImageResource(R.drawable.fl_off);
                turnOffFlash();
                if(mTimerSwitch.isChecked()) myCountDownTimer.cancel();
                edText.setTextColor(Color.WHITE);
                edText.setEnabled(true);
                edText.setText(a);
                edText.setEnabled(true);
            }
        });

        mTimerSwitch.setChecked(isTimerOn); // Переключатель
        mTimerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            closeKeyboard();
            String a = Integer.toString(seconds);
            if (isChecked) {
                startTimer();
                isTimerOn = true;
                edText.setText(a);
               // edText.setEnabled(false);
            } else {
                isTimerOn = false;
                myCountDownTimer.cancel();
                edText.setText(a);
                edText.setEnabled(true);
                edText.setTextColor(Color.WHITE);
            }
        });
        //endregion

        edText.setOnLongClickListener(v -> {
            edText.setTextColor(Color.YELLOW);


                    // Toast saveValueSecond
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_save_value_second, (ViewGroup) findViewById(R.id.id_toast_save_value));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(R.string.toast_save_seconds);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
                    // КОНЕЦ Toast saveValueSecond
            closeKeyboard();


            return false;
        });


    }


    void saveParams() {
        sPref = getPreferences(MODE_PRIVATE);
        ed = sPref.edit();
        ed.putString(SAVED_TEXT, edText.getText().toString());
        ed.apply();
    }

    int loadParam() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "13");
        return  Integer.parseInt(savedText);
    }

    public void startTimer () {
        String a = Integer.toString(seconds);
        edText.setText(a);
        myCountDownTimer = new MyCountDownTimer(seconds*1000, 1000);
        if(isTorchOn){
            myCountDownTimer.start();
        }
        myCountDownTimer.onTick(10);
    }

    public void onClickMainLayout(View view) {
        closeKeyboard();
    }

    public void onClickEdText(View view) {


    }

    public void onClickmTorchOnOffButton(View view) {

    }


    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long m) {
            String a = Long.toString( m / 1000);
            edText.setText(a);
            edText.setEnabled(false);
            edText.setTextColor(Color.GRAY);
        }
        @Override
        public void onFinish() {
            isTorchOn = false;
            mTorchOnOffButton.setImageResource(R.drawable.fl_off);
            mTimerSwitch.setChecked(false);
            turnOffFlash();
            edText.setText("0");
        }


    }

    // region onResume
    @Override
    protected void onResume() {
        super.onResume();
        do_not_turn_rotate = true;
        if (isTorchOn) {
            mTorchOnOffButton.setImageResource(R.drawable.fl_on);
            turnOnFlash();
        } else {
            mTorchOnOffButton.setImageResource(R.drawable.fl_off);
            turnOffFlash();
        }


        if (!sp.getBoolean("key_block_off", false)) {
            imageView2.setVisibility(View.INVISIBLE);
        } else {
            imageView2.setVisibility(View.VISIBLE);
        }

        boolean flagKeepScreenOn = sp.getBoolean("key_keep_screen", true);

        if(flagKeepScreenOn) {  // Отключена блокировка экрана
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
    // endregion


    @Override
    protected void onStart() {
        super.onStart();
        seconds = loadParam();
    }

    @Override
    protected void onPause() {
        super.onPause();
        blockOff = sp.getBoolean("key_block_off", false);
        if(!blockOff) turnOffFlash();
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
        closeKeyboard();
       if (do_not_turn_rotate) turnOffFlash();
    }

    // region Включение/выключение камеры
    private void turnOnFlash() {
        if (isCameraFlash){
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
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
            try {
                String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);   //Turn OFF
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
    // endregion

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


    // Скрывает клавиатуру и убирает фокус из edText
    private void closeKeyboard(){


        seconds = Integer.parseInt(edText.getText().toString()); // Get value from EditText into variable


        View view = this.getCurrentFocus(); // Скрывает клавиатуру
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),  0);
        }


        edText.clearFocus(); // Убирает фокус из edText
        //  edText.setFocusableInTouchMode(false);
        //  edText.setFocusable(false);
        //  edText.setFocusableInTouchMode(true);
        //  edText.setFocusable(true);

        if (edText.getText().toString().equals("") || edText.getText().toString().equals("0")){
            edText.setText("" + loadParam());
        } else {
            saveParams();
        }




    }




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