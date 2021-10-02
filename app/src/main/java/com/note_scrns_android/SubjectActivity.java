package com.note_scrns_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectActivity extends AppCompatActivity {
    TextView drawer_txt,new_note,txt_title;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        drawer_txt=(TextView)findViewById(R.id.drawer_icon);
        new_note=(TextView)findViewById(R.id.new_note);
        txt_title=(TextView)findViewById(R.id.txt_title);

        recyclerView=(RecyclerView) findViewById(R.id.note_recycler);
        drawer_txt.setVisibility(View.VISIBLE);
        drawer_txt.setText("Back");
        new_note.setText("New");
        txt_title.setText("Subjects");
        drawer_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        new_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mainDialog = new AlertDialog.Builder(SubjectActivity.this);
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
                        Subjects subject =  new Subjects(sub.getText().toString());

                        NotesDatabase.getInstance(SubjectActivity.this).getSubjectDao().insert(subject);
                        madapter.list.clear();
                        madapter.list.addAll(NotesDatabase.getInstance(SubjectActivity.this).getSubjectDao().getAll());
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
        });
    }
}