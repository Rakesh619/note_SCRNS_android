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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note_scrns_android.Adapter.notes_Adapter;
import com.note_scrns_android.Models.Notes;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView drawer_txt,new_note,txt_title;
    ImageView search_icon;
    EditText search;
    RecyclerView recyclerView;
    notes_Adapter notesAdapter;
    NotesDatabase notesDatabase;
    List<Notes> listNotes;
    LinearLayout itemlayout;
    RelativeLayout no_note;

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
        itemlayout=(LinearLayout) findViewById(R.id.item_layout);
        no_note=(RelativeLayout) findViewById(R.id.no_notes);

        drawer_txt.setVisibility(View.GONE);
        new_note.setText("New");
        txt_title.setText("Notes");
        notesAdapter=new notes_Adapter();
        notesDatabase = NotesDatabase.getInstance(MainActivity.this);
        listNotes =  NotesDatabase.getInstance(MainActivity.this).getNoteDao().getAll();
        if(listNotes.size()==0){
            no_note.setVisibility(View.VISIBLE);
            itemlayout.setVisibility(View.GONE);
        }else {
            no_note.setVisibility(View.GONE);
            itemlayout.setVisibility(View.VISIBLE);
        }

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
                filter(s.toString());

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(notesAdapter);
    }
}