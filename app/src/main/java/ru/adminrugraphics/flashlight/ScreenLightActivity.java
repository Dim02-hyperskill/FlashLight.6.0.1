package ru.adminrugraphics.flashlight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenLightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_light);

        // Это чтобы экран не гас
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // это скрывает статус бар
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Это для максимальной яркости экрана
        WindowManager.LayoutParams layoutParam=getWindow().getAttributes();
        layoutParam .screenBrightness=1.0f; //значения от 0,0 до 1,0
        getWindow().setAttributes(layoutParam);
    }


    @Override  // Возврат на предыдущую активити через кнпку "назад"
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
