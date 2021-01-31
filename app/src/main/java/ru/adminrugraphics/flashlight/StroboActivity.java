package ru.adminrugraphics.flashlight;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.MessageFormat;

import static android.graphics.Color.*;


public class StroboActivity extends AppCompatActivity {
    TextView tvQuantity_1, tvDurationPause_1, tvDurationFlash_1, tvInfo_1;
    Button btnStartStop_1;
    Handler handler;
    ImageButton imBtnSync;
    SeekBar seekBarQuantity1, seekBarDuration_1, seekBarFlash_1;
    boolean sign1=true, markSync=true, isCameraFlash;
    int b = 0, i=0, countdownRemainingFlashes, durationPause_1 = 100, durationFlash_1 =100;
    int quantityFlashes_1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strobo);

        handler = new Handler();
        btnStartStop_1 = findViewById(R.id.btn_start_stop_1);
        btnStartStop_1.setText(getString(R.string.start_in_strobo));
        btnStartStop_1.setBackgroundColor(GRAY);
        imBtnSync = findViewById(R.id.im_btn_sync);
        imBtnSync.setColorFilter(getColor(R.color.colorKhaki));
        seekBarQuantity1 = findViewById(R.id.seek_bar_quantity_1);
        seekBarDuration_1 = findViewById(R.id.seek_bar_duration_1);
        seekBarFlash_1 = findViewById(R.id.seek_bar_flash_1);
        tvQuantity_1 = findViewById(R.id.tv_quantity_1);
        tvQuantity_1.setText(getString(R.string.quantity_1));
        tvDurationPause_1 = findViewById(R.id.tv_duration_pause_1);
        tvDurationFlash_1 = findViewById(R.id.tv_duration_flash_1);
        tvInfo_1 = findViewById(R.id.tvInfo_1);

        // region проверка на наличие камеры   УДАЛИТЬ ПРИ РЕЛИЗЕ
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

        seekBarQuantity1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantityFlashes_1 = progress;
                tvInfo_1.setText(getString(R.string.number_of_flashes));
                tvQuantity_1.setText(MessageFormat.format("{0}", quantityFlashes_1));
                if(quantityFlashes_1 == 0){
                    tvQuantity_1.setText(getString(R.string.quantity_1));
                } else countdownRemainingFlashes = quantityFlashes_1; // для обратного отчета вспышек
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        seekBarDuration_1.setProgress(durationPause_1 /100);
        double dd = durationPause_1 /1000.0;
        tvDurationPause_1.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.duration_pause), dd, getString(R.string.sec)));
        seekBarDuration_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double q = seekBarQuantity1.getMax();
                if(markSync){
                    seekBarFlash_1.setProgress(progress);
                }
                progress = (int) (((progress * progress) / (q * q)) * (q - 1) + 1);
                double ccc = progress/10.0;
                String c = String.valueOf(ccc);
                tvDurationPause_1.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.duration_pause), c, getString(R.string.sec)));
                durationPause_1 = progress * 100;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        seekBarFlash_1.setProgress(durationFlash_1 /100);
        double ss = durationFlash_1 /1000.0;
        tvDurationFlash_1.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.duration_flash), ss, getString(R.string.sec)));
        seekBarFlash_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(markSync){
                    seekBarDuration_1.setProgress(progress);
                }
                progress = (int) (((progress * progress) / (99.0f * 99.0f)) * (99 - 1) + 1); // Approximate an exponential curve with x^2
                double c = progress/10.0;
                tvDurationFlash_1.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.duration_flash), c, getString(R.string.sec)));
                durationFlash_1 = progress * 100;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });
    }

    private void jop() {
        handler.removeCallbacksAndMessages(null);
        if(i != -3){
            btnStartStop_1.setBackgroundColor(RED);
            turnOnFlash();
            tvInfo_1.setText(getString(R.string.remaining_flashes));
            handler.postDelayed(() -> {
                jop1();
            }, durationFlash_1);
        } else {
            btnStartStop_1.setBackgroundColor(GRAY);
            turnOffFlash();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void jop1() {
        handler.removeCallbacksAndMessages(null); // Это закрывает postDelayed(()
        btnStartStop_1.setBackgroundColor(GRAY);
        turnOffFlash();
        handler.postDelayed(() -> {
            if(i == 0){
                jop();
            } else {
                countdownRemainingFlashes -= 1;  // Отсчёт отавшихся вспышек
                tvQuantity_1.setText(MessageFormat.format("{0}", countdownRemainingFlashes)); // Отсчёт отавшихся вспышек
                b += 1;
                if (b < i) {
                    jop();
                } else {
                    b=0;
                    sign1 = true;
                    btnStartStop_1.setText(getString(R.string.stop_in_strobo));
                    countdownRemainingFlashes = quantityFlashes_1;
                    tvQuantity_1.setText(MessageFormat.format("{0}", quantityFlashes_1));

                    tvInfo_1.setText(getString(R.string.number_of_flashes));
                    seekBarQuantity1.setOnTouchListener((v, event) -> false); // Включает перемещение полунка сикбара
                }
            }
        }, durationPause_1);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void onClick(View view) {
        //countdownRemainingFlashes = quantity_of_flashes_First; // для обратного отчета вспышек
        i = quantityFlashes_1;
        if (sign1){
            seekBarQuantity1.setOnTouchListener((v, event) -> true); // отключает перемещение ползунка сикбара
            jop();
            sign1 = false;
            btnStartStop_1.setText(getString(R.string.start_in_strobo));
            if(i != 0){
                if(countdownRemainingFlashes == quantityFlashes_1){
                    countdownRemainingFlashes -= 1;
                }
                tvQuantity_1.setText(MessageFormat.format("{0}", countdownRemainingFlashes));
            }
        } else {
            seekBarQuantity1.setOnTouchListener((v, event) -> false); // Включает перемещение полунка сикбара
            seekBarQuantity1.setEnabled(true);
            i = -3;
            handler.removeCallbacksAndMessages(null);
            sign1 = true;
            btnStartStop_1.setText(getString(R.string.stop_in_strobo));
            btnStartStop_1.setBackgroundColor(GRAY);
            turnOffFlash();
        }
    }

    public void onClickSync(View view) {
        if(markSync){
            markSync = false;
            imBtnSync.setColorFilter(GRAY);
        } else {
            imBtnSync.setColorFilter(getColor(R.color.colorKhaki));
            markSync = true;
        }
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

@Override
protected void onDestroy() {
        super.onDestroy();
        turnOffFlash();
        handler.removeCallbacksAndMessages(null); // Это закрывает postDelayed(()
        }


@Override  // Возврат на предыдущую активити через кнпку "назад"
public void onBackPressed() {
        super.onBackPressed();
        turnOffFlash();
        handler.removeCallbacksAndMessages(null); // Это закрывает postDelayed(()
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        }



}