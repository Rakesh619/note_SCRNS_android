package com.note_scrns_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.note_scrns_android.Models.SubjectPojo;
import com.note_scrns_android.R;

import java.util.List;

public abstract class subjectlist_Adapter extends RecyclerView.Adapter<subjectlist_Adapter.ViewHolder> {

    public Context context;
    public List<SubjectPojo> list;

    public subjectlist_Adapter(Context context, List<SubjectPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public subjectlist_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  itemview= LayoutInflater.from(context).inflate(R.layout.subject_item,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull subjectlist_Adapter.ViewHolder holder, int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSubject(position);
            }
        });
        holder.subject.setText(list.get(position).getSubject_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView subject;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject=(TextView)itemView.findViewById(R.id.subject_txt);
            delete=(ImageView) itemView.findViewById(R.id.sub_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectSubject(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            editSubject(getAdapterPosition());
            return true;
        }
    }
    public abstract void deleteSubject(int i);
    public abstract void editSubject(int i);
    public abstract void selectSubject(int i);
}
