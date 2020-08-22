package com.Motawer.kalemah.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Models.examItemModel;
import com.Motawer.kalemah.Quizz_activity;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class ExamItemsAdapter extends RecyclerView.Adapter<ExamItemsAdapter.ItemsViewHolder> {
    View view;
    List<Integer> points = new ArrayList<>();
    List<Boolean> levelList = new ArrayList<>();
    ArrayList<Word> wordsList = new ArrayList<>();
    ArrayList<examItemModel> Models = new ArrayList<>();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    int allUserPoints = 0;
    int categories_level;
    //int index=1;

    int level;

    public ExamItemsAdapter(ArrayList<examItemModel> Models, int categories_level) {
        this.Models = Models;
        this.categories_level = categories_level;
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

        examItemModel examItemModel = Models.get(position);
        checkpoints(examItemModel, holder);
        holder.title.setText(examItemModel.getTitle());
        setCatigories(holder);
        if (examItemModel.getIndex().size() > 1) {
            for (int i = 1; i <= examItemModel.getIndex().size(); i++) {
                if (allUserPoints > 0) {
                    if (allUserPoints >= i * 60) {
                        //   index=index+1;
                        level = i;
                    }
                } else if (allUserPoints == 0) {
                    level = 1;
                }
            }
            for (int i = 0; i < examItemModel.getIndex().size(); i++) {
                int index = examItemModel.getIndex().get(i);
                checkLevels(index, holder);
            }

        } else if (examItemModel.getIndex().size() == 1) {
            getWords(examItemModel.getIndex().get(0));
        }


    }

    private void checkLevels(int index, ItemsViewHolder holder) {
        myRef.child("UserLevels").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                levelList.add(snapshot.getValue(Boolean.class));
                            if (levelList.get(index).equals(true)) {
                                getWords(index);
                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(v.getContext(), Quizz_activity.class);
                                        intent.putExtra("WordsList", wordsList);
                                        intent.putExtra("Level", level);
                                        v.getContext().startActivity(intent);
                                    }

                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getWords(int level) {
        myRef.child("WordsEng").child(String.valueOf(level)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Word word = snapshot.getValue(Word.class);
                        wordsList.add(word);
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setCatigories(ItemsViewHolder holder) {
        if (categories_level == 1) {
            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StyleableToast.makeText(view.getContext(), " beginners level words "
                            , Toast.LENGTH_LONG, R.style.toast_help).show();
                }
            });

            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_a);
        } else if (categories_level == 2) {
            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StyleableToast.makeText(view.getContext(), "Intermediate level words", Toast.LENGTH_LONG, R.style.toast_help).show();
                }
            });
            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_b);
        } else {
            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StyleableToast.makeText(view.getContext(), "Advanced level words", Toast.LENGTH_LONG, R.style.toast_help).show();
                }
            });
            holder.colorIndecator.setBackgroundResource(R.drawable.level_round_c);
        }

    }

    private void checkpoints(examItemModel examItemModel, @NonNull ItemsViewHolder holder) {
        // to adapter
        myRef.child("UserPoints").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {

                            points.add(dataSnapshot1.getValue(Integer.class));
                    }

//                        if (points.get(pointer) != 0) {
//                    if (examItemModel.getIndex().size() > 1) {
//                        allUserPoints = allUserPoints + points.get(pointer);
//                    } else {
//                        allUserPoints = allUserPoints + points.get(examItemModel.getIndex().get(0));
//
//                    }


                    //  getPoints(points, examItemModel);
                    holder.progressBar.setMax(examItemModel.getIndex().size() * 60);
                    holder.progressBar.setProgress(allUserPoints);
                    setStars(holder, allUserPoints, examItemModel);
                    holder.numOfLevels.setText(String.valueOf(examItemModel.getIndex().size()));
                    for (int levels = 0; levels < examItemModel.getIndex().size(); levels++) {
                        // int counter=1;
                        if (examItemModel.getIndex().size() > 1)
                            if (points.get(examItemModel.getIndex().get(levels)) >= 60) {
                                if (examItemModel.getIndex().size() >= levels + 1)
                                    holder.currentLevel.setText(String.valueOf(examItemModel.getIndex().get(levels + 1)));
                            }
//                        else if (points.get(examItemModel.getIndex().get(levels)) >= 120 && points.get(examItemModel.getIndex().get(levels))<180)
//                            { if (examItemModel.getIndex().size() >= levels + 1)
//                                    holder.currentLevel.setText(String.valueOf(examItemModel.getIndex().get(levels + 1)));
//                            }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPoints(List<Integer> points, examItemModel examItemModel) {


    }

    private void setStars(ItemsViewHolder holder, int allUserPoints, examItemModel examItemModel) {
        if (examItemModel.getIndex().size() > 1) {
            if (allUserPoints > 0) {
                if (allUserPoints >= 40) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    if (allUserPoints > 80) {
                        holder.star2.setImageResource(R.drawable.ic_star);
                    }
                    if (allUserPoints == 120) {
                        holder.star3.setImageResource(R.drawable.ic_star);
                    }
                }

            }
        } else {
            if (allUserPoints > 0) {
                if (allUserPoints >= 15) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    if (allUserPoints > 35) {
                        holder.star2.setImageResource(R.drawable.ic_star);
                    }
                    if (allUserPoints == 60) {
                        holder.star3.setImageResource(R.drawable.ic_star);
                    }
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return Models.size();
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
