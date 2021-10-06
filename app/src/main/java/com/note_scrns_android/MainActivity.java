package com.note_scrns_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.note_scrns_android.Adapter.noteslist_Adapter;
import com.note_scrns_android.Models.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView drawer_txt,txt_title;
    ImageView new_note;
    ImageView search_icon;
    EditText search;
    RecyclerView recyclerView;
    noteslist_Adapter notesAdapter;
    DatabaseHelper databaseHelper;
    List<Notes> listNotes;
    LinearLayout itemlayout;
    RelativeLayout no_note;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        txt_title=(TextView)findViewById(R.id.txt_title);
        new_note=(ImageView)findViewById(R.id.new_note);
        recyclerView=(RecyclerView) findViewById(R.id.note_recycler);
        itemlayout=(LinearLayout) findViewById(R.id.item_layout);
        no_note=(RelativeLayout) findViewById(R.id.no_notes);
        search=(EditText) findViewById(R.id.search_txt);
        search_icon=(ImageView) findViewById(R.id.search_icon);

        drawer_txt.setVisibility(View.GONE);
        new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_more_vert_24));
        txt_title.setText("All Notes");
        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        listNotes =  DatabaseHelper.getInstance(MainActivity.this).getNoteInterface().getAll();
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
                Intent i=new Intent(getApplicationContext(), NewView_noteActivity.class);
                i.putExtra("from","new");
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
                search_note(s.toString());

            }
        });

        notesAdapter = new noteslist_Adapter(this,listNotes, DatabaseHelper.getInstance(MainActivity.this).getSubjectInterface().getAll()) {
            @Override
            public void deletenote(final int pos) {

                final AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater)getApplicationContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.delete_dialog, null);
                mainDialog.setView(dialogView);

                final Button cancel = (Button) dialogView.findViewById(R.id.cancel);
                final Button save = (Button) dialogView.findViewById(R.id.save);
                final  ImageView cross=(ImageView) dialogView.findViewById(R.id.cross);
                final AlertDialog alertDialog = mainDialog.create();
                alertDialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHelper.getNoteInterface().delete(listNotes.get(pos));
                        listNotes.remove(pos);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                });
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        };
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(notesAdapter);
    }
    private void search_note(String text) {
        listNotes =  DatabaseHelper.getInstance(MainActivity.this).getNoteInterface().getAll();
        List<Notes> temp = new ArrayList();
        for (Notes n :listNotes) {
            if(n.getTitle().toLowerCase().contains(text.toLowerCase()) || n.getDescription().toLowerCase().contains(text.toLowerCase())){
                temp.add(n);
            }
        }
        notesAdapter.notes = temp;
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}