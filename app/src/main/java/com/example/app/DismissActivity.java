package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public final class DismissActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_dismiss);
        ((TextView) findViewById(R.id.tvFinishYes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                finishAffinity();
            }
        });
        ((TextView) findViewById(R.id.tvFinishNo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Intent intent = new Intent(DismissActivity.this, MainActivity2.class);
                intent.putExtra("flag", "audios");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity2.class));
    }
}