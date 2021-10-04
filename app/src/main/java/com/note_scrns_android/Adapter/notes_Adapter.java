package com.note_scrns_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note_scrns_android.Models.Notes;
import com.note_scrns_android.Models.Subjects;
import com.note_scrns_android.New_noteActivity;
import com.note_scrns_android.R;

import java.util.List;

public class notes_Adapter extends RecyclerView.Adapter<notes_Adapter.ViewHolder> {
    Context context;
    public List<Notes> notes;
    public List<Subjects> subjects;

    public notes_Adapter(Context context, List<Notes> notes, List<Subjects> subjects) {
        this.context = context;
        this.notes = notes;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  itemview= LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,title,description,txtSubjectItem;
        ImageView note_img,delete,map;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.date);
            title=(TextView)itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.desc);
            note_img=(ImageView) itemView.findViewById(R.id.note_image);
            map=(ImageView) itemView.findViewById(R.id.map);
            delete=(ImageView) itemView.findViewById(R.id.delete);
            txtSubjectItem =  (TextView)itemView.findViewById(R.id.txtSubjectItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i=new Intent(context, New_noteActivity.class);
            i.putExtra("from","update");
            i.putExtra("selectedIndex",getAdapterPosition());
            context.startActivity(i);
        }
    }
}
