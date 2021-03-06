package com.note_scrns_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note_scrns_android.MapActivity;
import com.note_scrns_android.Models.Notes;
import com.note_scrns_android.Models.SubjectPojo;
import com.note_scrns_android.NewView_noteActivity;
import com.note_scrns_android.R;
import com.note_scrns_android.Utils.ImageConverter;

import java.util.Date;
import java.util.List;

public abstract class noteslist_Adapter extends RecyclerView.Adapter<noteslist_Adapter.ViewHolder> {
    Context context;
    public List<Notes> notes;
    public List<SubjectPojo> subjectpojo;

    public noteslist_Adapter(Context context, List<Notes> notes, List<SubjectPojo> subjects) {
        this.context = context;
        this.notes = notes;
        this.subjectpojo = subjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  itemview= LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int pos=holder.getAdapterPosition();
        Log.e("@#@#","get posi"+position);


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletenote(position);
            }
        });

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, MapActivity.class);
                i.putExtra("selectedIndex",position);
                context.startActivity(i);
            }
        });
        holder.title.setText(notes.get(position).getTitle());
        holder.description.setText(notes.get(position).getDescription());
        for(SubjectPojo sub:subjectpojo){
            if(sub.getSubject_id() == notes.get(position).getSubject_id_fk()){
                holder.txtSubjectItem.setText(sub.getSubject_name());
            }
        }

        long millisecond = notes.get(position).getCreated();
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        holder.date.setText(dateString);

        if(notes.get(position).getNote_image() != null){
            holder.note_img.setVisibility(View.VISIBLE);
            Bitmap image = ImageConverter.convertByteArray2Bitmap(notes.get(position).getNote_image());
            if(image != null){
                holder.note_img.setImageBitmap(image);
            }

        }
        else{
            holder.note_img.setVisibility(View.GONE);
            holder.note_img.setImageDrawable(null);
        }

    }

    @Override
    public int getItemCount() {
        return notes.size();
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
            Intent i=new Intent(context, NewView_noteActivity.class);
            i.putExtra("from","update");
            i.putExtra("selectedIndex",getAdapterPosition());
            context.startActivity(i);
        }
    }

    public void updateList(List<Notes> list){
        notes = list;
        notifyDataSetChanged();
    }
    public abstract void deletenote(int i);
}
