package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.note_scrns_android.Adapter.notes_Adapter;

public class MainActivity extends AppCompatActivity {
    TextView drawer_txt,new_note,txt_title;
    ImageView search_icon;
    EditText search;
    RecyclerView recyclerView;
    notes_Adapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        txt_title=(TextView)findViewById(R.id.txt_title);
        new_note=(TextView)findViewById(R.id.new_note);
        search=(EditText) findViewById(R.id.search_txt);
        search_icon=(ImageView) findViewById(R.id.search_icon);
        recyclerView=(RecyclerView) findViewById(R.id.note_recycler);
        drawer_txt.setVisibility(View.GONE);
        new_note.setText("New");
        txt_title.setText("Notes");
        notesAdapter=new notes_Adapter();

        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),New_noteActivity.class);
                startActivity(i);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(notesAdapter);
    }
}