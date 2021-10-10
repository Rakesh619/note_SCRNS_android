package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.note_scrns_android.Adapter.subjectlist_Adapter;
import com.note_scrns_android.Models.SubjectPojo;

import java.util.List;

public class NotesSubjectActivity extends AppCompatActivity {
    TextView drawer_txt,txt_title;
    ImageView new_note;
    RecyclerView recyclerView;
    subjectlist_Adapter madapter;
    List<SubjectPojo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesubject);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(ImageView)findViewById(R.id.new_note);
        txt_title=(TextView)findViewById(R.id.txt_title);

        recyclerView=(RecyclerView) findViewById(R.id.note_recycler);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_create_new_folder_24));
        txt_title.setText("Subjects");
        list = DatabaseHelper.getInstance(NotesSubjectActivity.this).getSubjectInterface().getAll();

        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mainDialog = new AlertDialog.Builder(NotesSubjectActivity.this);
                LayoutInflater inflater = (LayoutInflater)getApplicationContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.subject_dialog, null);
                mainDialog.setView(dialogView);

                final Button save = (Button) dialogView.findViewById(R.id.save);
                final ImageView cross=(ImageView) dialogView.findViewById(R.id.cross);
                final EditText sub = (EditText) dialogView.findViewById(R.id.sub_txt);
                final AlertDialog alertDialog = mainDialog.create();
                alertDialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //save item
                        if(sub.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),"Please enter Subject",Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }else {
                            SubjectPojo subject = new SubjectPojo(sub.getText().toString());

                            DatabaseHelper.getInstance(NotesSubjectActivity.this).getSubjectInterface().insert(subject);
                            madapter.list.clear();
                            madapter.list.addAll(DatabaseHelper.getInstance(NotesSubjectActivity.this).getSubjectInterface().getAll());
                            recyclerView.getAdapter().notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    }
                });
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
                madapter = new subjectlist_Adapter(NotesSubjectActivity.this,list) {
                    @Override
                    public void deleteSubject(final int pos) {

                        final AlertDialog.Builder mainDialog = new AlertDialog.Builder(NotesSubjectActivity.this);
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.delete_dialog, null);
                        mainDialog.setView(dialogView);

                        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);
                        final Button save = (Button) dialogView.findViewById(R.id.save);
                        final ImageView cross=(ImageView) dialogView.findViewById(R.id.cross);
                        final TextView cat_name=(TextView)dialogView.findViewById(R.id.cat_name);
                        final AlertDialog alertDialog = mainDialog.create();
                        alertDialog.show();
                        cat_name.setText("You want to delete this subject?");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //delete item
                                DatabaseHelper.getInstance(NotesSubjectActivity.this).getSubjectInterface().delete( list.get(pos));
                                list.remove(pos);
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

                    @Override
                    public void editSubject(final int i) {

                        final AlertDialog.Builder mainDialog = new AlertDialog.Builder(NotesSubjectActivity.this);
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialogView = inflater.inflate(R.layout.subject_dialog, null);
                        mainDialog.setView(dialogView);

                        final Button save = (Button) dialogView.findViewById(R.id.save);
                        final ImageView cross=(ImageView) dialogView.findViewById(R.id.cross);
                        final EditText sub = dialogView.findViewById(R.id.sub_txt);
                        sub.setText(list.get(i).getSubject_name());
                        final AlertDialog alertDialog = mainDialog.create();
                        alertDialog.show();

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //save item
                                SubjectPojo subject =  list.get(i);
                                subject.setSubject_name(sub.getText().toString());
                                DatabaseHelper.getInstance(NotesSubjectActivity.this).getSubjectInterface().update(subject);
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

                    @Override
                    public void selectSubject(int i) {
                        Intent intent = new Intent();
                        SubjectPojo subject =  list.get(i);
                        intent.putExtra("data", subject.getSubject_id());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                };
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(madapter);


    }
}