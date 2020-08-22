package com.Motawer.kalemah.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Models.ItemExam;
import com.Motawer.kalemah.Quizz_activity;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;

import java.util.ArrayList;
import java.util.List;

public class exerciseAdapter extends RecyclerView.Adapter<exerciseAdapter.ItemsViewHolder> {
    ArrayList<ItemExam> itemExams = new ArrayList<>();
    //    List<Boolean> levelList = new ArrayList<>();
//    int currentCat;
//    int itemExam.getFinalPoints();
//    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<Word> currentList = new ArrayList<>();
    int level;
    //    ArrayList<Integer> size=new ArrayList<>();
    List<Integer> thispoints = new ArrayList<>();
//    String title;
    View view;

    public exerciseAdapter(ArrayList<ItemExam> itemExams) {
        this.itemExams = itemExams;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exams_level_item, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        int p = 0;
        int l = 0;

        ItemExam itemExam = itemExams.get(position);

        holder.title.setText(itemExam.getTitle().toUpperCase());
        if (itemExam.getWordArrayList().size() > 50 && itemExam.getWordArrayList().size() < 150)
            holder.numOfLevels.setText("2");
        if (itemExam.getWordArrayList().size() <= 60)
            holder.numOfLevels.setText("1");
        if (itemExam.getLevelList().size() > itemExam.getIntegers().get(0)) {
            for (int i = 0; i < itemExam.getLevelList().size(); i++) {
                if (itemExam.getLevelList().get(itemExam.getIntegers().get(i))) {
                    l += 1;
                    holder.currentLevel.setText(String.valueOf(l));
                }
            }
        } else {
            holder.progressBar.setProgress(0);
        }
        currentPoints(itemExam.getPoints(),itemExam.getIntegers());
        starshandler(holder);
//
        if (itemExam.getCurrentCat() == 1)
            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_a);
        if (itemExam.getCurrentCat() == 2)
            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_b);
        if (itemExam.getCurrentCat() == 3)
            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_c);
        for (int i = 0; i < itemExam.getIntegers().size(); i++) {
            if (itemExam.getIntegers().size() > 1) {
                p = itemExam.getPoints().get(itemExam.getIntegers().get(i)) + p;
                holder.progressBar.setMax(itemExam.getIntegers().size() * 60);

                holder.progressBar.setProgress(p);
            } else {
                p = itemExam.getPoints().get(itemExam.getIntegers().get(i));
                holder.progressBar.setMax(60);
                holder.progressBar.setProgress(p);
            }

        }

//
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemExam.getIntegers().size() == 1) {
                    if (itemExam.getPoints().get(itemExam.getIntegers().get(0) - 1) < 30) {
                        return;
                    }else
                        {
                            level=itemExam.getIntegers().get(0)+1;
                        }
                }
                Intent intent = new Intent(v.getContext(), Quizz_activity.class);
                if (itemExam.getIntegers().size() > 1) {
                    splitwords(itemExam.getWordArrayList(), itemExam);
                    if (currentList.size() != 0)
                        intent.putExtra("WordsList", currentList);

                } else {
                    intent.putExtra("WordsList", itemExam.getWordArrayList());
                }

                intent.putExtra("Level", level);
                v.getContext().startActivity(intent);
            }

        });


    }

    private void starshandler(ItemsViewHolder holder)
    {

        if (thispoints.size()>1)
        {
            int allpoints=0;
            for (int i=0;i<thispoints.size();i++)
                allpoints+=thispoints.get(i);
            if (allpoints>=((thispoints.size()*60)/3) &&allpoints<((thispoints.size()*60)/2))
            {
                holder.star1.setImageResource(R.drawable.ic_star);
            }
            if (allpoints>=(thispoints.size()*60)/2 &&allpoints<(thispoints.size()*60))
            {
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star3.setImageResource(R.drawable.ic_star);
            }
            if (allpoints==(thispoints.size()*60))
        {
            holder.star1.setImageResource(R.drawable.ic_star);
            holder.star3.setImageResource(R.drawable.ic_star);
            holder.star2.setImageResource(R.drawable.ic_star);
        }
        }else
            {
                int allpoints=thispoints.get(0);

                if (allpoints>=60/3 &&allpoints<40)
                {
                    holder.star1.setImageResource(R.drawable.ic_star);
                }
                if (allpoints>=40 &&allpoints<60)
                {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star);
                }
                if (allpoints==60)
                {holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star);
                }
            }
    }

    private void currentPoints(List<Integer> points, ArrayList<Integer> integers)
    {
        if (integers.size()>1)
        {
            thispoints=new ArrayList<>();
            for (int ip=0;ip<integers.size();ip++)
            {
                thispoints.add(points.get(integers.get(ip)));
            }

        } else
            {    thispoints=new ArrayList<>();

                thispoints.add(points.get(integers.get(0)));
            }
    }

    private void splitwords(ArrayList<Word> wordArrayList, ItemExam itemExam) {
        currentList = new ArrayList<>();

        for (int p1 = 0; p1 < itemExam.getIntegers().size(); p1++)
            if (itemExam.getLevelList().size() > itemExam.getIntegers().get(p1))
                if (itemExam.getLevelList().get(itemExam.getIntegers().get(p1)) != null) {
                    for (int counter = itemExam.getIntegers().get(p1) * 50; counter < itemExam.getSize()
                            .get(itemExam.getIntegers().get(p1)); counter++) {
                        currentList.add(wordArrayList.get(counter));
                        level=itemExam.getIntegers().get(p1)+1;
                    }
                    System.out.println(currentList.size());
                }
    }

    @Override
    public int getItemCount() {
        return itemExams.size();

    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        TextView currentLevel, numOfLevels, title;
        ImageView star1, star2, star3;
        ImageButton help, colorIndecator;
        ProgressBar progressBar;
        CardView cardView;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            currentLevel = itemView.findViewById(R.id.current_level);
            numOfLevels = itemView.findViewById(R.id.number_of_level);
            title = itemView.findViewById(R.id.level_title);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            help = itemView.findViewById(R.id.help);
            cardView = itemView.findViewById(R.id.card);
            colorIndecator = itemView.findViewById(R.id.categories_color);
            progressBar = itemView.findViewById(R.id.progressBar);


        }
    }
}

