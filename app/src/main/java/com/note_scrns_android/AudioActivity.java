package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AudioActivity extends AppCompatActivity {
    TextView drawer_txt,new_note,txt_title;
    Button record,play,stop,choose;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    public String path;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        record = (Button) findViewById(R.id.audioRecord);
        play = (Button) findViewById(R.id.audioPlay);
        stop = (Button) findViewById(R.id.audioStop);
        choose = (Button) findViewById(R.id.audioSelect);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(TextView)findViewById(R.id.new_note);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setVisibility(View.VISIBLE);
        new_note.setText("Save");
        txt_title=(TextView)findViewById(R.id.txt_title);
        txt_title.setText("Recorder");
        path=  "";
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}