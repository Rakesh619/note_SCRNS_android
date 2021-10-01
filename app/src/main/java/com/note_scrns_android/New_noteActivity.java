package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class New_noteActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 102;
    private static final int GALLERY_REQUEST = 101;
    TextView drawer_txt,new_note,txt_title,deal_txt;
    EditText title,description;
    Button record,subject;
    RelativeLayout share_layout;
    ImageView share_pic,deal_icon;
    FrameLayout share_frame;
    String path;
    private static final int REQUEST_CODE = 1;
    String pathAudio;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(TextView)findViewById(R.id.new_note);
        title=(EditText) findViewById(R.id.new_title);
        txt_title=(TextView)findViewById(R.id.txt_title);
        deal_txt=(TextView)findViewById(R.id.deal_txt);
        deal_icon=(ImageView) findViewById(R.id.deal_icon);
        description=(EditText) findViewById(R.id.description);
        record=(Button) findViewById(R.id.record_audio);
        subject=(Button) findViewById(R.id.select_subject);
        share_layout=(RelativeLayout) findViewById(R.id.share_layout);
        share_pic=(ImageView) findViewById(R.id.share_pic);
        share_frame=(FrameLayout) findViewById(R.id.share_frame);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setText("Save");
        txt_title.setText("New Note");
        share_layout.setVisibility(View.VISIBLE);
        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        gallery();

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            image = (Bitmap) data.getExtras().get("data");
            share_pic.setImageBitmap(image);
            deal_icon.setVisibility(View.GONE);
            deal_txt.setVisibility(View.GONE);
        }
        else if(reqCode == 112 && resultCode == RESULT_OK){
            pathAudio = data.getStringExtra("audio");
        }
        else if(reqCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                share_pic.setImageBitmap(image);
                deal_icon.setVisibility(View.GONE);
                deal_txt.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        else if (reqCode == 11){
            if(resultCode == RESULT_OK) {
                int subjectID = data.getIntExtra("data",-1);
                if(subjectID != -1){
//                    for (Subjects sub: notesDatabase.getSubjectDao().getAll()) {
//                        if(sub.getSubject_id() == subjectID){
//                            selectedSubject = sub;
//                            subject.setText(sub.getSubject_name());
//                        }
//                    }

                }
            }
        }
        else{

        }
    }
    public void gallery() {

        final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Your Option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result= Utility.checkPermission(ShareDeal.this);

                if (items[item].equals("Take Photo")) {

                    CaptureImage();
                } else if (items[item].equals("Choose from Library")) {
                    OpenGallery();
                } else if(items[item].equals("Cancel")){
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