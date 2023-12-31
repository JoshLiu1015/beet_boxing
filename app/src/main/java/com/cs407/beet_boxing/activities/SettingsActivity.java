package com.cs407.beet_boxing.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.R;
import com.cs407.beet_boxing.persistence.ConfigData;
import com.cs407.beet_boxing.persistence.PersistentInfo;

public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBarVolume;
    AudioManager audioManager;

    Button backButton;
    Button tutorialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigData config = PersistentInfo.getConfig();
        if (config == null) {
            return;
        }
        setContentView(R.layout.settings_activity);
        backButton = findViewById(R.id.backButton);
        tutorialButton = findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HelpActivity.class));
            finish();
        });
        Button recordingsListButton = findViewById(R.id.recordingsListButton);
        recordingsListButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RecordingsListActivity.class));
            finish();
        });

        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarVolume.setMax(15);
        seekBarVolume.setMin(0);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // get the boolean value passed from ActivityGarden
        Intent intent = getIntent();
        boolean isFromGarden = intent.getBooleanExtra("fromGarden", false);

        // if the setting button was clicked from ActivityGarden
        // go back to ActivityGarden, otherwise, go back to MainActivity
        if (isFromGarden) {
            backButton.setOnClickListener(view -> backToGarden());
        }
        else {
            backButton.setOnClickListener(view -> backToMain());
        }

        seekBarVolume.setProgress(curVolume);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0 );
                config.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*Switch controlSchemeSwitch = findViewById(R.id.switch3);
        controlSchemeSwitch.setChecked(config.getControlScheme() == EnumControlScheme.DRAG);
        controlSchemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            config.setControlScheme(isChecked ? EnumControlScheme.DRAG : EnumControlScheme.TILT);
            Log.i("INFO", "control scheme: " + (isChecked ? "Drag" : "Tilt"));
        });*/
    }

    public void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void backToGarden() {
        Intent intent = new Intent(this, GardenActivity.class);
        startActivity(intent);
        finish();
    }
}