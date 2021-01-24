package ru.adminrugraphics.flashlight;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            String version = pInfo.versionName;
            TextView tvVersion = (TextView) findViewById(R.id.tv_get_version_app);
            String a = (String) getText(R.string.get_version);
            tvVersion.setText(String.format("%s %s", a, version));
        }


        TextView tvLink = findViewById(R.id.tv_link);
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());
        tvLink.setText( Html.fromHtml(getResources().getString(R.string.link)) );


        TextView tvDescription = findViewById(R.id.tv_description);
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        tvDescription.setText( Html.fromHtml(getResources().getString(R.string.description_user_manual)) );





    }
}
