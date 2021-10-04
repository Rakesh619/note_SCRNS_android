package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.note_scrns_android.Models.Notes;
import com.note_scrns_android.Models.Subjects;

import java.util.Date;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    TextView drawer_txt,new_note,txt_title;
    double lat,longi;
    Notes note;
    Subjects sub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(TextView)findViewById(R.id.new_note);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setVisibility(View.GONE);
        txt_title=(TextView)findViewById(R.id.txt_title);
        txt_title.setText("Note on Map");
        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        Intent i = new Intent();
        List<Notes> notes = NotesDatabase.getInstance(getApplicationContext()).getNoteDao().getAll();
        int index = getIntent().getIntExtra("selectedIndex",-1);
        if (index != -1){
            note = notes.get(index);
            lat = note.getLatitude();
            longi = note.getLongitude();
            sub = NotesDatabase.getInstance(this).getSubjectDao().getSubject(note.getSubject_id_fk()).get(0);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        long millisecond = note.getCreated();
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        mMap = googleMap;
        LatLng sydney = new LatLng(lat, longi);
        MarkerOptions options = new MarkerOptions().position(sydney)
                .title(note.getTitle())
                .snippet("Desc: " + note.getDescription()+" Subject: "+sub.getSubject_name() + " Date Created: "+dateString);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}