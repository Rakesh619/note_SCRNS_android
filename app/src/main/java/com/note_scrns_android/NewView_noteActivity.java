package com.note_scrns_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.note_scrns_android.Models.Notes;
import com.note_scrns_android.Models.SubjectPojo;
import com.note_scrns_android.Utils.ImageConverter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewView_noteActivity extends AppCompatActivity {
    @BindView(R.id.drawer_icon)
    TextView drawer_txt;
    @BindView(R.id.new_note)
    ImageView new_note;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.attachment)
    ImageView attach;
    @BindView(R.id.new_title)
    EditText title;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.select_subject)
    Button subject;
    @BindView(R.id.share_pic)
    ImageView share_pic;
    @BindView(R.id.record_path)
    Button record_path;
    private static final int CAMERA_REQUEST = 102;
    private static final int GALLERY_REQUEST = 101;
    private static final int REQUEST_CODE = 1;
    String pathAudio;
    Bitmap image;
    Location userlocation;
    String from="",audio_path;
    LocationManager locationManager;
    LocationListener locationListener;
    DatabaseHelper databaseHelper;
    SubjectPojo selectedSubject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        attach.setVisibility(View.VISIBLE);
        record_path.setVisibility(View.GONE);
        databaseHelper = DatabaseHelper.getInstance(NewView_noteActivity.this);

        Intent i=getIntent();
        from=i.getStringExtra("from");

        if(from.equalsIgnoreCase("new")){
            share_pic.setVisibility(View.GONE);

            txt_title.setText("New Note");
            new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_done_all_24));
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    userlocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (!hasLocationPermission())
                requestLocationPermission();
            else
                startUpdateLocation();

        }else {
            txt_title.setText("Update Note");
            new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_done_all_24));
            getUpdatedNotes();

        }


    }

    @OnClick(R.id.drawer_icon)
    public void click(){
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

   @OnClick(R.id.new_note)
   public void saveNote(){
       if(from.equalsIgnoreCase("new")) {
           if (CheckValidation()) {
               Notes note;
               if (image != null) {
                   note = new Notes(description.getText().toString(), title.getText().toString(), userlocation.getLatitude(), userlocation.getLongitude(), new Date().getTime(), selectedSubject.getSubject_id(), ImageConverter.convertImage2ByteArray(image), pathAudio);
               } else {
                   note = new Notes(description.getText().toString(), title.getText().toString(), userlocation.getLatitude(), userlocation.getLongitude(), new Date().getTime(), selectedSubject.getSubject_id(), null, pathAudio);
               }
               databaseHelper.getNoteInterface().insert(note);
               drawer_txt.performClick();
           }
       }else {
           if(CheckValidation()) {
               List<Notes> notes = databaseHelper.getNoteInterface().getAll();
               int index = getIntent().getIntExtra("selectedIndex",-1);
               if (index != -1){
                   Notes note = notes.get(index);
                   note.setTitle(title.getText().toString());
                   note.setDescription(description.getText().toString());
                   note.setSubject_id_fk(selectedSubject.getSubject_id());
                   if(image != null){
                       note.setNote_image(ImageConverter.convertImage2ByteArray(image));
                   }if(pathAudio!=null){
                       note.setNote_audio(pathAudio);

                   }
                   databaseHelper.getNoteInterface().update(note);
               }

               drawer_txt.performClick();
           }
       }
   }

   @OnClick(R.id.attachment)
   public void attach(){
       openAttachments();
   }

   @OnClick(R.id.record_path)
   public void audio(){
       Intent i=new Intent(getApplicationContext(),RecorderAudioActivity.class);
       i.putExtra("from","update");
       i.putExtra("path",audio_path);
       startActivityForResult(i,112);
   }

   @OnClick(R.id.select_subject)
   public void selectSubject(){
       Intent i=new Intent(getApplicationContext(), NotesSubjectActivity.class);
       startActivityForResult(i, 11);

   }
    private void getUpdatedNotes() {
        List<Notes> notes = databaseHelper.getNoteInterface().getAll();
        int index = getIntent().getIntExtra("selectedIndex", -1);
        if (index != -1) {
            Notes note = notes.get(index);
            title.setText(note.getTitle());
            description.setText(note.getDescription());
            byte[] data = note.getNote_image();
             audio_path=note.getNote_audio();
            Log.e("@#@#","get path"+audio_path);
            if (data != null) {
                image = ImageConverter.convertByteArray2Bitmap(data);
                share_pic.setImageBitmap(image);
                share_pic.setVisibility(View.VISIBLE);
                record_path.setVisibility(View.GONE);
            }
            if(audio_path!=null){
                record_path.setVisibility(View.VISIBLE);
            }
            SubjectPojo sub = databaseHelper.getSubjectInterface().getSubject(note.getSubject_id_fk()).get(0);
            subject.setText(sub.getSubject_name());
            selectedSubject = sub;

        }
    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

    public boolean CheckValidation(){

        if (title.getText().toString().trim().length() == 0) {
            title.setError("Please enter title");
            title.requestFocus();
            return false;

        }else if(description.getText().toString().trim().length() == 0) {

            description.setError("Please enter description");
            description.requestFocus();
            return false;
        }
        else if(selectedSubject == null){
            Toast.makeText(getApplicationContext(),"Please Select Subject",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            image = (Bitmap) data.getExtras().get("data");
            share_pic.setImageBitmap(image);
            share_pic.setVisibility(View.VISIBLE);
            record_path.setVisibility(View.GONE);
        }
        else if(reqCode == 112 && resultCode == RESULT_OK){
            pathAudio = data.getStringExtra("audio");
            record_path.setVisibility(View.VISIBLE);
           audio_path=pathAudio;


        }
        else if(reqCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                share_pic.setImageBitmap(image);
                share_pic.setVisibility(View.VISIBLE);
                record_path.setVisibility(View.GONE);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        else if (reqCode == 11){
            if(resultCode == RESULT_OK) {
                int subjectID = data.getIntExtra("data",-1);
                if(subjectID != -1){
                    for (SubjectPojo sub: databaseHelper.getSubjectInterface().getAll()) {
                        if(sub.getSubject_id() == subjectID){
                            selectedSubject = sub;
                            subject.setText(sub.getSubject_name());
                        }
                    }

                }
            }
        }
        else{

        }
    }

    public void openAttachments() {

        final CharSequence[] items = { "Camera", "Gallery","Record Audio","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Attachment");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    CaptureImage();
                } else if (items[item].equals("Gallery")) {
                    OpenGallery();
                } else if(items[item].equals("Record Audio")){

                    Intent i=new Intent(getApplicationContext(),RecorderAudioActivity.class);
                    i.putExtra("from","new");
                    i.putExtra("path","");
                    startActivityForResult(i,112);
                }
                else if(items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public void CaptureImage() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void OpenGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }
}