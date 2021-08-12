package com.play.view.videoplayer.hd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


public class SettingActivity extends AppCompatActivity {
    String package1;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.action_settings));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_ios);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Intent i = getIntent();
        Bundle extras = i.getExtras();

        try {
            package1 = extras.getString("package");
        } catch (Exception ff) {

        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
//        actionBar.setBackgroundDrawable(new ColorDrawable(R.color.colorAccent));
    }

    /*
    @Override
    public void onBackPressed() {
        super.onBackPressed();

         {
            Intent intent = new Intent(this, VideoFolder.class);
            startActivity(intent);
            finish();
        }
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
