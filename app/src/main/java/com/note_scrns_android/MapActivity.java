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
import com.note_scrns_android.Models.SubjectPojo;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    @BindView(R.id.new_note)
    ImageView new_note;

    @BindView(R.id.drawer_icon)
    TextView drawer_txt;

    @BindView(R.id.txt_title)
    TextView txt_title;

    double lat,longi;
    Notes note;
    SubjectPojo sub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setVisibility(View.GONE);
        txt_title.setText("Note on Map");
        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        List<Notes> notes = DatabaseHelper.getInstance(getApplicationContext()).getNoteInterface().getAll();
        int index = getIntent().getIntExtra("selectedIndex",-1);
        if (index != -1){
            note = notes.get(index);
            lat = note.getLatitude();
            longi = note.getLongitude();
            sub = DatabaseHelper.getInstance(this).getSubjectInterface().getSubject(note.getSubject_id_fk()).get(0);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        long millisecond = note.getCreated();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        LatLng loca = new LatLng(lat, longi);
        MarkerOptions options = new MarkerOptions().position(loca)
                .title(note.getTitle())
                .snippet("Desc " + note.getDescription()+" Sub "+sub.getSubject_name() + " Date "+dateString);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loca));
    }
}