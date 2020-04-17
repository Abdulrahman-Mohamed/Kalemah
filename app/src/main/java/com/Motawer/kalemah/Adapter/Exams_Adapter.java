package com.Motawer.kalemah.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Models.ExamsItems;
import com.Motawer.kalemah.R;

import java.util.ArrayList;
import java.util.List;

public class Exams_Adapter extends RecyclerView.Adapter<Exams_Adapter.Exams_ViewHolder> {
    private List<ExamsItems> itemsList = new ArrayList<ExamsItems>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Exams_Adapter(List<ExamsItems> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public Exams_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_item_exams, parent, false);
        return new Exams_ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Exams_ViewHolder holder, int position)
    {
        ExamsItems examsItems=itemsList.get(position);
        holder.textView.setText(examsItems.getTextView().toString());
        holder.imageView.setImageResource(examsItems.getImageView());
        holder.upon.setImageResource(examsItems.getImageUpon());


    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
    public ExamsItems getItemAt(int position) {
        return itemsList.get(position);
    }

    class Exams_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,upon;
        TextView textView;
        public Exams_ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_card);
            upon=itemView.findViewById(R.id.image_upon);
            textView=itemView.findViewById(R.id.textview_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }}
