package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AudioActivity extends AppCompatActivity {
    TextView drawer_txt,new_note,txt_title;
    Button record,play,stop,choose;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    public String path;
    MediaRecorder rec = new MediaRecorder();
    MediaPlayer mp = new MediaPlayer();

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
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
            }
        });
        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //Subjects subject =  list.get(i);
                intent.putExtra("audio", path);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        record.setTag("record");
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record.getTag() == "record"){
                    try {
                        start();
                        record.setTag("stop");
                        record.setText("Stop");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    stop();
                    record.setText("Record");
                    record.setTag("record");
                }

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playOrStopRecording(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void start() throws IOException {
        verifyStoragePermissions(this);
        String file_path=getApplicationContext().getFilesDir().getPath();

        File file= new File(file_path);

        Long date=new Date().getTime();
        Date current_time = new Date(Long.valueOf(date));

        rec.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        rec.setAudioChannels(1);
        rec.setAudioSamplingRate(8000);
        rec.setAudioEncodingBitRate(44100);
        rec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if (!file.exists()){
            file.mkdirs();
        }

        path =file+"/sss.3gp";
        rec.setOutputFile(path);

        try {
            rec.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Sorry! file creation failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
        rec.start();




    }

    public void stop() {
        rec.stop();
        rec.reset();

    }

    public void playOrStopRecording(String path) throws IOException {
        if(mp.isPlaying()){
            mp.stop();
        }
        else{
            mp = new MediaPlayer();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mp.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
            } else {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            try {
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mp.setDataSource(path);
                mp.prepareAsync();
                mp.setVolume(10, 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}