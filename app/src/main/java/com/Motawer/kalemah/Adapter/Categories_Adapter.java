package com.Motawer.kalemah.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Excercise_Levels;
import com.Motawer.kalemah.Models.CategoriesItems;
import com.Motawer.kalemah.R;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_ViewHolder> {
    View view;
    int currentlevel;
    ArrayList<CategoriesItems> list = new ArrayList<>();
    Context context;

    public Categories_Adapter(Context context, ArrayList<CategoriesItems> list) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public Categories_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item, parent, false);
        return new Categories_ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Categories_ViewHolder holder, int position)
    {
        if (list != null) {
            final CategoriesItems categoriesItems = list.get(position);
            holder.MyCurrentLevel.setText(categoriesItems.getMyCurrentLevel());
            holder.numberOfLevels.setText(categoriesItems.getNumberOfLevels());
            currentlevel = Integer.parseInt(categoriesItems.getMyCurrentLevel());
            holder.progressBar.setMax(Integer.parseInt(categoriesItems.getNumberOfLevels()));
            holder.help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoriesItems.getTitle().equals("Beginners Words")) {
                        StyleableToast.makeText(view.getContext(), "this for general Beginners words you " +
                                        "should pass first before you can continue to the Intermediate section"
                                , Toast.LENGTH_LONG, R.style.toast_help).show();

                    } else if (categoriesItems.getTitle().equals("Intermediate Words")) {
                        StyleableToast.makeText(view.getContext(), "this for general Intermediate words you " +
                                        "should pass first before you can continue to the Advanced section"
                                , Toast.LENGTH_LONG, R.style.toast_help).show();
                    } else
                        StyleableToast.makeText(view.getContext(), "this for general Advanced words, this are hard and not related words "
                                , Toast.LENGTH_LONG, R.style.toast_help).show();
                }


            });

            if (categoriesItems.getTitle().equals("Intermediate Words") && currentlevel == 0)
            {
                int blackTrans = Color.parseColor("#1A000000");
                holder.locked.setBackgroundColor(blackTrans);
                holder.locked.setVisibility(View.VISIBLE);

            }
            if (categoriesItems.getTitle().equals("Advanced Words") && currentlevel == 0)
            {
                int blackTrans = Color.parseColor("#1A000000");
                holder.locked.setBackgroundColor(blackTrans);
                holder.locked.setVisibility(View.VISIBLE);
            }

            holder.progressBar.setProgress(currentlevel);
            holder.title.setText(categoriesItems.getTitle());

            if (categoriesItems.getTitle().equals("Beginners Words")) {
                holder.categories_color.setBackgroundResource(R.drawable.level_round_a);
                holder.card.setBackgroundResource(R.drawable.card_view);

               // holder.cardimage.setImageResource(R.drawable.checklist);



            } else if (categoriesItems.getTitle().equals("Intermediate Words")) {
                holder.categories_color.setBackgroundResource(R.drawable.level_round_b);
                holder.card.setBackgroundResource(R.drawable.card_view2);


            } else {
                holder.categories_color.setBackgroundResource(R.drawable.level_round_c);
                holder.card.setBackgroundResource(R.drawable.card_view3);

            }
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (categoriesItems.getTitle().equals("Beginners Words")) {
                        Intent intent = new Intent(v.getContext(), Excercise_Levels.class);
                        intent.putExtra("level", 1);
                        v.getContext().startActivity(intent);
                        ((Activity)context).finish();

                    }
                    if (categoriesItems.getTitle().equals("Intermediate Words") && currentlevel != 0)
                    {
                        Intent intent = new Intent( ((Activity)context), Excercise_Levels.class);
                        intent.putExtra("level", 2);
                       // v.getContext().startActivity(intent);
                        ((Activity)context).startActivity(intent);
                        ((Activity)context).finish();



                    } else if (categoriesItems.getTitle().equals("Intermediate Words") && currentlevel == 0)
                    {
                        StyleableToast.makeText(view.getContext(), "Sorry You should pass the Beginners Level First"
                                , Toast.LENGTH_LONG, R.style.toast_help).show();
                    }
                    if (categoriesItems.getTitle().equals("Advanced Words") && currentlevel != 0)
                    {
                        Intent intent = new Intent(v.getContext(), Excercise_Levels.class);
                        intent.putExtra("level", 3);
                        v.getContext().startActivity(intent);
                        ((Activity)context).finish();

                    } else if (categoriesItems.getTitle().equals("Advanced Words") && currentlevel == 0)
                    {
                        StyleableToast.makeText(view.getContext(), "Sorry You should pass the Intermediate Level First"
                                , Toast.LENGTH_LONG, R.style.toast_help).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class Categories_ViewHolder extends RecyclerView.ViewHolder {
    ImageButton help, categories_color;
    CardView card;
    TextView title, numberOfLevels, MyCurrentLevel;
    ProgressBar progressBar;
    RelativeLayout locked;
    ImageView cardimage;

    public Categories_ViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.card);
        categories_color = itemView.findViewById(R.id.categories_color);
        title = itemView.findViewById(R.id.title);
        help = itemView.findViewById(R.id.help);
        numberOfLevels = itemView.findViewById(R.id.number_of_level);
        MyCurrentLevel = itemView.findViewById(R.id.current_level);
        progressBar = itemView.findViewById(R.id.progressBar);
        locked = itemView.findViewById(R.id.locked);
        cardimage =itemView.findViewById(R.id.cardimage);


    }
}