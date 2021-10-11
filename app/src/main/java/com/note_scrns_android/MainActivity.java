package com.note_scrns_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.note_scrns_android.Adapter.noteslist_Adapter;
import com.note_scrns_android.Models.Notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_icon)
    TextView drawer_txt;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.new_note)
    ImageView new_note;
    @BindView(R.id.note_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.item_layout)
    LinearLayout  itemlayout;
    @BindView(R.id.no_notes)
    RelativeLayout no_note;
    @BindView(R.id.search_txt)
    EditText search;
    @BindView(R.id.search_icon)
    ImageView search_icon;
    List<Notes> listNotes;
    noteslist_Adapter notesAdapter;
    DatabaseHelper databaseHelper;
    Boolean isSortTitleAc = false;
    Boolean isSortDateAc = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Sort");
        new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_create_new_folder_24));
        txt_title.setText("Notes");
        databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        listNotes =  DatabaseHelper.getInstance(MainActivity.this).getNoteInterface().getAll();
        if(listNotes.size()==0){
            no_note.setVisibility(View.VISIBLE);
            itemlayout.setVisibility(View.GONE);
        }else {
            no_note.setVisibility(View.GONE);
            itemlayout.setVisibility(View.VISIBLE);
        }

        //search filter
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
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(notesAdapter);
    }

    @OnClick(R.id.drawer_icon)
    public void Sorting(){
        PopupMenu popup = new PopupMenu(MainActivity.this, drawer_txt);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().equals("By Date")){

                    if(isSortDateAc) {
                        isSortDateAc = false;
                        Toast.makeText(getApplicationContext(),"Descending Order Date",Toast.LENGTH_SHORT).show();
                        Collections.sort(listNotes, new Comparator<Notes>(){
                            DateFormat f = new SimpleDateFormat("MM/dd/yyyy");

                            public int compare(Notes obj1, Notes obj2) {
                                // ## Sorting Datewise order
                                if(obj1.getCreated() > obj2.getCreated()) return 1;
                                else if(obj1.getCreated() < obj2.getCreated()) return -1;
                                else return 0;
                            }
                        });

                    }
                    else{
                        isSortDateAc = true;
                        Toast.makeText(getApplicationContext(),"Ascending Order Date",Toast.LENGTH_SHORT).show();
                        Collections.sort(listNotes, new Comparator<Notes>(){
                            DateFormat f = new SimpleDateFormat("MM/dd/yyyy");

                            public int compare(Notes obj1, Notes obj2) {
                                // ## Sorting Datewise order
                                if(obj1.getCreated() < obj2.getCreated()) return 1;
                                else if(obj1.getCreated() > obj2.getCreated()) return -1;
                                else return 0;
                            }
                        });
                    }

                }else {
                    //sorting functionality
                    if(isSortTitleAc){
                        isSortTitleAc = false;
                        Toast.makeText(getApplicationContext(),"Descending Order",Toast.LENGTH_SHORT).show();
                        Collections.sort(listNotes, new Comparator<Notes>(){
                            public int compare(Notes obj1, Notes obj2) {
                                // ## Descending order
                                return obj2.getTitle().compareToIgnoreCase(obj1.getTitle()); // To compare string values
                                // return Integer.valueOf(obj1.getId()).compareTo(obj2.getId()); // To compare integer values

                            }
                        });
                    }
                    else{
                        isSortTitleAc = true;
                        Toast.makeText(getApplicationContext(),"Ascending Order",Toast.LENGTH_SHORT).show();

                        Collections.sort(listNotes, new Comparator<Notes>(){
                            public int compare(Notes obj1, Notes obj2) {
                                // ## Ascending order
                                return obj1.getTitle().compareToIgnoreCase(obj2.getTitle()); // To compare string values
                                // return Integer.valueOf(obj1.getId()).compareTo(obj2.getId()); // To compare integer values

                            }
                        });
                    }



                }
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;
            }
        });
        popup.show();
    }

    @OnClick(R.id.new_note)
    public void Click(){
        Intent i=new Intent(getApplicationContext(),NewView_noteActivity.class);
        i.putExtra("from","new");
        startActivity(i);
    }
    //Searching particular note with alphabets
    private void search_note(String text) {
        listNotes =  DatabaseHelper.getInstance(MainActivity.this).getNoteInterface().getAll();
        List<Notes> temp = new ArrayList();
        for (Notes n :listNotes) {
            if(n.getTitle().toLowerCase().contains(text.toLowerCase()) || n.getDescription().toLowerCase().contains(text.toLowerCase())){
                temp.add(n);
            }
        }
        notesAdapter.updateList(temp);

    }
}