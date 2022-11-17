package com.aice.xfermode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_test);
        Button button = findViewById(R.id.btn_test);
        XModeTestView xModeTestView = findViewById(R.id.xmode_view);
        textView.setText(xModeTestView.getXMode());
        button.setOnClickListener(view -> {
            xModeTestView.changeXMode();
            textView.setText(xModeTestView.getXMode());
        });
    }
}