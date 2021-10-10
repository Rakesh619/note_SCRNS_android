package com.note_scrns_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RecorderAudioActivity extends AppCompatActivity {
    TextView drawer_txt,txt_title;
    Button record,play,stop,choose;
    ImageView new_note;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    public String path,from="",audio_path="";
    MediaRecorder rec = new MediaRecorder();
    MediaPlayer mp = new MediaPlayer();

    private final String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_audio);
        record = (Button) findViewById(R.id.audioRecord);
        play = (Button) findViewById(R.id.audioPlay);
        stop = (Button) findViewById(R.id.audioStop);
        choose = (Button) findViewById(R.id.audioSelect);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(ImageView)findViewById(R.id.new_note);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setVisibility(View.VISIBLE);
        new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_done_all_24));
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
        Intent i=getIntent();
        from=i.getStringExtra("from");
        audio_path=i.getStringExtra("path");

        if(from.equalsIgnoreCase("new")){
            choose.setVisibility(View.VISIBLE);
            record.setVisibility(View.VISIBLE);
            new_note.setVisibility(View.VISIBLE);
        }else {
            choose.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            new_note.setVisibility(View.GONE);

        }
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
                if(from.equalsIgnoreCase("new")) {
                    Intent intent = new Intent();
                    //Subjects subject =  list.get(i);
                    intent.putExtra("audio", path);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Intent intent = new Intent();
                    //Subjects subject =  list.get(i);
                    intent.putExtra("audio", audio_path);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(from.equalsIgnoreCase("new")) {
                        playOrStopRecording(path);
                    }else {
                        playOrStopRecording(audio_path);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){

                    mp.stop();
                }
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

    }
    public void playOrStopRecording(String path) throws IOException {
        if(mp.isPlaying()){

            mp.stop();
        }
        else{

            mp = new MediaPlayer();

            try {
                mp.setDataSource(path);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() throws IOException {
        verifyStoragePermissions(this);
        String file_path=getApplicationContext().getFilesDir().getPath();

        Long date=new Date().getTime();
        Date current_time = new Date(Long.valueOf(date));
        String abc = getFilesDir().getAbsolutePath();
        File file= new File(abc);
        path =file+"/audio.m4a";
        rec.setAudioSource(MediaRecorder.AudioSource.MIC);
        rec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        rec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        rec.setOutputFile(path);
        rec.prepare();
        rec.start();



    }

    public void stop() {
        rec.stop();
        rec.reset();
        rec.release();
        Toast.makeText(this,path,Toast.LENGTH_LONG).show();

    }
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    this.permissions,
                    REQUEST_RECORD_AUDIO_PERMISSION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ){
            Toast.makeText(getApplicationContext(),"Give permission to use mic",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                path = uri.getPath();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}