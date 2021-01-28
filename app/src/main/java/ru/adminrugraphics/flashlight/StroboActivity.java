package ru.adminrugraphics.flashlight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;

public class StroboActivity extends AppCompatActivity {
    //region Widgets & Variable
    boolean mark = true;
    Button btn1;
    EditText edText1, edText2, edText3, edTextSecond1, edTextSecond2, edTextSecond3;
    SeekBar seekBar1, seekBar2, seekBar3, seekBarSecond1, seekBarSecond2, seekBarSecond3;
    CountDownTimer myCDR_1, myCDR_2, myCDR_3, myCDR_4;
    int n = 0, n_o_f_F, n_o_f_S;
    int number_of_flashes_First = 1, flash_duration_First = 2, pause_duration_First = 3;
    int number_of_flashes_Second = 4, flash_duration_Second = 5, pause_duration_Second = 6;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strobo);

        //region Find View By Id
        btn1 = findViewById(R.id.btn1);
        btn1.setBackgroundColor(Color.GRAY);
        edText1 = findViewById(R.id.edText1);
        edText1.setText(String.valueOf(number_of_flashes_First));
        edText2 = findViewById(R.id.edText2);
        edText2.setText(String.valueOf(flash_duration_First));
        edText3 = findViewById(R.id.edText3);
        edText3.setText(String.valueOf(pause_duration_First));
        edTextSecond1 = findViewById(R.id.edTextSecond1);
        edTextSecond1.setText(String.valueOf(number_of_flashes_Second));
        edTextSecond2 = findViewById(R.id.edTextSecond2);
        edTextSecond2.setText(String.valueOf(flash_duration_Second));
        edTextSecond3 = findViewById(R.id.edTextSecond3);
        edTextSecond3.setText(String.valueOf(pause_duration_Second));
       // seekBar1 = findViewById(R.id.seekBar1);
       // seekBar1.setProgress(number_of_flashes_First);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar2.setProgress(flash_duration_First);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar3.setProgress(pause_duration_First);
        seekBarSecond1 = findViewById(R.id.seekBarSecond1);
        seekBarSecond1.setProgress(number_of_flashes_Second);
        seekBarSecond2 = findViewById(R.id.seekBarSecond2);
        seekBarSecond2.setProgress(flash_duration_Second );
        seekBarSecond3 = findViewById(R.id.seekBarSecond3);
        seekBarSecond3.setProgress(pause_duration_Second);
        // endregion

        //region Number of Flashes First
         edText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edText1.getText().toString().equals("")) {
                    int a = Integer.parseInt(edText1.getText().toString()); // Get value from EditText into variable
                    if(a == 0){
                        number_of_flashes_First = 999999999;
                    } else  number_of_flashes_First = a;

                } else {

                }
                edText1.setSelection(edText1.getText().length()); //
            }
        });
        //endregion

        //region Flash Duration First
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                flash_duration_First = progress;
                edText2.setText(MessageFormat.format("{0}", flash_duration_First));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        edText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edText2.getText().toString().equals("")) {
                    flash_duration_First = Integer.parseInt(edText2.getText().toString()); // Get value from EditText into variable
                    seekBar2.setProgress(flash_duration_First);
                }
                edText2.setSelection(edText2.getText().length()); //
            }
        });
        //endregion

        //region pause Duration First
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pause_duration_First = progress;
                edText3.setText(MessageFormat.format("{0}", pause_duration_First));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        edText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edText3.getText().toString().equals("")) {
                    pause_duration_First = Integer.parseInt(edText3.getText().toString()); // Get value from EditText into variable
                    seekBar3.setProgress(pause_duration_First);
                }
                edText3.setSelection(edText3.getText().length()); //
            }
        });
        //endregion

        //region Number of Flashes Second
        seekBarSecond1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                number_of_flashes_Second = progress;
                edTextSecond1.setText(MessageFormat.format("{0}", number_of_flashes_Second));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        edTextSecond1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edTextSecond1.getText().toString().equals("")) {
                    number_of_flashes_Second = Integer.parseInt(edTextSecond1.getText().toString()); // Get value from EditText into variable
                    seekBarSecond1.setProgress(number_of_flashes_Second);
                }
                edTextSecond1.setSelection(edTextSecond1.getText().length()); //
            }
        });
        //endregion

        //region Flash Duration Second
        seekBarSecond2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                flash_duration_Second = progress;
                edTextSecond2.setText(MessageFormat.format("{0}", flash_duration_Second));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        edTextSecond2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edTextSecond2.getText().toString().equals("")) {
                    flash_duration_Second = Integer.parseInt(edTextSecond2.getText().toString()); // Get value from EditText into variable
                    seekBarSecond2.setProgress(flash_duration_Second);
                }
                edTextSecond2.setSelection(edTextSecond2.getText().length()); //
            }
        });
        //endregion

        //region pause Duration Second
        seekBarSecond3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pause_duration_Second = progress;
                edTextSecond3.setText(MessageFormat.format("{0}", pause_duration_Second));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });

        edTextSecond3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!edTextSecond3.getText().toString().equals("")) {
                    pause_duration_Second = Integer.parseInt(edTextSecond3.getText().toString()); // Get value from EditText into variable
                    seekBarSecond3.setProgress(pause_duration_Second);
                }
                edTextSecond3.setSelection(edTextSecond3.getText().length()); //
            }
        });
        //endregion
    }

    public void onClickBtn1(View view) {
        if(mark){
            n_o_f_F = number_of_flashes_First;
            n_o_f_S = number_of_flashes_Second;
            myCDR_1 = new MyCDT_1(flash_duration_First*100, 100); // flash first
            myCDR_2 = new MyCDT_2(pause_duration_First*100, 100); // pause first
            myCDR_3 = new MyCDT_3(flash_duration_Second*100, 100); // flash second
            myCDR_4 = new MyCDT_4(pause_duration_Second*100, 100); // pause second
            myCDR_2.start();
            mark = false;
        } else {
            mark = true;
            allTimerCancel();
            turnOffFlash();
            btn1.setBackgroundColor(Color.GRAY);
            n=0;
        }
    }

    //region Flash Logic (Timer)
    private class MyCDT_1 extends CountDownTimer {
        MyCDT_1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);}
        @Override
        public void onTick(long millisUntilFinished) {}
        @Override
        public void onFinish() {
            turnOffFlash();
            btn1.setBackgroundColor(Color.GRAY);
            myCDR_2.start();
            myCDR_1.cancel();
        }
    }
    private class MyCDT_2 extends CountDownTimer {
        MyCDT_2(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);}
        @Override
        public void onTick(long millisUntilFinished) {}
        @Override
        public void onFinish() {
            myCDR_2.cancel();
            if (n < n_o_f_F & pause_duration_First !=0) {
                turnOnFlash();
                btn1.setBackgroundColor(Color.RED);
                myCDR_1.start();
                myCDR_2.cancel();
                n++;
            } else {
                myCDR_4.start();
                n=0;
                myCDR_2.cancel();
            }
        }
    }
    private class MyCDT_3 extends CountDownTimer {
        MyCDT_3(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);}
        @Override
        public void onTick(long millisUntilFinished) {}
        @Override
        public void onFinish() {
            turnOffFlash();
            btn1.setBackgroundColor(Color.GRAY);
            myCDR_4.start();
            myCDR_3.cancel();
        }
    }
    private class MyCDT_4 extends CountDownTimer {
        MyCDT_4(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);}
        @Override
        public void onTick(long millisUntilFinished) {}
        @Override
        public void onFinish() {
            myCDR_2.cancel();
            if (n < n_o_f_S & pause_duration_Second !=0) {
                turnOnFlash();
                btn1.setBackgroundColor(Color.CYAN);
                myCDR_3.start();
                myCDR_4.cancel();
                n++;
            } else {
                allTimerCancel();
                turnOffFlash();
                btn1.setBackgroundColor(Color.GRAY);
                n=0;
                mark = true;
            }
        }
    }
    //endregion

    //region Включение/отключение камеры
    public void turnOffFlash() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, false);   //Turn OFF
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void turnOnFlash() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = camManager.getCameraIdList()[0];
            camManager.setTorchMode(cameraId, true);   //Turn ON
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    //endregion


/*    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
        allTimerCancel();
    }*/

/*    @Override
    protected void onStop() {
        super.onStop();
        turnOffFlash();
        allTimerCancel();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mark){  // это чтобы при неработающем таймере не было ошибки при его отключении
            turnOffFlash();
            allTimerCancel();
        }
    }

    public void allTimerCancel() {
            myCDR_1.cancel();
            myCDR_2.cancel();
            myCDR_3.cancel();
            myCDR_4.cancel();
    }

    @Override  // Возврат на предыдущую активити через кнпку "назад"
    public void onBackPressed() {
        super.onBackPressed();
        if(!mark){ // это чтобы при неработающем таймере не было ошибки при его отключении
            turnOffFlash();
            allTimerCancel();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}