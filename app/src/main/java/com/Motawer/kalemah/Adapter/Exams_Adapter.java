package com.Motawer.kalemah.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Categories_Activity;
import com.Motawer.kalemah.Models.ExamsItems;
import com.Motawer.kalemah.My_Words_Quizz;
import com.Motawer.kalemah.R;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class Exams_Adapter extends RecyclerView.Adapter<Exams_Adapter.Exams_ViewHolder> {
    private List<ExamsItems> itemsList = new ArrayList<ExamsItems>();
    private OnItemClickListener mListener;
    View view;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Exams_Adapter(List<ExamsItems> itemsList)
    {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public Exams_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext())
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( examsItems.getTextView().equals("Exercise"))
                {
                    Intent intent = new Intent(v.getContext(),Categories_Activity.class);
                    v.getContext().startActivity(intent);

                }
                if ( itemsList.get(position).getTextView().toString().equals("Words"))
                {
                    Intent intent = new Intent(v.getContext(), My_Words_Quizz.class);
                    v.getContext().startActivity(intent);

                }

            }
        });
        holder.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(examsItems.getTextView()).equals("Exercise"))
                {
                    StyleableToast.makeText(view.getContext(), "this allows you to study general words"
                            , Toast.LENGTH_LONG, R.style.toast_help).show();

                }
                if (String.valueOf(examsItems.getTextView()).equals("Words"))
                {
                    StyleableToast.makeText(view.getContext(), "this allows you to study the words that you have inserted manually"
                            , Toast.LENGTH_LONG, R.style.toast_help).show();

                }
            }
        });


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
        ImageButton help;
        CardView cardView;
        TextView textView;
        public Exams_ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_card);
            upon=itemView.findViewById(R.id.image_upon);
            textView=itemView.findViewById(R.id.textview_title);
            help=itemView.findViewById(R.id.help);
            cardView=itemView.findViewById(R.id.card);


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
