package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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