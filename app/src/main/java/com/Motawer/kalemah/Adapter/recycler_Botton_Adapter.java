package com.Motawer.kalemah.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.R;

public class recycler_Botton_Adapter extends RecyclerView.Adapter<BottonViewHolder> {
    String[] alphapets;
    Context context;
    View view;
    String highligtedItemPosition=null;

    public void setHighligtedItemPosition(String highligtedItemPosition) {
        this.highligtedItemPosition = highligtedItemPosition;
        notifyDataSetChanged();
    }

    onLetterButtonclicked buttonclicked;

    public recycler_Botton_Adapter(String[] alphapets, Context context) {
        this.alphapets = alphapets;
        this.context = context;
    }

    @NonNull
    @Override
    public BottonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.botton_item_recycler, parent, false);
        return new BottonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottonViewHolder holder, int position) {
        holder.button.setText(alphapets[position]);
        String charact = holder.button.getText().toString();
        if (highligtedItemPosition != null)
        if (charact.toLowerCase().equals(highligtedItemPosition)) {
            holder.button.setBackgroundResource(R.drawable.right_choise_button);
            holder.button.setEnabled(false);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("char" + charact);
                boolean state = buttonclicked.onButtonClicked(charact.toLowerCase());
                if (state) {
                    holder.button.setBackgroundResource(R.drawable.right_choise_button);
                    holder.button.setEnabled(false);
                } else {
                    holder.button.setBackgroundResource(R.drawable.wrong_choice_botton);
                    holder.button.setEnabled(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BottonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        try {
            buttonclicked = (onLetterButtonclicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }
    }
}

class BottonViewHolder extends RecyclerView.ViewHolder {
    Button button;

    public BottonViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);
    }
}
