package it.a4smart.vate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import it.a4smart.vate.common.TTS;

public class PreloadActivity extends AppCompatActivity implements Runnable {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TTS.createNewInstance(getApplicationContext(), this);
    }

    @Override
    public void run() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
